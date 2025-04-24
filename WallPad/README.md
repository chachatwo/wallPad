# 아파트 월패드 시스템 (WallPad)

사용자의 아파트 생활을 웹 기반으로 통합한 스마트 월패드 시스템
로그인, 방문차량 예약, 하자보수 요청, 공지사항, 일정 조회 등 실생활에서 필요한 기능을 통합 구현한 Spring Boot 기반 웹 프로젝트입니다.

---

## 프로젝트 기획 배경

- 실제 아파트 생활에서 자주 겪는 민원, 방문차량 처리, 공지 전달의 불편함을 개선하기 위해 기획
- 입주민이 실시간으로 정보를 확인하고 직접 요청/예약할 수 있는 웹 시스템 구현을 목표
- PC 및 모바일 웹 어디서든 간편하게 접속하고 이용할 수 있도록 설계

---

## 사용 기술 (Tech Stack)

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

## 배포 환경

본 프로젝트는 AWS EC2 환경에 배포 완료된 상태이며, 실제 웹 브라우저에서 접속 가능한 상태입니다.

- **배포 방식:** Amazon EC2 + Spring Boot 독립 실행형 (`.jar`)
- **접속 주소:** http://3.34.226.110:8080/ (※ Elastic IP 할당으로 고정되어 있음)
- **운영 환경:** Ubuntu 20.04, OpenJDK 11, MySQL, Git, GCS 연동
- **세션 관리:** Redis 미설치 환경 고려 → HttpSession 기반으로 설정
- **배포 상태:** 2025년 4월 9일 기준 모든 기능 배포 완료 및 정상 작동 확인

## 환경별 인증 구조 및 세션 관리 전략

개발 환경에서는 Redis를 활용한 세션 관리 및 이중 로그인 방지 기능을 실습하며 확장성 중심의 인증 구조를 구성하였습니다.
반면, 배포 환경에서는 Redis 미설치 상황을 고려하여 기본 HttpSession(JSESSIONID) 기반 인증 방식으로 유연하게 전환하였습니다.

- **개발 환경**: spring-session-data-redis 기반 세션 관리 + 이중 로그인 제어 실습 (현재는 주석 처리됨)
- **배포 환경**: Redis 미설치 → Spring Boot 기본 세션(HttpSession) 방식으로 동작  
  ※ 추후 운영 환경에서는 Redis 기반 세션 관리 구조로 확장 가능하며, 현재 프로젝트에서는 세션 인증이 기본적으로 HttpSession을 통해 이루어지고 있습니다.

---

## 주요 기능

### 회원가입 및 로그인

- 유효성 검사 적용(아이디/이메일 중복, 이메일 포맷, 동호수 정규식, 전화번호 자동 하이픈 처리)
- 세션 기반 인증 + JWT 기반 이메일 인증/비밀번호 재설정 (10분 유효) 
- 수동 쿠키 기반 Remember-Me 구현 (HttpOnly 적용)

### 대시보드

- 로그인한 사용자의 아파트 번호 기준으로 필터링된 콘텐츠 제공
- 공지사항, 수리 요청, 차량 예약, 일정 등 요약 제공
- 날씨, 미세먼지 API(OpenWeatherMap + AirVisual) 연동
- 실시간 시계 표시 (setInterval 기반)
- 실시간 알림 표시(WebSocket[STOMP + SockJS] 기반)

### 공지사항 관리

- 공지사항 리스트에 DataTables 적용: 검색 + 페이징
- 클릭 시 상세 내용 모달 출력  
- 백엔드(Redis) 1분 캐싱

### 방문차량 예약 및 입차

- 차량번호, 출입 허용 기간 입력 후 예약  
- 예약된 차량만 입차 가능, 실시간 예약현황 조회  
- 차량 번호 유효성 검사 및 실시간 모달 확인 
- 로그인 세션을 통해 사용자 아파트 동호수를 자동으로 식별 및 매핑

### 하자보수 신청

- 대분류 > 중분류 > 소분류 선택  
- 이미지 다중 업로드  
- Google Cloud Storage(GCS)에 저장 후 imagePath만 DB에 저장
- 로그인 세션을 통해 사용자 아파트 동호수를 자동으로 식별 및 매핑

