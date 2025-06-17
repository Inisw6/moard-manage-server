# moard-model-server

## 개요
이 프로젝트는 강화 학습 기반 추천시스템인 Moard에서 콘텐츠, 로그 관리를 위한 Spring Boot 기반 RESTful API 서버입니다. 
사용자 행동 데이터를 수집하고, 다양한 소스의 콘텐츠를 통합하여 개인화된 추천을 제공합니다.

## ERD
![erd_diagram](https://github.com/user-attachments/assets/e0100097-6644-4a34-96f3-28a550426205)

## 환경
- Java 17
- Spring Boot 3.4.5
- MySQL 8.0
- Docker & Docker Compose
- Gradle

## 실행방법

### 로컬 개발 환경
1. MySQL 데이터베이스 실행
```bash
docker-compose up mysql
```

2. 애플리케이션 실행
```bash
./gradlew bootRun
```

### Docker 환경
```bash
docker-compose up --build
```

## 프로젝트 구조
```
src/main/java/com/inisw/moard/
├── api/                           # 외부 API 통합
│   ├── embedding/                 # 임베딩 관련 API
│   ├── naver/                     # 네이버 API 연동
│   ├── predict/                   # 예측 관련 API
│   └── youtube/                   # 유튜브 API 연동
├── config/                        # 애플리케이션 설정
│   ├── SwaggerConfig.java         # API 문서화 설정
│   ├── WebClientConfig.java       # 비동기 HTTP 클라이언트 설정
│   └── YoutubeApiProperties.java  # 유튜브 API 설정
├── content/                       # 콘텐츠 관리
│   ├── Content.java               # 콘텐츠 엔티티
│   ├── ContentService.java        # 콘텐츠 비즈니스 로직
│   └── ContentRepository.java     # 콘텐츠 데이터 접근
├── recommendation/                # 추천 시스템
│   ├── content/                   # 추천한 콘텐츠 관련
│   ├── Recommendation.java        # 추천 엔티티
│   ├── RecommendationService.java  # 추천 로직
│   ├── RecommendationController.java  # 추천 API
│   └── RecommendationStatisticsController.java  # 추천 통계
├── searchquery/                    # 검색 쿼리 관리
│   ├── SearchQuery.java            # 검색 쿼리 엔티티
│   ├── SearchQueryService.java     # 검색 쿼리 처리
│   └── SearchQueryController.java  # 검색 API
├── stock/                          # 주식 관련 기능
│   ├── StockInfo.java              # 주식 정보 엔티티
│   └── StockInfoRepository.java    # 주식 데이터 접근
├── user/                           # 사용자 관리
│   ├── User.java                   # 사용자 엔티티
│   ├── UserService.java            # 사용자 관리 로직
│   ├── UserController.java         # 사용자 API
│   └── log/  # 사용자 로그 관리
└── MoardApplication.java  # 애플리케이션 진입점
```

## 주요 컴포넌트 설명

### 1. API 통합 (api/)
- **Embedding API**: 콘텐츠 임베딩 처리
- **Naver API**: 네이버 검색 및 뉴스 데이터 수집
- **Predict API**: 예측 모델 연동
- **YouTube API**: 유튜브 콘텐츠 수집
- **ContentAggregatorService**: 다양한 소스의 콘텐츠 통합 관리

### 2. 설정 관리 (config/)
- **SwaggerConfig**: API 문서화 설정
- **WebClientConfig**: 비동기 HTTP 클라이언트 설정
- **YoutubeApiProperties**: 유튜브 API 설정 관리

### 3. 콘텐츠 관리 (content/)
- **Content 엔티티**: 콘텐츠 메타데이터 관리
- **ContentService**: 콘텐츠 CRUD 및 비즈니스 로직
- **ContentType**: 콘텐츠 유형 정의
- **DoubleListConverter**: 임베딩 벡터 변환 유틸리티

### 4. 추천 시스템 (recommendation/)
- **Recommendation 엔티티**: 추천 데이터 관리
- **RecommendationService**: 추천 로직 구현
- **RecommendationController**: 추천 API 엔드포인트
- **RecommendationStatisticsController**: 추천 통계 관리
- **ModelStatisticsDto**: 모델 성능 통계

### 5. 검색 관리 (searchquery/)
- **SearchQuery 엔티티**: 검색 쿼리 데이터 관리
- **SearchQueryService**: 검색 쿼리 처리 로직
- **SearchQueryController**: 검색 API 엔드포인트

### 6. 주식 정보 (stock/)
- **StockInfo 엔티티**: 주식 정보 데이터 관리
- **StockInfoRepository**: 주식 데이터 접근 계층

### 7. 사용자 관리 (user/)
- **User 엔티티**: 사용자 정보 관리
- **UserService**: 사용자 관리 로직
- **UserController**: 사용자 API 엔드포인트
- **로그 관리**: 사용자 행동 로그 수집 및 분석

### 8. 데이터베이스
- MySQL 8.0을 사용하며, UTF-8 인코딩을 지원
- Docker 컨테이너로 실행되며, 데이터는 영구 볼륨에 저장
- JPA를 통한 객체-관계 매핑

### 9. API 문서화
- Swagger UI를 통한 API 문서화 지원
- `/swagger-ui.html`에서 API 문서 확인 가능

### 10. 주요 의존성
- Spring Boot Web: RESTful API 구현
- Spring Data JPA: 데이터베이스 접근
- Spring WebFlux: 비동기 웹 클라이언트
- Lombok: 보일러플레이트 코드 감소
- MySQL Connector: MySQL 데이터베이스 연결
- SpringDoc OpenAPI: API 문서화

### 11. 개발 도구
- Spring Boot DevTools: 개발 시 자동 재시작 지원
- JUnit: 테스트 프레임워크
