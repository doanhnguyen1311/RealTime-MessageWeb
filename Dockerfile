# Sử dụng OpenJDK làm base image
FROM openjdk:21-jdk-slim

# Đặt biến môi trường để giảm thời gian khởi động
ENV SPRING_PROFILES_ACTIVE=prod

# Định nghĩa thư mục làm việc trong container
WORKDIR /app

# Sao chép file JAR vào container
COPY target/*.jar app.jar

# Expose port ứng dụng sẽ chạy
EXPOSE 8080

# Chạy ứng dụng Spring Boot
CMD ["java", "-jar", "app.jar"]
