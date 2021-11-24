CREATE TABLE sessions (
  id SERIAL,
  start_time TIMESTAMP NOT NULL,
  end_time TIMESTAMP NOT NULL,
  title VARCHAR(150) NOT NULL,
  description TEXT NULL,
  access_link VARCHAR(255) NULL,
  speaker_id INTEGER NOT NULL,
  conference_id INTEGER NOT NULL,
  CONSTRAINT sessions_pkey PRIMARY KEY (id)
);

CREATE INDEX idx_sessions_speaker_id
    ON sessions(speaker_id);

CREATE INDEX idx_sessions_conference_id
    ON sessions(conference_id);