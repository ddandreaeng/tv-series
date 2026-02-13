---
id: TASK-1
title: 'MVP: TV Series (Quarkus Java 21)'
status: Done
assignee: []
created_date: '2026-02-11 15:11'
updated_date: '2026-02-12 16:42'
labels: []
dependencies: []
documentation:
  - SPEC.MD
priority: high
ordinal: 1000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Epic MVP per backend Quarkus 3.31.2 (Java 21) + frontend embedded (JS vanilla).
Requisiti e contratti sono definiti in SPEC.MD.
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [x] #1 CRUD `/api/series` implementato e documentato via OpenAPI/Swagger
- [x] #2 Error handling RFC7808 (`application/problem+json`) per validazione e errori applicativi
- [x] #3 PostgreSQL con Dev Services attivo in dev/test + vincoli di persistenza
- [x] #4 Logging JSON + tracing OpenTelemetry + metriche Prometheus
- [x] #5 Suite test: unit (TDD) + integration `@QuarkusTest` con Dev Services
- [x] #6 Frontend static embedded con Fetch API verso `/api`
<!-- AC:END -->
