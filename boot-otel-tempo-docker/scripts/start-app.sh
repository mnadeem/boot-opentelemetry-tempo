#!/bin/bash

ulimit -n 10000

[ -z "$MIN_HEAP_SIZE" ] && MIN_HEAP_SIZE=40M
[ -z "$MAX_HEAP_SIZE" ] && MAX_HEAP_SIZE=512M
[ -z "$THREADSTACK_SIZE" ] && THREADSTACK_SIZE=228k
[ -z "$JAVA_GC_ARGS" ] && JAVA_GC_ARGS=-XX:MinHeapFreeRatio=20 -XX:MaxHeapFreeRatio=40 -XX:+UseSerialGC -XX:GCTimeRatio=4 -XX:AdaptiveSizePolicyWeight=90
[ -z "$PROG_ARGS" ] && PROG_ARGS=""

set -e

## Verify that SpringBoot app.jar exists 
if [ ! -f "${APP_HOME}/${APP_NAME}.jar" ]; then
   echo "Springboot jar '${APP_HOME}/${APP_NAME}.jar' not found! Exiting..."
   exit 1
fi

echo $JAVA_OPTS $MIN_HEAP_SIZE $MAX_HEAP_SIZE $THREADSTACK_SIZE $JAVA_GC_ARGS $JAVA_DIAG_ARGS $JAVA_OPTS_APPEND $PROG_ARGS

java $JAVA_OPTS \
 -Xms${MIN_HEAP_SIZE} \
 -Xmx${MAX_HEAP_SIZE} \
 -Xss${THREADSTACK_SIZE} \
 $JAVA_GC_ARGS \
 $JAVA_DIAG_ARGS \
 $JAVA_OPTS_APPEND \
 -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE \
 -jar ${APP_HOME}/${APP_NAME}.jar \
 $PROG_ARGS
 