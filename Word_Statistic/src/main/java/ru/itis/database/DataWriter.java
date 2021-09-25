package ru.itis.database;

import java.io.File;
import java.util.Map;

public interface DataWriter {
    void save(Map<String,Integer> words);
    void createTable(File file);
    void findAll();

}
