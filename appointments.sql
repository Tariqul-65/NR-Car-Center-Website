
CREATE TABLE IF NOT EXISTS appointments (
    appointment_id INT   NOT NULL AUTO_INCREMENT,
    user_id        INT   NOT NULL,
    vehicle_id     INT   NOT NULL,
    appt_date      DATE  NOT NULL,
    appt_time      TIME  NOT NULL,
    status         ENUM('Pending','Confirmed','Cancelled','Completed') NOT NULL DEFAULT 'Pending',
    notes          TEXT  DEFAULT NULL,
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_appointments PRIMARY KEY (appointment_id),
    CONSTRAINT fk_appt_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_appt_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id)
);
CREATE INDEX idx_appt_user    ON appointments (user_id);
CREATE INDEX idx_appt_vehicle ON appointments (vehicle_id);
CREATE INDEX idx_appt_date    ON appointments (appt_date);
CREATE INDEX idx_appt_status  ON appointments (status);
