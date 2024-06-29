INSERT INTO member (member_id, name, email, password)
VALUES  (90001, '테스트_마스터', 'master@gmail.com', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3');

INSERT INTO member_role (member_role_id, name, member_id)
VALUES  (90001, 'ADMIN', 90001);
