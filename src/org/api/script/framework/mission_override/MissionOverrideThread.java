package org.api.script.framework.mission_override;

import org.api.script.SPXScript;
import org.api.script.framework.mission.Mission;
import org.api.script.framework.mission.MissionHandler;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class MissionOverrideThread implements Runnable {

    private final SPXScript spxScript;

    public MissionOverrideThread(SPXScript spxScript) {
        this.spxScript = spxScript;
        spxScript.getScheduledThreadPoolExecutor().scheduleAtFixedRate(this, 0, 5, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        final MissionHandler missionHandler = getSpxScript().getMissionHandler();
        if (missionHandler == null)
            return;

        final Mission currentMission = missionHandler.getCurrent();
        if (currentMission == null)
            return;

        if (!(currentMission instanceof MissionOverride))
            return;

        final MissionOverride missionOverride = (MissionOverride) currentMission;
        final MissionOverrideFactory missionOverrideFactory = Arrays.stream(missionOverride.overriddenMissionEntries()).filter(a -> a.entryIsReady() && a.canOverride()).findFirst().orElse(null);
        if (missionOverrideFactory == null)
            return;

        missionHandler.getMissions().add(missionOverrideFactory.getOverrideMission());
        missionHandler.getMissions().add(missionHandler.getCurrent());
        missionHandler.endCurrent();
    }

    private SPXScript getSpxScript() {
        return spxScript;
    }
}
