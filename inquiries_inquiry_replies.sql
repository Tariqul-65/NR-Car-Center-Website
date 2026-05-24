
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
