FROM openjdk:8-jre-alpine

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0

WORKDIR /app-kyc/
# add directly the war
COPY target/proximax-kyc-*.war /app-kyc/app.war
#COPY config.yml /app-kyc/config.yml

EXPOSE 9000
CMD ["java","-jar","app.war","--spring.config.name=config"]