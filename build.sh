#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

EXERCISES_DIR=exercises
SOLUTIONS_DIR=solutions
STAGING_DIR=staging
SCRIPT_DIR=scripts

function help() {
    echo "Usage:"
    echo "  build.sh <command>"
    echo "  Commands:"
    echo "    validate - Run through each exercise, stage the exercise, then apply the solution. Verify the exercise builds in it's final state."   
}

function validate() {
    WORKING_DIR=$(pwd)

    EXERCISES=($(ls $SOLUTIONS_DIR/ | grep "^[0-9]*"))

    TMP_DIR=target/tmp
    rm -rf $TMP_DIR
    mkdir -p $TMP_DIR

    cp -r $EXERCISES_DIR $TMP_DIR
    cp -r $SOLUTIONS_DIR $TMP_DIR
    cp -r $STAGING_DIR $TMP_DIR 

    cd $TMP_DIR/$EXERCISES_DIR

    for EXERCISE in "${EXERCISES[@]}"
    do
        echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
        echo $EXERCISE
        ./exercise.sh stage $EXERCISE
        ./exercise.sh solve $EXERCISE
        echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"

        if [ -f "pom.xml" ]; then
            mvn clean test
        fi
    done

    rm -rf $TMP_DIR

    cd $WORKING_DIR
}

## Determine which command is being requested, and execute it.
COMMAND=${1:-"help"}
if [ "$COMMAND" = "validate" ]; then
    validate
elif [ "$COMMAND" = "help" ]; then
    help
else
    echo "INVALID COMMAND: $COMMAND"
    help
    exit 1
fi