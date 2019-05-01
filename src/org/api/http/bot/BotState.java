package org.api.http.bot;

public enum BotState {

    DISABLED("is_disabled"),
    LOCKED("is_locked"),
    AUTHENTICATOR("is_auth"),
    BILLING("is_billing"),
    INVALID("is_invalid");

    private final String tableName;

    BotState(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}
