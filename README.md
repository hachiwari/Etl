# Etl

# Polskie znaki w bazie i przy liquibase:
1) na bazie wykonać:
ALTER DATABASE `DB_NAME` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
2) dodać do linku jdbc: ?useUnicode=true&characterEncoding=UTF-8
jdbc:mysql://localhost:3306/DB_NAME?useUnicode=true&characterEncoding=UTF-8
