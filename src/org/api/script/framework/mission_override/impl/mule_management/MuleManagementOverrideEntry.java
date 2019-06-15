package org.api.script.framework.mission_override.impl.mule_management;

import com.google.gson.JsonArray;
import org.api.script.framework.goal.GoalList;
import org.api.script.framework.mission.Mission;
import org.api.script.framework.mission_override.MissionOverrideFactory;
import org.api.script.impl.mission.mule_slave_management.mule_management_mission.MuleManagementMission;
import org.rspeer.runetek.adapter.component.Item;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public class MuleManagementOverrideEntry extends MissionOverrideFactory {

    private final Predicate<Item> item;
    private MuleManager muleManager;

    public MuleManagementOverrideEntry(Mission mission, Predicate<Item> item, GoalList goalList) {
        super(mission, goalList);
        this.item = item;
    }

    public MuleManagementOverrideEntry(Mission mission, Predicate<Item> item, BooleanSupplier goalOverride) {
        super(mission, goalOverride);
        this.item = item;
    }

    public MuleManagementOverrideEntry(Mission mission, Predicate<Item> item, GoalList goalList, BooleanSupplier goalOverride) {
        super(mission, goalList, goalOverride);
        this.item = item;
    }

    @Override
    public Mission getOverrideMission() {
        return new MuleManagementMission(getMission().getScript(), this);
    }

    @Override
    public boolean entryIsReady() {
        final int userId = getMission().getScript().getRsVegaTrackerWrapper().getUserDataTracker().getId();
        if (userId <= 0)
            return false;

        final JsonArray accountActiveIsMuleData = getMission().getScript().getRsVegaTrackerWrapper().getRandomAccountActiveIsMule(userId);
        if (accountActiveIsMuleData == null || accountActiveIsMuleData.size() <= 0)
            return false;

        setMuleManager(new MuleManager(accountActiveIsMuleData));
        return true;
    }

    public Predicate<Item> getItem() {
        return item;
    }

    public MuleManager getMuleManager() {
        return muleManager;
    }

    public void setMuleManager(MuleManager muleManager) {
        this.muleManager = muleManager;
    }
}
