package ru.ifmo.wst.lab1.command.args;

import ru.ifmo.wst.lab1.command.CommandArgDescription;
import ru.ifmo.wst.lab1.command.CommandArgDescriptionWrapper;

public class EmptyStringToNull<T> extends CommandArgDescriptionWrapper<T> {
    public EmptyStringToNull(CommandArgDescription<T> wrapped) {
        super(wrapped);
    }

    @Override
    public T convertFromString(String arg) {
        String trim = arg.trim();
        if (trim.isEmpty()) {
            return null;
        }
        return wrapped.convertFromString(arg);
    }
}
