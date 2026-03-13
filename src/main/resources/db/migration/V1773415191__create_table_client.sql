CREATE TABLE IF NOT EXISTS client(
    id uuid PRIMARY KEY,
    name VARCHAR(75) NOT NUll,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    nationality VARCHAR(40) NOT NULL,
    marital_status VARCHAR(10) NOT NULL,
    work VARCHAR(40) NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL,
    active BOOLEAN NOT NULL,

    attorney_id uuid NOT NULL,
    address_id uuid UNIQUE NOT NULL,

    FOREIGN KEY (attorney_id) REFERENCES lawyer(id),
    FOREIGN KEY (address_id) REFERENCES address(id)
    );