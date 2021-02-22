DROP TABLE IF EXISTS account CASCADE;
DROP TABLE IF EXISTS holder CASCADE;
DROP TABLE IF EXISTS transfer CASCADE;

CREATE TABLE account (
    uid character varying PRIMARY KEY,
    type character varying,
    balance double precision,
    holder_id character varying REFERENCES holder(id)
);

CREATE UNIQUE INDEX account_pkey ON account(uid text_ops);

CREATE TABLE holder (
    id character varying PRIMARY KEY,
    birth_date date,
    city character varying,
    country character varying,
    first_name character varying,
    last_name character varying,
    street character varying,
    zip_code character varying
);

CREATE UNIQUE INDEX holder_pkey ON holder(id text_ops);

CREATE TABLE transfer (
    id character varying PRIMARY KEY,
    from_account_id character varying REFERENCES account(uid),
    to_account_id character varying REFERENCES account(uid),
    amount double precision,
    execution_date timestamp without time zone
);

CREATE UNIQUE INDEX transfer_pkey ON transfer(id text_ops);