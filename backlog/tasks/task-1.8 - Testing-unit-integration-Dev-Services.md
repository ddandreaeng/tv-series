---
id: TASK-1.8
title: 'Testing: unit + integration (Dev Services)'
status: Done
assignee: []
created_date: '2026-02-11 15:17'
updated_date: '2026-02-12 16:40'
labels: []
dependencies: []
documentation:
  - SPEC.MD
parent_task_id: TASK-1
priority: high
ordinal: 8000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Completare suite di test: unit (dominio/use case) e integration (HTTP+DB) usando Dev Services PostgreSQL.
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [x] #1 Unit test JUnit 5 per domain+application (TDD) (§11.1)
- [x] #2 Integration test `@QuarkusTest` coprono CRUD, 400(validazione), 404, 409 (§11.2)
- [x] #3 Integration test usano Dev Services PostgreSQL/Testcontainers senza DB esterno (§5.2, §11.2)
<!-- AC:END -->
