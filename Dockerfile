# --- Stage 1: Build ---
# Sử dụng image Maven có sẵn Java 17 để build
FROM maven:3.9.6-amazoncorretto-17 AS build

# Tạo thư mục làm việc
WORKDIR /app

# Copy file pom.xml trước để cache dependencies (Tối ưu tốc độ build)
COPY pom.xml .
# Tải dependencies về (nếu pom.xml không đổi, bước này sẽ được cache)
RUN mvn dependency:go-offline

# Copy toàn bộ source code vào
COPY src ./src

# Build ra file .jar (Skip test để build nhanh hơn demo, thực tế nên chạy test)
RUN mvn clean package -DskipTests

# --- Stage 2: Run ---
# Sử dụng image Java 17 siêu nhẹ (Alpine) chỉ để chạy app
FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

# Copy file .jar từ Stage 1 sang Stage 2
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Lệnh chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]