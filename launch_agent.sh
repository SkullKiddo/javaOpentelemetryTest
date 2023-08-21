#!/bin/bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

# mvn clean install -f ${SCRIPT_DIR}/pom.xml

mvn -f ${SCRIPT_DIR}/pom.xml exec:java
