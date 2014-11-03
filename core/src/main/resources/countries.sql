
DROP TABLE countries;

CREATE TABLE countries
(
  common_name character varying(128) NOT NULL,
  formal_name character varying(128),
  _type character varying(128) NOT NULL,
  sub_type character varying(128),
  sovereignty character varying(128),
  capital character varying(128),
  iso_4217_currency_code character varying(16),
  iso_4217_currency_name character varying(16),
  itu_t_telephone_code character varying(32),
  iso_3166_1_2_letter_code character(2),
  iso_3166_1_3_letter_code character(3),
  iso_3166_1_number integer,
  iana_country_code_tld character varying(16)
)
WITH (
  OIDS=FALSE
);