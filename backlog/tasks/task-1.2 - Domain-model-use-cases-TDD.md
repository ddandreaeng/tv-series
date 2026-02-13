---
id: TASK-1.2
title: Domain model + use cases (TDD)
status: Done
assignee: []
created_date: '2026-02-11 15:17'
updated_date: '2026-02-12 12:24'
labels: []
dependencies: []
documentation:
  - SPEC.MD
parent_task_id: TASK-1
priority: high
ordinal: 3000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Definire dominio e casi d’uso CRUD in stile Clean Architecture, guidati da TDD.
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [x] #1 Package clean-arch (domain/application/adapter) come §3
- [x] #2 Use case CRUD definiti in application e dipendono solo da port di domain
- [x] #3 Unit test JUnit 5 coprono la logica di business senza dipendere da Quarkus (§11.1)
<!-- AC:END -->
