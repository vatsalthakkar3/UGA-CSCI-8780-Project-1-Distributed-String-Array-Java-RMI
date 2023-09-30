# CSCI-8780 Advanced Distributed Systems - Project 1: Distributed String Array with Concurrency Control (JAVA RMI)

## Prerequisites

Before you begin, make sure you have the following installed:

- Java Development Kit - 17 (JDK -17)

## Getting Started

Follow these steps to set up and run the project:

### Step 1: Open Three Terminals

Open three separate terminal windows or command prompts.

### Step 2: Start the RMI Registry

In the first terminal, navigate to the 'server' directory:

```bash
cd server
```
Then, start the RMI Registry on port 9100:

```bash
rmiregistry 9100
```
### Step 3: Start the Server

In the second terminal, navigate to the 'server' directory:

```bash
cd server
```
Compile the server code:

```bash
javac *.java
```
Start the server:
```bash
java Server
```

### Step 4: Start the Client

In the third terminal, navigate to the 'client' directory

```bash
cd client
```

Compile the client code:

```bash
javac *.java
```

Start the client and interact with server:
```bash
java Client
```
## Usage
Once the server and client are running, you can interact with the Distributed String Array with Concurrency Control. Follow the prompts in the client application to perform various operations on the distributed string array.