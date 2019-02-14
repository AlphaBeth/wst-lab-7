package ru.ifmo.wst.lab1.db;

public class IgnoreCaseContainsCondition extends AbstractCondition {
    public IgnoreCaseContainsCondition(String columnName, String value) {
        super(columnName, value == null ? value : "%" + value.trim().toLowerCase() + "%", String.class);
    }

    @Override
    public String build() {
        if (getValue() != null) {
            return String.format("LOWER(%s) LIKE ?", getColumnName());
        }
        return null;
    }

    @Override
    public String getValue() {
        return super.getValue() == null ? null : (String)super.getValue();
    }
}
