---
id: TASK-1.3
title: Persistence adapter (PostgreSQL + Hibernate ORM)
status: Done
assignee: []
created_date: '2026-02-11 15:17'
updated_date: '2026-02-12 12:25'
labels: []
dependencies: []
documentation:
  - SPEC.MD
parent_task_id: TASK-1
priority: high
ordinal: 3500
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Implementare adapter di persistenza PostgreSQL (Hibernate ORM) e mapping Entity↔Domain, usando Dev Services.
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [x] #1 Tabella `tv_series` mappata con PK UUID e NOT NULL per titolo/anno/genere/regista (§5)
- [x] #2 Vincolo unico su `(titolo, anno, regista)` (consigliato in §5)
- [x] #3 Dev Services avvia PostgreSQL in dev e test senza DB esterno (§5.2)
<!-- AC:END -->
