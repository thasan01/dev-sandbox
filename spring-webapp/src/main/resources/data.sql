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
INSERT INTO app_auth_profile (auth_source_type, enabled, password) VALUES (1, TRUE, '{bcrypt}$2a$10$icsONDurXiIqCBLahemVM.a.wFBU37XITX38fKjGodkqKY2yKRXfy'); --id=2, user2, password='abc123'
INSERT INTO app_auth_profile (auth_source_type, enabled, password) VALUES (1, TRUE, '{bcrypt}$2a$10$icsONDurXiIqCBLahemVM.a.wFBU37XITX38fKjGodkqKY2yKRXfy'); --id=3, user3, password='abc123'

--User to AuthProfiles Mappings
INSERT INTO app_user_auth_profiles (user_id, auth_profile_id) VALUES (1, 1);
INSERT INTO app_user_auth_profiles (user_id, auth_profile_id) VALUES (2, 2);
INSERT INTO app_user_auth_profiles (user_id, auth_profile_id) VALUES (3, 3);

--Permissions (<CATEGORY>::<RESOURCE>::<OPERATION>)
INSERT INTO app_permission (name) VALUES ('*::*::*');      --id=1
INSERT INTO app_permission (name) VALUES ('*::*::CREATE'); --id=2
INSERT INTO app_permission (name) VALUES ('*::*::UPDATE'); --id=3
INSERT INTO app_permission (name) VALUES ('*::*::DELETE'); --id=4
INSERT INTO app_permission (name) VALUES ('*::*::VIEW');   --id=5
INSERT INTO app_permission (name) VALUES ('UI::*::CREATE'); --id=6
INSERT INTO app_permission (name) VALUES ('UI::*::UPDATE'); --id=7
INSERT INTO app_permission (name) VALUES ('UI::*::DELETE'); --id=8
INSERT INTO app_permission (name) VALUES ('UI::*::VIEW');   --id=9
INSERT INTO app_permission (name) VALUES ('API::*::CREATE'); --id=10
INSERT INTO app_permission (name) VALUES ('API::*::UPDATE'); --id=11
INSERT INTO app_permission (name) VALUES ('API::*::DELETE'); --id=12
INSERT INTO app_permission (name) VALUES ('API::*::VIEW');   --id=13

--Roles
INSERT INTO app_role (name) VALUES ('ADMIN');     --id=1
INSERT INTO app_role (name) VALUES ('GUEST');     --id=2
INSERT INTO app_role (name) VALUES ('DEVELOPER'); --id=3

--Roles to Permissions Mappings
INSERT INTO app_role_permissions (role_id, permission_id) VALUES (1,1); -- ADMIN -> *::*::*
INSERT INTO app_role_permissions (role_id, permission_id) VALUES (2,9); -- GUEST -> UI::*::VIEW
INSERT INTO app_role_permissions (role_id, permission_id) VALUES (2,13); -- GUEST -> API::*::VIEW
INSERT INTO app_role_permissions (role_id, permission_id) VALUES (3,10); -- DEVELOPER -> API::*::CREATE
INSERT INTO app_role_permissions (role_id, permission_id) VALUES (3,11); -- DEVELOPER -> API::*::UPDATE
INSERT INTO app_role_permissions (role_id, permission_id) VALUES (3,12); -- DEVELOPER -> API::*::UPDATE
INSERT INTO app_role_permissions (role_id, permission_id) VALUES (3,13); -- DEVELOPER -> API::*::DELETE

--Roles to Users Mappings
INSERT INTO app_user_roles (user_id, role_id) VALUES (1,1); -- user1 -> ADMIN
INSERT INTO app_user_roles (user_id, role_id) VALUES (2,3); -- user2 -> DEVELOPER
INSERT INTO app_user_roles (user_id, role_id) VALUES (3,2); -- user3 -> GUEST
