package com.digdes.school.factories;

import com.digdes.school.commands.Command;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class CommandsFactory {
    private final String commandsFile = "commands.csv";
    private final Properties properties = new Properties();

    public CommandsFactory() {
        LoadCommands();
    }

    private void LoadCommands() {
        InputStream inStream = CommandsFactory.class.getClassLoader().getResourceAsStream(commandsFile);
        try {
            properties.load(inStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Command getCommand(String line) {
        Command command = null;
        try {
            command = (Command) Class.forName(properties.getProperty(line.toUpperCase())).getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }
        return command;
    }
}
