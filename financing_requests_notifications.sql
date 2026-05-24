
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

