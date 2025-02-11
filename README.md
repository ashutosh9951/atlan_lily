# Atlan Lily- Architecture and Implementation

This document outlines my journey in building the Atlan platform for ingesting metadata.  I'm aiming to create a scalable system that can manage metadata for various data assets, offering capabilities for catalogue, search, and lineage tracking. Here's how I'm approaching it, step-by-step:

## Use Cases
Here's a summary of the problem statements we aimed to solve:

1.  **Primary Use Case - (Inbound, Internal) - Bulk Metadata Ingestion:** Ingesting a large volume (1B assets) of existing metadata, primarily table columns and BI fields, with eventual consistency into the Atlan platform.
2.  **(Inbound, External) - Near Real-time Data Observability Issues:**  Integrating with external data observability tools like Monte Carlo to ingest table health and data reliability issues into Atlan in near real-time.
3.  **(Outbound, Internal) - Internal Enrichment Automation:** Automating metadata enrichment within Atlan, such that changes to an entity trigger corresponding changes in downstream lineage-connected entities.
4.  **(Outbound, External) - Data Access Security & Compliance Enforcement:** Enabling external data tools to react in near real-time to governance annotations (PII/GDPR) made in Atlan, enforcing access control accordingly.

## Secondary Use Cases
1. Integration with SaaS sources and targets such as Slack, JIRA, MS Teams, Freshdesk, and Okta.
2. Cost estimation and ways to optimize.
3. Observability of the platform.


## HLD Diagrams

