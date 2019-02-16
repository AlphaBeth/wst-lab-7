package ru.ifmo.wst.lab1.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class CommandArg<C, T> {
    @Getter
    private final CommandArgDescription<T> argDescription;
    private final BiConsumer<C, T> commandArgBuilder;

    public void acceptArgValue(C builtArg, T arg) {
        commandArgBuilder.accept(builtArg, arg);
    }
}
