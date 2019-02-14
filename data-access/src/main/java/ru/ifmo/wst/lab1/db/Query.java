package ru.ifmo.wst.lab1.db;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
public class Query {
    @Getter
    private final String queryString;
    private final List<Condition> conditions;

    public void initPreparedStatement(PreparedStatement ps) throws SQLException {
        int i = 1;
        for (Condition condition : conditions) {
            Class<?> valueClass = condition.getType();
            int sqlType = classToSQLType(valueClass);
            if (condition.getValue() == null) {
                ps.setNull(i, sqlType);
            } else {
                switch (sqlType) {
                    case Types.BIGINT:
                        ps.setLong(i, (Long) condition.getValue());
                        break;
                    case Types.VARCHAR:
                        ps.setString(i, (String) condition.getValue());
                        break;
                    case Types.TIMESTAMP:
                        ps.setDate(i, (java.sql.Date) condition.getValue());
                        break;
                    default:
                        throw new RuntimeException(condition.toString());
                }

            }
            ++i;
        }
    }

    private int classToSQLType(Class<?> aClass) {
        if (aClass == Long.class) {
            return Types.BIGINT;
        } else if (aClass == String.class) {
            return Types.VARCHAR;
        } else if (aClass == Date.class) {
            return Types.TIMESTAMP;
        }
        throw new IllegalArgumentException(aClass.getName());
    }
}
