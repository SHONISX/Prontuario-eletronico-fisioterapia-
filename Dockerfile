FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY src ./src
COPY lib ./lib

RUN mkdir -p bin && \
    javac -cp "lib/*" -d bin $(find src -name "*.java")

CMD ["java", "-cp", "bin:lib/*", "Server"]