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

CREATE INDEX ON user_ladder (name);

CREATE TABLE IF NOT EXISTS ladder (
    ladder_id timeuuid,
    name text,
    activity text,
    create_time timestamp,
    PRIMARY KEY (ladder_id)
);

