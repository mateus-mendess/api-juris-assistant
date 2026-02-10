CREATE TABLE  IF NOT EXISTS user_account(
    id uuid PRIMARY KEY,
    email VARCHAR(120) UNIQUE NOT NULL,
    password VARCHAR(255),
    provider VARCHAR(15) NOT NULL,
    is_enable BOOLEAN NOT NULL
    );