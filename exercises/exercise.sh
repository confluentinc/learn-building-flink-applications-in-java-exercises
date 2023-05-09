#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

EXERCISE_DIR=./
SOLUTIONS_DIR=../solutions
STAGING_DIR=../staging

if [ ! -d $STAGING_DIR ]; then
    echo "$STAGING_DIR could not be found."
    exit 1
fi

if [ ! -d $SOLUTIONS_DIR ]; then
    echo "$SOLUTIONS_DIR could not be found."
    exit 1
fi

function help() {
    echo "Usage:"
    echo "  exercises.sh <Command>"
    echo "  Commands:"
    echo "    stage <Exercise Filter> - Setup the exercise."
    echo "        <Exercise Filter> - A portion of the exercise name (eg. the exercise number) that will be used to select the exercise."
    echo "    solve <Exercise Filter> <File Filter> - Solve the exercise."
    echo "        <Exercise Filter> - A portion of the exercise name (eg. the exercise number) that will be used to select the exercise."
    echo "        <File Filter> - (Optional) A portion of a file name that will be used to select while file to copy from the solution."
    echo "    list - List all exercises."
    echo "  Exercise Filter: A portion of the name of the exercise. Eg. The Exercise Number. If multiple matches are found, the first one will be chosen."
}

function stage() {
    EXERCISE_FILTER=$1
    MATCHED_EXERCISES=($(ls $STAGING_DIR | grep ".*$EXERCISE_FILTER.*"))
    EXERCISE=${MATCHED_EXERCISES[0]}

    echo "STAGING $EXERCISE"

    cp -r $STAGING_DIR/$EXERCISE/. $EXERCISE_DIR
}

function solve() {
    EXERCISE_FILTER=$1
    FILE_FILTER=${2:-""}
    MATCHED_EXERCISES=($(ls $SOLUTIONS_DIR | grep ".*$EXERCISE_FILTER.*"))
    EXERCISE=${MATCHED_EXERCISES[0]}
    SOLUTION=$SOLUTIONS_DIR/$EXERCISE

    if [ -z $FILE_FILTER ]; then
        echo "SOLVING $EXERCISE"
    
        cp -r $SOLUTION/. $EXERCISE_DIR
    else
        WORKING_DIR=$(pwd)
        cd $SOLUTION
        MATCHED_FILES=($(find . -iname "*$FILE_FILTER*"))
        cd $WORKING_DIR

        if [ -z ${MATCHED_FILES:-""} ]; then
            echo "FILE NOT FOUND: $FILE_FILTER"
            exit 1
        fi

        FILE_PATH=${MATCHED_FILES[0]}

        echo "COPYING $FILE_PATH FROM $EXERCISE"

        cp $SOLUTION/$FILE_PATH $EXERCISE_DIR/$FILE_PATH
    fi

}

function list() {
    EXERCISES=$(ls $SOLUTIONS_DIR)

    for ex in "${EXERCISES[@]}"
    do
        echo "$ex"
    done
}

COMMAND=${1:-"help"}

## Determine which command is being requested, and execute it.
if [ "$COMMAND" = "stage" ]; then
    EXERCISE_FILTER=${2:-""}
    if [ -z $EXERCISE_FILTER ]; then
        echo "MISSING EXERCISE ID"
        help
        exit 1
    fi
    stage $EXERCISE_FILTER
elif [ "$COMMAND" = "solve" ]; then
    EXERCISE_FILTER=${2:-""}
    FILE_FILTER=${3:-""}
    if [ -z $EXERCISE_FILTER ]; then
        echo "MISSING EXERCISE ID"
        help
        exit 1
    fi
    solve $EXERCISE_FILTER $FILE_FILTER
elif [ "$COMMAND" = "list" ]; then
    list
elif [ "$COMMAND" = "help" ]; then
    help
else
    echo "INVALID COMMAND: $COMMAND"
    help
    exit 1
fi