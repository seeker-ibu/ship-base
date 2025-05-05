# 🚢 Royal Caribs Proxy System

## 📌 Problem Statement

In an effort to reduce internet costs, the cruise ship **Royal Caribs** has struck a deal with a satellite internet provider. The new agreement charges the ship **based solely on the number of TCP connections**, not the amount of data transferred.

To make this cost-saving strategy useful, all outgoing HTTP requests must be routed through a **single TCP connection** to the internet.

---

## 🧠 Solution Design

So we designed a proxy system where:

- A **client-side proxy** runs on the ship. (ship proxy)
- A **server-side proxy** runs on the cloud (or satellite uplink).
- All HTTP traffic from the ship routes through the client proxy.
- The client and server proxies communicate over **a single persistent TCP connection**.

---

## 🛠️ System Architecture

### ⚙️ Client Proxy

- **Language:** Java  
- **Responsibilities:**
  - Accept HTTP requests over local sockets (from browsers or internal applications).
  - Use two threads:
    - **Listener Thread:** Accepts and enqueues incoming HTTP requests into a **blocking queue** (FIFO).
    - **Sender Thread:** Dequeues requests and sends them to the server proxy via a **single TCP connection**.
  - A dedicated `Orchestrator` class handles sending and receiving messages over the TCP connection using input/output streams.

### ⚙️ Server Proxy

- **Language:** Java  
- **Responsibilities:**
  - Listens for incoming messages on the single TCP connection from the client proxy.
  - Parses the HTTP request.
  - Forwards the request to the actual destination (internet).
  - Captures the response from the internet.
  - Sends the response back to the client proxy over the same TCP connection.

---

## 🔄 System Flow

1. User makes an HTTP request on the ship.
2. Request goes to the **client proxy**.
3. Client proxy places the request in a **blocking queue**.
4. Sender thread sends the request to the **server proxy** over a **persistent TCP connection**.
5. Server proxy processes the request and sends it to the internet.
6. Response from the internet is sent back via the same connection.
7. Client proxy receives and delivers the response to the user.

---

## 📁 Project Structure

- `Royal-Caribs/` – Contains the logic for accepting HTTP requests, queueing, and TCP communication.
- `server-proxy/` – Handles incoming proxy requests and communicates with the external internet.
- `common` - Common classes between others.

---

## ⚠️ Project Caveats

- **Single Point of Failure**:  
  If the server proxy goes down, all HTTP communication from the ship halts, since there's only one persistent TCP connection.
- **Latency Considerations**:  
  Since all traffic is funneled through a single connection and sequential, there could be delays under heavy load.

- **No Built-in Authentication**:  
  There's no mechanism in place to verify the identity of the client proxy. This could be a security risk if the system is deployed outside a trusted network.

- **No Retry Mechanism**:  
  If the TCP connection drops unexpectedly, the system doesn’t currently attempt automatic reconnection or queue reprocessing.

---

## 🚧 Future Improvements

- Implement TLS encryption over the single TCP connection.
- Add authentication between client and server proxies.
- Add caching on server proxy for repeated requests.
- Retry mechanism


---

## 🚀 Running the Project with Docker

```bash
STEPS

#cloning the repo
git clone https://github.com/seeker-ibu/ship-base.git
cd base-ship

#building the common project
cd common
mvn clean install
cd ../


#Building the proxy-server
cd server-proxy/
mvn clean install

#Moving common jar to proxy-server directory for dependecny used in docker
cp -r ../common/target/common-1.0.0.jar .

#Creating a docker network bridge for making both image containers in same network
sudo docker network create royalcaribs-network

#Build serverproxy container image
sudo docker build -t serverproxy .

#Running serverproxy docket container on port 9090
sudo docker run -d --name serverproxy --network royalcaribs-network -p 9090:9090 serverproxy

#Building the client-proxy
cd ../
cd Royal-Caribs/
mvn clean install

#Moving common jar to proxy-server directory for dependecny used in docker
cp -r ../common/target/common-1.0.0.jar .

#Build royalcaribs container image
sudo docker build -t royalcaribs .

#Running royalcaribs docket container on port 8080
sudo docker run -d --name royalcaribs --network royalcaribs-network -p 8080:8080 royalcaribs

#list out running containers
sudo docker ps (make sure, both client-proxy(royalcaribs) and server-proxy(serverproxy) is running

#Further logs can be checked 
sudo docker logs royalcaribs
sudo docker logs serverproxy







