CREATE TABLE speakers (
    id SERIAL,
    first_name VARCHAR(200) NOT NULL,
    last_name VARCHAR(200) NOT NULL,
    email VARCHAR(500) NOT NULL,
    professional_title VARCHAR(200),
    CONSTRAINT speakers_pkey PRIMARY KEY (id)
);
