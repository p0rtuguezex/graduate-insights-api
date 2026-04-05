-- Initialization script placeholder for local Docker MySQL bootstrap.
-- Keep this file present so docker compose volume mount does not fail.

CREATE DATABASE IF NOT EXISTS graduate_insights
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;