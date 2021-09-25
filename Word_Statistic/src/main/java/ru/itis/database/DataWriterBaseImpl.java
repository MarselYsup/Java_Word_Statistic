package ru.itis.database;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class DataWriterBaseImpl implements DataWriter{
    private final JdbcTemplate jdbcTemplate;
    private static String tableName="db";
    private static String CREATE_DATABASE = "create table $ (" +
            "    id serial primary key," +
            "    word varchar(150) unique not null," +
            "    count int not null" +
            "); ";
    private static String INSERT_OR_UPDATE = "Insert into $ (word,count) " +
            "values (?,?) on conflict (word) do update set count = $.count+excluded.count;";
    private static String SELECT_ALL = "select * from $ order by count DESC";
    private RowMapper<String> rowMapper = (resultSet, i) -> {
        String name = resultSet.getString("word");
        int count = resultSet.getInt("count");
        System.out.println(name+" - "+count);
        return name+" - "+count;

    };

    public DataWriterBaseImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void save(Map<String, Integer> words) {
        for (Map.Entry<String, Integer> word :
                words.entrySet()) {
            jdbcTemplate.update(INSERT_OR_UPDATE,word.getKey(),word.getValue());
        }
    }

    @Override
    public void createTable(File file) {
        tableName = "db"+file.getName().replaceFirst("[.][^.]+$", "");
        CREATE_DATABASE = CREATE_DATABASE.replace("$",tableName);
        INSERT_OR_UPDATE = INSERT_OR_UPDATE.replace("$",tableName);
        SELECT_ALL = SELECT_ALL.replace("$",tableName);
        jdbcTemplate.update(CREATE_DATABASE);
    }

    @Override
    public void findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet row = null;
        try {
            connection = jdbcTemplate.getDataSource().getConnection();
            statement = connection.prepareStatement(SELECT_ALL,
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            row = statement.executeQuery();
            while(row.next()) {
                String word = row.getString("word");
                Integer count = row.getInt("count");
                System.out.println(word+" - "+count);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        finally {
            if(row!=null) {
                try {
                    row.close();
                } catch (SQLException e) {
                    throw new IllegalArgumentException(e);
                }
            }
            if(statement!=null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new IllegalArgumentException(e);
                }
            }
            if(connection!=null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }
    }


}
