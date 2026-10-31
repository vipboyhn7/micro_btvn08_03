# Appointment-Service

Microservice này chạy ở cổng `8083` và gọi `Doctor-Service` mặc định ở `http://localhost:8082`.
Lỗi kết nối hoặc HTTP lỗi từ Doctor-Service được Circuit Breaker `doctorServiceCB` theo dõi.
Sau tối thiểu 5 lần gọi, nếu tỷ lệ lỗi đạt 50%, mạch chuyển từ `CLOSED` sang `OPEN`
và request tiếp theo nhận lỗi ngay thay vì chờ timeout.

## Chạy

```bash
mvn spring-boot:run
```

Có thể đổi địa chỉ Doctor-Service:

```bash
DOCTOR_SERVICE_URL=http://localhost:8082 mvn spring-boot:run
```

## Gọi thử

```bash
curl -i -X POST http://localhost:8083/api/v1/appointments \
  -H 'Content-Type: application/json' \
  -d '{"patientName":"Nguyen Van A","doctorId":"1","reason":"Dau bung"}'
```

Khi Doctor-Service lỗi, client nhận HTTP `503` và JSON:

```json
{
  "message": "Hiện tại không thể kiểm tra thông tin bác sĩ, vui lòng thử lại sau vài giây",
  "status": 503,
  "error": "Doctor Service Error",
  "timestamp": "2026-10-31T07:57:13.504992Z"
}
```

Tắt Doctor-Service rồi thực hiện request để kích hoạt fallback. Sau tối thiểu 5
request lỗi, Circuit Breaker chuyển trạng thái và ghi log:

```text
CircuitBreaker 'doctorServiceCB' changed state from CLOSED to OPEN
```

Khi lỗi kết nối chỉ xảy ra tạm thời, `patientRetry` sẽ thử tối đa 3 lần,
mỗi lần cách nhau 2 giây. Chỉ lỗi `ResourceAccessException` được retry; sau
khi cả 3 lần đều thất bại, fallback trả HTTP `503`.

## Rate Limiter tìm kiếm bác sĩ

```http
GET http://localhost:8083/api/v1/doctors/search?name=huong
```

API cho phép tối đa 5 request trong mỗi 10 giây. Request thứ 6 nhận HTTP `429`
ngay lập tức với JSON lỗi `Too Many Requests`; Rate Limiter không chờ để cấp
quyền truy cập.
