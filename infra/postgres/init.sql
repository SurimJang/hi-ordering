-- 스키마 및 테이블 생성
CREATE SCHEMA IF NOT EXISTS "user";
CREATE SCHEMA IF NOT EXISTS "order";
CREATE SCHEMA IF NOT EXISTS "menu";
CREATE SCHEMA IF NOT EXISTS "payment";
CREATE SCHEMA IF NOT EXISTS "sales";
CREATE SCHEMA IF NOT EXISTS "category";

CREATE SEQUENCE hibernate_sequence START WITH 10 INCREMENT BY 1;
-- 테이블 생성
CREATE TABLE IF NOT EXISTS "user"."user" (
    id SERIAL PRIMARY KEY,
    username VARCHAR(20) NOT NULL,
    password VARCHAR(20) NOT NULL,
    storename VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS "category"."category" (
    id SERIAL PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL,
    store_id INT NOT NULL,
    CONSTRAINT fk_store_id FOREIGN KEY (store_id) REFERENCES "user"."user" (id)
);

CREATE TABLE IF NOT EXISTS "menu"."menu" (
    id SERIAL PRIMARY KEY,
    menu_name VARCHAR(100) NOT NULL,
    menu_price INT NOT NULL,
    qty INT NOT NULL,
    category_id INT NOT NULL,
    store_id INT NOT NULL,
    CONSTRAINT fk_category_id FOREIGN KEY (category_id) REFERENCES "category"."category" (id),
    CONSTRAINT fk_store_id FOREIGN KEY (store_id) REFERENCES "user"."user" (id)
);

CREATE TABLE IF NOT EXISTS "order"."order" (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    order_status VARCHAR(50) DEFAULT 'none',
    payment_amount INT DEFAULT 0,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES "user"."user" (id)
);

CREATE TABLE IF NOT EXISTS "order"."order_menus" (
    id SERIAL PRIMARY KEY,
    order_id INT NOT NULL,
    menu_id INT NOT NULL,
    qty INT NOT NULL,
    CONSTRAINT fk_order_id FOREIGN KEY (order_id) REFERENCES "order"."order" (id),
    CONSTRAINT fk_menu_id FOREIGN KEY(menu_id) REFERENCES "menu"."menu"(id)
);

CREATE TABLE IF NOT EXISTS "payment"."payment" (
    id SERIAL PRIMARY KEY,
    payment_amount INT NOT NULL,
    user_id INT NOT NULL,
    order_id INT NOT NULL,
    payment_status VARCHAR(50) DEFAULT 'none',
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES "user"."user" (id),
    CONSTRAINT fk_order_id FOREIGN KEY (order_id) REFERENCES "order"."order" (id)
);

-- 초기 레코드 입력
INSERT INTO "user"."user" (username, password, storename, created_at, updated_at) 
VALUES
('store001', '1234', '이루다제면소 일산점', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('store002', '1234', '이루다제면소 판교점', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('store003', '1234', '이루다제면소 시청점', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('store004', '1234', '이루다제면소 부산점', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO "category"."category" (category_name, store_id)
VALUES
('소바', 1),
('가츠', 1),
('우동', 1),
('미니우동세트', 1),
('세트메뉴', 1),
('사이드', 1),
('음료/주류', 1);

INSERT INTO "menu"."menu" (menu_name, menu_price, qty, category_id,store_id)
VALUES
('소바', 8000, 1, 1, 1),
('자루소바', 10000, 1, 1, 1),
('소바 곱빼기', 9000, 1, 1, 1),
('비빔소바', 8500, 1, 1, 1),
('들기름소바', 8500, 1, 1, 1),
('온소바', 8000, 1, 1, 1),
('한돈 수제돈가츠', 9000, 1, 2, 1),
('치즈가츠', 9000, 1, 2, 1),
('닭안심 수제치킨가츠', 8000, 1, 2, 1),
('붓가케 우동(국물없는 우동)', 8000, 1, 3, 1),
('냉우동', 8000, 1, 3, 1),
('가케(기본)우동', 8000, 1, 3, 1),
('김치나베 돈가츠우동', 9000, 1, 3, 1),
('유부우동', 8500, 1, 3, 1),
('자루우동', 8000, 1, 3, 1),
('냉우동 곱빼기', 9000, 1, 3, 1),
('김치나베 치즈가츠우동', 10000, 1, 3, 1),
('김치나베 치킨가츠우동', 10000, 1, 3, 1),
('만두나베우동', 9000, 1, 3, 1),
('불고기우동(순한맛)', 9000, 1, 3, 1),
('눈꽃 명란크림우동', 12000, 1, 3, 1),
('카라이(매운)우동', 8500, 1, 3, 1),
('치킨가라아게 우동(국물없는 우동)', 9000, 1, 3, 1),
('불고기우동(매운맛)', 9000, 1, 3, 1),
('미니우동&돈가츠', 8000, 1, 4, 1),
('미니우동&치즈가츠', 8000, 1, 4, 1),
('미니우동&치킨가츠', 8000, 1, 4, 1),
('이루다 소소한 세트(돈가츠)', 10000, 1, 5, 1),
('이루다 오붓한 세트(돈가츠)', 12000, 1, 5, 1),
('이루다 오붓한 세트(치킨가츠)', 12000, 1, 5, 1),
('이루다 소소한 세트(치킨가츠)', 10000, 1, 5, 1),
('자루우동 고명추가', 2000, 1, 6, 1),
('계란후라이', 1000, 1, 6, 1),
('소바 육수추가', 1000, 1, 6, 1),
('수제팽이버섯 튀김', 2000, 1, 6, 1),
('치킨가라아게(5P)', 4000, 1, 6, 1),
('새우 3P', 4000, 1, 6, 1),
('간장계란밥', 4000, 1, 6, 1),
('공기밥', 2000, 1, 6, 1),
('새우 5P', 6000, 1, 6, 1),
('반숙계란튀김 1P', 2000, 1, 6, 1),
('감자고로케', 4000, 1, 6, 1),
('우동면추가', 2000, 1, 6, 1),
('자루소바 육수추가', 1000, 1, 6, 1),
('음료', 2000, 1, 7, 1),
('구슬아이스크림', 1500, 1, 7, 1),
('처음처럼', 4000, 1, 7, 1),
('테라', 4000, 1, 7, 1),
('카스', 4000, 1, 7, 1),
('뽀로로음료', 4000, 1, 7, 1),
('라무네 사이다', 5000, 1, 7, 1),
('소바면추가', 2000, 1, 7, 1),
('계란밥쯔유 500g', 4000, 1, 7, 1),
('참이슬후레쉬', 4000, 1, 7, 1),
('청하', 4000, 1, 7, 1),
('진로이즈백', 4000, 1, 7, 1);

INSERT INTO "order"."order" (user_id, created_at, updated_at, order_status, payment_amount)
VALUES
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'completed', 28500),  -- 소바, 들기름소바, 붓가케 우동, 간장계란밥
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'completed', 18000),           -- 소바 곱빼기, 한돈 수제돈가츠
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'completed', 16500),          -- 비빔소바, 냉우동
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'completed', 25500),      -- 온소바, 김치나베 돈가츠우동, 카라이 우동
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'completed', 12000); -- 간장계란밥, 공기밥, 음료, 구슬아이스크림

INSERT INTO "order"."order_menus" (order_id, menu_id, qty)
VALUES
(1,1,1),
(1,5,2),
(1,10,3),
(1,37,1),
(2,3,1),
(2,7,2),
(3,4,1),
(3,11,1),
(4,6,1),
(4,13,2),
(4,22,1),
(5,37,1),
(5,38,2),
(5,43,1),
(5,47,2);
