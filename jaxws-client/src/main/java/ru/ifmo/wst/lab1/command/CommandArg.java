package ru.ifmo.wst.lab1.command;

public interface CommandArg<T, C> {
    String getName();

    String getDescription();

    String getErrorMessage();

    T convertFromString(String arg);

    void acceptConverted(T converted, C consumer);
}
