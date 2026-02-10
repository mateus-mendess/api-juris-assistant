CREATE TABLE IF NOT EXISTS lawyer(
    id uuid PRIMARY KEY,

    name VARCHAR(50) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    oab_number VARCHAR(10) NOT NULL,
    oab_state VARCHAR(2) NOT NULL,
    phone VARCHAR(15) UNIQUE NOT NULL,
    profile_photo VARCHAR(255),

    FOREIGN KEY (id) REFERENCES user_account(id),

    CONSTRAINT uq_lawyer_oab
    UNIQUE (oab_number, oab_state)
);