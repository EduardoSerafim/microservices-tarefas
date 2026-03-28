# 🧩 Sistema de Gerenciamento de Tarefas - Microservices

Este projeto é um sistema distribuído de gerenciamento de tarefas baseado em arquitetura de microservices, desenvolvido com **Spring Boot** e **Spring Cloud**, durante a Pós-gradução em arquitetura e desenvolvimento Java da FIAP

---

## 📐 Arquitetura

O sistema é composto pelos seguintes microservices:

* **config-service** → Centralização de configurações com Spring Cloud Config Server
* **discovery-service** → Service Discovery com Netflix Eureka
* **gateway-service** → API Gateway utilizando Spring Cloud Gateway integrado ao Eureka
* **user-service** → Gerenciamento de usuários
* **task-service** → Gerenciamento de tarefas
* **notification-service** → Envio de notificações por e-mail

---

## 🔧 Papel do Config Service

O **config-service** é responsável por fornecer configurações centralizadas para todos os microservices.

### Benefícios:

* Externalização de configurações
* Facilidade para alterar propriedades sem recompilar serviços
* Suporte a múltiplos ambientes (dev, prod, etc.)
* Integração com repositórios Git

---

## 🧱 Tecnologias Utilizadas

* Java 17+
* Spring Boot
* Spring Cloud

  * Config Server
  * Eureka Server / Client
  * Gateway
  * OpenFeign
* RabbitMQ
* Java Mail Sender
* Thymeleaf (templates de e-mail)
* Spring Web
* Spring Actuator
* OpenAPI (Swagger)

---

## 🔗 Comunicação entre serviços

### 🔄 Comunicação síncrona

A comunicação síncrona entre os microservices é realizada utilizando **OpenFeign**:

* **user-service ↔ task-service**

  * O *task-service* consulta dados de usuários de forma direta
  * Integração declarativa e simplificada via interfaces Feign

---

### 📨 Comunicação assíncrona

A comunicação assíncrona é realizada utilizando **RabbitMQ**:

* **task-service → notification-service**

  * Ao criar ou atualizar uma tarefa, um evento é publicado
  * O *notification-service* consome esse evento e envia o e-mail ao usuário
  * Garante desacoplamento e maior resiliência

---

### 🌐 Outros mecanismos

* **Configuração centralizada:** via Config Server (**config-service**)
* **Service Discovery:** via Eureka (**discovery-service**)
* **Gateway:** roteamento dinâmico baseado nos serviços registrados

---

## ⚙️ Como Executar o Projeto

### Pré-requisitos

* Java 17+
* Maven
* RabbitMQ rodando
* Git

---

### 🔥 Ordem de inicialização dos serviços

⚠️ **IMPORTANTE:** A ordem é essencial devido às dependências entre serviços.

1. **config-service** (Config Server)
2. **discovery-service** (Eureka Server)
3. **gateway-service**
4. **user-service**
5. **task-service**
6. **notification-service**

---

### ▶️ Executando via Maven

```bash
mvn clean install
mvn spring-boot:run
```

Ou execute individualmente:

```bash
cd config-service
mvn spring-boot:run
```

---

## 🌐 Endpoints principais

### ⚙️ Config Server

```
http://localhost:8888
```

### 🔍 Eureka Dashboard

```
http://localhost:8761
```

### 🚪 API Gateway

```
http://localhost:8080
```

---

## 📬 Notificações

O **notification-service** envia e-mails automaticamente utilizando:

* Java Mail Sender
* Templates com Thymeleaf

Fluxo:

1. task-service cria/atualiza tarefa
2. Evento enviado via RabbitMQ
3. notification-service consome
4. E-mail é disparado ao usuário

---

## 📑 Documentação da API

A documentação é gerada automaticamente com **OpenAPI (Swagger)**.

```
http://localhost:8080/swagger-ui.html
```

---

## 📊 Monitoramento

Endpoints do **Spring Actuator**:

```
/actuator/health
/actuator/info
```

---

## 📨 Mensageria (RabbitMQ)

* Comunicação assíncrona entre microservices
* Utilizado entre **task-service** e **notification-service**
* Uso de exchanges, filas e bindings
* Baseado em eventos (event-driven architecture)

---

## 📌 Funcionalidades

* Cadastro de usuários
* Gerenciamento de tarefas
* Integração síncrona entre serviços (OpenFeign)
* Notificações por e-mail (assíncronas)
* Configuração centralizada
* Descoberta de serviços automática
* API documentada com Swagger

