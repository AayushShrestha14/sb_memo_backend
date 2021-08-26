BEGIN
    DECLARE @count SMALLINT
    SET @count = (SELECT COUNT(*) FROM permission WHERE permission_name = 'Memo Type')
    if (@count = 0)
        BEGIN
            DECLARE @creditMemoId SMALLINT
            INSERT INTO permission (permission_name, fa_icon, front_url, orders, status)
            VALUES ('Memo Type', 'email-outline', null, 65, 1)
            SET @creditMemoId = (SELECT id FROM permission WHERE permission_name = 'Memo Type')

            SET IDENTITY_INSERT sub_nav ON
            INSERT INTO sub_nav (id, sub_nav_name, front_url, fa_icon)
            VALUES (12, 'Create Memo Type', '/home/credit-memo/type', 'email-outline')
            INSERT INTO sub_nav (id, sub_nav_name, front_url, fa_icon)
            VALUES (13, 'Create Memo Category', '/home/credit-memo/category', 'email-outline')
            SET IDENTITY_INSERT sub_nav OFF

            INSERT INTO permission_sub_navs (permission_id, sub_navs_id)
            VALUES (@creditMemoId, 12)
            INSERT INTO permission_sub_navs (permission_id, sub_navs_id)
            VALUES (@creditMemoId, 13)

            INSERT INTO role_permission_rights (created_at, last_modified_at, permission_id, role_id)
            VALUES ('2019-04-04 13:17:01', '2019-04-04 13:17:07', @creditMemoId, 1)
        END
END;

BEGIN
    DECLARE @count SMALLINT
    SET @count = (SELECT COUNT(*) FROM permission WHERE permission_name = 'Memo-Dashboard')
    DECLARE @count2 SMALLINT
    SET @count2= (SELECT COUNT(*) FROM sub_nav WHERE sub_nav_name = 'Pending')
    if (@count = 1 AND @count2 = 0)
        BEGIN
            SET IDENTITY_INSERT sub_nav ON
--             INSERT INTO sub_nav (id, sub_nav_name, front_url, fa_icon)
--             VALUES (13, 'Compose', '/home/credit-memo/compose', 'email-outline')
            INSERT INTO sub_nav (id, sub_nav_name, front_url, fa_icon)
            VALUES (14, 'Pending', '/home/credit-memo/inbox', 'inbox')
            INSERT INTO sub_nav (id, sub_nav_name, front_url, fa_icon)
            VALUES (15, 'Approved', '/home/credit-memo/approved', 'money-check')
            INSERT INTO sub_nav (id, sub_nav_name, front_url, fa_icon)
            VALUES (16, 'Rejected', '/home/credit-memo/rejected', 'ban')
            SET IDENTITY_INSERT sub_nav OFF


            INSERT INTO permission_sub_navs (permission_id, sub_navs_id)
            VALUES (104, 13)
             INSERT INTO permission_sub_navs (permission_id, sub_navs_id)
            VALUES (104, 14)
            INSERT INTO permission_sub_navs (permission_id, sub_navs_id)
            VALUES (104, 15)
            INSERT INTO permission_sub_navs (permission_id, sub_navs_id)
            VALUES (104, 16)


        END
END;
