CREATE TABLE IF NOT EXISTS user (
    id uuid,
    email text,
    password text,
    first_name text,
    last_name text,
    PRIMARY KEY (email)
);

CREATE INDEX id_index ON user (id);

CREATE TABLE IF NOT EXISTS match (
    id timeuuid,
    first_participant uuid,
    second_participant uuid,
    result int,
    created timestamp,
    scheduled timestamp,
    status int,
    PRIMARY KEY (id, first_participant, second_participant)
);

CREATE INDEX first_participant_index ON match (first_participant);
CREATE INDEX second_participant_index ON match (second_participant);