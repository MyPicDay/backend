# MyPicDay 백엔드 프로젝트

본 프로젝트는 일기 작성 및 커뮤니티 기능을 제공하는 MyPicDay 서비스의 백엔드 API 서버입니다.

## 1. 프로젝트 구조

본 프로젝트는 모듈화되고 확장 가능한 구조를 지향하며, 일반적인 Spring Boot 프로젝트 구조를 따릅니다. 주요 소스 코드는 `src/main/java/mypicday/store` 패키지 하위에 위치합니다.

**최상위 패키지 구조:**

- **`mypicday.store.global`**: 여러 도메인에서 공통적으로 사용되는 모듈을 관리합니다.
    - 예: 전역 예외 처리, 공통 유틸리티, 기본 엔티티, 애플리케이션 실행 시 초기화 로직 등
- **`mypicday.store.<도메인명>`** (예: `mypicday.store.user`): 각 기능 도메인별 패키지입니다.

**각 도메인 패키지 내부 구조:**

각 도메인 패키지(예: `user`)는 일반적으로 다음과 같은 하위 패키지를 포함하여 계층형 아키텍처(Layered Architecture)를 따릅니다.

```
mypicday.store/<도메인명>/
├── controller/       # API 요청 처리 및 응답 담당 (HTTP 엔드포인트)
├── dto/              # 데이터 전송 객체 (Data Transfer Objects - Request/Response)
├── entity/           # 데이터베이스 테이블과 매핑되는 JPA 엔티티
├── exception/        # 해당 도메인에서 발생할 수 있는 특화된 예외 정의
├── repository/       # 데이터베이스 접근 계층 (Spring Data JPA 인터페이스)
└── service/          # 비즈니스 로직 구현 및 트랜잭션 관리
```

**예시 (user 도메인):**

```
src/main/java/
└── mypicday/
    └── store/
        ├── global/
        │   └── ... (전역 모듈)
        └── user/                 
            ├── controller/       # UserController.java
            ├── dto/              # UserDto.java, UserRequest.java 등
            ├── entity/           # User.java
            ├── exception/        # UserNotFoundException.java 등
            ├── repository/       # UserRepository.java
            └── service/          # UserService.java
```

**기타 주요 디렉토리 및 파일:**

- `src/main/resources/static`: 정적 리소스 (CSS, JavaScript, 이미지 파일 등)가 위치합니다.
- `src/main/resources/application.yml`: 모든 프로필에서 공통적으로 사용될 기본 설정을 정의합니다.
- `src/main/resources/application-{profile}.yml` (예: `application-dev.yml`, `application-prod.yml`): 특정 프로필(환경)에 대한 설정을 정의합니다. 여기서 정의된 내용은 `application.yml`의 설정을 덮어쓰거나 확장합니다. 예를 들어, `application-dev.yml`은 개발 환경 설정을, `application-prod.yml`은 운영 환경 설정을 담당합니다.

## 2. 커밋 컨벤션
```
<타입>: <제목> #이슈번호 (이슈번호는 선택 사항)
```

**타입 (Type):**

- `feat`: 새로운 기능 추가
- `fix`: 버그 수정
- `docs`: 문서 변경 (README.md, 주석 등)
- `style`: 코드 스타일 변경 (포맷팅, 세미콜론 누락 등 기능 변경 없는 경우)
- `refactor`: 코드 리팩토링 (기능 변경 없이 내부 구조 개선)
- `test`: 테스트 코드 추가/수정 (프로덕션 코드 변경 없음)
- `chore`: 빌드 관련, 패키지 매니저 설정 변경 등 (프로덕션 코드 변경 없음)
- `perf`: 성능 개선
- `ci`: CI/CD 관련 설정 변경
- `revert`: 이전 커밋으로 되돌리기

**예시:**

```
feat: 사용자 가입 기능 구현 #42

- 사용자 이름, 이메일, 비밀번호를 받아 회원 가입 처리
- 비밀번호는 BCrypt로 암호화하여 저장
```

```
fix: TestUserGenerator가 dev 프로필에서만 동작하도록 수정 #55

기존에는 모든 프로필에서 테스트 사용자가 생성되었으나,
이제 application-dev.yml을 통해 dev 프로필이 활성화된 경우에만
테스트 사용자가 생성되도록 @Profile("dev") 어노테이션을 추가함.
```