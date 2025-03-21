Sistema de Gestão de Visitas para Unidade Prisional

Sistema desenvolvido para gerenciar o agendamento e controle de visitas em unidades prisionais.

Tecnologias Utilizadas

- Java 17
- Spring Boot 3.1.5
- Spring Security com Basic Auth
- Spring Data JPA
- PostgreSQL
- H2 Database (desenvolvimento)
- Swagger/OpenAPI
- Docker
- JUnit 5 e Mockito para testes

Funcionalidades

- Gerenciamento de detentos
- Gerenciamento de visitantes
- Agendamento e controle de visitas
- Autenticação e autorização com Basic Auth
- Documentação da API com Swagger
- Testes unitários

Pré-requisitos

Para executar o projeto, você precisa ter instalado:

- Java 17
- Maven (opcional, caso não use Docker)
- Docker e Docker Compose (recomendado)

Configuração e Execução

Usando Docker (Recomendado)

1. Clone o repositório:
   
bash
git clone https://github.com/clb-braz/gestao-visitas-prisional.git
cd gestao-visitas-prisional

2. Execute o projeto usando Docker Compose:
bash
docker-compose up --build

O sistema estará disponível em:

- API: http://localhost:8080/api
- Swagger UI: http://localhost:8080/api/swagger-ui.html

Usando Maven

1. Clone o repositório:
bash
git clone https://github.com/clb-braz/gestao-visitas-prisional.git
cd gestao-visitas-prisional
2. Execute os testes:
bash
mvn test
3. Execute a aplicação:
bash
mvn spring-boot:run

Autenticação

O sistema utiliza Basic Auth com os seguintes usuários predefinidos:

1. Administrador
   - Username: admin
   - Password: admin
   - Role: ROLE_ADMIN

2. Agente Penitenciário
   - Username: agente
   - Password: agente
   - Role: ROLE_AGENTE

3. Visitante
   - Username: visitante
   - Password: visitante
   - Role: ROLE_VISITANTE

Endpoints da API

Detentos
- GET /api/detentos - Lista todos os detentos
- GET /api/detentos/{id} - Busca detento por ID
- POST /api/detentos - Cadastra novo detento
- PUT /api/detentos/{id} - Atualiza detento
- DELETE /api/detentos/{id} - Remove detento
- PATCH /api/detentos/{id}/status - Altera status do detento

Visitantes
- GET /api/visitantes - Lista todos os visitantes
- GET /api/visitantes/{id} - Busca visitante por ID
- POST /api/visitantes - Cadastra novo visitante
- PUT /api/visitantes/{id} - Atualiza visitante
- DELETE /api/visitantes/{id} - Remove visitante
- PATCH /api/visitantes/{id}/status - Altera status do visitante

Visitas
- GET /api/visitas - Lista todas as visitas
- GET /api/visitas/{id} - Busca visita por ID
- POST /api/visitas - Agenda nova visita
- PATCH /api/visitas/{id}/status - Altera status da visita
- GET /api/visitas/detento/{detentoId} - Lista visitas por detento
- GET /api/visitas/visitante/{visitanteId} - Lista visitas por visitante

Exemplos de Uso

Criando um novo detento (usando cURL)
bash
curl -X POST http://localhost:8080/api/api/detentos \
-H "Content-Type: application/json" \
-H "Authorization: Basic YWRtaW46YWRtaW4=" \
-d '{
"nome": "Jose Silva",
"matricula": "123456",
"dataNascimento": "1990-01-01",
"pavilhao": "A",
"regime": "FECHADO",
"ativo": true
}'

Listando detentos (usando cURL)
bash
curl -X GET http://localhost:8080/api/api/detentos \
-H "Authorization: Basic YWRtaW46YWRtaW4="


Segurança

A aplicação utiliza Basic Auth para autenticação e autorização. Os endpoints são protegidos por roles:

- ROLE_ADMIN: Acesso total
- ROLE_AGENTE: Acesso de leitura e algumas operações específicas
- ROLE_VISITANTE: Acesso limitado às suas próprias visitas



Licença

Este projeto está sob a licença MIT.

Autor

Cleberson BRAZ de Sousa [GitHub](https://github.com/clb-braz)
