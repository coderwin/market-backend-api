# 지원자 이호진
---
아래는 **현재 프로젝트 구조(src/main/java)**를 기반으로 깔끔하게 정리한
✨ **GitHub용 README.md 초안**입니다.
Spring Boot 전형적인 구조 + 실제 패키지들을 모두 반영해서 **바로 GitHub에 넣어도 되는 품질**로 작성했습니다.

원하면 **ERD 추가**, **API 명세 자동 생성**, **배포용/과제 제출용 버전** 등으로 다시 커스터마이징도 가능해!

---

# 📘 README.md (제안본)

```markdown
# ALLRA Backend

Java Spring Boot 기반으로 구현한 전자 상거래(장바구니 · 상품 · 주문) 백엔드 시스템입니다.  
회원 가입/로그인, 상품 조회, 장바구니 관리, 주문 처리 등 핵심 비즈니스 로직을 포함하며,  
업무 도메인에 따라 패키지를 분리하고 테스트 코드를 포함하여 유지보수성을 높였습니다.

---

## 🚀 Tech Stack

| Category | Stack |
|---------|-------|
| **Language** | Java 17 |
| **Framework** | Spring Boot |
| **Database** | MySQL 8 |
| **ORM** | JPA / Hibernate |
| **Build Tool** | Gradle |
| **Test** | JUnit5, SpringBootTest |
| **Tooling** | Lombok, Validation, Docker Compose |

---

## 📂 Project Structure

현재 실제 프로젝트 구조를 기반으로 작성되었습니다.

```

src/main/java
└─ com.market.allra
├─ api                # Controller 계층
├─ application        # Service 계층
├─ common             # 공통 유틸, dto, 예외, 응답 포맷
├─ configs            # WebMvc 설정, Interceptor
├─ domain             # Aggregate Root / Entity / Repository
└─ infra              # 외부 연동, Session 관리 등

```

### 세부 패키지 구성

#### 📌 `api`
- `MemberApi`, `ProductApi`, `BasketApi`, `OrderApi` 등
- Request/Response DTO 분리
- 표준화된 Response 구조 제공

#### 📌 `application`
- 각 기능별 Service
    - `MemberService`
    - `ProductService`
    - `BasketService`
    - `OrderService`

#### 📌 `domain`
- 도메인 중심 설계  
- Aggregate 단위로 패키지 분리

```

domain
├─ base                # BaseEntity, BaseTimeEntity
├─ basket              # Basket, BasketProduct
├─ category            # Category
├─ like                # ProductLike
├─ member              # Member, Role, Login
├─ order               # Order, OrderItem
├─ product             # Product, Image, Stock
├─ repository          # JPA Repository 모음

```

#### 📌 `common`
- `ExceptionHandler`
- `ApiResponse`
- `CursorRequest` (커서 기반 페이지네이션)
- 인증 실패 처리, 검증 유틸

#### 📌 `infra`
- `SessionContextHolder`, `SessionInfoDAO`, `LoginService`

#### 📌 `configs`
- `WebConfig`, `SessionInitInterceptor`

---

## ✨ 주요 기능

### 👤 회원
- 회원가입
- 로그인 / 로그아웃
- 세션 기반 인증
- 중복 사용자 검증

### 🛒 장바구니
- 장바구니 생성 및 조회
- 장바구니에 상품 추가
- 수량 변경
- 품절 상품 표시 처리
- 삭제 상품 예외 처리

### 🛍 상품
- 상품 등록/수정/삭제
- 상품 목록 조회 (커서 기반 페이지네이션)
- 카테고리 필터링
- 인기순/최신순 정렬

### 📦 주문
- 주문 생성
- 주문 아이템 계산
- 결제 상태(PENDING, APPROVED 등) 관리

---

## 🧪 테스트

테스트는 JUnit5 기반이며 기능 단위로 구성되어 있습니다.

```

src/test/java
└─ com.market.allra
├─ api
├─ application
├─ domain
└─ LikeServiceTest.java

````

각 테스트는 다음을 검증합니다:

- Service 레이어 단위 비즈니스 검증
- 상품/장바구니/주문 데이터 흐름 검증
- 회원 로그인, 세션 로직 테스트
- JPA 연관관계 및 Cascade 동작 테스트

---

## 🐳 Docker 실행(MySQL)

```yml
version: "3.8"

services:
  mysql:
    image: mysql:8.0
    container_name: allar_db_mysql
    environment:
      TZ: Asia/Seoul
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
      MYSQL_DATABASE: ${MYSQL_DATABASE}
      MYSQL_USER: ${MYSQL_USER}
      MYSQL_PASSWORD: ${MYSQL_PASSWORD}
    ports:
      - "3306:3306"
    volumes:
      - allra_mysql_data:/var/lib/mysql
      - ./logs/mysql:/var/log/mysql
    networks:
      - allra_net
    restart: unless-stopped

volumes:
  allra_mysql_data:

networks:
  allra_net:
    driver: bridge
````

---

## 🔧 실행 방법

### 1) 의존성 설치

```
./gradlew clean build
```

### 2) Spring Boot 실행

```
./gradlew bootRun
```

### 3) MySQL Docker 구동

```
docker compose up -d
```

---

## 🗺 향후 개선 예정

* JWT 기반 인증 적용
* Redis 세션 클러스터링
* QueryDSL 기반 고도화된 검색
* Kafka 기반 비동기 주문 처리
* 테스트 커버리지 확대

---

## 🧑‍💻 Author

**이호진 (Backend Developer)**

* GitHub: [https://github.com/coderwin](https://github.com/coderwin)
* Email: [devlhj@naver.com](mailto:devlhj@naver.com)

```

---

# ✨ 다음 작업도 가능해요
원하면 아래 항목들도 작성해줄게!

- [ ] API 명세서 (표/Swagger 스타일)
- [ ] ERD 다이어그램 생성
- [ ] 패키지 의존성 그래프
- [ ] 과제 제출용 요약 README
- [ ] 기술 블로그 글 스타일 문서화

원하는 추가 항목 있어?
```
