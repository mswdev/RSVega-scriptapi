package org.api.script;

import com.beust.jcommander.JCommander;
import org.api.game.BankCache;
import org.api.http.data_tracking.RSVegaTrackerWrapper;
import org.api.script.blocking_event.LoginBlockingEvent;
import org.api.script.framework.mission.Mission;
import org.api.script.framework.mission.MissionHandler;
import org.api.script.framework.mission_override.MissionOverrideThread;
import org.api.ui.fxui.FXGUI;
import org.api.ui.fxui.FXGUIBuilder;
import org.api.ui.swingui.GUI;
import org.api.ui.swingui.GUIBuilder;
import org.rspeer.script.Script;
import org.rspeer.script.events.LoginScreen;
import org.rspeer.script.events.WelcomeScreen;
import org.rspeer.ui.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Queue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Level;

public abstract class SPXScript extends Script {

    private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2);
    private final RSVegaTrackerWrapper rsVegaTrackerWrapper = new RSVegaTrackerWrapper(this);
    private final MissionOverrideThread missionOverrideThread = new MissionOverrideThread(this);
    private final BankCache bankCache = new BankCache(this);
    private MissionHandler missionHandler;
    private FXGUIBuilder fxGuiBuilder;
    private GUIBuilder guiBuilder;

    @Override
    public void onStart() {
        Log.log(Level.FINE, "Info", "Starting " + getMeta().name() + "!");
        removeBlockingEvent(LoginScreen.class);
        removeBlockingEvent(WelcomeScreen.class);
        addBlockingEvent(new LoginBlockingEvent(this, this));

        createDirectoryFolders();
        if (getArguments() != null && getArgs() != null && getArgs().length() > 0) {
            JCommander.newBuilder()
                    .addObject(getArguments())
                    .build()
                    .parse(getArgs().split(" "));
        } else {
            if (getFXGUI() != null) {
                setFxGuiBuilder(new FXGUIBuilder(getFXGUI()));
                getFxGuiBuilder().invokeGUI();
            }

            if (getGUI() != null) {
                setGuiBuilder(new GUIBuilder(getGUI()));
                getGUIBuilder().invokeGUI();
            }
        }

        setMissionHandler(new MissionHandler(createMissionQueue()));
    }

    @Override
    public int loop() {
        if (getFxGuiBuilder() != null)
            if (getFxGuiBuilder().isInvoked())
                return 100;

        if (getGUIBuilder() != null)
            if (getGUIBuilder().isInvoked())
                return 100;

        if (getMissionHandler().isStopped())
            setStopping(true);

        return getMissionHandler().execute();
    }

    @Override
    public void onStop() {
        getScheduledThreadPoolExecutor().shutdown();
        if (getFxGuiBuilder() != null)
            getFxGuiBuilder().close();

        if (getGUIBuilder() != null)
            getGUIBuilder().close();

        Log.log(Level.FINE, "Info", "Thanks for using " + getMeta().name() + "!");
    }

    /**
     * Creates the script directories if they do not exist.
     */
    private void createDirectoryFolders() {
        final Path botDirectoryPath = Paths.get(SPXScriptUtil.getScriptDataPath(getMeta().name()));
        if (Files.exists(botDirectoryPath))
            return;

        try {
            Files.createDirectory(botDirectoryPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void setMissionHandler(MissionHandler missionHandler) {
        this.missionHandler = missionHandler;
    }

    public BankCache getBankCache() {
        return bankCache;
    }

    public GUIBuilder getGUIBuilder() {
        return guiBuilder;
    }

    public RSVegaTrackerWrapper getRsVegaTrackerWrapper() {
        return rsVegaTrackerWrapper;
    }

    public ScheduledThreadPoolExecutor getScheduledThreadPoolExecutor() {
        return scheduledThreadPoolExecutor;
    }

    private FXGUIBuilder getFxGuiBuilder() {
        return fxGuiBuilder;
    }

    private void setFxGuiBuilder(FXGUIBuilder fxGuiBuilder) {
        this.fxGuiBuilder = fxGuiBuilder;
    }

    public GUIBuilder getGuiBuilder() {
        return guiBuilder;
    }

    private void setGuiBuilder(GUIBuilder guiBuilder) {
        this.guiBuilder = guiBuilder;
    }
}