### 주요 일정 캘린더

- 월별 일정 렌더링 및 날짜 클릭 시 상세 모달 표시
- 프론트(JavaScript) 1분 캐싱 + 백엔드(Redis) 1분 캐싱
- 사용자의 반복 요청에 따른 트래픽 최소화

---

## 프론트엔드 UX 기능

| 기능 | 설명 |
|------|------|
| 실시간 시계 | `setInterval`로 현재 시간 표시 |
| 날씨/미세먼지 | OpenWeatherMap + AirVisual API 연동 |
| 공지 모달 뷰 | 공지 클릭 시 상세내용 모달 표시 |
| 차량 입차 제한 | 예약된 차량만 입차 허용 (Ajax 기반) |
| 일정 캐싱 | 5분 캐싱 기반 일정 렌더링 |
| 유효성 검사 | 회원가입 시 이메일, 전화번호, 동호수 등 실시간 검사 |

---

## 반응형 모바일 지원

- Bootstrap 기반 반응형 레이아웃 적용  
- 991px 이하에서도 주요 UI 요소 최적화  
- 모바일 웹브라우저에서도 바로 사용 가능  

---

## 인증 및 보안 처리

- 프로젝트 초기에는 로그인부터 세션 설정까지 모두 수동 인증 방식으로 구현  
- 이후 보안성과 확장성 강화를 위해 Spring Security를 점진적으로 도입(로그인 처리, CSRF 방지, Remember-Me 기능 등 적용)
- 현재는 수동 인증 로직을 제거하고, Spring Security + Redis 기반 세션 인증 구조로 안정화  
- Redis 세션 저장소를 활용해 이중 로그인 차단 구현  
- Spring Security를 통해 접속 경로 제어 및 접근 권한 관리

---

## Redis 기반 세션 관리 및 캐싱 처리

- **세션 저장소로 Redis 사용**
  - Spring Security + Spring Session + Redis 연동 (`@EnableRedisHttpSession`)
  - 인증 정보는 `SecurityContext`에 저장되며, Redis에 자동으로 세션 저장
  - 동일 계정으로 로그인 시 기존 세션을 Redis에서 조회 후 강제 삭제하여 이중 로그인 차단 구현
  - `SecurityContextHolder.getContext().getAuthentication().getName()` 방식으로 인증된 사용자 식별
  - 다중 사용자 환경에서도 안정적인 인증/세션 관리 가능
  ※ 현재 EC2 배포 테스트 환경에서는 Redis를 제외하고 HttpSession 기반으로 운영 중이며, 실제 운영 환경에서는 Redis 기반 구조로 확장 가능합니다.

- **공지사항 / 유지보수 일정 캐싱 처리**
  - `@Cacheable("notices")`, `@Cacheable("maintenanceSchedules")` 적용
  - 프론트엔드에서도 유지보수 일정은 JS 레벨에서 1분 캐싱하여 재호출 방지

---

## 실시간 알림 🔔

- 방문차량 입차 시 실시간 알림 표시
- WebSocket(STOMP + SockJS) 기반
- `/user/queue/alerts` 개인 채널로 메시지 수신
- 읽지 않은 알림 수 배지(Badge)로 자동 표시 및 읽음 처리, /api/notifications/unread-count API를 통해 갱신
- 최신 알림 5개 실시간 UI 갱신
[차량 입차] → [알림 DB 저장] → [WebSocket 전송] → [프론트에서 badge + 알림 리스트 표시]

---

- **테스트 코드 기반 검증**  
  - `AuthService`의 이메일 인증 로직에 대해 `JUnit5 + Mockito` 기반 단위 테스트 작성
  - 실제 메일 발송 대신 `JavaMailSender`를 Mock 처리하여 외부 의존 없이 검증
  - 테스트 클래스: `src/test/java/com/wallpad/project/service/AuthServiceTest.java`

---

## 화면 미리보기

| 대시보드 | 하자보수 신청 | 차량 예약 |
|----------|----------------|-------------|
| ![dashboard](./screenshots/dashboard.png) | ![repair](./screenshots/repair.png) | ![parking](./screenshots/parking.png) |

