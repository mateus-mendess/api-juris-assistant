CREATE TABLE IF NOT EXISTS user_roles(
    user_id uuid,
    role_id INT,

    PRIMARY KEY(user_id, role_id),

    FOREIGN KEY (user_id) REFERENCES user_account(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);