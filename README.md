# Инструкция по использованию и устройству программы #


Структура:

| Класс/Интерфейс | Описание |
| ------ | ------ |
| Main(class) | запуск проекта |
| WordsCounter | интерфейс, с методам основного функционала приложения |
| WordsCounterHTMLImpl | класс, реализующий интерфейс WordsCounter  |
| Parser | интерфейс, с методом *parse* необходимый для парсинга файла |
| HTMLParser | класс, реализующий интерфейс Parser. *parse* удаляет все теги в строке и кодовые слова,использующие html |
| DataWriter | интерфейс для записи данных |
|DataWriterBaseImpl|класс,реализующий *DataWriter*. Запись в БД используется **PostgreSQL**|

Запуск происходит в классе ***Main*** 
Основной функционал программы находится в ***WordsCounterHTMLImpl*** :
1. Запускается метод *start*, который принимает на вход путь
2. Создается объект класса *File*, если все в порядке с путем и файлом.
3. Считывается построчно, парсится теги и остальная служебная информация html с помощью *HTMLParser*
4. Разделяются слова в строке с помощь листа "Сепараторов"-(List  <String>  separators)
5. Слова сохраняются в HashMap, если слова встречалось добавляется единица к значению(Value), в противном случае ставится 1.
6. В процессе прочтения строчек, осуществляется проверка занимаемой памяти HashMap. В случае большого кол-во операций или занимаемого размера происходит запись в таблицу и очищения данных с HashMap.
7. После окончания прочтения файла оставшиеся данные переводятся в таблицу  
8. Выводится все данные в порядке от наибольшего к наименьшему с помощью метода *findAll* в классе ***DataWriterBaseImpl***
9. Программа завершается.

**Дополнительно**:
При записи данных используется запрос Inert or Update - в случае повторения слова, добавляется текущая частота к нынешней.
Таблица создается после проверки файла(Название таблицы db +  Название файла(без расширения))