| 일정 조회 | 공지사항 |
|-------------|-----------|
| ![schedule](./screenshots/schedule.png) | ![notices](./screenshots/notices.png) |

---

## ERD (Entity Relationship Diagram)

![ERD](./screenshots/apartment_wallpad_erd_detailed.png)

---

## Swagger 연동

- Swagger UI: `/swagger-ui/index.html` 경로에서 API 확인 가능
- [swagger.md](./swagger.md) 파일에 별도 정리됨

```java
@Operation(summary = "회원가입 처리", description = "입력한 회원 정보를 세션에 저장하고 이메일 인증 링크를 발송합니다.")
@PostMapping("/users/insert")
public String signUp(@ModelAttribute SignUpDTO signUpDTO, HttpSession session) {
    ...
}
```

---

## 기술적 도전과 해결 경험

### 월별 일정 캘린더 반복 호출 문제 → 캐싱을 통한 사전 성능 최적화 적용
월별 유지보수 일정을 불러올 때마다 서버에 쿼리가 요청되는 구조였고, 사용자가 셀렉트 박스에서 월(month)를 반복해서 바꾸는 행위 만으로도 상당한 트래픽 증가가 우려되었습니다.
향후 실제 사용자 환경에서 성능 저하가 우려되었기 때문에 다음과 같은 다중 캐싱 전략을 도입하여 성능을 개선했습니다.

  1. **프론트엔드(JavaScript) 기반 캐싱**
     유지보수 일정 데이터를 1분 동안 메모리에 보관하여 동일 월로의 전환 시 서버 요청을 차단하고 즉시 렌더링하도록 처리했습니다.
     이를 통해 사용자 체감 속도 향상과 불필요한 API 호출을 줄일 수 있었습니다.

  2. **백엔드(Redis) 기반 캐싱**  
     일정 조회 API에 `@Cacheable("maintenanceSchedules")` 어노테이션을 적용하여 스케쥴 데이터를
     Redis에 1분간 캐싱하도록 설정했습니다. 짧은 TTL(1분)을 적용함으로써 일정이 자주 변경되는 상황에서도 캐시 신선도를 유지하면서,
     동일한 요청에 대해서는 DB 접근 없이 Redis에서 빠르게 응답하도록 처리했습니다.

프론트엔드 캐싱은 사용자 관점에서의 응답 속도 개선과 즉각적인 상호작용을 제공해 줄 것이고, 백엔드(Redis) 캐싱은 서버 부하를 줄이고 공통 데이터 요청을
효율적으로 처리하는 데 역할할 것으로 기대하였습니다. 그 결과 사용자 경험과 서버 성능을 동시에 확보할 수 있었습니다.


### 인증 구조의 변화: 수동 인증에서 Spring Security 기반으로
프로젝트 초기에 저는 로그인, 인증, 세션 관리 전반을 직접 제어하고자 Spring Security의 자동 인증 체계 대신, 수동 인증 방식을 도입했습니다.
@PostMapping("/login") 방식으로 로그인 로직을 직접 작성하고, 세션 저장, 쿠키 설정, 이중 로그인 제어 등의 흐름을 전부 수동으로 설계했습니다.
"보안은 단순히 설정값이 아니라, 직접 설계할 수 있어야 한다." 이 철학을 바탕으로 인증 흐름을 처음부터 끝까지 직접 구현해보고자 했습니다.

