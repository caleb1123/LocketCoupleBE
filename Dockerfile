# Giai đoạn 1: Giải quyết và tải xuống phụ thuộc
FROM eclipse-temurin:17-jdk-jammy as deps

WORKDIR /build

# Sao chép mvnw với quyền thực thi
COPY --chmod=0755 mvnw mvnw
COPY .mvn/ .mvn/

# Tải xuống phụ thuộc để tận dụng bộ nhớ đệm của Docker
RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 ./mvnw dependency:go-offline -DskipTests

# Giai đoạn 2: Xây dựng ứng dụng
FROM deps as package

WORKDIR /build

COPY ./src src/
RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 \
    ./mvnw package -DskipTests && \
    mv target/$(./mvnw help:evaluate -Dexpression=project.artifactId -q -DforceStdout)-$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout).jar target/app.jar

# Giai đoạn 3: Trích xuất lớp
FROM package as extract

WORKDIR /build

RUN java -Djarmode=layertools -jar target/app.jar extract --destination target/extracted

# Giai đoạn 4: Giai đoạn cuối để chạy ứng dụng
FROM eclipse-temurin:17-jre-jammy AS final

# Tạo người dùng không có đặc quyền
ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser
USER appuser

# Sao chép các tệp cần thiết từ giai đoạn trích xuất
COPY --from=extract build/target/extracted/dependencies/ ./
COPY --from=extract build/target/extracted/spring-boot-loader/ ./
COPY --from=extract build/target/extracted/snapshot-dependencies/ ./
COPY --from=extract build/target/extracted/application/ ./

EXPOSE 8080

ENTRYPOINT [ "java", "org.springframework.boot.loader.launch.JarLauncher" ]
