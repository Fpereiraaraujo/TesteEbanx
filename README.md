=============================================================================
PROJETO: DESAFIO TÉCNICO EBANX - ACCOUNT API
AUTOR: Fernando Pereira
=============================================================================

DESCRIÇÃO
-----------------------------------------------------------------------------
API REST desenvolvida para o gerenciamento de contas financeiras. O sistema
permite realizar operações de depósito, saque, transferência e consulta de
saldo, seguindo estritamente as especificações do desafio técnico do EBANX.

O projeto foi construído utilizando a Arquitetura Hexagonal (Ports & Adapters)
para garantir o desacoplamento entre as regras de negócio e os frameworks.

TECNOLOGIAS UTILIZADAS
-----------------------------------------------------------------------------
- Linguagem: Java 21
- Framework: Spring Boot 3.2.4
- Banco de Dados: MySQL 8.0 (Produção/Docker) e H2 (Testes Automatizados)
- Containerização: Docker e Docker Compose
- Testes: JUnit 5, Mockito e Spring Boot Test
- Documentação: Swagger UI (OpenAPI)

PRÉ-REQUISITOS
-----------------------------------------------------------------------------
Para rodar este projeto localmente, você precisa ter instalado:
1. Docker Desktop (e Docker Compose)
2. Maven (Opcional, caso queira buildar fora do Docker)
3. Java JDK 21 (Opcional, caso queira rodar fora do Docker)

COMO RODAR O PROJETO (VIA DOCKER)
-----------------------------------------------------------------------------
Esta é a maneira recomendada, pois sobe a aplicação e o banco de dados juntos.

1. Abra o terminal na pasta raiz do projeto.

2. Gere o arquivo executável (.jar):
   Comando: mvn clean package -DskipTests

3. Suba os containers da aplicação e do banco de dados:
   Comando: docker-compose up --build

4. Aguarde a mensagem de confirmação no terminal:
   "Started TesteEbanxApplication in ... seconds"

ENDPOINTS E ACESSO
-----------------------------------------------------------------------------
Após subir a aplicação, você pode acessá-la nos seguintes endereços:

- API Base: http://localhost:8080
- Documentação Swagger (Interface Visual): http://localhost:8080/swagger-ui.html

LISTA DE OPERAÇÕES DISPONÍVEIS
-----------------------------------------------------------------------------

1. RESETAR ESTADO (Limpa o banco de dados)
   - Método: POST
   - URL: http://localhost:8080/reset
   - Retorno esperado: 200 OK

2. CONSULTAR SALDO
   - Método: GET
   - URL: http://localhost:8080/balance?account_id={id_da_conta}
   - Exemplo: http://localhost:8080/balance?account_id=100
   - Retorno esperado (se existir): 200 OK (Corpo: valor do saldo)
   - Retorno esperado (se não existir): 404 Not Found (Corpo: 0)

3. REALIZAR EVENTO (Depósito, Saque ou Transferência)
   - Método: POST
   - URL: http://localhost:8080/event
   - Header: Content-Type: application/json

EXEMPLOS DE JSON PARA TESTE (Copie e cole no Postman/Insomnia)
-----------------------------------------------------------------------------

[CENÁRIO A] Depósito (Cria a conta se não existir)
{
  "type": "deposit",
  "destination": "100",
  "amount": 10
}

[CENÁRIO B] Saque
{
  "type": "withdraw",
  "origin": "100",
  "amount": 5
}

[CENÁRIO C] Transferência
{
  "type": "transfer",
  "origin": "100",
  "destination": "300",
  "amount": 15
}

COMO RODAR OS TESTES AUTOMATIZADOS
-----------------------------------------------------------------------------
O projeto possui testes unitários e de integração cobrindo Controllers e
UseCases. Para executá-los:

1. No terminal, execute:
   Comando: mvn test

Os testes utilizam um banco de dados em memória (H2), não interferindo no
banco de dados principal (MySQL).

ESTRUTURA DE PASTAS (ARQUITETURA HEXAGONAL)
-----------------------------------------------------------------------------
/src/main/java/com/FernandoPereira/TesteEbanx
  |
  +-- application
  |     +-- port (Interfaces de Entrada e Saída)
  |     +-- usecase (Regras de Negócio e Implementação das Portas de Entrada)
  |
  +-- domain (Objetos de Domínio / DTOs)
  |
  +-- framework
        +-- adapter
              +-- in (Controllers REST)
              +-- out (Repositórios e Implementação das Portas de Saída)

CONTATO
-----------------------------------------------------------------------------
Desenvolvedor: Fernando Pereira
