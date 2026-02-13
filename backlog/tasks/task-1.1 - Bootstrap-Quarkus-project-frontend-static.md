---
id: TASK-1.1
title: Bootstrap Quarkus project + frontend static
status: Done
assignee: []
created_date: '2026-02-11 15:17'
updated_date: '2026-02-12 12:20'
labels: []
dependencies: []
documentation:
  - SPEC.MD
parent_task_id: TASK-1
priority: high
ordinal: 2000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Setup progetto Quarkus 3.31.2 con Java 21 e dipendenze base; predisporre static resources per frontend.
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [x] #1 Build con Java 21: `./mvnw test` OK
- [x] #2 Swagger UI disponibile in dev su `/q/swagger-ui`
- [x] #3 Static resources servite da `src/main/resources/META-INF/resources` (es. `/` → index.html)
<!-- AC:END -->
