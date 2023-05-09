#!/bin/bash

mvn clean package

LOCAL_FLINK_PATH=(../flink*/bin/flink)
GLOBAL_FLINK_PATH=$(which flink)
FLINK_PATH=""

if [ -f "$LOCAL_FLINK_PATH" ]; then
    echo "USING LOCAL FLINK INSTALLATION AT $LOCAL_FLINK_PATH".
    FLINK_PATH=$LOCAL_FLINK_PATH
elif [ -f "$GLOBAL_FLINK_PATH" ]; then
    echo "USING GLOBAL FLINK INSTALLATION AT $GLOBAL_FLINK_PATH".
    FLINK_PATH=$GLOBAL_FLINK_PATH
else
    echo "NO FLINK INSTALLATION FOUND!"
    exit 1
fi

$FLINK_PATH run target/travel-itinerary-0.1.jar
