Dobbiamo sviluppare un greenfiled in quarkus con java 21. Sarà un backend con frontend embedded servito come risorse statiche in javascript vanilla.
L0architettura deve seguire i principi di clean arch quindi fail fast e TDD per la logica di business. 

Specifiche tecniche:

API: Restful documentate con OpanAPI/Swagger.
Gestione degli errori standardizzata tramite
problem details rfc 7808

Dati: Database relazionale postgresql. Useremo i 
quarkus dev services con testcontainer per lo
sviluppo e i test di integrazione.

Model: il dominio è un CRUD di Serie TV uuid,
titolo, anno genere, regista e sinossi. Usa i 
java records per dto e proiezioni.

Osservabilità: Logget standard in json,
istrumentazione automatica di opentelemetry nel
router http e metriche prometheus.

Validazione: Uso estensivo di jakarta bean
validation.

Frontend: Js vanilla moderno strutturato a moduli 
che comunicano con il be tramite fetch api.

Creami un file SPEC.MD che descriva la struttura 
del progetto e dei packge, la definizione formale
degli endpoint API, il modello dati e vincoli di 
persistenza.

Strategia di testing unit con Junit 5 + 
integration comn dev services.

Configurazione dell'osservabilità.