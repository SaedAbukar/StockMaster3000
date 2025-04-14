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

-- Create the Inventories table
CREATE TABLE inventories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- auto-incrementing primary key
    name VARCHAR(255) NOT NULL,             -- name of the inventory item, non-nullable
    user_id BIGINT,                         -- Optional foreign key to link inventory to users (for inventory ownership)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Track when the inventory was created
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Track updates
    CONSTRAINT fk_inventory_user FOREIGN KEY (user_id) REFERENCES _users(id)  -- Linking inventories to users (if applicable)
);


-- Create Suppliers table
CREATE TABLE suppliers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- Auto-incrementing primary key
    name VARCHAR(255) NOT NULL UNIQUE,     -- Ensure suppliers are unique
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Track when the supplier was added
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Track updates
);


-- Create the Reports table
CREATE TABLE reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,       -- Auto-incrementing primary key
    summary TEXT NOT NULL,                       -- Summary of the report
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
    nutritions TEXT,                            -- TEXT data type for storing nutritional values (depends on DB)
    amountOfDaysUntilExpiration INT,            -- Expiration days for the product
    language_code VARCHAR(5) DEFAULT 'en',      -- Language code db localization
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

-- Optional - you can add these products after you've manually created a database, take into consideration that which inventory_id you're on
INSERT INTO products (name, price, quantity, nutritions, amountOfDaysUntilExpiration, language_code, supplier_id, category_id, inventory_id)
VALUES
-- English
('Whole Grain Bread', 3.49, 50, 'Calories: 220, Protein: 8g, Carbs: 36g, Fat: 3g', 5, 'en', 1, 2, 2),
('Organic Milk', 1.89, 120, 'Calories: 150, Protein: 8g, Carbs: 12g, Fat: 8g', 10, 'en', 2, 3, 2),
('Apple Juice', 2.39, 100, 'Calories: 120, Carbs: 28g, Sugars: 22g', 30, 'en', 3, 1, 2),
('Cheddar Cheese', 5.99, 80, 'Calories: 110, Protein: 7g, Carbs: 1g, Fat: 9g', 60, 'en', 4, 4, 2),

-- Finnish
('Ruisleipä', 2.99, 80, 'Kalorit: 200, Proteiini: 6g, Hiilihydraatit: 35g, Rasva: 2g', 7, 'fi', 3, 2, 2),
('Kauramaito', 2.59, 100, 'Kalorit: 120, Proteiini: 3g, Hiilihydraatit: 16g, Rasva: 4g', 14, 'fi', 2, 3, 2),
('Mustikkakeitto', 4.49, 60, 'Kalorit: 150, Proteiini: 2g, Hiilihydraatit: 37g, Rasva: 0g', 10, 'fi', 1, 5, 2),
('Kalakeitto', 5.29, 45, 'Kalorit: 250, Proteiini: 12g, Hiilihydraatit: 15g, Rasva: 18g', 5, 'fi', 4, 4, 2),

-- Greek
('Φέτα Τυρί', 4.75, 60, 'Θερμίδες: 264, Πρωτεΐνη: 14g, Υδατάνθρακες: 4g, Λίπος: 21g', 21, 'el', 4, 4, 2),
('Ελαιόλαδο', 6.90, 40, 'Θερμίδες: 119, Λίπος: 14g', 365, 'el', 4, 5, 2),
('Γιαούρτι', 3.20, 90, 'Θερμίδες: 130, Πρωτεΐνη: 8g, Υδατάνθρακες: 10g, Λίπος: 7g', 14, 'el', 5, 3, 2),
('Χυμός Πορτοκάλι', 2.50, 110, 'Θερμίδες: 100, Υδατάνθρακες: 25g, Σάκχαρα: 22g', 20, 'el', 3, 1, 2),
('Αχλάδι', 1.89, 130, 'Θερμίδες: 57, Πρωτεΐνη: 1g, Υδατάνθρακες: 15g, Λίπος: 0g', 7, 'el', 6, 2, 2),

-- Russian
('Гречневая крупа', 1.49, 200, 'Калории: 343, Белки: 13g, Углеводы: 72g, Жиры: 3g', 180, 'ru', 5, 6, 2),
('Кефир', 2.10, 90, 'Калории: 110, Белки: 9g, Углеводы: 6g, Жиры: 5g', 12, 'ru', 5, 3, 2),
('Молоко', 1.25, 150, 'Калории: 150, Белки: 8g, Углеводы: 12g, Жиры: 8g', 7, 'ru', 2, 3, 2),
('Картофель', 2.99, 200, 'Калории: 77, Белки: 2g, Углеводы: 17g, Жиры: 0g', 20, 'ru', 1, 2, 2),
('Шоколад', 4.59, 50, 'Калории: 250, Белки: 3g, Углеводы: 45g, Жиры: 12g', 180, 'ru', 6, 1, 2);


-- Drop the user account 'ivan' if it exists
DROP USER IF EXISTS 'viet'@'localhost';

-- Create the 'ivan' account with a secure password
CREATE USER 'viet'@'localhost' IDENTIFIED BY 'root';

-- Grant appropriate privileges to 'ivan'
GRANT ALL PRIVILEGES ON stockmaster3000.* TO 'viet'@'localhost';

-- Flush privileges to apply the changes
FLUSH PRIVILEGES;
