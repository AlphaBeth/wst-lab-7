package ru.ifmo.wst.lab1.command.args;

import ru.ifmo.wst.lab1.command.AbstractCommandArg;
import ru.ifmo.wst.lab1.command.ConvertException;

import java.util.function.BiConsumer;

public class LongArg<C> extends AbstractCommandArg<Long, C> {
    public LongArg(String errorMessage, String name, String description, BiConsumer<C, Long> consumer) {
        super(errorMessage, name, description, consumer);
    }

    public LongArg(String name, String description, BiConsumer<C, Long> consumer) {
        super("Not an integer number", name, description, consumer);
    }

    @Override
    public Long convertFromString(String arg) {
        String trim = arg.trim();
        try {
            return Long.parseLong(trim);
        } catch (NumberFormatException exc) {
            throw new ConvertException();
        }
    }
}
