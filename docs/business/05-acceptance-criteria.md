# 6. Acceptance Criteria

Checklist này được chuyển từ nghiệp vụ và yêu cầu kỹ thuật trong tài
liệu gốc để dùng làm checklist khi implement/review.

## Business

-   [ ] User phải đăng nhập trước khi sử dụng chức năng điểm danh.
-   [ ] Điểm danh chỉ được thực hiện trong `09:00–11:00` hoặc
    `19:00–21:00`.
-   [ ] User chỉ được điểm danh tối đa 1 lần/ngày.
-   [ ] User không cần điểm danh liên tiếp theo ngày.
-   [ ] Mỗi tháng tối đa 7 ngày điểm danh.
-   [ ] Sang tháng mới tiến trình điểm danh reset từ đầu.
-   [ ] Điểm thưởng theo thứ tự ngày điểm danh: `1, 2, 3, 5, 8, 13, 21`.
-   [ ] Điểm thưởng được mô tả là cấu hình động ở CMS.
-   [ ] Khi điểm danh thành công, điểm Lotus+ được cộng cho user.
-   [ ] Khi đã điểm danh trong ngày, UI hiển thị `Đã điểm danh` và
    disable button.
-   [ ] Ngoài khung giờ và chưa điểm danh, UI hiển thị thông báo khung
    giờ thay vì button điểm danh.

## API

-   [ ] API tạo người dùng.
-   [ ] API lấy profile người dùng.
-   [ ] API lấy trạng thái các ngày điểm danh.
-   [ ] API điểm danh.
-   [ ] API lịch sử cộng điểm có pagination.
-   [ ] API trừ điểm.

## Technical

-   [ ] Java Spring Boot.
-   [ ] MySQL hoặc PostgreSQL.
-   [ ] Redis.
-   [ ] Redisson.
-   [ ] Distributed lock được cân nhắc/áp dụng cho API điểm danh theo
    yêu cầu/gợi ý.
-   [ ] Liquibase database migration.
-   [ ] Transaction.
-   [ ] Database indexes.
-   [ ] Tận dụng Redis ở các trường hợp phù hợp.
-   [ ] Code được đẩy lên GitHub.
-   [ ] Có tài liệu API với Postman.

## Agent Review

Trước khi coding agent tự đưa ra quyết định mới, kiểm tra:

1.  Quyết định đó có được quy định trong business docs không?
2.  Có implementation hiện tại nào cần được reuse không?
3.  Có đang tự tạo thêm business rule không?
4.  Có thay đổi phạm vi ngoài requirement không?
5.  Nếu requirement chưa rõ, cần hỏi thay vì tự suy diễn.
