# Moard Spring API

## 프로젝트 소개
Moard는 주식 관련 정보를 제공하는 Spring Boot 기반의 REST API 서비스입니다.

## 기술 스택
- Java 17
- Spring Boot 3.4.5
- Spring Data JPA
- H2 Database
- Swagger (OpenAPI 3.0)
- Lombok
- Gradle

## 주요 기능
- 사용자 관리 (User)
- 콘텐츠 관리 (Content)
- 주식 정보 관리 (Stock)
- 추천 시스템 (Recommendation)
- 검색 쿼리 관리 (SearchQuery)

## 프로젝트 구조
```
src/main/java/com/inisw/moard/
├── api/          # API 엔드포인트
├── config/       # 설정 클래스
├── content/      # 콘텐츠 관련 기능
├── recommendation/ # 추천 시스템
├── searchquery/  # 검색 쿼리 관리
├── stock/        # 주식 관련 기능
└── user/         # 사용자 관리
```

## 시작하기

### 필수 조건
- Java 17
- Gradle

### 환경 변수 설정
프로젝트 루트 디렉토리에 `.env` 파일을 생성하고 다음 환경 변수들을 설정해주세요:

```env
NAVER_CLIENT_ID=your-naver-client-id
NAVER_CLIENT_SECRET=your-naver-client-secret
# YouTube API 설정
YOUTUBE_API_KEY=your-youtube-api-key
```

### 실행 방법
1. 프로젝트 클론
```bash
git clone [repository-url]
```

2. 프로젝트 빌드
```bash
./gradlew build
```

3. 애플리케이션 실행
```bash
./gradlew bootRun
```

### Docker로 실행
```bash
docker-compose up
```

## API 문서
Swagger UI를 통해 API 문서를 확인할 수 있습니다:
```
http://localhost:8080/swagger-ui.html
```