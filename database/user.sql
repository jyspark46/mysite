desc user;
select * from guestbook;

-- join
insert into user values(null, '관리자', 'admin@mysite.com', password('1234'), 'male', current_date());

-- login
select no, name from user where email = 'jyspark46@gmail.com' and password=password('1234');

-- test
select * from user;

-- role 추가
alter table user add column role enum('ADMIN', 'USER') not null default 'USER';

-- role 변경
update user set role='ADMIN' where no = 1;
