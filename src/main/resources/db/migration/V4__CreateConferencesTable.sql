CREATE TABLE conferences (
    id SERIAL,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    total_participants INTEGER NOT NULL DEFAULT 0,
    participant_limit INTEGER,
    user_id INTEGER NOT NULL,
    CONSTRAINT conferences_pkey PRIMARY KEY (id)
);

CREATE INDEX idx_conferences_user_id
    ON conferences(user_id);