---
id: TASK-1.5
title: RFC7808 problem details (error handling)
status: Done
assignee: []
created_date: '2026-02-11 15:17'
updated_date: '2026-02-12 15:55'
labels: []
dependencies: []
documentation:
  - SPEC.MD
parent_task_id: TASK-1
priority: high
ordinal: 5000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Standardizzare gestione errori via RFC 7808 Problem Details (application/problem+json).
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [x] #1 Error responses con `application/problem+json` e campi `type,title,status,detail,instance` (§7)
- [x] #2 Validazione → 400 con `errorCode=VALIDATION_ERROR` e `errors` campo→messaggi (§7)
- [x] #3 404 per not found, 409 per conflict/duplicati, 500 fallback con `errorCode=INTERNAL_ERROR` (§7)
<!-- AC:END -->
