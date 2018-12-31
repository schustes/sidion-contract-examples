# Contract tests #

Dieses Repository enthält Beispiele für Contract tests mit Spring Cloud Contract (SCC), Pact (JVM) und der Kombination von
beidem (Fokus auf letzterem).

Als Beispielanwendung wurden Fragmente einer Online-Bibliothek gewählt, umd sowohl Rest Verträge (books-catalog-service)
als auch Message Verträge (books-order) abzubilden.

## Aufbau ##

#### Beispielanwendung ####
- books-catalog-service: Dummy Anwendung zum Abrufen von Büchern
- books-order-service: Dummy Anwendung zum Kaufen von Büchern
- books-client: Ein Client, der an books-catalog-service und books-order-service deligiert und alles Consumer Contracts
in den verschiedenen Formaten definiert (rest / messaging mit pact / scc).

Es handelt sich um keine wirkliche Anwendung. Eine Postman collection existiert, um mit den Schnittstellen zu experimentieren.

#### Consumer Tests: Im Sub-Projekt books-client ####
- BookCatalogConsumerPactTest.java: Rest-Api Consumer Test mit Pact
- BookOrderedEventConsumerPactTest.java: Messaging Consumer Test mit Pact
- BookCatalogConsumerSccStubrunnerTest.java: Rest-Api Consumer Test mit scc
- BookOrderedEventConsumerSccStubrunnerTest.java: Messaging Consumer Test mit scc

#### Rest-Api Producer Tests####
- books-catalog-service-pact-only-test: Beispiel für einen mit pact-jvm implementierten Test
- books-catalog-service-scc-only-test: Beispiel für einen mit scc implementierten Test
- books-catalog-service-pact-provided-scc-test: Beispiel für einen mit scc implementierten Test der als Quelle Pact Verträge verwendet

#### Messaging Producer Tests ####
- books-order-service-pact-only-test: Beispiel für einen mit pact-jvm implementierten Test
- books-order-service-scc-only-test: Beispiel für einen mit scc implementierten Test
- books-order-service-pact-provided-scc-test: Beispiel für einen mit scc implementierten Test der als Quelle Pact Verträge verwende

## Stolpersteine ##
Es wurde ein eigener fork von spring-cloud-contract zu grunde gelegt. Hintergrund sind diverse bugs in der pact und
scc Integration (issues 796 und 797 in scc github).

## Voraussetzungen ##
- Pact Broker auf einer lokalen Maschine, der auf localhost:80 erreichbar ist (alternative Konfiguration in Annotationen und build.gradle Dateien anpassen). Als einfachste Lösung wird die docker Löunsg von Pact empfohlen (siehe Doku unter
https://hub.docker.com/r/dius/pact-broker/)
- Eine RabbitMQ Default Installation (localhost, Port 5672). Nur erforderlich wenn man nicht nur die Tests, sondern auch die Dummy-App ausprobieren möchte

## Tests durchführen ##
Nachdem ein Pact Broker gestartet wurde im Root Verzeichnis gradle test ausführen. Führt alle Contract Tests in den Subprojekten aus.
