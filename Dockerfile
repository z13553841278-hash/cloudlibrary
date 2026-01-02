# 构建阶段：使用Maven编译
FROM swr.cn-north-4.myhuaweicloud.com/ddn-k8s/docker.io/maven:3.8.5-openjdk-17 AS builder
COPY settings.xml /usr/share/maven/conf/settings.xml
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# 运行阶段：轻量级JRE
#FROM swr.cn-north-4.myhuaweicloud.com/ddn-k8s/docker.io/eclipse-temurin:17.0.12_7-jre-alpine
# 调试阶段：轻量级JDK
FROM swr.cn-north-4.myhuaweicloud.com/ddn-k8s/docker.io/eclipse-temurin:17-jdk-alpine
ENV JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8"
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar
# 调试阶段：需要源代码
COPY --from=builder /build/src ./src

EXPOSE 8080
EXPOSE 5005

#CMD ["java", "-jar", "app.jar"]
# 调试代码
CMD ["java", \ 
     "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", \
     "-Djava.security.egd=file:/dev/./urandom", \
     "-jar", "app.jar"]

# 设置容器编码为UTF-8
ENV LANG C.UTF-8
ENV LC_ALL C.UTF-8