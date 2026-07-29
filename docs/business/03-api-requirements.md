# 4. API Requirements

Backend Service phải được viết bằng Java Spring Boot.

## API 1 -- Tạo người dùng

Mục tiêu:

-   Tạo người dùng.

Tài liệu gốc chưa quy định chi tiết: - Request schema - Response
schema - Authentication - Validation - Duplicate-user behavior

Không tự suy diễn các phần trên.

## API 2 -- Get profile người dùng

Mục tiêu:

-   Lấy profile người dùng.
-   Màn hình nghiệp vụ có nhu cầu hiển thị avatar, tên hiển thị và số
    điểm Lotus+ hiện tại.

## API 3 -- Get trạng thái các ngày điểm danh

Mục tiêu:

-   Lấy danh sách trạng thái các ngày điểm danh.
-   Phải thể hiện rõ ngày nào **đã điểm danh** và ngày nào **chưa điểm
    danh**.

Business context:

-   Mỗi tháng tối đa 7 ngày điểm danh.
-   Sang tháng mới reset tiến trình điểm danh.
-   Số điểm phụ thuộc vào thứ tự ngày điểm danh trong tháng.

## API 4 -- Điểm danh

Mục tiêu:

-   Cho phép user điểm danh.
-   Cộng điểm Lotus+ tương ứng.
-   Sau khi thành công, trạng thái ngày đó chuyển thành đã điểm danh.

Yêu cầu/gợi ý kỹ thuật trong tài liệu:

-   Có thể dùng Redis để đánh dấu ngày nào đã điểm danh.
-   Áp dụng distributed lock của Redis.

## API 5 -- Lịch sử cộng điểm

Mục tiêu:

-   Lấy lịch sử các lần cộng điểm.
-   Phải hỗ trợ phân trang đầy đủ.

## API 6 -- Trừ điểm

Mục tiêu:

-   Cung cấp API trừ điểm Lotus+.

Tài liệu gốc chưa quy định chi tiết: - Điều kiện được trừ điểm. - Có cho
phép số dư âm hay không. - Request/response schema. - Idempotency. -
Error handling.

Không tự suy diễn các quy tắc trên.
