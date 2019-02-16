package ru.ifmo.wst.lab1.command;

public interface CommandArgDescription<T> {
    String getName();

    String getDescription();

    String getErrorMessage();

    T convertFromString(String arg);

}
