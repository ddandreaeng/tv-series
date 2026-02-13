# SPEC — TV Series (Quarkus 3 + Java 21)

## 1. Obiettivo
Backend **Quarkus** (Java 21) con **frontend embedded** servito come risorse statiche (JS vanilla a moduli). Dominio: CRUD di *Serie TV*.

Principi architetturali:
- **Clean Architecture** (dipendenze verso l’interno; domain indipendente da framework)
- **Fail-fast** (validazioni e invarianti anticipate, errori coerenti)
- **TDD** per la logica di business (unit test sul dominio e sui casi d’uso)

---

## 2. Stack tecnico (estensioni Quarkus suggerite)
- REST: `quarkus-resteasy-reactive` + Jackson (`quarkus-resteasy-reactive-jackson`)
- OpenAPI/Swagger UI: `quarkus-smallrye-openapi`
- Persistenza: `quarkus-hibernate-orm` + `quarkus-jdbc-postgresql`
- Validazione: `quarkus-hibernate-validator`
- RFC7808 Problem Details: mapping custom via `ExceptionMapper` / `ServerExceptionMapper` (RESTEasy Reactive)
- Observability:
  - JSON logging: `quarkus-logging-json` (o `quarkus.log.console.json.*`)
  - Tracing OTel: `quarkus-opentelemetry`
  - Metrics Prometheus: `quarkus-micrometer-registry-prometheus`
- Test:
  - Unit: JUnit 5 (built-in)
  - Integration: `@QuarkusTest` + RestAssured (`quarkus-rest-assured`)
  - DB Dev Services / Testcontainers: Dev Services Quarkus per PostgreSQL

---

## 3. Struttura progetto e package (Clean Architecture)
### 3.1 Layout (indicativo)
```
 tv-series/
 ├─ src/
 │  ├─ main/
 │  │  ├─ java/<basePackage>/
 │  │  │  ├─ domain/
 │  │  │  │  ├─ model/
 │  │  │  │  ├─ port/
 │  │  │  │  └─ error/
 │  │  │  ├─ application/
 │  │  │  │  ├─ usecase/
 │  │  │  │  └─ service/
 │  │  │  ├─ adapter/
 │  │  │  │  ├─ in/
 │  │  │  │  │  └─ rest/
 │  │  │  │  │     ├─ dto/
 │  │  │  │  │     ├─ mapper/
 │  │  │  │  │     └─ error/
 │  │  │  │  └─ out/
 │  │  │  │     └─ persistence/
 │  │  │  │        ├─ entity/
 │  │  │  │        ├─ repository/
 │  │  │  │        └─ mapper/
 │  │  └─ resources/
 │  │     ├─ application.properties
 │  │     └─ META-INF/resources/        # frontend embedded
 │  │        ├─ index.html
 │  │        └─ app/
 │  │           ├─ api.js
 │  │           ├─ views.js
 │  │           └─ main.js
 │  └─ test/java/<basePackage>/
 │     ├─ domain/
 │     ├─ application/
 │     └─ adapter/in/rest/
 └─ pom.xml
```

> Nota: `<basePackage>` es. `it.acme.tvseries`.

### 3.2 Regole di dipendenza
- `domain` non dipende da Quarkus/JPA/HTTP.
- `application` dipende da `domain` e definisce casi d’uso (interfacce/servizi) usando le *port* del dominio.
- `adapter/in/rest` traduce HTTP ⇄ use case (DTO record, mapping, validazioni, RFC7808).
- `adapter/out/persistence` implementa le port di persistenza (Hibernate ORM) e mappa Entity ⇄ Domain.

---

## 4. Modello di dominio
### 4.1 Entità: Serie TV
Attributi:
- `id`: UUID
- `titolo`: string
- `anno`: int
- `genere`: string
- `regista`: string
- `sinossi`: string

### 4.2 DTO e proiezioni (Java records)
**DTO di lettura**
```java
public record TvSeriesDto(
  UUID id,
  String titolo,
  int anno,
  String genere,
  String regista,
  String sinossi
) {}
```

**DTO di creazione**
```java
public record CreateTvSeriesRequest(
  String titolo,
  int anno,
  String genere,
  String regista,
  String sinossi
) {}
```

**DTO di aggiornamento (PUT, sostitutivo)**
```java
public record UpdateTvSeriesRequest(
  String titolo,
  int anno,
  String genere,
  String regista,
  String sinossi
) {}
```

**Proiezione “lista” (più leggera)**
```java
public record TvSeriesListItem(
  UUID id,
  String titolo,
  int anno,
  String genere
) {}
```

---

## 5. Persistenza (PostgreSQL)
### 5.1 Tabella
Nome tabella: `tv_series`

