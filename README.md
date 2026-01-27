# 📍 Employee Management API

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![Flyway](https://img.shields.io/badge/Flyway-Migrations-red?style=for-the-badge)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-yellowgreen?style=for-the-badge)

Employee Management é uma API REST desenvolvida em Spring Boot para gerenciamento de funcionários, permitindo operações de cadastro, consulta, atualização e desativação/ativação de funcionários.

O intuito desse projeto é aplicar conhecimentos de:
- Versionamento de banco de dados com Flyway
- Criação e execução de migrations
- Integração com PostgreSQL
- Swagger UI
- Implementação de criptografia assimétrica utilizando RSA (public/private keys) para proteção de dados sensíveis

---

## 🚀 Tecnologias utilizadas

* **Java 21**
* **Spring Boot 3.5.9**
* **Spring Web MVC**
* **Spring Data JPA**
* **Spring Security**
* **OAuth2 Resource Server – autenticação baseada em JWT**
* **Bean Validation**
* **Lombok**
* **PostgreSQL**
* **Flyway – versionamento e migrations do banco**
* **Springdoc OpenAPI (Swagger UI) – documentação da API**

---

## 🏗️ Arquitetura

O projeto segue uma **arquitetura em camadas**:

```text
config/       → Configurações de segurança e API
controller/   → Camada de entrada 
dto/          → DTOs de Request e Response 
entities/     → Entidades JPA 
repositories/ → Repositórios Spring Data JPA
services/     → Regras de negócio
```

---

## 🔐 Segurança

A aplicação utiliza **Spring Security + OAuth2 Resource Server (JWT)**.

### 🔑 Autenticação
- Login via `/auth/login`
- Retorno de um **JWT Token**
- O token deve ser enviado no header `Authorization: Bearer <token>`
- Somente endpoints públicos podem ser acessados sem autenticação

### 👮 Autorização
- Controle de acesso por **roles**
- Uso de `@PreAuthorize`

### 👥 Perfis de usuário
- **ROLE_ADMIN**
    - Consulta funcionários e departamentos
    - Pode inserir, atualizar, desativar e reativar funcionários
- **ROLE_MANAGER**
    - Consula funcionários e departamentos

--- 

## 📡 Endpoints

### 🔐 Autenticação

| Método | Endpoint         | Descrição                |
|--------|------------------|--------------------------|
| POST   | `/auth/register` | Registro de usuário      |
| POST   | `/auth/login`    | Login e geração do token |

### 🏢 DEPARTMENTS 

| Método | Endpoint          | Acesso  | Descrição                    |
|--------|-------------------|---------|------------------------------|
| GET    | `/departments`    | Público | Busca todos os departamentos |
| GET    | `/departments/id` | Público | Busca departamento por id    |

### 🎉 Employees

| Método | Endpoint                     | Acesso                    | Descrição                           |
|--------|------------------------------|---------------------------|-------------------------------------|
| GET    | `/employees/{id}`            | ROLE_ADMIN e ROLE_MANAGER | Busca funcionário por id            |
| GET    | `/employees/department/{id}` | ROLE_ADMIN e ROLE_MANAGER | Lista funcionários por departamento |
| POST   | `/employees`                 | ROLE_ADMIN                | Insere novo funcionário             |
| PUT    | `/employees/{id}`            | ROLE_ADMIN                | Atualiza funcionário por id         |
| PATCH  | `/employees/{id}/deactivate` | ROLE_ADMIN                | Desativa funcionário por id         |
| PATCH  | `/employees/{id}/reactivate` | ROLE_ADMIN                | Reativa funcionário por id          |

---

##  Melhorias Futuras

- [ ] Refresh Token
- [ ] Logout
- [ ] Testes unitários e de integração
- [ ] Dockerização
- [ ] Docker Compose  
