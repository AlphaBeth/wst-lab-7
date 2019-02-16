package ru.ifmo.wst.lab1.command.args;

import ru.ifmo.wst.lab1.command.AbstractCommandArgDescription;
import ru.ifmo.wst.lab1.command.ConvertException;

public class LongArg extends AbstractCommandArgDescription<Long> {
    public LongArg(String errorMessage, String name, String description) {
        super(errorMessage, name, description);
    }

    public LongArg(String name, String description) {
        super("Not an integer number", name, description);
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