Vincoli (logici):
- PK: `id` UUID
- NOT NULL: `titolo`, `anno`, `genere`, `regista`
- `anno` in range ragionevole (es. 1900..2100) via validazione applicativa; opzionale check DB.
- Indici per query frequenti: `titolo`, `genere`, `anno`
- Vincolo di “quasi-unicità” (opzionale ma consigliato): `unique (titolo, anno, regista)` per prevenire duplicati evidenti.

DDL indicativa:
```sql
create table tv_series (
  id uuid primary key,
  titolo varchar(200) not null,
  anno int not null,
  genere varchar(80) not null,
  regista varchar(120) not null,
  sinossi text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  constraint uq_tv_series unique (titolo, anno, regista)
);

create index ix_tv_series_titolo on tv_series (titolo);
create index ix_tv_series_genere on tv_series (genere);
create index ix_tv_series_anno on tv_series (anno);
```

> `created_at/updated_at` sono consigliati per auditing minimo; se non desiderati, possono essere rimossi.

### 5.2 Dev Services / Testcontainers
- In **dev** e **test** Quarkus avvia automaticamente PostgreSQL via Dev Services (Testcontainers) se non configurata una datasource esterna.
- I test di integrazione usano lo stesso meccanismo (isolamento per suite tramite container dedicato).

---

## 6. API REST (OpenAPI) — Definizione formale
Base path: `/api`

Media types:
- Success: `application/json`
- Errori: `application/problem+json` (RFC 7808)

### 6.1 Risorsa: TV Series
#### 6.1.1 List
`GET /api/series`

Query params (tutti opzionali):
- `q`: string (ricerca full/partial su titolo)
- `genere`: string
- `annoFrom`: int
- `annoTo`: int
- `page`: int (default 0)
- `size`: int (default 20, max 100)
- `sort`: string (es. `titolo,asc`)

Response `200 OK`:
```json
{
  "items": [
    {"id":"...","titolo":"...","anno":2020,"genere":"Drama"}
  ],
  "page": 0,
  "size": 20,
  "total": 1
}
```
Schema item: `TvSeriesListItem`.

#### 6.1.2 Create
`POST /api/series`

Request body: `CreateTvSeriesRequest`

Response:
- `201 Created` con body `TvSeriesDto` e header `Location: /api/series/{id}`
- `400 Bad Request` (validazione)
- `409 Conflict` (violazione vincoli, es. duplicato su `(titolo, anno, regista)`)

#### 6.1.3 Get by id
`GET /api/series/{id}`

Path params:
- `id`: UUID

Response:
- `200 OK` body `TvSeriesDto`
- `404 Not Found` se non esiste
- `400 Bad Request` se UUID non valido

#### 6.1.4 Update (replace)
`PUT /api/series/{id}`

Request body: `UpdateTvSeriesRequest`

Response:
- `200 OK` body `TvSeriesDto`
- `404 Not Found`
- `400 Bad Request` (validazione / UUID)
- `409 Conflict` (vincoli)

#### 6.1.5 Delete
`DELETE /api/series/{id}`

Response:
- `204 No Content`
- `404 Not Found`
- `400 Bad Request` se UUID non valido

### 6.2 Convenzioni HTTP
- Tutti gli endpoint sono **idempotenti** tranne `POST`.
- `PUT` è *replace* (tutti i campi richiesti, niente patch parziali in questa versione).

---

## 7. Error handling standardizzato (RFC 7808)
Tutti gli errori applicativi/validazione devono restituire:
- `Content-Type: application/problem+json`
- body con campi RFC7808: `type`, `title`, `status`, `detail`, `instance`
- estensioni utili (non RFC) per client:
  - `errorCode` (string)
  - `errors` (mappa campo → lista messaggi) per validazioni

Esempio (validazione):
```json
{
  "type": "https://example.com/problems/validation-error",
  "title": "Validation error",
  "status": 400,
  "detail": "Request validation failed",
  "instance": "/api/series",
  "errorCode": "VALIDATION_ERROR",
  "errors": {
    "titolo": ["must not be blank"],
    "anno": ["must be greater than or equal to 1900"]
  }
}
```

Mappature minime:
- `ConstraintViolationException` / `ValidationException` → 400
- `NotFound` (dominio) → 404
- `Duplicate/Conflict` (dominio o DB constraint) → 409
- `IllegalArgumentException` (input malformato) → 400
- fallback non gestito → 500 (con `errorCode=INTERNAL_ERROR`)

---

## 8. Validazione (Jakarta Bean Validation)
Validazioni consigliate sui DTO (fail-fast all’ingresso) e, dove serve, invarianti anche a livello di dominio.

