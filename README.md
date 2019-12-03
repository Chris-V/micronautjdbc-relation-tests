# micronaut-data-jdbc @Relation tests

This repository demos some micronaut-data-jdbc relation tests. Docker must be present to run tests. testcontainers is used to bootstrap a MariaDB container.

`./gradlew test` should succeed. Note the failing test for the nullable relation.
