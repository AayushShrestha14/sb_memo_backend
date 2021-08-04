BEGIN
    DECLARE @creditMemoId SMALLINT
    SET @creditMemoId = (SELECT id FROM permission WHERE permission_name = 'Credit Memo')

    DELETE FROM role_permission_rights WHERE permission_id = @creditMemoId
    DELETE FROM permission_sub_navs WHERE permission_id = @creditMemoId
    DELETE FROM sub_nav WHERE sub_nav_name in ('Credit Memo Type')
    DELETE FROM permission WHERE id = @creditMemoId
END;