하지만 프로젝트가 발전하고 보안 요구가 늘어날수록, 다음과 같은 한계에 부딪혔습니다:

  ⚠️ 한계점
  ❌ CSRF 방어 미비 : 수동 인증 구조에서는 Spring Security의 기본 방어 기능을 활용할 수 없음
  ❌ 쿠키/세션 보안 설정 어려움	: HttpOnly, Secure, SameSite 등의 설정을 매번 직접 처리해야 함
  ❌ 이중 로그인 방지 기능 부족	: sessionManagement().maximumSessions(1) 같은 자동 제어 불가
  ❌ 유지보수 어려움	: 인증 로직을 매번 직접 작성해야 하며 확장에 불리함
  
  🔄 점진적인 구조 전환: 수동 인증 → Spring Security + 커스텀 제어 방식
  ✅ CSRF 방어 적용 : Spring Security의 기본 설정을 활성화하여 CSRF 공격을 차단했습니다.
  ✅ 수동 쿠키 기반 Remember-Me : AuthenticationSuccessHandler를 통해 사용자가 로그인 시 선택한 아이디 저장 기능을 쿠키로 직접 구현했습니다.
  ✅ SecurityFilterChain 기반 인증 흐름 재정비 : formLogin(), logout(), sessionManagement() 등 Spring Security 설정을 활용하는 방식으로 재정비하였습니다.
  ✅ Redis 기반 수동 이중 로그인 방지 : Spring Session과 Redis를 연동하여, 동일 사용자 로그인 시 기존 세션을 제거하는 커스텀 로직을 적용했습니다.

 ```java
// Redis 기반 이중 로그인 제어 로직
Map<String, ? extends Session> userSessions = sessionRepository.findByPrincipalName(username);
for (Session s : userSessions.values()) {
  if (!s.getId().equals(session.getId())) {
    sessionRepository.deleteById(s.getId());
  }
}
 ```
그 결과, 기존의 수동 인증 설계 경험을 바탕으로 Spring Security의 자동 기능을 점진적으로 도입하고 보완하면서 보안성, 확장성, 유지보수성 모두를 확보한 
인증 구조를 완성할 수 있었습니다. 이 과정은 단순한 기능 구현을 넘어, 프레임워크의 철학을 이해하고, 필요에 따라 유연하게 설계할 수 있는 능력을 키운 중요한 경험이었습니다.


### EC2 인스턴스 장애 → 루트 볼륨 분리 및 서버 복구
2025년 4월, EC2 인스턴스가 '실행 중'으로 표시되지만 SSH 접속, Instance Connect 모두 실패하는 장애가 발생했습니다.  
콘솔 메시지와 상태 검사 결과를 바탕으로 내부 OS 부팅 실패 또는 네트워크 연결 손상으로 판단했습니다.

  ⚠️ 문제 상황  
  ❌ EC2 상태: 실행 중  
  ❌ 증상: SSH, scp, Instance Connect 모두 실패  
  ❌ 로그: 콘솔 메시지에서 커널 로딩 실패 추정

  🔄 해결 절차  
  1. 인스턴트 중지 후 재시작 시도(오류 미해결)
  2. EC2 인스턴스 중지 및 루트 볼륨(EBS) 분리  
  3. 새 인스턴스 생성 후 `/dev/xvdf`로 연결  
  4. 볼륨 마운트 후 `/mnt/recovery`에서 `.jar`, 설정 파일, SQL 백업 복사  
  5. MySQL 재설치 및 데이터 복원  
  6. 환경 변수 및 GCS 키 재설정  
  7. `application.properties` 수정 후 애플리케이션 재기동

  ✅ 결과 및 성과  
  - EC2 인프라에 대한 구조 이해도 향상  
  - 장애 대응 실전 경험 확보

| 인스턴스 연결 에러 | 새인스턴스 생성 | 볼륨 연결 |
|------------------|----------------------|------------------|
| ![인스턴스 에러](./screenshots/ec2_instance_error.png) | ![새 인스턴스 생성](./screenshots/ec2_instance_created.png) | ![EBS 볼륨 연결](./screenshots/ec2_volume_attached.png) |

