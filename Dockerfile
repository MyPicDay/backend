# 1. Base Image 설정 (Temurin JDK 17 사용)
FROM eclipse-temurin:17-jdk-alpine

# 2. 작업 디렉터리 생성
WORKDIR /app

# 3. JAR 파일 복사
COPY build/libs/mypicday-0.0.1-SNAPSHOT.jar app.jar

# 4. 포트 개방
EXPOSE 8080

# 5. 실행 명령어 설정
ENTRYPOINT ["java", "-jar", "app.jar"]
