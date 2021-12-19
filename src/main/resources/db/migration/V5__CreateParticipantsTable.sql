CREATE TABLE participants (
    id SERIAL,
    name VARCHAR(200) NOT NULL,
    email VARCHAR(255) NOT NULL,
    subscribed_at TIMESTAMP NOT NULL,
    conference_id INTEGER NOT NULL,
    CONSTRAINT participants_pkey PRIMARY KEY (id),
    CONSTRAINT conference_email_unique UNIQUE (email, conference_id)
);

CREATE INDEX idx_participants_conference_id
    ON participants(conference_id);
