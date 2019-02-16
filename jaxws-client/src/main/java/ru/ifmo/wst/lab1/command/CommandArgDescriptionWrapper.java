package ru.ifmo.wst.lab1.command;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommandArgDescriptionWrapper<T> implements CommandArgDescription<T> {
    protected final CommandArgDescription<T> wrapped;

    @Override
    public String getName() {
        return wrapped.getName();
    }

    @Override
    public String getDescription() {
        return wrapped.getDescription();
    }

    @Override
    public String getErrorMessage() {
        return wrapped.getErrorMessage();
    }

    @Override
    public T convertFromString(String arg) {
        return wrapped.convertFromString(arg);
    }

}
