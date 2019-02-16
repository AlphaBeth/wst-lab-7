package ru.ifmo.wst.lab1.command.args;

import ru.ifmo.wst.lab1.command.AbstractCommandArgDescription;

public class StringArg extends AbstractCommandArgDescription<String> {
    public StringArg(String errorMessage, String name, String description) {
        super(errorMessage, name, description);
    }

    public StringArg(String name, String description) {
        this("Not a string", name, description);
    }

    @Override
    public String convertFromString(String arg) {
        return arg;
    }
}
