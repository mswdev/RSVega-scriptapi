package org.api.script;

import com.beust.jcommander.JCommander;
import org.api.client.screenshot.Screenshot;
import org.api.game.BankCache;
import org.api.http.RSVegaTracker;
import org.api.http.RSVegaTrackerThread;
import org.api.script.framework.item_management.ItemManagement;
import org.api.script.framework.item_management.ItemManagementEntry;
import org.api.script.framework.item_management.ItemManagementTracker;
import org.api.script.framework.mission.Mission;
import org.api.script.framework.mission.MissionHandler;
import org.api.script.impl.mission.item_management_mission.ItemManagementMission;
import org.api.ui.fxui.FXGUI;
import org.api.ui.fxui.FXGUIBuilder;
import org.api.ui.swingui.GUI;
import org.api.ui.swingui.GUIBuilder;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.script.Script;
import org.rspeer.ui.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public abstract class SPXScript extends Script implements RenderListener {

    public BankCache bankCache;
    protected String scriptDataPath = getDataDirectory() + File.separator + "SPX" + File.separator + getMeta().name();
    private MissionHandler missionHandler;
    private FXGUIBuilder fxGuiBuilder;
    private GUIBuilder guiBuilder;
    private ItemManagementTracker itemManagementTracker;
    private MissionHandler itemManagementMissionHandler;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    @Override
    public void onStart() {
        Log.log(Level.FINE, "Info", "Starting " + getMeta().name() + "!");

        RSVegaTracker.insertAccount();
        RSVegaTracker.insertBot();
        RSVegaTracker.insertSession(getMeta().name(), new Date());
        scheduleThreadPool();

        createDirectoryFolders();
        if (getArguments() != null && getArgs() != null && getArgs().length() > 0) {
            JCommander.newBuilder()
                    .addObject(getArguments())
                    .build()
                    .parse(getArgs().split(" "));
        }

        missionHandler = new MissionHandler(createMissionQueue());

        if (getFXGUI() != null) {
            fxGuiBuilder = new FXGUIBuilder(getFXGUI());
            fxGuiBuilder.invokeGUI();
        }

        if (getGUI() != null) {
            guiBuilder = new GUIBuilder(getGUI());
            guiBuilder.invokeGUI();
        }

        bankCache = new BankCache(this);
        bankCache.start();
    }

    @Override
    public int loop() {
        if (fxGuiBuilder != null)
            if (fxGuiBuilder.isInvoked())
                return 100;

        if (guiBuilder != null)
            if (guiBuilder.isInvoked())
                return 100;

        if (missionHandler.isStopped())
            setStopping(true);

        final ItemManagementEntry itemManagementEntry = getReadyItemManagementEntry();
        if (itemManagementEntry != null || itemManagementMissionHandler != null) {
            if (itemManagementMissionHandler == null)
                itemManagementMissionHandler = new MissionHandler(new LinkedList<>(Collections.singleton(new ItemManagementMission(this, itemManagementEntry, itemManagementTracker, itemManagementTracker.getItemManagement().itemsToSell()))));

            if (!itemManagementMissionHandler.isStopped())
                return itemManagementMissionHandler.execute();
            else
                itemManagementMissionHandler = null;
        }

        return missionHandler.execute();
    }

    @Override
    public void onStop() {
        RSVegaTracker.updateSession(new Date());
        scheduledThreadPoolExecutor.shutdown();

        if (fxGuiBuilder != null)
            fxGuiBuilder.close();

        if (guiBuilder != null)
            guiBuilder.close();

        Log.log(Level.FINE, "Info", "Thanks for using " + getMeta().name() + "!");
    }

    public abstract Queue<Mission> createMissionQueue();

    public Object getArguments() {
        return null;
    }

    public FXGUI getFXGUI() {
        return null;
    }

    public GUI getGUI() {
        return null;
    }

    public MissionHandler getMissionHandler() {
        return missionHandler;
    }

    public FXGUIBuilder getFXMLHandler() {
        return fxGuiBuilder;
    }

    public GUIBuilder getGUIBuilder() {
        return guiBuilder;
    }

    @Override
    public void notify(RenderEvent renderEvent) {
        Screenshot.renderQueue(renderEvent);
    }

    /**
     * Schedules the thread pool executor.
     */
    private void scheduleThreadPool() {
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new RSVegaTrackerThread(), 10, 10, TimeUnit.SECONDS);
    }

    /**
     * Gets a ready item management entry that has an item that can be bought.
     *
     * @return A ready item management entry.
     */
    private ItemManagementEntry getReadyItemManagementEntry() {
        final Mission currentMission = missionHandler.getCurrent();
        if (!(currentMission instanceof ItemManagement))
            return null;

        if (itemManagementTracker == null || itemManagementTracker.getItemManagement() != currentMission)
            itemManagementTracker = new ItemManagementTracker(this, (ItemManagement) currentMission);

        itemManagementTracker.update();
        return itemManagementTracker.shouldBuy();
    }

    /**
     * Creates the script directories if they do not exist.
     */
    private void createDirectoryFolders() {
        final Path botDirectoryPath = Paths.get(scriptDataPath);
        if (!Files.exists(botDirectoryPath)) {
            try {
                Files.createDirectories(botDirectoryPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

