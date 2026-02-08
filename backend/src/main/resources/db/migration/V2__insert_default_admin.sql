-- V2: デフォルト管理者ユーザーの作成
-- username: admin / password: admin123

INSERT INTO users (username, password, display_name, role, created_at, updated_at)
VALUES (
    'admin',
    '$2b$10$6WMo8CM0BiF5TFbCQsvV3.TQPImrI0aMKKs5lTGeSDx/Qdyg2B7qW',
    'Administrator',
    'ADMIN',
    NOW(6),
    NOW(6)
);
