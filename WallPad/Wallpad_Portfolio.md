
# 📌 아파트 월패드 시스템 포트폴리오 (WallPad)

**프로젝트 기간:** 2025년 1월 ~ 2025년 4월
**참여인원:** 1인 개발(단독 수행)  
**역할:** 백엔드/풀스택 개발 (인증/보안, Redis 세션, 이미지 업로드, 배포 등 주요 기능 전담)  
**기술 스택:** Spring Boot, MyBatis, Spring Security, Redis, AWS EC2, GCS  

---

## 🌱 프로젝트 소개 및 목표

**『아파트 월패드 시스템 (WallPad)』** 은 입주민이 실생활에서 겪는 민원, 차량 예약, 유지보수 일정 등의 불편을 웹 기반으로 해결하는 스마트 웹 시스템입니다.  
입주민이 쉽고 빠르게 정보를 확인하고 직접 민원 및 차량 예약을 처리할 수 있도록 설계했습니다.

---

## 🎯 기획 배경

- 실제 아파트 생활에서 입주민이 스스로 민원을 접수하던 아날로그 방식에서 벗어나, 민원 처리 및 차량 예약 등의 기능을 웹 기반 디지털 시스템으로 전환하여 접근성과 효율성을 향상
- 다양한 사용자 환경을 고려하여, PC와 모바일에서도 손쉽게 이용 가능한 반응형 웹 서비스로 설계

---

## 🚀 사용 기술 및 개발 환경

| 구분 | 기술 |
|------|------|
| Backend | Java 11, Spring Boot, Spring Security, MyBatis, JWT, Session |
| Frontend | HTML5, CSS3, JavaScript, jQuery, Bootstrap |
| Database | MySQL |
| Cloud | Google Cloud Storage (GCS) |
| Mail | JavaMailSender |
| Realtime & Messaging | WebSocket, STOMP, SockJS, SimpMessagingTemplate |
| 기타 | Lombok, DataTables, Ajax, Redis, Cache |
| 테스트 | JUnit5, Mockito |

---

## 🌟 주요 기능 및 화면 소개

| 기능 | 설명 | 화면 |
|------|------|-----|
| 회원가입 및 로그인 | JWT 기반 이메일 인증, UUID 토큰 기반 비밀번호 찾기(reset), 중복 검사 및 유효성 검사, 수동쿠키 ID기억하기 구현 | ![signup](./screenshots/signup.png) |
| 대시보드 | 동호수 식별 정보 제공(공지사항, 차량예약 등), 날씨 API(OpenWeatherMap, AirVisual) 포함, 실시간 알림 기능(WebSocket[STOMP + SockJS] 기반) | ![dashboard](./screenshots/dashboard.png) |
| 방문차량 예약 및 입차 관리 | 동호수 자동 식별(세션 기반), 차량번호 유효성 검사, 실시간 예약현황 노출 | ![parking](./screenshots/parking.png) |
| 하자보수 신청 | 다중 이미지 업로드, GCS 연동 처리 | ![repair](./screenshots/repair.png) |
| 주요 일정 캘린더 | 프론트+백엔드 캐싱 기반 일정 조회 최적화 | ![schedule](./screenshots/schedule.png) |

---

## 🛠️ 기술적 도전과 문제 해결 경험

### 📌 문제1. 성능 최적화: 다중 캐싱 전략 도입
- **상황:** 유지보수 일정 조회 시 반복된 서버 요청으로 성능 저하 우려
- **해결:** 프론트엔드(JavaScript)와 백엔드(Redis)의 다중 캐싱 전략 도입
- **성과:** API 호출 빈도 감소 체감, 사용자 응답속도 체감 개선

### 📌 문제2. 인증 방식 개선: Spring Security로 점진적 전환
- **상황:**  로그인, 인증, 세션 관리 전반을 직접 설계하고자 Spring Security의 자동 인증 대신 수동 방식(@PostMapping 등)을 도입했으나, CSRF 방어 미비, 이중 로그인 제어 부족, 보안 설정의 복잡성 등 여러 한계에 직면
- **해결:** Spring Security를 점진적으로 도입하면서 기존 수동 로직과 유연하게 통합하고, 로그인 성공 시 커스텀 핸들러(AuthenticationSuccessHandler)를 통해 쿠키 기반 ID 저장 로직도 수동 제어
- **성과:** 보안성 강화(CSRF 대응, 세션 관리 향상), 유지보수성 확보, Redis 세션을 통한 이중 로그인 차단 구현, 그리고 수동 인증 체계에 대한 실전 설계 경험 축적

