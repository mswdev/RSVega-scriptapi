package org.api.script.impl.mission.questing.data;

import com.beust.jcommander.Parameter;
import org.api.game.questing.QuestType;

public class Args {

    @Parameter(names = "-quests", converter = QuestTypeConverter.class, arity = 1)
    public QuestType[] questTypes;

    @Parameter(names = "-sevenQp", arity = 1)
    public boolean sevenQp;

    @Parameter(names = "-randomizeOrder", arity = 1)
    public boolean randomizeOrder;
}