### EC2 인스턴스 접속 불가 및 서비스 중단 문제 반복 → Elastic IP + systemd 적용을 통한 운영 자동화
개발 완료 후 EC2에 jar를 직접 배포해 운영을 시작했지만, 지속적으로 예상치 못한 인스턴스 접속 장애와 서비스 중단 이슈가 발생했습니다.

  ⚠️ 문제 상황  
  ❌ EC2 인스턴스가 '실행 중'으로 표시되지만, SSH, scp, Instance Connect 모두 연결 불가
  ❌ 중지 → 시작으로 해결은 가능했지만, 매번 퍼블릭 IP가 변경되어 불편
  ❌ 또한 인스턴스가 재기동되더라도 .jar는 자동 실행되지 않아 수동 명령으로 매번 재시작 불편

  🔄 해결 절차(운영 자동화 구조 도입)
  1. Elastic IP 할당 및 연결
  - 퍼블릭 IP 변경 문제를 해결하기 위해 탄력적 IP(Elastic IP) 할당
  - EC2 인스턴스에 고정 IP를 연결하여, 중지/재시작 이후에도 동일한 접속 주소 유지 가능
  - 배포 주소의 불필요한 수정과 서비스 접근 오류 제거
  
  2. systemd 서비스 등록
  - EC2 인스턴스 부팅 시 .jar 파일이 자동 실행되도록 systemd 서비스 등록
  - /etc/systemd/system/wallpad.service 파일 생성 후 아래와 같이 설정

  ```적용 명령어
  ExecStart=/bin/bash -c 'java -jar /home/ubuntu/WallPad-0.0.1-SNAPSHOT.jar'
  StandardOutput=file:/home/ubuntu/log.txt
  StandardError=file:/home/ubuntu/error.log
  ```

  ✅ 결과 및 성과
  - Elastic IP 적용을 통해 IP 주소 고정 → 접속 경로 안정성 확보
  - systemd 자동 실행 구조로 인해 서버 재기동 시에도 수동 개입 없이 서비스 즉시 복구 가능
  - "중지-시작 후 수동 재설정"이라는 운영 부담을 제거하고, 운영 자동화 기반을 구축
  이 경험을 통해 단순한 배포를 넘어, 운영 환경의 안정성과 무중단 서비스 구조를 직접 설계·구현한 경험을 얻을 수 있었습니다.


| 엘라스틱 IP 할당 | systemd 적용 | 
|------------------|----------------------|
| ![엘라스틱 연결](./screenshots/ec2_instance_elastic.png) | ![systemd 적용](./screenshots/ec2_instance_systemd.png) | 

---

## 개발자 코멘트

- 실사용 입주민의 관점에서 기능을 설계하고 UX 개선에 집중했습니다  
- 인증/보안은 Spring Security의 장점과 수동 인증 방식을 적절히 조합하여 유연하게 구현했습니다  
- Redis 기반 캐싱 및 세션 처리를 통해 사용자 경험과 서버 부하 사이의 균형을 고려했습니다  
- 모바일 환경까지 대응 가능한 반응형 UI로 다양한 접속 환경을 지원합니다  

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

## 실행 방법(로컬 테스트 시)

```bash
# 1. 프로젝트 클론
git clone https://github.com/chachatwo/wallpad.git
cd wallpad

# 2. 빌드
./mvnw clean install

# 3. 실행
./mvnw spring-boot:run

# 4. 접속
http://localhost:8080
```

`.env` 또는 `application.properties` 설정 필요:
- DB 접속 정보  
- GCS 인증 키 경로 및 버킷명  
  (※ `GOOGLE_APPLICATION_CREDENTIALS`, `GOOGLE_CLOUD_STORAGE_BUCKET` 환경변수 설정)
- 메일 서버 SMTP 정보  
- JWT 시크릿 키  

---

## 데이터베이스 세팅 가이드

이 프로젝트는 `wallpad` 데이터베이스와 초기 데이터를 기반으로 동작합니다.

### DB 초기화
1. **MySQL 실행 후 `wallpad_full.sql` 파일을 실행하세요.**  
`wallpad` 데이터베이스 및 관련 테이블, 샘플 데이터가 자동 생성됩니다.

```bash
mysql -u [사용자명] -p < wallpad_full.sql
```

2. **DB 접속 정보 설정**
application.properties 또는 .env 파일에 본인의 MySQL 접속 정보를 입력해야 합니다.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/wallpad
spring.datasource.username=본인_계정명
spring.datasource.password=본인_비밀번호
```
※ 샘플 계정 (root / 1234) 사용이 불가능한 경우, 본인의 MySQL 사용자 정보에 맞게 수정해 주세요.

---

## 👤 개발자 정보

| 이름 | 역할 | 기술 |
|------|------|------|
| [차원철] | 백엔드 개발 | Spring Boot, Redis, MySQL, 인증/보안, MyBatis, GCS |
| GitHub | [https://github.com/chachatwo] | [dnjscjf0104@naver.com] |

---