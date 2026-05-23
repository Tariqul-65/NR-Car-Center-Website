
CREATE DATABASE IF NOT EXISTS autodrive_db;
USE autodrive_db;

-- 1. Table: users (Owner: Tariqul Islam Parbat)
CREATE TABLE IF NOT EXISTS users (
    user_id    INT            NOT NULL AUTO_INCREMENT,
    full_name  VARCHAR(100)   NOT NULL,
    email      VARCHAR(150)   NOT NULL,
    password   VARCHAR(255)   NOT NULL,
    phone      VARCHAR(20)    DEFAULT NULL,
    role       ENUM('admin','customer') NOT NULL DEFAULT 'customer',
    created_at DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active  TINYINT(1)     NOT NULL DEFAULT 1,
    CONSTRAINT pk_users PRIMARY KEY (user_id),
    CONSTRAINT uq_users_email UNIQUE (email)
);
CREATE INDEX idx_users_role ON users (role);
CREATE INDEX idx_users_email ON users (email);

-- 2. Table: brands (Owner: Tariqul Islam Parbat)
CREATE TABLE IF NOT EXISTS brands (
    brand_id   INT            NOT NULL AUTO_INCREMENT,
    brand_name VARCHAR(100)   NOT NULL,
    country    VARCHAR(100)   DEFAULT NULL,
    logo_url   VARCHAR(255)   DEFAULT NULL,
    CONSTRAINT pk_brands PRIMARY KEY (brand_id),
    CONSTRAINT uq_brands_name UNIQUE (brand_name)
);
CREATE INDEX idx_brands_name ON brands (brand_name);

-- 3. Table: vehicles (Owner: Farhana Akter)
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

-- 4. Table: vehicle_images (Owner: Farhana Akter)
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

-- 5. Table: appointments (Owner: Sayma Hossain Tamim)
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

-- 6. Table: inquiries (Owner: Md. Wasiu Rahman Siyam)
CREATE TABLE IF NOT EXISTS inquiries (
    inquiry_id INT          NOT NULL AUTO_INCREMENT,
    user_id    INT          NOT NULL,
    vehicle_id INT          DEFAULT NULL,
    subject    VARCHAR(200) NOT NULL,
    message    TEXT         NOT NULL,
    status     ENUM('Open','Replied','Closed') NOT NULL DEFAULT 'Open',
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_inquiries PRIMARY KEY (inquiry_id),
    CONSTRAINT fk_inq_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_inq_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id) ON DELETE SET NULL
);
CREATE INDEX idx_inq_user    ON inquiries (user_id);
CREATE INDEX idx_inq_vehicle ON inquiries (vehicle_id);
CREATE INDEX idx_inq_status  ON inquiries (status);

-- 7. Table: inquiry_replies (Owner: Md. Wasiu Rahman Siyam)
CREATE TABLE IF NOT EXISTS inquiry_replies (
    reply_id   INT      NOT NULL AUTO_INCREMENT,
    inquiry_id INT      NOT NULL,
    admin_id   INT      NOT NULL,
    reply_text TEXT     NOT NULL,
    replied_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_inquiry_replies PRIMARY KEY (reply_id),
    CONSTRAINT fk_reply_inquiry FOREIGN KEY (inquiry_id) REFERENCES inquiries(inquiry_id) ON DELETE CASCADE,
    CONSTRAINT fk_reply_admin FOREIGN KEY (admin_id) REFERENCES users(user_id)
);

-- 8. Table: financing_requests (Owner: Sadiya Yasmin)
CREATE TABLE IF NOT EXISTS financing_requests (
    finance_id     INT            NOT NULL AUTO_INCREMENT,
    user_id        INT            NOT NULL,
    vehicle_id     INT            NOT NULL,
    loan_amount    DECIMAL(12,2)  NOT NULL,
    down_payment   DECIMAL(12,2)  NOT NULL,
    tenure_months  INT            NOT NULL,
    interest_rate  DECIMAL(5,2)   NOT NULL,
    monthly_emi    DECIMAL(12,2)  DEFAULT NULL,
    status         ENUM('Draft','Submitted','Approved','Rejected') NOT NULL DEFAULT 'Draft',
    created_at     DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_financing PRIMARY KEY (finance_id),
    CONSTRAINT fk_fin_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_fin_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id)
);

-- 9. Table: notifications (Owner: Sadiya Yasmin)
CREATE TABLE IF NOT EXISTS notifications (
    notification_id INT          NOT NULL AUTO_INCREMENT,
    user_id         INT          NOT NULL,
    title           VARCHAR(200) NOT NULL,
    message         TEXT         NOT NULL,
    type            ENUM('appointment','inquiry','finance','general') NOT NULL,
    is_read         TINYINT(1)   NOT NULL DEFAULT 0,
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_notifications PRIMARY KEY (notification_id),
    CONSTRAINT fk_notif_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);


INSERT INTO brands (brand_name, country, logo_url) VALUES
('Toyota',   'Japan',  'https://cdn.jsdelivr.net/gh/scooterlord/logos/toyota.svg'),
('Honda',    'Japan',  'https://cdn.jsdelivr.net/gh/scooterlord/logos/honda.svg'),
('BMW',      'Germany','https://cdn.jsdelivr.net/gh/scooterlord/logos/bmw.svg'),
('Ford',     'USA',    'https://cdn.jsdelivr.net/gh/scooterlord/logos/ford.svg'),
('Mercedes', 'Germany','https://cdn.jsdelivr.net/gh/scooterlord/logos/mercedes-benz.svg');

-- Add Baseline Car Records [cite: 57]
INSERT INTO vehicles (brand_id, model, year, color, price, mileage, fuel_type, transmission, condition_v, status, description) VALUES
(1, 'Corolla Grandee', 2023, 'Super White', 2800000.00, 0, 'Hybrid', 'Automatic', 'New', 'Available', 'Brand new factory allocation with dual zone climate mapping module.'),
(2, 'Civic Turbo Oriel', 2022, 'Crystal Black', 2500000.00, 5200, 'Petrol', 'Automatic', 'Used', 'Available', 'Mint tracking performance specs, single owner pre-owned flagship vehicle.'),
(3, 'X5 M-Sport xDrive', 2021, 'Carbon Blue', 7500000.00, 14200, 'Diesel', 'Automatic', 'Used', 'Available', 'Premium executive luxury package. Clean mileage registration records.');

-- Assign Image Gallery URLs for Catalog Interface [cite: 65]
INSERT INTO vehicle_images (vehicle_id, image_url, is_primary) VALUES
(1, 'https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?q=80&w=600', 1),
(2, 'https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?q=80&w=600', 1),
(3, 'https://images.unsplash.com/photo-1555215695-3004980ad54e?q=80&w=600', 1);

-- Default Super Admin User Seed Entry (Login Password: adminpassword123)
INSERT INTO users (full_name, email, password, phone, role) VALUES
('NR Showroom Administrator', 'admin@nrcarcenter.com', '$2b$10$C77tX7mS4pC67wS0iG/UuODq7m1b7SOmgTq/Nge6g89K4kGbeNAGi', '01711578507', 'admin');