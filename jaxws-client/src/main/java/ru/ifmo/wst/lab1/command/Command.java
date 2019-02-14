package ru.ifmo.wst.lab1.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
@EqualsAndHashCode(of = {"name"})
public class Command<V> {
    @Getter
    private final String name;
    @Getter
    private final String description;
    @Getter
    private final List<CommandArg<?, V>> args;
    private final Supplier<V> valueProducer;

    public Command(String name, String description) {
        this(name, description, Collections.emptyList(), null);
    }

    public V newValue() {
        return valueProducer == null ? null : valueProducer.get();
    }
}
