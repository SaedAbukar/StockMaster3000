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

-- Create the Categories table
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- auto-incrementing primary key
    name VARCHAR(255) NOT NULL             -- name of the category, non-nullable
);

-- Inserting default categories
INSERT INTO categories (name) VALUES ('Food'), ('Beverages'), ('Electronics');


-- Create the Inventories table
CREATE TABLE inventories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- auto-incrementing primary key
    name VARCHAR(255) NOT NULL             -- name of the inventory item, non-nullable
);

-- Inserting inventories
INSERT INTO inventories (name) VALUES ('Fridge');


-- Create Suppliers table
CREATE TABLE suppliers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- Auto-incrementing primary key
    name VARCHAR(255) NOT NULL              -- Name of the supplier (cannot be null)
);

-- Inserting suppliers
INSERT INTO suppliers (name) VALUES ('Fazer'), ('Valio'), ('Rainbow');


-- Create the Reports table
CREATE TABLE reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,       -- Auto-incrementing primary key
    start_date DATE,                            -- Start date of the report
    end_date DATE,                              -- End date of the report
    summary VARCHAR(255),                       -- Summary of the report
    summary_data JSON,                          -- If your DB supports JSON, otherwise TEXT
    inventory_id BIGINT NOT NULL,               -- Foreign key referencing the inventory

    CONSTRAINT fk_inventory FOREIGN KEY (inventory_id) REFERENCES inventories(id)
);



-- Create the Products table
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,      -- auto-incrementing primary key
    name VARCHAR(255) NOT NULL,                 -- name of the product
    price DOUBLE NOT NULL,                      -- price of the product
    quantity INT NOT NULL,                      -- quantity of the product
    nutritions JSON,                            -- JSON data type for storing nutritional values (depends on DB)
    amountOfDaysUntilExpiration INT,        -- expiration days for the product
    supplier_id BIGINT NOT NULL,                -- foreign key for the supplier
    category_id BIGINT NOT NULL,                -- foreign key for the category
    inventory_id BIGINT NOT NULL,               -- foreign key for the inventory

    -- Foreign key constraints:
    CONSTRAINT fk_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories(id),
    CONSTRAINT fk_inventory FOREIGN KEY (inventory_id) REFERENCES inventories(id)
);



-- Drop the user account 'appuser' if it exists
DROP USER IF EXISTS 'viet'@'localhost';

-- Create the 'appuser' account with a secure password
CREATE USER 'viet'@'localhost' IDENTIFIED BY 'root';

-- Grant appropriate privileges to 'appuser'
GRANT ALL PRIVILEGES ON stockmaster3000.* TO 'viet'@'localhost';

-- Flush privileges to apply the changes
FLUSH PRIVILEGES;