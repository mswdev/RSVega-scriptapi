package org.api.script.impl.mission.tutorial_island_mission.data.args;

import com.beust.jcommander.Parameter;
import org.rspeer.runetek.api.movement.position.Position;

public class Args {

    @Parameter(names = "-dropItems", arity = 1)
    public boolean dropItems;

    @Parameter(names = "-bankItems", arity = 1)
    public boolean bankItems;

    @Parameter(names = "-walkPosition", converter = PositionConverter.class, arity = 1)
    public Position walkPosition;

    @Parameter(names = "-hideRoofs", arity = 1)
    public boolean hideRoofs;

    @Parameter(names = "-setAudio", arity = 1)
    public int setAudio;

    @Parameter(names = "-setBrightness", arity = 1)
    public int setBrightness;

    @Parameter(names = "-stayLoggedIn", arity = 1)
    public boolean stayLoggedIn;

    @Parameter(names = "-twoCaptchaApiKey", arity = 1)
    public String twoCaptchaApiKey;

    @Parameter(names = "-accountList", arity = 1)
    public String accountList;

    @Parameter(names = "-accountsToCreate", arity = 1)
    public int accountsToCreate;

    @Parameter(names = "-proxyList", arity = 1)
    public String proxyList;

    @Parameter(names = "-accountsPerProxy", arity = 1)
    public int accountsPerProxy;

}

