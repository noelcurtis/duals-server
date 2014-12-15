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

CREATE TABLE IF NOT EXISTS match (
    id timeuuid,
    ladder_id uuid,
    first_participant uuid,
    second_participant uuid,
    result int,
    created timestamp,
    scheduled timestamp,
    status int,
    PRIMARY KEY ((id, ladder_id), first_participant, second_participant)
);

CREATE INDEX ON match (first_participant);
CREATE INDEX ON match (second_participant);

CREATE TABLE IF NOT EXISTS user_ladder (
    user_id uuid,
    ladder_id timeuuid,
    name text,
    activity text,
    creator boolean,
    scheduled_matches int,
    wins int,
    rank int,
    PRIMARY KEY (user_id, ladder_id)
)
WITH CLUSTERING ORDER BY (ladder_id DESC);

CREATE TABLE IF NOT EXISTS ladder (
    ladder_id timeuuid,
    name text,
    activity text,
    create_time timestamp,
    PRIMARY KEY (ladder_id)
);
