INSERT INTO app_user (user_name, email) VALUES ('user1', 'user1@doamin.com'); --id=1
INSERT INTO app_user (user_name, email) VALUES ('user2', 'user2@doamin.com'); --id=2
INSERT INTO app_user (user_name, email) VALUES ('user3', 'user3@doamin.com'); --id=3
INSERT INTO app_user (user_name, email) VALUES ('user4', 'user4@doamin.com'); --id=4
INSERT INTO app_user (user_name, email) VALUES ('user5', 'user5@doamin.com'); --id=5
INSERT INTO app_user (user_name, email) VALUES ('user6', 'user6@doamin.com'); --id=6
INSERT INTO app_user (user_name, email) VALUES ('user7', 'user7@doamin.com'); --id=7
INSERT INTO app_user (user_name, email) VALUES ('user8', 'user8@doamin.com'); --id=8
INSERT INTO app_user (user_name, email) VALUES ('user9', 'user9@doamin.com'); --id=9
INSERT INTO app_user (user_name, email) VALUES ('user10', 'user10@doamin.com'); --id=10

INSERT INTO app_auth_profile (auth_source_type, enabled, password) VALUES (1, TRUE, '{bcrypt}$2a$10$icsONDurXiIqCBLahemVM.a.wFBU37XITX38fKjGodkqKY2yKRXfy'); --id=1

INSERT INTO app_user_auth_profiles (app_user_id, auth_profiles_id) VALUES (1, 1);

