package com.digdes.school.factories;

import com.digdes.school.conditions.Condition;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class ConditionsFactory {
    private final String conditionsFile = "conditions.csv";
    private final Properties properties = new Properties();

    public ConditionsFactory() {
        LoadCommands();
    }

    private void LoadCommands() {
        InputStream inStream = CommandsFactory.class.getClassLoader().getResourceAsStream(conditionsFile);
        try {
            properties.load(inStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Condition getCondition(String line) {
        Condition condition = null;
        try {
            condition = (Condition) Class.forName(properties.getProperty(line.toLowerCase())).getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }
        return condition;
    }
}
