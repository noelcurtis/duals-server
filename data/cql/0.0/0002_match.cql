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