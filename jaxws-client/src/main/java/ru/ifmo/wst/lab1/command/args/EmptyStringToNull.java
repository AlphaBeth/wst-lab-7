package ru.ifmo.wst.lab1.command.args;

import ru.ifmo.wst.lab1.command.CommandArg;
import ru.ifmo.wst.lab1.command.CommandArgWrapper;

public class EmptyStringToNull<T, C> extends CommandArgWrapper<T, C> {
    public EmptyStringToNull(CommandArg<T, C> wrapped) {
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
