package org.api.script;

import com.beust.jcommander.JCommander;
import org.api.client.screenshot.Screenshot;
import org.api.game.BankCache;
import org.api.http.data_tracking.RSVegaTrackerWrapper;
import org.api.script.blocking_event.LoginBlockingEvent;
import org.api.script.framework.item_management.ItemManagement;
import org.api.script.framework.item_management.ItemManagementEntry;
import org.api.script.framework.item_management.ItemManagementTracker;
import org.api.script.framework.mission.Mission;
import org.api.script.framework.mission.MissionHandler;
import org.api.script.framework.mule_management.MuleManagementEntry;
import org.api.script.framework.mule_management.MuleManagementTracker;
import org.api.script.impl.mission.item_management_mission.ItemManagementMission;
import org.api.script.impl.mission.mule_slave_management.mule_management_mission.MuleManagementMission;
import org.api.ui.fxui.FXGUI;
import org.api.ui.fxui.FXGUIBuilder;
import org.api.ui.swingui.GUI;
import org.api.ui.swingui.GUIBuilder;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.script.Script;
import org.rspeer.script.events.LoginScreen;
import org.rspeer.script.events.WelcomeScreen;
import org.rspeer.ui.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Level;

public abstract class SPXScript extends Script implements RenderListener {

    private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(4);
    private final RSVegaTrackerWrapper rsVegaTrackerWrapper = new RSVegaTrackerWrapper(this);
    private final MuleManagementTracker muleManagementTracker = new MuleManagementTracker(this);
    private final BankCache bankCache = new BankCache();
    private MissionHandler missionHandler;
    private FXGUIBuilder fxGuiBuilder;
    private GUIBuilder guiBuilder;
    private ItemManagementTracker itemManagementTracker;
    private MissionHandler itemManagementMissionHandler;

    @Override
    public void onStart() {
        Log.log(Level.FINE, "Info", "Starting " + getMeta().name() + "!");
        removeBlockingEvent(LoginScreen.class);
        removeBlockingEvent(WelcomeScreen.class);
        addBlockingEvent(new LoginBlockingEvent(this, this));
        Game.getEventDispatcher().register(bankCache);

        createDirectoryFolders();
        if (getArguments() != null && getArgs() != null && getArgs().length() > 0) {
            JCommander.newBuilder()
                    .addObject(getArguments())
                    .build()
                    .parse(getArgs().split(" "));
        } else {
            if (getFXGUI() != null) {
                fxGuiBuilder = new FXGUIBuilder(getFXGUI());
                fxGuiBuilder.invokeGUI();
            }

            if (getGUI() != null) {
                guiBuilder = new GUIBuilder(getGUI());
                guiBuilder.invokeGUI();
            }
        }

        missionHandler = new MissionHandler(createMissionQueue());
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

        final MuleManagementEntry muleManagementEntry = muleManagementTracker.getReadyMuleManagementEntry();
        if (muleManagementEntry != null) {
            missionHandler.getMissions().add(new MuleManagementMission(this, muleManagementEntry));
            missionHandler.getMissions().add(missionHandler.getCurrent());
            missionHandler.endCurrent();
            muleManagementTracker.setMuleManagementEntry(null);
        }

        return missionHandler.execute();
    }

    @Override
    public void onStop() {
        getScheduledThreadPoolExecutor().shutdown();
        Game.getEventDispatcher().deregister(bankCache);
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

    public BankCache getBankCache() {
        return bankCache;
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
        final Path botDirectoryPath = Paths.get(SPXScriptUtil.getScriptDataPath(getMeta().name()));
        if (!Files.exists(botDirectoryPath)) {
            try {
                Files.createDirectories(botDirectoryPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public RSVegaTrackerWrapper getRsVegaTrackerWrapper() {
        return rsVegaTrackerWrapper;
    }

    public ScheduledThreadPoolExecutor getScheduledThreadPoolExecutor() {
        return scheduledThreadPoolExecutor;
    }
}

