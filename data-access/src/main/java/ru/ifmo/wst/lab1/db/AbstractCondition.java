package ru.ifmo.wst.lab1.db;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractCondition implements Condition {
    private final String columnName;
    private final Object value;
    private final Class<?> type;

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public Class<?> getType() {
        return type;
    }
}
