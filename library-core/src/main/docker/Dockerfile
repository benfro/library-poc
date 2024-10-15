FROM openjdk:22-slim

RUN apt-get update \
    && apt-get install -y curl \
    && rm -rf /var/lib/apt/lists/*

# Environment variables for Liquibase
ENV LIQUIBASE_VERSION=4.21.1
ENV LIQUIBASE_HOME=/liquibase

# Environment variable for PostgreSQL JDBC driver
ENV POSTGRES_VERSION=42.5.4

# Download and install Liquibase
RUN mkdir ${LIQUIBASE_HOME} \
    && curl -L https://github.com/liquibase/liquibase/releases/download/v${LIQUIBASE_VERSION}/liquibase-${LIQUIBASE_VERSION}.tar.gz | tar -xz -C ${LIQUIBASE_HOME}

# Download and place the PostgreSQL JDBC driver in the classpath
RUN curl -o postgresql-${POSTGRES_VERSION}.jar -L https://jdbc.postgresql.org/download/postgresql-${POSTGRES_VERSION}.jar

# Add the JDBC driver to the classpath
ENV CLASSPATH=/postgresql-${POSTGRES_VERSION}.jar

# Add the liquibase scripts to PATH
ENV PATH=${PATH}:${LIQUIBASE_HOME}

COPY target/library-core-0.0.1-SNAPSHOT.jar.jar /opt/library-core.jar
COPY src/main/docker/entrypoint.sh /opt/entrypoint.sh

ENTRYPOINT ["sh", "/opt/entrypoint.sh"]
