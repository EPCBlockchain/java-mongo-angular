FROM openjdk:8-jre-alpine

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0 \
    CONFIG_FILE=config.yml

# Create the directory
#ENV MAIL_TEMPLATE_PATH /data/mails
 #RUN mkdir -p $MAIL_TEMPLATE_PATH

# Copy the mail templates
#COPY mails/templates $MAIL_TEMPLATE_PATH/templates/
#COPY mails $MAIL_TEMPLATE_PATH/

# Expose as a volume
# VOLUME $MAIL_TEMPLATE_PATH

# add directly the war
ADD *.war /app.war
COPY ${CONFIG_FILE} /${CONFIG_FILE}

EXPOSE 8080
CMD echo "The application will start in ${JHIPSTER_SLEEP}s..." && \
    sleep ${JHIPSTER_SLEEP} && \
    java -jar /app.war --spring.config.location=file:./${CONFIG_FILE}
    #-Djava.security.egd=file:/dev/./urandom
