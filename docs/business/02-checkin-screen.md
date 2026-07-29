# 3. Yêu cầu màn hình Điểm danh hàng ngày

## 3.1. Dữ liệu hiển thị

Màn hình có các thông tin:

  -----------------------------------------------------------------------
  Trường                              Dữ liệu / mô tả
  ----------------------------------- -----------------------------------
  Avatar                              Ảnh avatar của người dùng

  Display name                        Tên hiển thị của người dùng. Ví dụ:
                                      `Daniel Craig`

  Lotus+ balance                      Số điểm thưởng mà user đang có. Ví
                                      dụ: `4.456 Lotus+`

  Daily check-in list                 Hiển thị tên ngày và số điểm thưởng
                                      cho mỗi ngày điểm danh

  Reward configuration                Số điểm thưởng được cấu hình động ở
                                      CMS
  -----------------------------------------------------------------------

## 3.2. Button / Label

### Trạng thái 1 -- Có thể điểm danh

Điều kiện:

-   Đang trong khung giờ cho phép điểm danh.
-   User chưa điểm danh trong ngày.

UI:

-   Hiển thị button **Điểm Danh**.
-   User có thể nhấn button để điểm danh và nhận thưởng.

### Trạng thái 2 -- Đã điểm danh

Điều kiện:

-   User đã điểm danh trong ngày.

UI:

-   Hiển thị button **Đã điểm danh**.
-   Disable button.

### Trạng thái 3 -- Ngoài khung giờ

Điều kiện:

-   Không trong khung giờ điểm danh.
-   User chưa điểm danh trong ngày.

UI:

-   Không hiển thị button điểm danh.
-   Hiển thị label:

`Khung giờ điểm danh:9h-11h và 19h-21h`
