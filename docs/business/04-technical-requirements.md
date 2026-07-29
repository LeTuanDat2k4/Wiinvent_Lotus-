# 5. Technical Requirements

## 5.1. Backend

Phải viết Backend Service bằng:

-   Java
-   Spring Boot

Không viết client game.

## 5.2. Database

Tài liệu cho phép:

-   MySQL
-   PostgreSQL

## 5.3. Redis

Sử dụng:

-   Redis
-   Redisson

Đặc biệt API điểm danh được gợi ý:

-   Dùng Redis để đánh dấu ngày nào đã điểm danh.
-   Áp dụng distributed lock của Redis.

## 5.4. Database Migration

Phải migrate database bằng:

-   Liquibase

## 5.5. Transaction

Giải pháp phải áp dụng Transaction.

## 5.6. Database Index

Giải pháp phải áp dụng Index đối với database.

## 5.7. Redis Usage

Tận dụng Redis trong các trường hợp có thể sử dụng được.

Lưu ý: tài liệu gốc không quy định cụ thể toàn bộ cache strategy hoặc
key schema. Không tự coi một thiết kế cụ thể là business requirement.

## 5.8. Deliverables

-   Đẩy code lên GitHub.
-   Gửi tài liệu API với Postman.
