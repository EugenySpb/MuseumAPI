CREATE TABLE admins (
                        id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        username VARCHAR(255) NOT NULL UNIQUE,
                        password VARCHAR(255) NOT NULL,
                        role VARCHAR(50) NOT NULL
);

CREATE TABLE events (
                        id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        title_event VARCHAR(255) NOT NULL,
                        start_date_time timestamp NOT NULL,
                        end_date_time timestamp NOT NULL,
                        museum_name VARCHAR(255) NOT NULL,
                        address VARCHAR(255) NOT NULL,
                        free_seats INT NOT NULL,
                        total_seats INT NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        created_who VARCHAR(255) NOT NULL
);

CREATE TABLE bookings (
                          id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          last_name VARCHAR(50) NOT NULL,
                          first_name VARCHAR(50) NOT NULL,
                          middle_name VARCHAR(50) NOT NULL,
                          email VARCHAR(255) NOT NULL,
                          phone_number VARCHAR(20) NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          event_id BIGINT NOT NULL,
                          FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
);

CREATE TABLE deactivated_token (
    id           uuid primary key,
    c_keep_until timestamp not null check ( c_keep_until > now() )
);


CREATE INDEX idx_admins_username ON admins(username);

CREATE INDEX idx_events_start_date_time ON events(start_date_time);
CREATE INDEX idx_events_end_date_time ON events(end_date_time);
CREATE INDEX idx_events_museum_name ON events(museum_name);

CREATE INDEX idx_bookings_email ON bookings(email);
CREATE INDEX idx_bookings_phone_number ON bookings(phone_number);
CREATE INDEX idx_bookings_event_id ON bookings(event_id);