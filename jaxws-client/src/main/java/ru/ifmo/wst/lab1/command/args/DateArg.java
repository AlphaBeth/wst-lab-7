package ru.ifmo.wst.lab1.command.args;

import ru.ifmo.wst.lab1.command.AbstractCommandArgDescription;
import ru.ifmo.wst.lab1.command.ConvertException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateArg extends AbstractCommandArgDescription<Date> {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public DateArg(String name, String description) {
        super("Must be date in format " + DATE_FORMAT, name, description);
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
