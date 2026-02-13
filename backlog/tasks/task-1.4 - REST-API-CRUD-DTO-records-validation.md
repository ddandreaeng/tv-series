---
id: TASK-1.4
title: REST API CRUD + DTO records + validation
status: Done
assignee: []
created_date: '2026-02-11 15:17'
updated_date: '2026-02-12 15:30'
labels: []
dependencies: []
documentation:
  - SPEC.MD
parent_task_id: TASK-1
priority: high
ordinal: 4000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Implementare adapter REST (DTO record, mapping, validazione) ed endpoint CRUD come da specifica.
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [x] #1 Endpoint `/api/series` CRUD implementati come da §6 (path, metodi, status code)
- [x] #2 DTO e proiezioni implementati come Java records (§4.2)
- [x] #3 Validazione Jakarta Bean Validation sui DTO (fail-fast) (§8)
- [x] #4 GET list restituisce payload paginato con `items,page,size,total` (§6.1.1)
<!-- AC:END -->