![component_diagram_01](https://github.com/user-attachments/assets/1f4234e7-4089-45d3-9e5f-fdba4713d5b8)

## Choosing the Right Technologies: Multi-Database Approach

For my metadata platform, I decided to use a combination of databases to leverage their strengths for different use cases:

*   **CockroachDB (for Core Metadata Storage):** I selected CockroachDB as the primary store for metadata entities.  Its SQL compatibility (PostgreSQL-like), consistency, and scalability are crucial for reliably managing metadata relationships and ensuring data integrity.  I want to use familiar SQL concepts and have a robust database that can grow.

| Feature                     | **PostgreSQL**                  | **Cassandra**                     | **DynamoDB**                      | **MongoDB**                       | **CockroachDB**                     |
|------------------------------|---------------------------------|-----------------------------------|-----------------------------------|-----------------------------------|-------------------------------------|
| **Horizontal Scalability**    | Vertical Scalability Focus     | Excellent Horizontal Scalability | Excellent Horizontal Scalability | Good Horizontal Scalability (Sharding)| Excellent Horizontal Scalability |
| **Data Consistency (ACID)**   | Strong Consistency               | Eventual Consistency             | Eventual Consistency             | Eventual Consistency             | Strong Consistency                   |
| **Relational Data Modeling** | Robust Relational Model         | Limited Relational Capabilities  | Limited Relational Capabilities  | Document-oriented (Relationships Limited) | Robust Relational Model         |
| **Query Language**           | Standard SQL                    | NoSQL (CQL-like)                  | NoSQL (Proprietary API)         | NoSQL (Document Query Language)  | Standard SQL                        |
| **High Availability**         | Requires Complex Configurations  | Built-in High Availability        | Built-in High Availability        | Requires Replica Sets for HA       | Built-in High Availability         |
| **Use Case Suitability for Metadata** | Great for smaller datasets, scaling challenges | Less suitable for relational metadata, excellent for performance in writes | Less suitable for relational metadata, excellent perf | Suitable for flexible metadata, consistency trade-offs | **Excellent for scalable, consistent, relational metadata** |

The selection of CockroachDB is primarily justified by its strengths of relational databases with the scalability and resilience demands of a modern metadata platform.  Its transactional guarantees and SQL interface simplify development and ensure data integrity, while its distributed nature provides the necessary scalability and high availability to accommodate growing metadata volumes and ensure continuous operation. 

However we will need to dig deep into the query patterns and benchmarks for our system to make the right choice, for example we might prefer cassandra for its excellent write performances if we dont need complex query patterns. Similarily dynamo is very performant,scalable but it restricts us to AWS.

*   **Elasticsearch (for Search & Discovery):**  To enable users to easily search and discover metadata assets, I'm integrating Elasticsearch. Elasticsearch's full-text search capabilities and speed are ideal for indexing metadata attributes and providing fast, flexible search functionalities.

*   **Neo4j (for Lineage Tracking & Relationships):** Lineage is a key aspect of metadata. Neo4j, as a graph database, is perfect for modeling and querying relationships between metadata assets (e.g., data flow, dependencies).  I plan to use Neo4j to visualize and trace the lineage of data assets.

*   **Spring Boot (for Application Framework):** Spring Boot provides a robust and efficient framework for building the application backend service.  Its dependency injection, auto-configuration, and ecosystem integrations simplify development and deployment.

*   **Kafka (for Event Streaming - Asynchronous Operations):**  To keep Elasticsearch and Neo4j in sync with CockroachDB (my source of truth), I'm using an event-driven approach with Kafka.  When metadata changes in CockroachDB, events will be published to Kafka, which then trigger updates in Elasticsearch and Neo4j asynchronously. This ensures consistency without blocking core metadata operations.

## Primary Use cases   

### Bulk Metdata ingestion (Historical data ingestion mode)
We might need to implement a hybrid approach for this dependending on what kind of connector it is,and volume of the data.
For example for data warehouses we can go for bulk import with files for initial bulk ingestion.

Steps in bulk ingestion mode will be : 
* The database connectors will write to an intermediate location like s3/ azure blob storage instead of kafka after transformation

* Directly Load into CockroachDB:  Bulk import your historical data straight into CockroachDB. Use CockroachDB's IMPORT feature or efficient bulk loading tools.

* Generate Events in Batches: For each batch of data from CockroachDB, create corresponding MetadataAssetSavedEvent objects in batches.

* Publish Events to Kafka in Batches: Send these batches of events to your Kafka topic, rather than sending one event at a time.

* Optimize Elasticsearch Consumer:  Make sure your Elasticsearch consumer (MetadataSyncService) uses Elasticsearch's bulk indexing for efficiency.

* Optimize Neo4j Consumer: Ensure your Neo4j consumer (LineageService) uses batch transactions to update Neo4j in bulk.


### Real-time Ingestion mode:


   **Solution:** Event-driven architecture with Kafka as a message queue. Asynchronous processing in Event Processor Service.
   
   **Rationale:** Kafka enables near real-time data streaming and decoupled processing. Webhooks for external event sources (Monte Carlo) provide immediate event delivery. Asynchronous processing ensures low latency for ingestion and downstream reactions.

###   **Authentication and Authorization as first-class citizens in the system:**
    *   **Solution:**  Spring Security for securing APIs and services. OAuth 2.0 or API Keys for API authentication.  RBAC for authorization. Service-to-service authentication using Spring Security or service mesh.


###   **Capability for preprocessing and postprocessing wherever needed on the Platform:


    *   **Solution:**
        *   **Preprocessing:**  Input validation and transformation in Ingestion APIs before messages are enqueued to Kafka.
        *   **Postprocessing:** Event Processor for initial handling. Enrichment Service for metadata enhancement. Governance Service for policy enforcement.
    *   **Rationale:** Allows for data cleansing, transformation, enrichment, and policy application at different stages of the ingestion and processing pipeline.


###   **Adaptability to inbound and outbound consumers to scale as per the volume of metadata change events hitting the platform:


    **Solution:** Kafka for handling high-volume event streams. Microservices architecture for horizontal scalability of Ingestion, Event Processor, Enrichment, and Governance Services. API Gateways for inbound API management. Outbound eventing for downstream consumers.
    **Rationale:** Kafka's scalability and partitioning. Microservices' stateless nature allows for scaling based on load. Decoupled components and event-driven communication contribute to overall platform adaptability.

## Secondary Use Cases

*   **Integration with SaaS sources and targets:**  The architecture can be extended by adding dedicated Ingestion APIs or Adapters for SaaS sources and outbound connectors in Governance/Enrichment services to interact with SaaS targets via their APIs.

*   **Observability of the platform:**  logging, metrics (using Micrometer/Prometheus), tracing (using Spring Cloud Sleuth/Zipkin), health checks, and dashboards (Grafana).


## Multi Tenancy

There are two models through which multitenancy can be supported :
* Complete private deployement for each tenant - This has the advantage of complete isolation and security, but here the challege would be to maintain all different deployements, and making iterative changes to the platform.This might be preferred by large enterprises.
* Multitenant Saas platform for all the tenant -  To isolate the resources for the tenants we will need to think of all different system components:
  kafka - separate topics for the tenants
  cockroachDB -  separate databases for the tenants
  elastic - separete indexes for the tenants
  We can then use the tenantId is the jwt token to set the right context for each API requests ,The application service layer controls the logic , so that it only accesses the relavent resources  



## Getting Started

To run this project:

1. **Prerequisites:**
    * Java 17+
    * Maven
    * docker

      ```bash
      cd docker
      
   
2.  **Clone the repository:**
    ```bash
    git clone [repository-url]
    cd atlan-lily
    ```


3.  **Run docker containers:**
    ```bash
    cd docker
    docker-compose up -d
    ```

4.  **Build the project:**
    ```bash
    mvn clean install
    ```

5.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```

6.  **Test the API (Example using `curl` for Bulk Ingestion):**
    ```bash
    curl -X POST \
      http://localhost:8080/api/ingestion/bulk \
      -H 'Content-Type: application/json' \
      -d '[
            {
              "type": "Database",
              "qualifiedName": "mydb.snowflake",
              "displayName": "My Snowflake DB",
              "vendor": "Snowflake",
              "version": "latest"
            },
            {
              "type": "Table",
              "qualifiedName": "mydb.snowflake.myschema.mytable",
              "displayName": "My Table",
              "schemaName": "myschema",
              "databaseQualifiedName": "mydb.snowflake"
            }
          ]'
    ```

