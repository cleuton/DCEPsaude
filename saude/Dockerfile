FROM dockerfile/java:openjdk-7-jdk
ADD saude-0.0.1-SNAPSHOT.jar /data/saude-0.0.1-SNAPSHOT.jar
ADD saude.yml /data/saude.yml
CMD java -jar /data/saude-0.0.1-SNAPSHOT.jar server /data/saude.yml > log.txt 2>&1
EXPOSE 3000
