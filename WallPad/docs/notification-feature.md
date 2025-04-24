# 실시간 알림(Notification) 기능 개발 요약

## 주요 변경사항

- WebSocket 기반 실시간 알림 기능 구현
- 차량 예약된 입차 시 알림 메시지 전송
- 사용자별 알림 저장 및 읽음 처리
- 알림 관련 REST API 추가
- Swagger 문서화 완료 (`@Operation`)

---

## 사용 기술

- STOMP + SockJS 기반 WebSocket 통신
- SimpMessagingTemplate을 이용한 브로드캐스트
- MyBatis Mapper를 이용한 알림 DB 처리
- Spring Security 기반 사용자 식별
- Swagger(OpenAPI 3) 어노테이션 적용

---

## 주요 기능 상세

| 기능명 | 설명 | API 경로 |
|--------|------|-----------|
| 알림 조회 | 읽지 않은 알림 목록 조회 | `GET /api/notifications/unread` |
| 알림 수 조회 | 읽지 않은 알림 수 조회 | `GET /api/notifications/unread-count` |
| 알림 읽음 처리 | 전체 알림 읽음 처리 | `POST /api/notifications/read` |
| 최신 알림 5개 | 최근 읽은 알림 5개 조회 | `GET /api/notifications/latest` |

---

## 테스트 방법

1. 차량 예약 후 입차 등록 시
2. 로그인된 대시보드 상단에서 알림 수신 확인
3. 알림 아이콘 클릭 → 읽지 않은 알림 목록 확인
4. 자동으로 읽음 처리 + 최신 알림 리스트 갱신

---

## 💬 기타 참고 사항

- 메시지는 `/user/queue/alerts` 경로로 전송되며, 아파트 호수 단위로 개인화됨
- 알림은 DB에 저장되고 읽음 여부는 `is_read` 컬럼으로 관리됨

