CREATE TABLE users (
    id SERIAL,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password TEXT NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT users_email_unique UNIQUE (email)
);
