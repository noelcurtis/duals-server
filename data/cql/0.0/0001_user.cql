CREATE TABLE IF NOT EXISTS user (
    id uuid,
    email text,
    password text,
    first_name text,
    last_name text,
    PRIMARY KEY (email)
);

CREATE INDEX id_index ON user (id);