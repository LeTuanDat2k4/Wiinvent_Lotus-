# 1. Tổng quan nghiệp vụ

## Use Case

  -----------------------------------------------------------------------
  Field                               Value
  ----------------------------------- -----------------------------------
  Use case ID                         Gami-1

  Use case name                       Điểm danh hàng ngày

  Description                         Cho phép người điểm danh hàng ngày
                                      theo khung giờ quy định để nhận
                                      thưởng Lotus+

  Actor                               Người dùng app

  Priority                            1

  Trigger                             Người dùng nhấn mục Điểm danh

  Pre-condition                       Người dùng đã đăng nhập app

  Post-condition                      Hiển thị màn hình Điểm danh hàng
                                      ngày
  -----------------------------------------------------------------------

## Main Flow

1.  User đăng nhập app và chọn mục **Điểm danh hàng ngày** tại màn Home.
2.  System hiển thị màn hình **Điểm danh hàng ngày**.
3.  User chọn button **Điểm danh**.
4.  System cộng điểm Lotus+ cho user và chuyển button **Điểm danh** sang
    **Đã điểm danh**.

## Exceptional Flow

Trường hợp người dùng vào màn hình điểm danh nhưng không ở khung giờ
được phép:

-   System không hiển thị button **Điểm danh**.

Chi tiết điều kiện và trạng thái UI nằm trong `01-business-rules.md` và
`02-checkin-screen.md`.
