CREATE TABLE IF NOT EXISTS power_of_attorney(
    id uuid PRIMARY KEY,

    name VARCHAR(80) NOT NULL,
    file_path VARCHAR(255) NOT NULL,

    type VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    client_id uuid NOT NULL,
    FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE
);