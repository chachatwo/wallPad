
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

## 🧾 사용 기술 및 특이사항
- Swagger UI: `/swagger-ui/index.html`
- OpenAPI 3.0 기반 자동 문서화
- DTO 기반 Request/Response 구조 설계
