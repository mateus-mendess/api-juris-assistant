CREATE TABLE IF NOT EXISTS address(
    id uuid PRIMARY KEY,
    street VARCHAR(80) NOT NULL,
    number VARCHAR(6) NOT NULL,
    complement VARCHAR(40),
    neighborhood VARCHAR(60) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(2) NOT NULL,
    zip_code VARCHAR(9) NOT NULL
);