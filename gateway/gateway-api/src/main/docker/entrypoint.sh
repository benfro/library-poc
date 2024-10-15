#!/bin/bash

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

log_message() {
    echo -e "$(date +'%Y-%m-%d %H:%M:%S') - $2 $1 ${NC}"
}

log_message "${YELLOW}🚀 Container startup initiated...${NC}"

#log_message "${BLUE}📚 Extracting changelog file from application binary...${NC}"
#jar xf /opt/gateway-api.jar BOOT-INF/classes/database/changes/schema.xml
#log_message "${GREEN}📚 Changelog file extraction complete.${NC}"
#
#log_message "${BLUE}🔧 Executing liquibase to upgrade database...${NC}"
#liquibase --driver=org.postgresql.Driver \
#    --classpath=${CLASSPATH} \
#    --changeLogFile=BOOT-INF/classes/database/changes/schema.xml \
#    --url="jdbc:postgresql://${DATABASE_HOST}:${DATABASE_PORT}/${DATABASE_NAME}" \
#    --username=${DATABASE_USER} \
#    --password=${DATABASE_PASSWORD} \
#    update
#log_message "${GREEN}🔧 Database upgrade successful.${NC}"

# Starting application...
log_message "${YELLOW}🚀 Starting application...${NC}"

java -Dspring.profiles.active=prod -jar /opt/gateway-api.jar
