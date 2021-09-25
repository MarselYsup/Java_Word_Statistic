package ru.itis;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.itis.database.DataWriter;
import ru.itis.database.DataWriterBaseImpl;
import ru.itis.maincode.WordsCounter;
import ru.itis.maincode.WordsCounterHTMLImpl;
import ru.itis.parsing.HTMLParser;

import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Properties property = new Properties();
        try {
            property.load(ClassLoader.getSystemResourceAsStream("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(property.getProperty("db.driver"));
        dataSource.setUsername(property.getProperty("db.user"));
        dataSource.setPassword(property.getProperty("db.password"));
        dataSource.setUrl(property.getProperty("db.url"));

        WordsCounter wordsCounter = new WordsCounterHTMLImpl(new HTMLParser(),new DataWriterBaseImpl(dataSource));
        System.out.print("Введите путь до файла: ");
        String path = sc.next();
        wordsCounter.start(path);






    }
}
