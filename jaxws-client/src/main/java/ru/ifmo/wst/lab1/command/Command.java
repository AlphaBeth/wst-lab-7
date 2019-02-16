package ru.ifmo.wst.lab1.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@RequiredArgsConstructor
@EqualsAndHashCode(of = {"name"})
public class Command<V> {
    @Getter
    private final String name;
    @Getter
    private final String description;
    @Getter
    private final List<CommandArg<V, ?>> args;
    private final Supplier<V> valueProducer;
    private final Consumer<V> commandExecuter;

    public Command(String name, String description, Consumer<V> commandExecuter) {
        this(name, description, Collections.emptyList(), null, commandExecuter);
    }

    public V newValue() {
        return valueProducer == null ? null : valueProducer.get();
    }

    public void execute(V argument) {
        commandExecuter.accept(argument);
    }
}