```java
Map<String, ? extends Session> userSessions = sessionRepository.findByPrincipalName(username);
for (Session s : userSessions.values()) {
    if (!s.getId().equals(session.getId())) {
        sessionRepository.deleteById(s.getId());
    }
}
```

### 📌 문제3. EC2 인스턴스 장애 → 루트 볼륨 분리 및 서버 복구
- **상황:**  
  AWS EC2 인스턴스가 '실행 중'으로 표시되지만 SSH 접속, SCP, Instance Connect 모두 실패 
  콘솔 메시지로 판단한 결과, OS 커널 부팅 실패 또는 네트워크 설정 손상 가능성 존재

- **해결:**  
  1. 인스턴트 중지 후 재시작 시도(오류 미해결)
  2. 기존 EC2 인스턴스 중지 및 루트 볼륨(EBS) 분리  
  3. 새 EC2 인스턴스를 생성하고 `/dev/xvdf`로 연결  
  4. `/mnt/recovery` 경로에 마운트하여 `.jar`, `application.properties`, `wallpad_full.sql` 등 핵심 파일 백업  
  5. MySQL 재설치 및 DB 복원  
  6. GCS 인증 키 및 환경변수 재설정 후 Spring Boot `.jar` 실행  

- **성과:**  
  EC2 인프라 구조에 대한 실전 이해도 향상  
  루트 볼륨 분리 및 신규 인스턴스 복원 절차 체득

| 인스턴스 연결 에러 | 새인스턴스 생성 | 볼륨 연결 |
|------------------|----------------------|------------------|
| ![인스턴스 에러](./screenshots/ec2_instance_error.png) | ![새 인스턴스 생성](./screenshots/ec2_instance_created.png) | ![EBS 볼륨 연결](./screenshots/ec2_volume_attached.png) |

### 📌 문제4. EC2 인스턴스 접속 불가 및 서비스 중단 문제 → Elastic IP + systemd 적용을 통한 운영 자동화
- **상황:** EC2 인스턴스가 '실행 중' 상태이나 SSH 등 모든 접속 불가 + 퍼블릭 IP 변경 + jar 수동 실행 반복 발생
- **해결:** Elastic IP 고정으로 주소 문제 해결, systemd 등록으로 서버 재시작 시 jar 자동 실행 구조 구축
- **성과:** 운영 자동화 달성, 무중단 서비스 구조 구현, 수동 배포 반복 제거

| 엘라스틱 IP 할당 | systemd 적용 | 
|------------------|----------------------|
| ![엘라스틱 연결](./screenshots/ec2_instance_elastic.png) | ![systemd 적용](./screenshots/ec2_instance_systemd.png) | 


### 📌 문제5. 협업 흐름 시뮬레이션 (Git 브랜치 전략 + Pull Request)

- **상황:** 1인 프로젝트였기 때문에 팀 협업 절차를 경험할 기회가 없었음  
- **해결:** 기능 개발 완료 후 `feature/notification` 브랜치를 생성하고, GitHub에서 실제 팀 프로젝트처럼 Pull Request(PR) 흐름을 시뮬레이션  
- **실행 절차:**  
  1. 기능 단위 브랜치 생성 및 작업  
  2. GitHub에서 PR 생성, 커밋 로그 및 변경 내용 명확히 정리  
- **성과:** 브랜치 전략과 PR 리뷰 흐름을 실습함으로써 협업 구조와 코드 관리 전략에 대한 이해도 향상

---

## 📐 시스템 아키텍처 및 ERD

- **시스템 구성도:** ![architecture](./screenshots/architecture.png)
- **ERD:** ![erd](./screenshots/apartment_wallpad_erd_detailed.png)

---

## 📋 API 문서화 및 테스트 코드 작성

