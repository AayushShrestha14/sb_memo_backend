BEGIN
DECLARE @count smallint
SET @count = (Select count(*) from loan_cycle)

if(@count < 2)
BEGIN

SET IDENTITY_INSERT loan_cycle ON
INSERT  INTO loan_cycle (id, cycle, label, created_by_id, modified_by_id, created_at, last_modified_at, version)
VALUES ('1', 'New', 'Document required during new Loan', null, null, '2019-06-20', '2019-06-26', '0'),
('2', 'Re-New', 'Document required during re-new Loan', null, null , '2019-06-20', '2019-06-26', '0'),
('3', 'Closure', 'Document required while closing Loan', null, null , '2019-06-20', '2019-06-26', '0'),
('4', 'Eligibility', 'Document required loan eligibility', null, null , '2019-06-20', '2019-06-26', '0')
SET IDENTITY_INSERT loan_cycle OFF
END
END;

BEGIN
    DECLARE @count SMALLINT
    SET @count = (SELECT COUNT(*) FROM loan_cycle lc WHERE lc.cycle = 'Credit Memo')
    IF (@count = 0)
        BEGIN
            SET IDENTITY_INSERT loan_cycle ON
            INSERT INTO loan_cycle (id, cycle, label, created_by_id, modified_by_id, created_at, last_modified_at,
                                    version)
            VALUES (5, 'Credit Memo', 'Documents required for Credit Memo', null, null,
                    '2019-06-20', '2019-06-26', '0')
            SET IDENTITY_INSERT loan_cycle OFF
        END
END;
