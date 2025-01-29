DROP DATABASE IF EXISTS stockmaster3000;
-- Create the database
CREATE DATABASE stockmaster3000;

-- Switch to the database
USE stockmaster3000;

-- Create the users table
CREATE TABLE _users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,       -- Changed to BIGINT for scalability
    username VARCHAR(50) NOT NULL UNIQUE,       -- Username, should remain unique
    password VARCHAR(255) NOT NULL,             -- Increased length for password hash (supporting bcrypt or similar)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Track when the user was created
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Track updates
);

-- Create the Categories table
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- auto-incrementing primary key for scalability
    name VARCHAR(255) NOT NULL UNIQUE,      -- Ensure categories are unique
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Track when the category was created
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Track updates
);

-- Inserting default categories
INSERT INTO categories (name) VALUES ('Food'), ('Beverages'), ('Electronics');

-- Create the Inventories table
CREATE TABLE inventories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- auto-incrementing primary key
    name VARCHAR(255) NOT NULL,             -- name of the inventory item, non-nullable
    user_id BIGINT,                         -- Optional foreign key to link inventory to users (for inventory ownership)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Track when the inventory was created
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Track updates
    CONSTRAINT fk_inventory_user FOREIGN KEY (user_id) REFERENCES _users(id)  -- Linking inventories to users (if applicable)
);

-- Inserting inventories
INSERT INTO inventories (name) VALUES ('Fridge');

-- Create Suppliers table
CREATE TABLE suppliers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- Auto-incrementing primary key
    name VARCHAR(255) NOT NULL UNIQUE,     -- Ensure suppliers are unique
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Track when the supplier was added
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Track updates
);

-- Inserting suppliers
INSERT INTO suppliers (name) VALUES ('Fazer'), ('Valio'), ('Rainbow');

-- Create the Reports table
CREATE TABLE reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,       -- Auto-incrementing primary key
    start_date DATE,                            -- Start date of the report
    end_date DATE,                              -- End date of the report
    summary VARCHAR(255),                       -- Summary of the report
    summary_data JSON,                          -- JSON column for detailed report data (if supported)
    inventory_id BIGINT NOT NULL,               -- Foreign key referencing the inventory
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Track when the report was created
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Track updates

    CONSTRAINT fk_reports_inventory FOREIGN KEY (inventory_id) REFERENCES inventories(id)
);

-- Create the Products table
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,      -- Auto-incrementing primary key
    name VARCHAR(255) NOT NULL,                 -- Name of the product
    price DOUBLE NOT NULL,                      -- Price of the product
    quantity INT NOT NULL,                      -- Quantity of the product
    nutritions JSON,                            -- JSON data type for storing nutritional values (depends on DB)
    amountOfDaysUntilExpiration INT,            -- Expiration days for the product
    supplier_id BIGINT NOT NULL,                -- Foreign key for the supplier
    category_id BIGINT NOT NULL,                -- Foreign key for the category
    inventory_id BIGINT NOT NULL,               -- Foreign key for the inventory
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Track when the product was added
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Track updates

    -- Foreign key constraints:
    CONSTRAINT fk_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories(id),
    CONSTRAINT fk_products_inventory FOREIGN KEY (inventory_id) REFERENCES inventories(id)
);

-- Drop the user account 'viet' if it exists
DROP USER IF EXISTS 'viet'@'localhost';

-- Create the 'viet' account with a secure password
CREATE USER 'viet'@'localhost' IDENTIFIED BY 'root';

-- Grant appropriate privileges to 'viet'
GRANT ALL PRIVILEGES ON stockmaster3000.* TO 'viet'@'localhost';

-- Flush privileges to apply the changes
FLUSH PRIVILEGES;
