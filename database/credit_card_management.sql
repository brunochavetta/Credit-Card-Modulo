CREATE DATABASE credit_card_management; 

USE credit_card_management;

CREATE TABLE accounts (
    id binary(16) PRIMARY KEY,
    account_number VARCHAR(50) NOT NULL,
    name VARCHAR(50) NOT NULL,
    balance DOUBLE,
    status ENUM ('ACTIVE', 'CLOSED', 'FROZEN'),
    currency ENUM ('USD', 'MXP', 'COL', 'BOB', 'EUR'),
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE customers (
    id binary(16) PRIMARY KEY,
    full_name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    id_number VARCHAR(10) NOT NULL,
    phone VARCHAR(80),
    address VARCHAR(80),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    account_id binary(16),
    salary DECIMAL(15, 2),
    FOREIGN KEY (account_id) REFERENCES accounts(id)
);

CREATE TABLE user_notification_preferences (
	id binary(16) PRIMARY KEY,
    notifications BOOLEAN DEFAULT FALSE,
    customer_id binary(16) NOT NULL,
    transaction_mail BOOLEAN,
    transaction_app BOOLEAN,
    payment_mail BOOLEAN,
    payment_app BOOLEAN,
    limit_exceeded_mail BOOLEAN,
    limit_exceeded_app BOOLEAN,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

CREATE TABLE notifications (
    id binary(16) PRIMARY KEY,
    customer_id binary(16) NOT NULL,
    type VARCHAR(50) NOT NULL,
    channel ENUM('APP', 'EMAIL'),
    subject VARCHAR(50) NOT NULL,
    message VARCHAR(200) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    seen BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

CREATE TABLE credit_card_applications (
    id binary(16) PRIMARY KEY,
    customer_id binary(16) NOT NULL,
    application_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('submitted', 'in_review', 'accepted', 'rejected', 'completed') NOT NULL,
    company_id varchar(11),
    company_name varchar(40),
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

CREATE TABLE application_documents (
    id binary(16) PRIMARY KEY,
    application_id binary(16) NOT NULL,
    document_type VARCHAR(50) NOT NULL,
    file_name VARCHAR(100) NOT NULL,
    mime_type VARCHAR(50) NOT NULL,
    content LONGBLOB NOT NULL,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (application_id) REFERENCES credit_card_applications(id) ON DELETE CASCADE
);

CREATE TABLE credit_card_type (
    id binary(16) PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL,
    description VARCHAR (100),
    max_credit_amount DECIMAL(15, 2) NOT NULL,
    monthly_fee DECIMAL(10, 2) NOT NULL
);

CREATE TABLE credit_cards (
    id binary(16) PRIMARY KEY,
    card_number VARCHAR(20) NOT NULL UNIQUE,
    issue_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expiration_date TIMESTAMP NOT NULL,
    select_credit_limit DECIMAL(15, 2) NOT NULL,
    total_spend DECIMAL(10,2) NOT NULL,
    credit_card_applications_id binary(16) NOT NULL,
    status ENUM('active', 'inactive', 'blocked') NOT NULL,
    credit_card_type_id binary(16) NOT NULL,
	security_code INT DEFAULT 0 NOT NULL UNIQUE,
    FOREIGN KEY (credit_card_applications_id) REFERENCES credit_card_applications(id) ON DELETE CASCADE,
    FOREIGN KEY (credit_card_type_id) REFERENCES credit_card_type(id)
);

CREATE TABLE credit_card_debts (
    id binary(16) PRIMARY KEY,
    card_id binary(16) NOT NULL,
    outstanding_amount DECIMAL(15, 2) NOT NULL,
    due_date DATE NOT NULL,
    month VARCHAR(10) NOT NULL,
    status ENUM('pending', 'partial_paid', 'full_paid') NOT NULL,
	FOREIGN KEY (card_id) REFERENCES credit_cards(id) ON DELETE CASCADE
);

CREATE TABLE credit_card_transactions (
    id binary(16) PRIMARY KEY,
    card_id binary(16) NOT NULL,
    debt_id binary(16) NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    amount DECIMAL(15, 2) NOT NULL,
    description VARCHAR(200),
    FOREIGN KEY (card_id) REFERENCES credit_cards(id) ON DELETE CASCADE,
    FOREIGN KEY (debt_id) REFERENCES credit_card_debts(id) ON DELETE CASCADE
);

CREATE TABLE credit_card_payments (
    id binary(16) PRIMARY KEY,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    amount DECIMAL(15, 2) NOT NULL,
    debt_id binary(16) NOT NULL,
    FOREIGN KEY (debt_id) REFERENCES credit_card_debts(id) ON DELETE CASCADE
);

-- EJEMPLOS:

-- Insertar un UUID de tipo binario
INSERT INTO accounts (id, account_number, name, balance, status, currency)
VALUES (UUID_TO_BIN(UUID()), '123456789', 'Current account', 3500.00, 'ACTIVE', 'USD');

-- Traducir el binario a un lenguaje entendible por humanos
SELECT CONCAT_WS('-',
    SUBSTR(HEX(id), 1, 8),
    SUBSTR(HEX(id), 9, 4),
    SUBSTR(HEX(id), 13, 4),
    SUBSTR(HEX(id), 17, 4),
    SUBSTR(HEX(id), 21)
) AS uuid
FROM accounts;

--  Traducir del lenguaje entendible por humanos a tipo binario
SELECT *
FROM customers
WHERE id = UUID_TO_BIN('D1A1A9EB-A175-11EF-A1E2-00410E4723A3');