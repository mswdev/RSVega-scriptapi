package org.api.script.impl.mission.mule_slave_management.slave_management_mission;

import org.rspeer.runetek.event.listeners.ChatMessageListener;
import org.rspeer.runetek.event.types.ChatMessageEvent;
import org.rspeer.runetek.event.types.ChatMessageType;

public class TradeMessageListener implements ChatMessageListener {

    private String playerTradingDisplayName;

    @Override
    public void notify(ChatMessageEvent chatMessageEvent) {
        if (chatMessageEvent.getType() == ChatMessageType.TRADE) {
            setPlayerTradingDisplayName(chatMessageEvent.getMessage().replace(" wishes to trade with you.", ""));
        }
    }

    public void setPlayerTradingDisplayName(String playerTradingDisplayName) {
        this.playerTradingDisplayName = playerTradingDisplayName;
    }

    public String getPlayerTradingDisplayName() {
        return playerTradingDisplayName;
    }
}
