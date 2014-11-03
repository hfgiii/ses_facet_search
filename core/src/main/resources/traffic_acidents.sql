DROP TABLE traffic_accidents;

CREATE TABLE traffic_accidents
(
  incident_id bigint NOT NULL,
  offense_id bigint NOT NULL,
  offense_code integer NOT NULL,
  offense_code_extension integer NOT NULL,
  offense_type_id character varying(64) NOT NULL,
  offense_category_id character varying(64) NOT NULL,
  first_occurrence_date timestamp without time zone NOT NULL,
  last_occurrence_date timestamp without time zone,
  reported_date timestamp without time zone NOT NULL,
  incident_address character varying(128) NOT NULL,
  geo_x real NOT NULL,
  geo_y real NOT NULL,
  geo_lon real NOT NULL,
  geo_lat real NOT NULL,
  district_id integer NOT NULL,
  precinct_id integer NOT NULL,
  neighborhood_id character varying(64) NOT NULL
)
WITH (
  OIDS=FALSE
);
 
