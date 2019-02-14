package ru.ifmo.wst.lab1.command;

import lombok.Getter;

import java.util.function.BiConsumer;

public abstract class AbstractCommandArg<T, C> implements CommandArg<T, C> {
    @Getter
    private final String errorMessage;
    @Getter
    private final String name;
    @Getter
    private final String description;
    private final BiConsumer<C, T> consumer;


    public AbstractCommandArg(String errorMessage, String name, String description, BiConsumer<C, T> consumer) {
        this.errorMessage = errorMessage;
        this.name = name;
        this.description = description;
        this.consumer = consumer;
    }

    @Override
    public void acceptConverted(T converted, C consumerObj) {
        consumer.accept(consumerObj, converted);
    }
}
