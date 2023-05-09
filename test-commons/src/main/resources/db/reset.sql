SELECT CONCAT('DROP DATABASE IF EXISTS `', schema_name, '`;')
FROM information_schema.schemata
WHERE schema_name NOT IN ('mysql', 'information_schema', 'performance_schema')
AND schema_name NOT LIKE 'temp%'
AND schema_name NOT LIKE 'sys%'
AND schema_name NOT LIKE 'sisa%';

DROP DATABASE IF EXISTS `app`;