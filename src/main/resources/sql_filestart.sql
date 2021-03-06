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

SELECT p.category, count(*) FROM Post p GROUP BY p.category ORDER BY 2 DESC;
select * from user;
select * from role;

select u.email, u.password, u.status from user u where u.email ="aa@aa.aa";
SELECT
	u.email,
    r.role_name
FROM
	user u
		JOIN
	user_roles ur ON (u.user_id = ur.user_user_id)
		JOIN
	role r ON (r.role_id = ur.roles_role_id)
WHERE
	u.email = ?;

update user set status=2 where user_id=8;
select * from user_roles;
select * from likes;
select * from dislikes;
select* from post;
select * from post;
select * from role;
select * from user;

SELECT p.* FROM post p ORDER BY ((SELECT count(*) FROM likes l WHERE l.post_id = p.post_id) - (SELECT count(*) FROM dislikes d WHERE d.post_id = p.post_id)) DESC;
SELECT p.* FROM post p ORDER BY (SELECT count(*) FROM likes l WHERE l.post_id = p.post_id) desc;
SELECT p.* FROM post p ORDER BY (SELECT count(*) FROM likes l WHERE l.post_id = p.post_id);
select post_id,count(*) from likes group by post_id order by 2 desc;
select post_id,count(*) from dislikes group by post_id order by 2 desc;

select * from comment;
select * from post;
alter table post drop column image_path;
select * from user;
SELECT u.email, r.role_name FROM user u JOIN user_roles ur ON (u.user_id = ur.user_user_id) JOIN role r ON (r.role_id = ur.roles_role_id);
create database demo;
select * from user;
select * from task where type=1;
select * from task;
