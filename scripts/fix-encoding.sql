-- Run this script in MySQL to recreate the schema with UTF-8 support
DROP DATABASE IF EXISTS graduate_insights;
CREATE DATABASE graduate_insights CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE graduate_insights;
SOURCE src/main/resources/tables.sql;
SOURCE src/main/resources/inserts.sql;
