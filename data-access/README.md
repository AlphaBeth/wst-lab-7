# Data access module

All code related to database queries.

## Packages

- model - entity classes
- dao - classes to execute queries and convert result sets
- db - utilities for query generation

## Entities

There is only one entity - exterminatus.

Table description (Postgresql)
```sql
CREATE TABLE exterminatus
(
  id        BIGINT PRIMARY KEY,
  initiator VARCHAR(512) NOT NULL,
  reason    VARCHAR(2048),
  method    VARCHAR(255) NOT NULL,
  planet    VARCHAR(255) NOT NULL,
  date      TIMESTAMP    NOT NULL
);
```

Sequence for id generation.

```sql
CREATE SEQUENCE exterminatus_sequence;
```

## Artifacts

Single artifact will be produced - module jar.
