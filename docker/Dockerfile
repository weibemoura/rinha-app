FROM bellsoft/liberica-openjre-alpine-musl:20

WORKDIR /data

COPY application.jar .

EXPOSE 8080

CMD ["java", "--enable-preview", "-jar", "-Dserver.port=8080", "application.jar"]