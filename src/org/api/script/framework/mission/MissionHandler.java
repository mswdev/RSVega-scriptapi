package org.api.script.framework.mission;

import org.rspeer.ui.Log;

import java.util.Queue;

public class MissionHandler {

    private Queue<Mission> missions;
    private boolean end;
    private boolean endCurrent;

    public MissionHandler(Queue<Mission> missions) {
        this.missions = missions;
    }

    /**
     * Executes the missions.
     *
     * @return The sleep time for the loop cycle.
     */
    public int execute() {
        if (missions == null || missions.isEmpty()) {
            end = true;
            return 100;
        }

        final Mission mission = missions.peek();
        if (!mission.hasStarted()) {
            Log.fine("[MISSION]: " + mission.getMissionName() + " mission has started.");
            mission.onMissionStart();
            mission.setStarted(true);
        }

        if (mission.shouldEnd() || endCurrent) {
            Log.fine("[MISSION]: " + mission.getMissionName() + " mission has ended.");
            mission.onMissionEnd();
            missions.poll();
            endCurrent = false;
            return 100;
        } else {
            if (mission.shouldPrintWorkerString())
                Log.info("[" + mission.getWorkerName() + "]: " + mission.getWorkerString());
            return mission.execute();
        }
    }

    /**
     * Gets the queue of missions.
     *
     * @return The queue containing the missions.
     */
    public Queue<Mission> getMissions() {
        return missions;
    }

    /**
     * Ends the current mission.
     */
    public void endCurrent() {
        endCurrent = true;
    }

    /**
     * Checks whether the mission is stopped.
     *
     * @return True if the mission is stopped; false otherwise.
     */
    public boolean isStopped() {
        return end;
    }

    /**
     * Gets the current mission.
     *
     * @return The current mission.
     */
    public Mission getCurrent() {
        return missions.peek();
    }
}

