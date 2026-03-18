CREATE TABLE IF NOT EXISTS users(
    id UUID PRIMARY KEY,

    email VARCHAR(120) UNIQUE NOT NULL,
    provider VARCHAR(15) NOT NULL,
    provider_user_id VARCHAR(255) NOT NULL,
    password VARCHAR(255),
    active BOOLEAN NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_provider_user UNIQUE (provider, provider_user_id)
);

CREATE TABLE IF NOT EXISTS roles(
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS user_roles(
    user_id uuid NOT NULL,
    role_id INT NOT NULL,

    PRIMARY KEY(user_id, role_id),

    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS addresses(
    id uuid PRIMARY KEY,

    street VARCHAR(80) NOT NULL,
    number VARCHAR(6) NOT NULL,
    complement VARCHAR(40),
    neighborhood VARCHAR(60) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(2) NOT NULL,
    zip_code VARCHAR(9) NOT NULL
    );

CREATE TABLE IF NOT EXISTS attorney(
    id uuid PRIMARY KEY,
    user_id uuid UNIQUE NOT NULL,

    name VARCHAR(60) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,

    oab_number VARCHAR(10) NOT NULL,
    oab_state VARCHAR(2) NOT NULL,

    phone VARCHAR(15) NOT NULL,
    profile_photo VARCHAR(255),

    address_id UUID NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (address_id) REFERENCES addresses(id),

    CONSTRAINT uq_lawyer_oab
    UNIQUE (oab_number, oab_state)
);

CREATE TABLE IF NOT EXISTS client(
    id uuid PRIMARY KEY,

    name VARCHAR(75) NOT NUll,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    nationality VARCHAR(40) NOT NULL,
    marital_status VARCHAR(10) NOT NULL,
    work VARCHAR(40) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    active BOOLEAN NOT NULL,

    attorney_id uuid NOT NULL,
    address_id uuid UNIQUE NOT NULL,

    FOREIGN KEY (attorney_id) REFERENCES attorney(id),
    FOREIGN KEY (address_id) REFERENCES addresses(id)
);


CREATE TABLE IF NOT EXISTS documents(
    id uuid PRIMARY KEY,

    file_name VARCHAR(80) NOT NULL,
    file_path VARCHAR(255) NOT NULL,

    document_type VARCHAR(20) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    client_id uuid NOT NULL,
    FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE
);