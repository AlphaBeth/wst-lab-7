package ru.ifmo.wst.lab1.command.args;

import ru.ifmo.wst.lab1.command.AbstractCommandArg;
import ru.ifmo.wst.lab1.command.ConvertException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.BiConsumer;

public class DateArg<C> extends AbstractCommandArg<Date, C> {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public DateArg(String name, String description, BiConsumer<C, Date> consumer) {
        super("Must be date in format " + DATE_FORMAT, name, description, consumer);
    }

    @Override
    public Date convertFromString(String arg) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            return simpleDateFormat.parse(arg.trim());
        } catch (ParseException e) {
            throw new ConvertException();
        }
    }
}
