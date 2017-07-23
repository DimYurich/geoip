# (1) Don't need JDK
# (2) prefer Alpine linux
# (4) using docker-aware build
FROM openjdk:8u131-jre-alpine

ADD build/libs/geoip-0.0.1-SNAPSHOT.jar geoip.jar
# (3) setting VM options
# IMPORTANT! Run this with *at least* 2 GB of RAM allocated in Docker
# Yes, I know 1g is an overkill for this, but c'mon
ENV JAVA_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -Xmx1g -XX:+PrintFlagsFinal \
               -XX:ErrorFile=fatal.log -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled \
               -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=10 \
               -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark \
               -XX:+PrintGCDateStamps -verbose:gc -XX:+PrintGCDetails -Xloggc:gc-log.log \
               -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M \
               -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=`date`.hprof"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /geoip.jar" ]