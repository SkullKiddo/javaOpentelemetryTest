#!/bin/bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )


mvn -f ${SCRIPT_DIR}/pom.xml clean install
mvn -f ${SCRIPT_DIR}/pom.xml exec:java
