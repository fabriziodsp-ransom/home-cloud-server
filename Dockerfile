FROM alpine:latest

# JDK v17 instalation. 

RUN apk update
RUN echo Installing JDK v17
RUN apk add openjdk17
RUN java -version
RUN echo Installing WGET utility
RUN apk add wget

# Unpacking tomcat v10.1.6
RUN echo Unpacking Tomcat v10.1.6 java server
RUN mkdir /usr/bin/tomcat
ADD apache-tomcat-10.1.6.tar.gz /usr/bin/tomcat

# The server .war file initial location (since tomcat isn't installed yet)
RUN mkdir /opt/app
WORKDIR /opt/app
COPY home-cloud-server-0.0.1.war .
RUN echo Testing tomcat...
RUN /usr/bin/tomcat/apache-tomcat-10.1.6/bin/catalina.sh version
CMD ["echo", "Welcome!"]
