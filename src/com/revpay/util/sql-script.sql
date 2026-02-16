-- USERS TABLE
CREATE TABLE users (
                       user_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       full_name VARCHAR2(100),
                       email VARCHAR2(100) UNIQUE,
                       phone VARCHAR2(15) UNIQUE,
                       password_hash VARCHAR2(255),
                       transaction_pin VARCHAR2(255),
                       account_type VARCHAR2(20) CHECK (account_type IN ('PERSONAL','BUSINESS')),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

------------------------------------------------------

-- WALLET TABLE
CREATE TABLE wallet (
                        wallet_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        user_id NUMBER UNIQUE,
                        balance NUMBER(15,2) DEFAULT 0,
                        CONSTRAINT fk_wallet_user
                            FOREIGN KEY (user_id) REFERENCES users(user_id)
);

------------------------------------------------------

-- TRANSACTIONS TABLE
CREATE TABLE transactions (
                              txn_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                              sender_id NUMBER,
                              receiver_id NUMBER,
                              amount NUMBER(15,2),
                              txn_type VARCHAR2(20),
                              status VARCHAR2(20),
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                              CONSTRAINT fk_txn_sender
                                  FOREIGN KEY (sender_id) REFERENCES users(user_id),

                              CONSTRAINT fk_txn_receiver
                                  FOREIGN KEY (receiver_id) REFERENCES users(user_id)
);

------------------------------------------------------

-- MONEY REQUESTS TABLE
CREATE TABLE money_requests (
                                request_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                requester_id NUMBER,
                                receiver_id NUMBER,
                                amount NUMBER(15,2),
                                status VARCHAR2(20) DEFAULT 'PENDING'
        CHECK (status IN ('PENDING','ACCEPTED','REJECTED')),
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                CONSTRAINT fk_request_requester
                                    FOREIGN KEY (requester_id) REFERENCES users(user_id),

                                CONSTRAINT fk_request_receiver
                                    FOREIGN KEY (receiver_id) REFERENCES users(user_id)
);

------------------------------------------------------

-- NOTIFICATIONS TABLE
CREATE TABLE notifications (
                               notification_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                               user_id NUMBER,
                               message VARCHAR2(255),
                               is_read NUMBER(1) DEFAULT 0 CHECK (is_read IN (0,1)),
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT fk_notification_user
                                   FOREIGN KEY (user_id) REFERENCES users(user_id)
);

select * from users;
select * from WALLET;
select * from INVOICES;
select * from notifications;
select * from MONEY_REQUESTS;

CREATE OR REPLACE PROCEDURE create_wallet (
    p_user_id IN wallet.user_id%TYPE
) AS
BEGIN
INSERT INTO wallet (user_id, balance)
VALUES (p_user_id, 0);

COMMIT;
END;
/

CREATE OR REPLACE PROCEDURE get_wallet_balance (
    p_user_id IN wallet.user_id%TYPE,
    p_balance OUT wallet.balance%TYPE
) AS
BEGIN
SELECT balance
INTO p_balance
FROM wallet
WHERE user_id = p_user_id;
END;
/

CREATE OR REPLACE PROCEDURE update_wallet_balance (
    p_user_id IN wallet.user_id%TYPE,
    p_new_balance IN wallet.balance%TYPE,
    p_rows_updated OUT NUMBER
) AS
BEGIN
UPDATE wallet
SET balance = p_new_balance
WHERE user_id = p_user_id;

p_rows_updated := SQL%ROWCOUNT;
COMMIT;
END;
/
