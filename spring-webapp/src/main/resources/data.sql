--Users
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

--AuthProfiles
INSERT INTO app_auth_profile (auth_source_type, enabled, password) VALUES (1, TRUE, '{bcrypt}$2a$10$icsONDurXiIqCBLahemVM.a.wFBU37XITX38fKjGodkqKY2yKRXfy'); --id=1, user1, password='abc123'
INSERT INTO app_auth_profile (auth_source_type, enabled, password) VALUES (1, TRUE, '{bcrypt}$2a$10$icsONDurXiIqCBLahemVM.a.wFBU37XITX38fKjGodkqKY2yKRXfy'); --id=1, user1, password='abc123'

--User to AuthProfiles Mappings
INSERT INTO app_user_auth_profiles (user_id, auth_profile_id) VALUES (1, 1);
INSERT INTO app_user_auth_profiles (user_id, auth_profile_id) VALUES (2, 2);

--Permissions
INSERT INTO app_permission (name) VALUES ('READ_ALL'); --id=1
INSERT INTO app_permission (name) VALUES ('CREATE_ALL'); --id=2
INSERT INTO app_permission (name) VALUES ('UPDATE_ALL'); --id=3
INSERT INTO app_permission (name) VALUES ('DELETE_ALL'); --id=4

--Roles
INSERT INTO app_role (name) VALUES ('ROLE_ADMIN');     --id=1
INSERT INTO app_role (name) VALUES ('ROLE_GUEST');     --id=2
INSERT INTO app_role (name) VALUES ('ROLE_DEVELOPER'); --id=3
INSERT INTO app_role (name) VALUES ('ROLE_AUDITOR');   --id=4

--Roles to Permissions Mappings
INSERT INTO app_role_permissions (role_id, permission_id) VALUES (1,1); -- ADMIN -> READ_ALL
INSERT INTO app_role_permissions (role_id, permission_id) VALUES (1,2); -- ADMIN -> CREATE_ALL
INSERT INTO app_role_permissions (role_id, permission_id) VALUES (1,3); -- ADMIN -> UPDATE_ALL
INSERT INTO app_role_permissions (role_id, permission_id) VALUES (1,4); -- ADMIN -> DELETE_ALL
INSERT INTO app_role_permissions (role_id, permission_id) VALUES (3,1); -- DEVELOPER -> READ_ALL
INSERT INTO app_role_permissions (role_id, permission_id) VALUES (3,2); -- DEVELOPER -> CREATE_ALL
INSERT INTO app_role_permissions (role_id, permission_id) VALUES (3,3); -- DEVELOPER -> UPDATE_ALL
INSERT INTO app_role_permissions (role_id, permission_id) VALUES (3,4); -- DEVELOPER -> DELETE_ALL


--Roles to Users Mappings
INSERT INTO app_user_roles (user_id, role_id) VALUES (1,1); -- user1 -> ADMIN
INSERT INTO app_user_roles (user_id, role_id) VALUES (2,3); -- user2 -> DEVELOPER
