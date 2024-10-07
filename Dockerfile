# Use an official Java runtime as a parent image
FROM openjdk:21-slim

# Set the working directory in the container
WORKDIR /app

# шаг сборки - build
# maven - это образ Linux + Maven
FROM maven as build
# создаем папку внутри этого Linux-а и называем ее app
WORKDIR /workspace/app
# в эту папку копируем наш pom.xml
COPY pom.xml .
# также в эту папку копируем наш исходный код
COPY src src
# запускаем maven на сборку
RUN mvn -DskipTests=true clean package
# создаем папку, которая называется dependency и в нее копируем все, что находится внутри нашего jar и имеет расширение jar
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# переходим к новому шагу
# берем linux, в котором уже нет maven-а и прочего мусора, а есть только JRE (Виртуальная машина)
FROM eclipse-temurin:21-jre-alpine
# назвали нашу папку с зависимостями DEPENDENCY
ARG DEPENDENCY=/workspace/app/target/dependency
# из предыдущего шага забираем все зависимости и копируем их в новый linux
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
# запускаем приложение вместе со всеми библиотеками
ENTRYPOINT ["java","-cp","app:app/lib/*", "de.ait.platform.PlatformApplication"]