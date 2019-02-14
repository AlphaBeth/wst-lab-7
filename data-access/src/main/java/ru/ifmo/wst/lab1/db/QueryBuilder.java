package ru.ifmo.wst.lab1.db;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QueryBuilder {
    private String tableName;
    private List<String> columnNames = new ArrayList<>();
    private List<Condition> conditions = new ArrayList<>();

    public QueryBuilder tableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public QueryBuilder selectColumn(String columnName) {
        this.columnNames.add(columnName);
        return this;
    }

    public QueryBuilder selectColumns(String... columns) {
        for (String column : columns) {
            selectColumn(column);
        }
        return this;
    }

    public QueryBuilder condition(Condition condition) {
        this.conditions.add(condition);
        return this;
    }

    public Query buildPreparedStatementQuery() {
        StringBuilder query = new StringBuilder("SELECT ");
        String selectColumns = String.join(", ", columnNames);
        query.append(selectColumns).append(" FROM ").append(tableName);
        List<Condition> actualConditions = conditions.stream().filter(c -> c.build() != null).collect(Collectors.toList());
        if (!actualConditions.isEmpty()) {
            query.append(" WHERE ");
            query.append(actualConditions.stream().map(Condition::build).collect(Collectors.joining(" AND ")));
        }
        return new Query(query.toString(), actualConditions);
    }
}
