package ru.itis.maincode;

import ru.itis.database.DataWriter;
import ru.itis.parsing.Parser;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class WordsCounterHTMLImpl implements WordsCounter{
    private final static int maxSizeMap = 100;
    private final static int maxSizeOperation = 10000;
    private int countOfOperation = 0;
    private final static List<String> separators = Arrays.asList(".","!","?",",","\"","\'",
            ";",":","[","]","(",")","\t","\r","\n","«","»","—","-","`",
            "1","2","3","4","5","6","7","8","9","0"
            ,"$","%","@","#","*","№","/","\\");
    private final Parser parser;
    private final DataWriter writer;

    public WordsCounterHTMLImpl(Parser parser, DataWriter writer) {
        this.parser = parser;
        this.writer = writer;
    }

    @Override
    public void start(String path) {
        File file = new File(path);
        if(file.isFile()&& !file.isDirectory()) {
            try {
                writer.createTable(file);
                BufferedReader bf = new BufferedReader(new FileReader(file));
                HashMap<String,Integer> hashMap = new HashMap<>();
                String line;
                while((line=bf.readLine())!=null) {

                    if(hashMap.size()>=maxSizeMap||countOfOperation>maxSizeOperation) {
                        countOfOperation=0;
                        writer.save(hashMap);
                        hashMap = new HashMap<>();
                    }
                    line = parser.parse(line);

                    if(!line.isEmpty()) {
                        for (String separator :
                                separators) {
                            line = line.replace(separator, "");
                        }
                        String[] lines = line.split(" ");

                        for (String strings :
                                lines) {
                            if (!strings.isEmpty()) {
                                countOfOperation++;
                                hashMap.put(strings.toLowerCase(), hashMap.containsKey(strings.toLowerCase()) ? hashMap.get(strings.toLowerCase()) + 1 : 1);
                            }
                        }
                    }


                }
                bf.close();
                if(hashMap.size()>0) {
                    writer.save(hashMap);
                    hashMap = null;
                }
                writer.findAll();

            } catch (IOException e) {
                System.out.println("Ошибка! Попробуйте заново отправить файл.");
            }
        }
        else {
            System.out.println("Неправильный путь к файлу!");
        }
    }


}
