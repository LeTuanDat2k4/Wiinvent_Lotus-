# Wiinvent Lotus — Backend Service (Gamification & Point System)

Hệ thống Backend Service xây dựng bằng **Java 21 / Spring Boot 3**, phục vụ tính năng **Điểm danh nhận thưởng Lotus+ hàng ngày** và **Quản lý tích/trừ điểm**.

---

## 1. Cấu trúc Dự án & Lý do Thiết kế

Dự án được tổ chức theo mô hình **Modular Monolith (Package-by-Feature)** kết hợp **Clean Architecture**:

```text
com.Wiinvent.Lotus
├── core                   # Hạ tầng kỹ thuật dùng chung (Shared Infrastructure)
│   ├── config             # Cấu hình Spring, Redis, App Properties
│   ├── dto                # Common Response Wrapper (ApiResponse, PageResponse)
│   ├── entity             # Base Entity (Id, createdAt, updatedAt, version)
│   ├── exception          # Global Exception Handler & Custom Exceptions
│   ├── ratelimit          # Redisson Rate Limiting Aspect & Annotation
│   ├── repository         # Base Repository
│   ├── security           # Security Config, JWT Provider, Internal API Key Filter
│   └── util               # Utility Classes (TimeWindow, TokenHash)
│
└── domain                 # Tách biệt theo từng Bounded Context (Domain Nghiệp vụ)
    ├── auth               # Đăng nhập & Quản lý Refresh Token
    ├── checkin            # Nghiệp vụ Điểm danh hàng ngày
    ├── point              # Lịch sử tích điểm & Trừ điểm
    ├── reward             # Cấu hình điểm thưởng (CMS Dynamic Config)
    └── user               # Quản lý người dùng & Profile
```

### Lý do thiết kế:
1. **Tách biệt Core và Domain:** Package `core` chứa toàn bộ hạ tầng kỹ thuật (Security, Cache, RateLimit, Global Exception). Các module trong `domain` chỉ tập trung vào logic nghiệp vụ thực tế, giảm thiểu phụ thuộc lẫn nhau (Loose Coupling).
2. **Dễ bảo trì và mở rộng:** Tổ chức theo `domain/{feature}` giúp developer dễ dàng tìm kiếm code, cô lập phạm vi thay đổi và sẵn sàng tách ra thành các Microservices riêng biệt trong tương lai khi hệ thống tăng trưởng.

---

## 2. Sử dụng Redis — Ở đâu & Tại sao?

Dự án sử dụng **Redis** kết hợp thư viện **Redisson** để tối ưu hóa hiệu năng, chống tranh chấp dữ liệu và bảo vệ hệ thống:

| Ứng dụng Redis | Vị trí trong Codebase | Tại sao sử dụng (Lý do) |
| :--- | :--- | :--- |
| **1. Distributed Lock** | [CheckInService.java](file:domain/checkin/service/CheckInService.java#L95) | Dùng `RLock` (`checkin:lock:{userId}:{today}`) chống **Race Condition** khi user spam click điểm danh đồng thời trong cùng một ngày. |
| **2. Check-in Status Cache** | [CheckInCacheService.java](file:/domain/checkin/service/CheckInCacheService.java#L48) | Dùng `RSet` (`checkin:user:{userId}:{YYYY-MM}`) để kiểm tra trạng thái điểm danh ngày/tháng với độ phức tạp **$O(1)$**, giải phóng hoàn toàn truy vấn DB. |
| **3. Refresh Token Dual-Storage** | [RefreshTokenCacheService.java](file:domain/auth/service/RefreshTokenCacheService.java#L20) | Dùng `RBucket` (`auth:refresh_token:{tokenHash}`) để xác thực nhanh Refresh Token ($O(1)$) và tận dụng cơ chế **Auto-Expire (TTL)** của Redis mà không cần dọn DB rác. DB PostgreSQL đóng vai trò lưu trữ bền vững (Audit Log). |
| **4. User Profile Cache** | [UserService.java](file:domain/user/service/UserService.java#L39) | Dùng `@Cacheable` (`user_profile::{userId}`) để phục vụ ngay lập tức API get profile. Tự động `@CacheEvict` khi số dư Lotus+ biến động (điểm danh, trừ điểm). |
| **5. Reward Config Cache** | [RewardService.java](file:domain/reward/service/RewardService.java#L21) | Dùng `@Cacheable` (`reward_configs`) cache cấu hình điểm thưởng (dữ liệu tĩnh/ít thay đổi), giảm 100% query đọc bảng `reward_config`. |
| **6. Rate Limiting** | [RateLimitAspect.java](file:/core/ratelimit/RateLimitAspect.java#L43) | Dùng `RRateLimiter` (thuật toán Token Bucket) giới hạn số request/phút theo IP hoặc UserId. |

---

## 3. Tài liệu API (API Specifications)

### Danh sách Endpoints

| HTTP Method | API Endpoint | Mô tả | Authentication | Rate Limit |
| :--- | :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/auth/login` | Đăng nhập hệ thống (nhận cặp Access & Refresh Token) | Public | 5 req / phút (IP) |
| `POST` | `/api/v1/auth/refresh` | Làm mới Access Token bằng Refresh Token | Public | 10 req / phút (IP) |
| `POST` | `/api/v1/users` | Tạo tài khoản người dùng mới | `ROLE_ADMIN` | Default |
| `GET` | `/api/v1/users/profile` | Lấy thông tin cá nhân & Số dư Lotus+ hiện tại | Authenticated | Default |
| `GET` | `/api/v1/check-ins/status` | Lấy danh sách & trạng thái điểm danh các ngày trong tháng | Authenticated | Default |
| `POST` | `/api/v1/check-ins` | Thực hiện điểm danh hàng ngày nhận thưởng Lotus+ | Authenticated | 10 req / phút (User) |
| `GET` | `/api/v1/points/history` | Xem lịch sử biến động điểm Lotus+ (Phân trang) | Authenticated | Default |
| `POST` | `/api/v1/points/deduct` | Trừ điểm Lotus+ của người dùng | `ADMIN` / `SERVICE` (`X-Internal-Api-Key`) | 10 req / phút (User) |

### Postman Collection
Tài liệu và mẫu Request/Response chi tiết được cung cấp tại:
*   [Lotus_Gamification_API.postman_collection.json](file:docs/Lotus_Gamification_API.postman_collection.json)

---

## 4. Hướng dẫn Khởi chạy Dự án (Getting Started)

### Yêu cầu môi trường (Prerequisites)
*   Java JDK 21+
*   Docker & Docker Compose

### Các bước khởi chạy:

1. **Khởi chạy PostgreSQL & Redis Container:**
   ```bash
   docker-compose up -d
   ```
   *   PostgreSQL: `localhost:5432` (DB: `lotus`, User: `lotus`, Password: `lotus`)
   *   Redis: `localhost:6379`

2. **Chạy kiểm thử (Unit / Integration Tests):**
   ```bash
   .\gradlew.bat test
   ```

3. **Chạy ứng dụng Backend (BootRun):**
   ```bash
   .\gradlew.bat bootRun
   ```
   *   Service sẽ khởi chạy tại: `http://localhost:8080`
   *   Liquibase sẽ tự động chạy database migration và nạp cấu hình điểm thưởng mặc định.