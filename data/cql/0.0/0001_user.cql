CREATE TABLE IF NOT EXISTS user (
    id uuid,
    email text,
    password text,
    first_name text,
    last_name text,
    auth_token text,
    auth_token_update_time timestamp,
    update_time timestamp,
    PRIMARY KEY (email)
);

CREATE INDEX ON user (id);
CREATE INDEX ON user (auth_token);