package org.api.script.impl.mission.firemaking_mission.data;

import com.beust.jcommander.Parameter;
import org.api.game.skills.firemaking.LogType;

public class Args {

    @Parameter(names = "-logType", arity = 1)
    public LogType logType;

    @Parameter(names = "-setProgressive", arity = 1)
    public boolean setProgressive = true;

}

