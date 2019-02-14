package ru.ifmo.wst.lab1.db;

public interface Condition {
    String build();
    Object getValue();
    Class<?> getType();
    String getColumnName();
    boolean equals(Object other);
}
