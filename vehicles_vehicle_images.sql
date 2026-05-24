
CREATE TABLE IF NOT EXISTS vehicles (
    vehicle_id   INT            NOT NULL AUTO_INCREMENT,
    brand_id     INT            NOT NULL,
    model        VARCHAR(100)   NOT NULL,
    year         YEAR           NOT NULL,
    color        VARCHAR(50)    DEFAULT NULL,
    price        DECIMAL(12,2)  NOT NULL,
    mileage      INT            NOT NULL DEFAULT 0,
    fuel_type    ENUM('Petrol','Diesel','Hybrid','Electric') NOT NULL,
    transmission ENUM('Manual','Automatic') NOT NULL,
    condition_v  ENUM('New','Used') NOT NULL DEFAULT 'New',
    status       ENUM('Available','Sold','Reserved') NOT NULL DEFAULT 'Available',
    description  TEXT           DEFAULT NULL,
    created_at   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_vehicles PRIMARY KEY (vehicle_id),
    CONSTRAINT fk_vehicle_brand FOREIGN KEY (brand_id) REFERENCES brands(brand_id)
);
CREATE INDEX idx_veh_brand      ON vehicles (brand_id);
CREATE INDEX idx_veh_status     ON vehicles (status);
CREATE INDEX idx_veh_fuel       ON vehicles (fuel_type);
CREATE INDEX idx_veh_condition  ON vehicles (condition_v);
CREATE INDEX idx_veh_price      ON vehicles (price);


CREATE TABLE IF NOT EXISTS vehicle_images (
    image_id    INT          NOT NULL AUTO_INCREMENT,
    vehicle_id  INT          NOT NULL,
    image_url   VARCHAR(255) NOT NULL,
    is_primary  TINYINT(1)   NOT NULL DEFAULT 0,
    uploaded_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_vehicle_images PRIMARY KEY (image_id),
    CONSTRAINT fk_vimg_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id) ON DELETE CASCADE
);
CREATE INDEX idx_vimg_vehicle   ON vehicle_images (vehicle_id);
CREATE INDEX idx_vimg_primary   ON vehicle_images (is_primary);
