package org.api.script.impl.mission.account_creation_mission.data;

import com.beust.jcommander.Parameter;

public class Args {

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
