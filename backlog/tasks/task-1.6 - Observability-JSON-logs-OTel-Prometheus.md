---
id: TASK-1.6
title: 'Observability: JSON logs + OTel + Prometheus'
status: Done
assignee: []
created_date: '2026-02-11 15:17'
updated_date: '2026-02-12 16:05'
labels: []
dependencies: []
documentation:
  - SPEC.MD
parent_task_id: TASK-1
priority: medium
ordinal: 6000
---

## Description

<!-- SECTION:DESCRIPTION:BEGIN -->
Configurare logging JSON, tracing OpenTelemetry e metriche Prometheus.
<!-- SECTION:DESCRIPTION:END -->

## Acceptance Criteria
<!-- AC:BEGIN -->
- [x] #1 Logging console in JSON abilitato (§9.1)
- [x] #2 OpenTelemetry tracing abilitato con auto-strumentazione HTTP e service.name=tv-series (§9.2)
- [x] #3 Metriche Prometheus esposte (tipicamente `/q/metrics`) (§9.3)
<!-- AC:END -->
