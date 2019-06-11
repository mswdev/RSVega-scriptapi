package org.api.script.impl.mission.questing.data;

import com.beust.jcommander.IStringConverter;
import org.api.game.questing.QuestType;

import java.util.Arrays;

public class QuestTypeConverter implements IStringConverter<QuestType[]> {

    @Override
    public QuestType[] convert(String s) {
        return Arrays.stream(s.split(",")).map(QuestType::valueOf).toArray(QuestType[]::new);
    }
}
