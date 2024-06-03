desc board;
select * from board;

-- join
insert into board values(1, '첫 글', '첫 번째 글입니다.', 1, now(), 1, 1, 1, 1);
insert into board values(2, '두 번째 글', '두 번째 글입니다.', 1, now(), 1, 1, 1, 2);

-- login
select no, name from board where email = 'jyspark46@gmail.com' and password=password('1234');

-- test
select * from board;