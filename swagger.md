
# 📘 WallPad 아파트 시스템 API 문서

> 하자보수, 차량예약, 로그인 등 주요 기능 API 명세서입니다.  
> 버전: `v1.0` | 문서 형식: `OpenAPI 3.0`

---

## ✅ API 목록 요약

| 메서드 | URL | 설명 |
|--------|-----|------|
| `POST` | `/users/insert` | 회원가입 처리 |
| `POST` | `/api/register/car` | 입차 차량 등록 |
| `POST` | `/api/parking/reserve/states` | 아파트 차량 예약 상태 조회 |
| `GET`  | `/verify-email` | 이메일 인증 처리 |
| `GET`  | `/check-username` | 아이디 중복 확인 |
| `GET`  | `/check-email` | 이메일 중복 확인 |
| `GET`  | `/api/schedules` | 유지보수 일정 전체 조회 |
| `GET`  | `//api/notifications/unread` | 읽지 않은 알림 목록 조회 |
| `GET`  | `/api/notifications/unread-count` | 읽지 않은 알림 개수 조회 |
| `GET`  | `/api/notifications/read` | 모든 알림 읽음 처리 |
| `GET`  | `/api/notifications/latest` | 최신 알림 5개 조회 |

---

## 📌 상세 API 설명

### 📥 POST `/users/insert`  
**회원가입 처리**  
- 사용자의 정보를 세션에 저장하고 이메일 인증 링크를 발송합니다.  
- 요청 파라미터: `SignUpDTO (ModelAttribute)`  
- 응답: `이메일을 확인하여 인증 링크를 클릭하세요.`

---

### 📥 POST `/api/register/car`  
**입차 차량 등록**  
- 차량번호 기반으로 예약 여부 확인 후 입차 등록  
- 요청 바디:
```json
{
  "carNumber": "12가3456",
  "entryTime": "2025-04-01T14:00"
}
```
- 응답: 등록된 `EntryCarDTO`

---

### 📥 POST `/api/parking/reserve/states`  
**아파트 차량 예약 상태 조회**  
- 세션 사용자 기반으로 차량 예약 리스트 조회  
- 응답 예시:
```json
[
  {
    "carNumber": "12가3456",
    "allowedPeriod": "2025-04-03 22:00:00"
  }
]
```

---

### 📤 GET `/verify-email`  
**이메일 인증 처리**  
- 이메일 인증 토큰을 검증하고 회원가입을 최종 완료  
- 파라미터: `token=JWT_TOKEN`  
- 리다이렉트: `/login` 또는 `/error`

---

### 📤 GET `/check-username`  
**아이디 중복 확인**  
- 요청 파라미터: `username=wallpad01`  
- 응답: `사용 가능한 아이디입니다.` 또는 `이미 사용 중인 아이디입니다.`

---

### 📤 GET `/check-email`  
**이메일 중복 확인**  
- 요청 파라미터: `email=wallpad@test.com`  
- 응답: `사용 가능한 이메일입니다.` 또는 `이미 사용 중인 이메일입니다.`

---

### 📤 GET `/api/schedules`  
**유지보수 일정 전체 조회**  
- 반환: 유지보수 일정 배열
```json
[
  {
    "maintenanceDate": "2025-04-05",
    "title": "전기 점검",
    "description": "아파트 전체 전기 점검",
    "startTime": "09:00",
    "endTime": "12:00",
    "status": "예정"
  }
]
```

---

### 📤 GET `/api/notifications/unread`
**읽지 않은 알림 목록 조회**
- 로그인한 사용자의 아파트 동호수 기준
- 응답 예시:
```json
[
  {
    "id": 1,
    "apartmentNumber": "101-202",
    "message": "방문차량이 입차하였습니다.",
    "isRead": false,
    "createdAt": "2025-04-20 13:40:12"
  }
]
```

---

### 📤 GET `/api/notifications/unread-count`
**읽지 않은 알림 개수 조회**
- 로그인한 사용자의 읽지 않은 알림 개수 반환
- 응답 예시:
```json
5
```

---

### 📤 POST `/api/notifications/read`
**모든 알림 읽음 처리**
- 로그인한 사용자의 알림 중 읽지 않은 항목을 모두 읽음 처리
- 응답: 상태코드 200 (내용 없음)

---

### 📤 GET `/api/notifications/latest`
**최신 알림 5개 조회**
- 로그인한 사용자의 최근 알림 5건을 최신순으로 반환
- 응답 예시:
```json
[
  {
    "id": 9,
    "apartmentNumber": "101-202",
    "message": "예약된 차량이 입차했습니다.",
    "isRead": true,
    "createdAt": "2025-04-20 13:25:00"
  }
]
```

---

## 🧾 사용 기술 및 특이사항
- Swagger UI: `/swagger-ui/index.html`
- OpenAPI 3.0 기반 자동 문서화
- DTO 기반 Request/Response 구조 설계
