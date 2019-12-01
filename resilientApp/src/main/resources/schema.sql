DROP TABLE IF EXISTS weather;
CREATE TABLE weather (
    id INT PRIMARY KEY,
    location_id INT NOT NULL,
    temperature DOUBLE NOT NULL,
    scale VARCHAR(15) NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL
);