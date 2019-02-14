package ru.ifmo.wst.lab1.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SampleDAO {
    private final DataSource dataSource;

    public List<String> sampleSelect() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute("SELECT * FROM (values ('a')) as vals");
            ResultSet rs = statement.getResultSet();
            List<String> res = new ArrayList<>();
            while (rs.next()) {
                res.add(rs.getString(1));
            }
            return res;
        }
    }

    public SampleDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
