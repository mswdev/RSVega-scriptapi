package org.api.script.impl.mission.firemaking_mission.data;

import com.beust.jcommander.Parameter;
import org.api.game.skills.firemaking.LogType;

public class Args {

    @Parameter(names = "-log_type")
    public LogType log_type;

    @Parameter(names = "-set_progressive", arity = 1)
    public boolean set_progressive = true;

}

