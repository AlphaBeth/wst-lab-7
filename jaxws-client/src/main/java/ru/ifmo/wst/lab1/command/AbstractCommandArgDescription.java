package ru.ifmo.wst.lab1.command;

import lombok.Getter;

public abstract class AbstractCommandArgDescription<T> implements CommandArgDescription<T> {
    @Getter
    private final String errorMessage;
    @Getter
    private final String name;
    @Getter
    private final String description;


    public AbstractCommandArgDescription(String errorMessage, String name, String description) {
        this.errorMessage = errorMessage;
        this.name = name;
        this.description = description;
    }

}
