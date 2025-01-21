DROP DATABASE IF EXISTS stockmaster3000;
-- Create the database
CREATE DATABASE stockmaster3000;

-- Switch to the database
USE stockmaster3000;

-- Create the users table
CREATE TABLE _users (
                        id INT AUTO_INCREMENT PRIMARY KEY,       -- Unique ID for each user
                        username VARCHAR(50) NOT NULL UNIQUE,   -- Username, must be unique
                        password VARCHAR(255) NOT NULL          -- Password for the user
);

-- Drop the user account 'appuser' if it exists
DROP USER IF EXISTS 'saed'@'localhost';

-- Create the 'appuser' account with a secure password
CREATE USER 'saed'@'localhost' IDENTIFIED BY '12345678';

-- Grant appropriate privileges to 'appuser'
GRANT ALL PRIVILEGES ON stockmaster3000.* TO 'saed'@'localhost';

-- Flush privileges to apply the changes
FLUSH PRIVILEGES;