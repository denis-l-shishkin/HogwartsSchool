ALTER TABLE student
    ALTER COLUMN age SET DEFAULT 20,
    ALTER COLUMN name SET NOT NULL,
    ADD CONSTRAINT age_c1 CHECK (age > 16),
    ADD CONSTRAINT name_unique UNIQUE (name),
    ADD CONSTRAINT faculty_id_c1 CHECK (faculty_id > 0 AND faculty_id < 5)
;

ALTER TABLE faculty
    ADD CONSTRAINT name_color_unique UNIQUE (name, color)
;
