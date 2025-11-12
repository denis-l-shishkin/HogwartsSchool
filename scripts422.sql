CREATE TABLE person (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INTEGER CHECK (Age > 0),
    has_driving_license BOOLEAN,
    car_id SERIAL REFERENCES car(id)
);

CREATE TABLE car (
    id SERIAL PRIMARY KEY,
    brand VARCHAR(100),
    model VARCHAR(100),
    cost INTEGER
);
