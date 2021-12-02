INSERT INTO SECURITY.USERS (ID, LOGIN, PASSWORD)
VALUES
(1, 'admin', md5('admin_pass')),
(2, 'user', md5('user_pass'));

INSERT INTO SECURITY.ROLES (ID, ROLE)
VALUES
(1, 'admin'),
(2, 'user');

INSERT INTO SECURITY.USER_ROLES (USER_ID, ROLE_ID)
VALUES
(1, 1),
(2, 2);
