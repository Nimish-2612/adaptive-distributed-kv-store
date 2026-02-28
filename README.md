# 🚀 Context-Aware Distributed Key–Value Store with Adaptive Replication


## 📌 Overview

This project implements a distributed key–value store with:

Consistent hashing for routing

Multiple independent storage nodes

Access frequency tracking

Adaptive replication based on key hotness

Manual node failure simulation

Unlike traditional fixed-replication systems, this system dynamically increases replicas for frequently accessed (hot) keys while keeping minimal replicas for cold keys.
## 🏗️ System Architecture

```
                    Client
                       │
                       ▼
                 Router (8080)
                       │
        ┌──────────────┼──────────────┐
        ▼              ▼              ▼
   KV Node 1       KV Node 2       KV Node 3
     (8081)           (8082)           (8083)
```
    
## Components
Component	Responsibility
Router	Request routing + access tracking + adaptive replication
KV Nodes	Store key–value pairs independently
Consistent Hash Ring	Distributes keys evenly
Access Counter	Tracks GET frequency per key
Replication Policy	Adjusts replicas dynamically


## 📂 Project Structure
kvnode/      → Storage node service
router/      → Routing & replication logic


## ⚙️ How to Run the System
🔹 1️⃣ Start Storage Nodes

Open kvnode project in IntelliJ.

Create three Application run configurations:

KV-8081
-Dserver.port=8081

KV-8082
-Dserver.port=8082

KV-8083
-Dserver.port=8083

Run all three.

Expected output:

Tomcat started on port 8081

Tomcat started on port 8082

Tomcat started on port 8083


🔹 2️⃣ Start Router

Open router project.

Ensure in application.properties:

server.port=8080

Run:

RouterApplication.java

Expected:

Tomcat started on port 8080

## 🧪 Basic Operations

All client requests go through:

http://localhost:8080

🔹 Store a Value

PUT http://localhost:8080/put?key=A&value=Apple

Example response:

Stored in http://localhost:8082

🔹 Retrieve a Value

GET http://localhost:8080/get?key=A

Response:

Apple

## 🔍 Verifying Distributed Storage

Step 1: Insert multiple keys

PUT key=A

PUT key=B

PUT key=C

PUT key=D

PUT key=E

Router will distribute across nodes.

Step 2: Verify Manually

Check nodes directly:

http://localhost:8081/get?key=A

http://localhost:8082/get?key=A

http://localhost:8083/get?key=A

Only one node should contain the value.

✔ Confirms consistent hashing

✔ Confirms distributed storage

## 🔥 Verifying Adaptive Replication

Step 1: Create Cold Key

PUT /put?key=ColdKey&value=Ice

Do not access again.

Step 2: Create Hot Key

PUT /put?key=HotKey&value=Fire

Access repeatedly:

GET /get?key=HotKey

Call 10+ times.

Step 3: Trigger Replication

PUT /put?key=HotKey&value=UpdatedFire

Replication increases automatically based on access count.

Step 4: Verify Replication

Check all nodes:

http://localhost:8081/get?key=HotKey

http://localhost:8082/get?key=HotKey

http://localhost:8083/get?key=HotKey

HotKey should appear in multiple nodes.

ColdKey should appear in only one.

✔ Demonstrates adaptive replication

## 💣 Simulating Node Failure

Step 1: Stop One Node

Stop any of:

KV-8081

KV-8082

KV-8083

Step 2: Test Availability

GET /get?key=HotKey

GET /get?key=ColdKey

Expected behavior:

Key Type	Behavior

HotKey	Still accessible

ColdKey	May fail

✔ Demonstrates improved availability for hot keys

✔ Shows trade-off between storage and fault tolerance

## 📊 Replication Policy

Access Count	Replicas

<5   --> 	1
5–10 -->	2
> 10 -->	3

Policy is configurable in RouterController.

## 🧠 Core Concepts Implemented

Consistent hashing

Virtual nodes

Distributed routing

Concurrent access tracking

Adaptive replication

Fault tolerance simulation

## 📈 Experimental Demonstration

This project demonstrates:

Load distribution fairness

Availability improvement for hot data

Storage overhead trade-offs

System behavior under node failure

## 🎓 Academic Relevance

This system mimics real-world distributed storage systems such as:

Dynamo-style architectures

Cassandra-like replication models

Cloud-scale availability design

It demonstrates how context-aware replication improves availability without uniformly increasing storage cost.

## 📌 Final Summary

This project presents a distributed key–value store that dynamically adjusts replication based on access frequency. Frequently accessed keys gain higher redundancy, improving fault tolerance and availability, while rarely accessed keys minimize storage overhead.


It combines practical implementation with core distributed systems theory.
