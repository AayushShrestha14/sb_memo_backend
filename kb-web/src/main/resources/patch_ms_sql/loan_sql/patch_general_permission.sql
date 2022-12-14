BEGIN

        DECLARE
@count smallint

    SET @count = (Select count(*) from permission)
    if (@count <10)
BEGIN
            SET IDENTITY_INSERT permission ON
            INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
            VALUES (1, 'Branch', 'camera-outline', '/home/admin/branch', 10, 1)
            INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
            VALUES (3, 'Role and Permission', 'lock-outline', '/home/admin/role', 19, 1)
            INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
            VALUES (6, 'Users', 'person-add-outline', '/home/admin/user', 2, 1)
            INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
            VALUES (13, 'Document', 'book-outline', '/home/admin/document', 55, 1)
            INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
            VALUES (100, 'Role Hierarchy', 'shield-outline', '/home/admin/roleHierarchy', 20, 1)
            INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
            VALUES (17, 'Dashboard', 'home-outline', '/home/admin/dashboard', 1, 1)
            INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
            VALUES (105, 'Email Configuration', 'inbox-outline', '/home/admin/email-config', 55, 1)
             INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
            VALUES (103, 'Memo-Catalogue', 'layers-outline', '/home/admin/memo-catalogue', 61, 1)
             INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
             VALUES (104, 'Memo-Dashboard', 'settings-2-outline', 'null', 62, 1)
            INSERT INTO permission (id, permission_name, fa_icon, front_url, orders, status)
            VALUES (160, 'Transfer Document', 'book-outline', '/home/loan/transfer-doc', 119, 1)

            SET IDENTITY_INSERT permission OFF


            -- **********************MAP PERMISSION TO ADMIN ROLE DEFAULT****************************** --
-- BRANCH --
            SET IDENTITY_INSERT role_permission_rights ON
            INSERT INTO role_permission_rights (id, created_at, last_modified_at, permission_id, role_id)
            VALUES (1, '2019-04-04 13:17:01', '2019-04-04 13:17:07', 1, 1)


-- Role And Permission --
            INSERT INTO role_permission_rights (id, created_at, last_modified_at, permission_id, role_id)
            VALUES (3, '2019-04-04 13:17:01', '2019-04-04 13:17:07', 3, 1)


-- Users --
            INSERT INTO role_permission_rights (id, created_at, last_modified_at, permission_id, role_id)
            VALUES (6, '2019-04-04 13:17:01', '2019-04-04 13:17:07', 6, 1)

-- Document --
            INSERT INTO role_permission_rights (id, created_at, last_modified_at, permission_id, role_id)
            VALUES (13, '2019-04-04 13:17:01', '2019-04-04 13:17:07', 13, 1)

-- Role Hierarchy --
            INSERT INTO role_permission_rights (id, created_at, last_modified_at, permission_id, role_id)
            VALUES (100, '2019-04-04 13:17:01', '2019-04-04 13:17:07', 100, 1)


-- Dashboard --
            INSERT INTO role_permission_rights (id, created_at, last_modified_at, permission_id, role_id)
            VALUES (17, '2019-04-04 13:17:01', '2019-04-04 13:17:07', 17, 1)


-- Menu Catalogue --
            INSERT INTO role_permission_rights (id, created_at, last_modified_at, permission_id, role_id)
            VALUES (103, '2019-04-04 13:17:01', '2019-04-04 13:17:07', 103, 1)
            -- Menu Dashboard --
             INSERT INTO role_permission_rights (id, created_at, last_modified_at, permission_id, role_id)
            VALUES (104, '2019-04-04 13:17:01', '2019-04-04 13:17:07', 104, 3)




            SET IDENTITY_INSERT role_permission_rights OFF
            -- **********************MAP PERMISSION TO ADMIN ROLE DEFAULT****************************** --

-- ************************BRANCH*******************************************
            SET IDENTITY_INSERT url_api ON
            INSERT INTO url_api (id, api_url, type) values (1, '/v1/branch', 'ADD BRANCH')
            INSERT INTO url_api (id, api_url, type) values (2, '/v1/branch', 'EDIT BRANCH')
            INSERT INTO url_api (id, api_url, type) values (3, '/v1/branch/csv', 'DOWNLOAD CSV')
            INSERT INTO url_api (id, api_url, type) values (4, '/v1/branch/get', 'VIEW BRANCH')
            SET IDENTITY_INSERT url_api OFF

            INSERT INTO permission_api_list(permission_id, api_list_id) values (1, 1)
            INSERT INTO permission_api_list(permission_id, api_list_id) values (1, 2)
            INSERT INTO permission_api_list(permission_id, api_list_id) values (1, 3)
            INSERT INTO permission_api_list(permission_id, api_list_id) values (1, 4)


            -- ************************BRANCH*******************************************


-- ************************USER*******************************************
            SET IDENTITY_INSERT url_api ON
            INSERT INTO url_api (id, api_url, type) values (12, '/v1/user', 'ADD USER')
            INSERT INTO url_api (id, api_url, type) values (30, '/v1/user/csv', 'DOWNLOAD CSV')
            SET IDENTITY_INSERT url_api OFF
            INSERT INTO permission_api_list(permission_id, api_list_id) values (6, 12)
            INSERT INTO permission_api_list(permission_id, api_list_id) values (6, 30)



            INSERT INTO role_permission_rights_api_rights values (1, 1)
            INSERT INTO role_permission_rights_api_rights values (1, 2)
            INSERT INTO role_permission_rights_api_rights values (1, 3)
            INSERT INTO role_permission_rights_api_rights values (1, 4)

END
END;















