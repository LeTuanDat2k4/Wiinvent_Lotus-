# 2. Business Rules

## 2.1. Khung giờ điểm danh

User cần điểm danh trong một trong hai khung giờ:

-   `09:00 – 11:00`
-   `19:00 – 21:00`

Nếu user chưa điểm danh trong ngày nhưng hiện tại không thuộc các khung
giờ trên, UI hiển thị:

> Khung giờ điểm danh:9h-11h và 19h-21h

## 2.2. Số lần điểm danh

-   User được điểm danh tối đa **1 lần mỗi ngày**.
-   User không bắt buộc phải điểm danh theo các ngày liên tiếp.

## 2.3. Số điểm nhận được

Mỗi lần điểm danh, số điểm Lotus+ được cộng phụ thuộc vào thứ tự ngày
điểm danh trong tháng:

    Ngày điểm danh   Điểm Lotus+
  ---------------- -------------
            Ngày 1            +1
            Ngày 2            +2
            Ngày 3            +3
            Ngày 4            +5
            Ngày 5            +8
            Ngày 6           +13
            Ngày 7           +21

Số điểm này được mô tả là **config** và phần màn hình ghi rõ số điểm
thưởng có thể được cấu hình động ở CMS.

## 2.4. Giới hạn theo tháng

-   Mỗi tháng user được điểm danh tối đa **7 ngày**.
-   Khi sang tháng mới, tiến trình điểm danh được **reset lại từ đầu**.

## 2.5. Nguyên tắc chống suy diễn

Các chi tiết sau không được tài liệu gốc quy định rõ và không nên tự suy
diễn khi implement:

-   Timezone áp dụng cho khung giờ.
-   Cách xử lý chính xác tại biên `11:00`, `21:00`.
-   Cách CMS cung cấp/cập nhật cấu hình điểm.
-   Quy tắc khi cấu hình CMS thiếu một ngày.
-   Cơ chế hoàn tác điểm nếu transaction thất bại một phần.
-   Quy tắc số dư tối thiểu khi trừ điểm.
-   Chi tiết authentication/authorization.
-   Chi tiết response/error code của API.

Nếu cần quyết định các vấn đề trên, cần xác nhận requirement.
