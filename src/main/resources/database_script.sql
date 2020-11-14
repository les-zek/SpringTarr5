-- utworzenie nowej bazy danych
create database spring_start
    character set utf8 collate utf8_polish_ci;

-- utworzenie użytkownika na serwerzez db
create user 'spring_start_user'@'localhost' identified by 'qwe123';
-- przypisanie uprawnien do użytkownika
grant SELECT, INSERT, DELETE, UPDATE, CREATE, ALTER, DROP, REFERENCES
    on spring_start.* to 'spring_start_user'@'localhost';