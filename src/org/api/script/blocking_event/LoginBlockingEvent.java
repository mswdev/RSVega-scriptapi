package org.api.script.blocking_event;

import org.api.http.data_tracking.data_tracker_factory.account.AccountDataTracker;
import org.api.script.SPXScript;
import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.Login;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.event.listeners.LoginResponseListener;
import org.rspeer.runetek.event.types.LoginResponseEvent;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptBlockingEvent;
import org.rspeer.script.events.LoginScreen;
import org.rspeer.script.events.WelcomeScreen;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginBlockingEvent extends ScriptBlockingEvent implements LoginResponseListener {

    private final SPXScript spxScript;
    private final LoginScreen loginScreen;
    private final WelcomeScreen welcomeScreen;

    public LoginBlockingEvent(Script ctx, SPXScript spxScript) {
        super(ctx);
        this.spxScript = spxScript;
        this.loginScreen = new LoginScreen(ctx);
        this.welcomeScreen = new WelcomeScreen(ctx);
    }

    private static int getLastSignIn() {
        final InterfaceComponent lastSignIn = Interfaces.getFirst(a -> a.getText().contains("You last logged in"));
        if (lastSignIn == null)
            return 0;

        if (lastSignIn.getText().contains("earlier today"))
            return 1;

        return Integer.parseInt(lastSignIn.getText().replaceAll("[^0-9]", ""));
    }

    private static int isMembers() {
        final InterfaceComponent notMembers = Interfaces.getFirst(a -> a.getText().contains("You are not a member."));
        return notMembers != null ? 0 : 1;
    }

    private static int hasBankPin() {
        final InterfaceComponent bankPinNotSet = Interfaces.getFirst(a -> a.getText().contains("You do not have a Bank PIN."));
        return bankPinNotSet != null ? 0 : 1;
    }

    @Override
    public void process() {
        if (loginScreen.validate()) {
            if (Login.getState() == Login.STATE_AUTHENTICATOR)
                Game.getClient().setGameState(Game.STATE_CREDENTIALS_SCREEN);

            loginScreen.process();
        }

        if (welcomeScreen.validate()) {
            final Map<String, String> accountData = new HashMap<>();
            accountData.put("last_sign_in", String.valueOf(getLastSignIn()));
            accountData.put("is_members", String.valueOf(isMembers()));
            accountData.put("is_bank_pin", String.valueOf(hasBankPin()));
            try {
                spxScript.getRsVegaTrackerWrapper().getAccountDataTracker().put(AccountDataTracker.getAccountData(accountData));
            } catch (IOException e) {
                e.printStackTrace();
            }

            welcomeScreen.process();
        }
    }

    @Override
    public boolean validate() {
        return loginScreen.validate() || welcomeScreen.validate();
    }

    @Override
    public void notify(LoginResponseEvent loginResponseEvent) {
        final Map<String, String> accountData = new HashMap<>();
        accountData.put("login_response", loginResponseEvent.getResponse().toString());
        if (loginResponseEvent.getResponse() == LoginResponseEvent.Response.ACCOUNT_DISABLED)
            accountData.put("disabled_date", new SimpleDateFormat("yyyy-MM-dd H:mm:ss").format(new Date()));

        try {
            spxScript.getRsVegaTrackerWrapper().getAccountDataTracker().put(AccountDataTracker.getAccountData(accountData));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
