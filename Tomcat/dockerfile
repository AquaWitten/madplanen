FROM tomcat:10.1.50-jre21-temurin
COPY app/build/libs/madplanen.war /usr/local/tomcat/webapps/
EXPOSE 8080
CMD ["catalina.sh", "run"]