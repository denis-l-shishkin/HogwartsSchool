CREATE TABLE person (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INTEGER CHECK (Age > 0),
    has_driving_license BOOLEAN
);

CREATE TABLE car (
    id SERIAL PRIMARY KEY,
    brand VARCHAR(100),
    model VARCHAR(100),
    cost INTEGER
);

CREATE TABLE person_car (
    person_id INTEGER REFERENCES person(id),
    car_id INTEGER REFERENCES car(id),
    PRIMARY KEY (person_id, car_id)
);

SELECT p.name, c.brand, c.model
FROM person as p
    INNER JOIN person_car as pc ON p.id = pc.person_id
    INNER JOIN car as c ON c.id = pc.car_id
WHERE p.name = 'Иванов Иван';

SELECT c.brand, c.model, p.name
FROM car as c
    INNER JOIN person_car as pc ON c.id = pc.car_id
    INNER JOIN person as p ON p.id = pc.person_id
WHERE c.brand = 'KIA' AND c.model = 'CEED';
