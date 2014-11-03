
DROP TABLE countries;

CREATE TABLE countries
(
  common_name character varying(32) NOT NULL,
  formal_name character varying(128) NOT NULL,
  _type character varying(32) NOT NULL,
  sub_type character varying(32),
  sovereignty character varying(32),
  capital character varying(128) NOT NULL,
  iso_4217_currency_code character varying(3) NOT NULL,
  iso_4217_currency_name character varying(16) NOT NULL,
  itu_t_telephone_code integer NOT NULL,
  iso_3166_1_2_letter_code character(2) NOT NULL,
  iso_3166_1_3_letter_code character(3) NOT NULL,
  iso_3166_1_number integer NOT NULL,
  iana_country_code_tld character(3) NOT NULL
)
WITH (
  OIDS=FALSE
);