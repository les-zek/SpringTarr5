-- utworzenie bazy danychuseruseruser
create database spring_start character set utf8 collate utf8_polish_ci;
-- utworzenie uzytkownika na serwerze db

create user 'spring_start_user'@'localhost' identified by 'qwe123';

-- przywileje dla uzytkownika
grant select, insert, delete, update, create, alter, drop, references
on spring_start.* to 'spring_start_user'@'localhost';

select * from user;
delete  from user where user_id=2;
select * from post;