- **Swagger API 문서화 완료:** [`/swagger-ui/index.html`](http://3.34.226.110:8080/swagger-ui/index.html)
- **JUnit5 + Mockito 기반 단위 테스트** 작성하여 인증 로직 검증 완료
- 아래는 이메일 인증 전송 기능에 대한 실제 테스트 코드

```java
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private AuthService authService;

    @Test
    void 이메일_전송_테스트() {
        String email = "dnjscjf0104@naver.com";
        String token = "testtesttest";

        MimeMessage message = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(message);

        authService.sendVerificationEmail(email, token);

        verify(javaMailSender, times(1)).send(message);
    }
}
```

---

## 🚩 배포 및 운영 경험

- AWS EC2 환경에 Spring Boot 프로젝트 독립 배포
- GCS 연동을 통한 이미지 관리 및 배포 환경 최적화 경험 확보

**접속 주소:** [AWS EC2](http://3.34.226.110:8080/)

---

## 🔮 한계점 및 향후 개선 계획

- 현재 배포 환경(AWS EC2/테스트)에서는 사용자의 Redis 미설치 예상하여 기능 제거(주석 처리)한 상태이나, 개발 단계에서는 Redis 기반 세션 관리 및 이중 로그인 제어 기능까지 정상적으로 구현됨. 실서비스 운영 시 Redis 연동 고려
- 하자보수 이미지 업로드 기능에 대해 Spring Batch 기반 비동기 처리를 적용하여, 향후 대용량 이미지 업로드 대응과 성능 최적화 방안을 고려 중
- 기존 사용자 중심 웹 시스템에서 관리자용 통계/모니터링 페이지 등 관리 기능으로의 확장 가능성 고려 중
- 현재는 PC 및 모바일 웹 기반이지만, 모바일 앱(PWA 또는 Android/iOS 전용 앱) 으로의 확장 개발을 통해 사용자 접근성 향상을 계획

---

## 💬 개발자 코멘트

- 사용자의 관점에서 기능을 설계하고 실제 문제 해결에 집중했습니다.
- 인증/보안 설계부터 배포, 성능 최적화까지 단독으로 수행하며 실무 대응 역량을 키웠습니다.
- 특히 Spring Security와 Redis 연동, GCS 파일 처리 경험을 통해 실전 백엔드 개발 능력을 강화했습니다.
- 1인 개발임에도 Git 브랜치 전략과 PR 시뮬레이션을 직접 수행하며 협업 흐름을 체험했고, 커밋 컨벤션, 기능 단위 브랜치 운영 등 실무형 Git 관리 역량을 강화했습니다.

---

## ✍️ 기술 블로그 정리 링크

프로젝트 개발 과정에서 마주한 기술적 고민, 문제 해결 과정, 구조적 설계 등  
심화 내용을 정리한 블로그 포스트입니다. 기술적 배경을 더 깊이 이해할 수 있습니다.

- [인증방식 / JWT vs 세션 - 왜 둘 다 썼나?](https://wccool.tistory.com/1)
- [Spring Boot + GCS로 이미지 업로드 구현하기](https://wccool.tistory.com/2)
- [수동 인증으로 시작한 나, 결국 Spring Security로 돌아온 이유](https://wccool.tistory.com/3)
- [프론트 + 백엔드 캐싱을 동시 적용해 성능 향상 시킨 경험](https://wccool.tistory.com/4)
- [AWS EC2 인스턴스 복구: 연결 실패부터 서비스 정상화까지](https://wccool.tistory.com/5)
- [AWS EC2 운영 자동화: Elastic IP + systemd로 무중단 서비스 만들기](https://wccool.tistory.com/6)
- [AWS EC2 인스턴스 생성부터 SSH 접속까지: Spring Boot 서버 배포 준비](https://wccool.tistory.com/7)
- [스프링부트 테스트 코드 적용: 이메일 인증 전송 테스트](https://wccool.tistory.com/8)
- [실시간 알림 기능 구현 - WebSocket 기반으로 알림 생성하기](https://wccool.tistory.com/9)

👉 전체 글 모음: [wccool 기술 블로그 바로가기](https://wccool.tistory.com)

---

## 📞 개발자 정보 및 연락처

| 이름 | 역할 | 기술 |
|------|------|------|
| 차원철 | 백엔드 개발 | Spring Boot, Redis, MySQL, 인증/보안, MyBatis, GCS |
| GitHub | [https://github.com/chachatwo](https://github.com/chachatwo) |
| Email | dnjscjf0104@naver.com |
