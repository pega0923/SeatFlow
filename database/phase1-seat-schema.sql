CREATE TABLE IF NOT EXISTS seats (
    id BIGINT NOT NULL AUTO_INCREMENT,
    seat_number VARCHAR(30) NOT NULL,
    location VARCHAR(100) NOT NULL,
    has_outlet BOOLEAN NOT NULL,
    near_window BOOLEAN NOT NULL,
    status VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_seats_seat_number UNIQUE (seat_number)
);

INSERT IGNORE INTO seats (seat_number, location, has_outlet, near_window, status)
VALUES
    ('A-01', '1st floor reading room', TRUE, TRUE, 'AVAILABLE'),
    ('A-02', '1st floor reading room', TRUE, FALSE, 'AVAILABLE'),
    ('B-01', '2nd floor study area', FALSE, TRUE, 'UNAVAILABLE');