Esempi (indicativi):
- `titolo`: `@NotBlank @Size(max=200)`
- `anno`: `@Min(1900) @Max(2100)`
- `genere`: `@NotBlank @Size(max=80)`
- `regista`: `@NotBlank @Size(max=120)`
- `sinossi`: `@Size(max=5000)` (o libero se `text`)

---

## 9. Observabilità
### 9.1 Logging JSON
Obiettivo: log strutturati (JSON) adatti a stack ELK / Loki.

Configurazione tipica (`application.properties`):
```properties
quarkus.log.console.json=true
quarkus.log.console.json.pretty-print=false
quarkus.log.level=INFO
```

Raccomandazioni:
- includere `traceId`/`spanId` nei log (MDC) quando OTel attivo.

### 9.2 OpenTelemetry (auto-instrumentation HTTP)
- Tracing automatico su richieste HTTP (router/REST).
- Export OTLP verso collector (in dev si può usare logging exporter).

Config tipica:
```properties
quarkus.otel.enabled=true
quarkus.otel.traces.enabled=true
quarkus.otel.metrics.enabled=false
quarkus.otel.exporter.otlp.endpoint=http://localhost:4317
quarkus.otel.service.name=tv-series
```

### 9.3 Metriche Prometheus
Endpoint metriche (tipico): `/q/metrics`.

Config tipica:
```properties
quarkus.micrometer.export.prometheus.enabled=true
quarkus.micrometer.binder.http-server.enabled=true
```

---

## 10. Frontend embedded (JS vanilla a moduli)
Servito da Quarkus come static resources:
- root: `src/main/resources/META-INF/resources/`
- entrypoint: `index.html`
- JS ES modules: `app/main.js` che importa `api.js`, `views.js`, ecc.

Comunicazione con backend:
- `fetch('/api/series', { ... })`
- gestione errori: se `response.headers['content-type']` contiene `application/problem+json`, renderizzare messaggio user-friendly.

---

## 11. Strategia di testing
### 11.1 Unit test (JUnit 5)
Target: **dominio** e **use case** (TDD).
- Nessuna dipendenza da Quarkus.
- Repository port mockata/fake.
- Verifica invarianti, validazioni di business, gestione duplicati, mapping.

### 11.2 Integration test (Quarkus + Dev Services)
Target: comportamento end-to-end (HTTP + DB).
- `@QuarkusTest` + RestAssured su endpoint REST
- Dev Services avvia PostgreSQL (Testcontainers) automaticamente.

Scenari minimi:
- CRUD completo
- validazione input (400 + problem+json)
- not found (404)
- conflict su duplicato (409)

---

## 12. Documentazione OpenAPI/Swagger
- OpenAPI generata automaticamente.
- Swagger UI disponibile in dev (tipico): `/q/swagger-ui`.

Requisiti:
- annotare DTO/endpoint se necessario per esempi e descrizioni.
- includere codici errore e schema `application/problem+json` nelle responses.

---

## 13. Acceptance Criteria (MVP)
### 13.1 API + contratti
- Gli endpoint CRUD di `/api/series` sono implementati come da §6 (status code e payload).
- L’API è documentata in OpenAPI e consultabile via Swagger UI in dev (`/q/swagger-ui`).

### 13.2 Error handling (RFC 7808)
- Tutti gli errori restituiscono `Content-Type: application/problem+json` con i campi `type,title,status,detail,instance`.
- Gli errori di validazione includono `errorCode=VALIDATION_ERROR` e una mappa `errors` campo→messaggi.
- `404` per risorse mancanti, `409` per conflitti/duplicati, `500` come fallback con `errorCode=INTERNAL_ERROR`.

### 13.3 Dati + Dev Services
- PostgreSQL è usato come DB relazionale.
- In `dev` e `test` funziona Dev Services (Testcontainers) senza configurare un DB esterno.
- La persistenza applica i vincoli del modello (PK UUID, NOT NULL, vincolo unico consigliato su `(titolo,anno,regista)`).

### 13.4 Validazione
- I DTO usano Jakarta Bean Validation in modo estensivo (fail-fast all’ingresso).

### 13.5 Osservabilità
- Logging su console in JSON.
- Tracing OpenTelemetry abilitato e auto-strumentato per HTTP.
- Metriche Prometheus esposte (es. `/q/metrics`).

### 13.6 Testing
- La logica di business (domain + use case) è coperta da unit test JUnit 5 (TDD).
- Sono presenti integration test `@QuarkusTest` che coprono CRUD, 400 (validazione), 404, 409.

### 13.7 Frontend embedded
- Il frontend è servito come risorse statiche da `META-INF/resources`.
- Il frontend usa JS vanilla a moduli (ESM) e comunica col backend via Fetch API, gestendo `application/problem+json`.
