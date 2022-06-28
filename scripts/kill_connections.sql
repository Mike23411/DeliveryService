SELECT 
CONCAT('KILL ', id, ';') 
FROM INFORMATION_SCHEMA.PROCESSLIST 
WHERE `User` = 'root'
AND `Host` = '127.0.0.1'
AND `db` = 'DeliveryService';