# GIGA LCK

LCK 팬들을 위한 경기 일정 확인, 리뷰 작성 및 팬들과 소통할 수 있는 통합 플랫폼입니다.

## 🚀 프로젝트 소개

**GIGA LCK**는 다양한 사이트에 흩어져 있는 LCK 정보를 한곳에 모아 제공하며, 팬들이 활발히 소통할 수 있도록 구축한 개인 프로젝트입니다. 경기 일정, 리뷰, 뉴스 크롤링 및 다시보기 링크를 제공하며, 댓글과 평점 시스템을 통해 팬들의 참여를 극대화합니다.

## 🎯 주요 기능

- **경기 일정 조회**: 월별, 팀별로 일정을 편리하게 확인 가능
- **경기 리뷰 및 평점**: 팬들이 자유롭게 경기에 대한 리뷰와 평점 작성 가능
- **뉴스 크롤링**: Naver API를 이용하여 최신 경기 관련 뉴스 제공
- **다시보기 링크 제공**: YouTube API를 통해 공식 다시보기 링크 제공
- **팀 및 선수 정보**: 상세한 팀 정보와 선수 전적 분석 제공
- **팬 소통 기능**: 응원 댓글 및 링크 공유(카카오톡 공유, 클립보드 복사) 기능 제공

## ⚙️ 기술 스택

### Frontend

- Vue.js
- Pinia
- Tailwind CSS
- Vite

### Backend

- Java (Spring Boot)
- Spring JPA
- PostgreSQL

### APIs

- YouTube API
- Naver API
- Riot API
- OpenAI API

## 📂 프로젝트 구조

```
.
├── frontend
│   ├── src
│   │   ├── components
│   │   ├── views
│   │   ├── assets
│   │   └── api
│   └── vite.config.js
└── backend
    ├── src
    │   ├── main
    │   │   ├── java
    │   │   └── resources
    │   └── test
    └── build.gradle
```

## 📌 프로젝트 실행 방법

### Frontend 실행

```sh
cd frontend
npm install
npm run dev
```

### Backend 실행

```sh
cd backend
./gradlew bootRun
```

## 🔑 API 키 설정

다음의 API 키들은 보안을 위해 재발급 받아야 하며, 발급받은 키를 `application.properties`에 입력해야 합니다.

```properties
spring.application.name=esports

# Riot API Key
riot.api.key=YOUR_NEW_RIOT_API_KEY

# OpenAI API Key
openai.api.key=YOUR_NEW_OPENAI_API_KEY

# YouTube API Key
youtube.api.key=YOUR_NEW_YOUTUBE_API_KEY

# Naver News API Key
naver.api.client-id=YOUR_NEW_NAVER_CLIENT_ID
naver.api.client-secret=YOUR_NEW_NAVER_CLIENT_SECRET

# PostgreSQL 연결 정보
spring.datasource.url=jdbc:postgresql://localhost:5432/esports
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver

# Hibernate JPA 설정
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Logging
logging.level.org.springframework.web.reactive.function.client.ExchangeFunctions=TRACE
logging.level.com.kt.esports=DEBUG
```

## 📝 배운 점 및 회고

- 첫 풀스택 개발 프로젝트로 Vue.js와 Spring Boot 간의 연동 경험
- 다양한 API 연동을 통해 실제 서비스와 유사한 환경 구축 경험
- AI 기반 코딩 도구(GPT, Cursor)를 활용하여 개발 효율 향상
- 최종 결정은 개발자의 이해와 판단이 필수임을 인지

## 🔮 향후 개선 계획

- 로그인 및 개인화 기능 추가
- 찜하기, 알림 구독 기능 구현
- KT의 타 스포츠에 유사 서비스 확장 가능
