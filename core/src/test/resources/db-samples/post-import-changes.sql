CREATE TABLE continent (
	name VARCHAR(16)
);

ALTER TABLE continent
  ADD CONSTRAINT pk_continent_name PRIMARY KEY(name);

INSERT INTO continent (name)
SELECT DISTINCT (continent)
FROM country c;

ALTER TABLE city
  ADD CONSTRAINT fk_city_country FOREIGN KEY (countrycode)
  REFERENCES country (code) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE country DROP CONSTRAINT country_continent_check;

ALTER TABLE country
  ADD CONSTRAINT fk_country_continent FOREIGN KEY (continent)
  REFERENCES continent (name) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION;