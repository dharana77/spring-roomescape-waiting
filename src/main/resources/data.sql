INSERT INTO reservation_time (reservation_time_id, start_at)
VALUES  (90001, '10:00'),
        (90002, '12:00'),
        (90003, '14:00'),
        (90004, '16:00'),
        (90005, '18:00'),
        (90006, '20:00');

INSERT INTO theme (theme_id, name, description, thumbnail)
VALUES (90001, '고양이 테마', '고양이 카페에서 탈출하는 테마입니다', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       (90002, '쏘우 테마', '심약자 주의! 공포 테마입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       (90003, '포켓몬 테마', '포켓몬고를 설치하셔야 합니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO member (member_id, name, email, password)
VALUES  (90001, '관리자', 'tester@gmail.com', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3');

INSERT INTO member_role (member_role_id, name, member_id)
VALUES  (90001, 'ADMIN', 90001);

INSERT INTO member (member_id, name, email, password)
VALUES  (90002, '신규_유저', 'newbie@gmail.com', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3');

INSERT INTO member_role (member_role_id, name, member_id)
VALUES  (90002, 'GUEST', 90002);
