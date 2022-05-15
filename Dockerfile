FROM adoptopenjdk:11-jre-openj9
RUN mkdir /opt/app
COPY /target/* /opt/app
