# HubSpot Integration API

API desenvolvida com Spring Boot para integrar com o HubSpot via OAuth 2.0, permitindo autenticação, criação de contatos e recebimento de webhooks.

---

⚙️ Tecnologias Utilizadas
☕ Core
Java 22

Spring Boot 3.2.4

Spring WebFlux – para chamadas reativas com WebClient

🔐 Autenticação & Autorização
OAuth2 Authorization Code Flow com Spring Security

📚 Documentação
Springdoc OpenAPI 2 – geração automática de documentação Swagger

🧰 Produtividade
Lombok – redução de boilerplate (@Getter, @RequiredArgsConstructor, etc.)

Spring Boot DevTools – reload automático durante o desenvolvimento

Hibernate Validator (Jakarta Validation) – validação de DTOs com @Valid

🧪 Testes
JUnit 5

Mockito – criação de mocks e testes unitários

☁️ Integrações e Ferramentas
HubSpot CRM API – criação de contatos via API REST

HubSpot Webhooks – recebimento de eventos de criação de contatos

Ngrok – exposição do servidor local para testes de webhook

---
## 📋 Pré-requisitos

Antes de começar, você precisará de:

- [Java 17+](https://adoptium.net/) instalado
- [Maven](https://maven.apache.org/) instalado
- Conta no [HubSpot Developer Portal](https://developers.hubspot.com/)
- Aplicativo registrado no HubSpot com:
  - Escopo `contacts` habilitado
  - URL de redirecionamento configurada

## Install
git clone https://github.com/Oreonzf/Hubspot.git

./mvnw clean install

## 🌐 Tornando sua aplicação acessível publicamente
O HubSpot exige um domínio público para redirecionar o usuário após o login. Você pode usar Ngrok ou publicar em uma nuvem como Render, Railway, Vercel ou Heroku.

✅ Opção 1: Usar Ngrok (modo local)
ngrok http 8080
Copie a URL HTTPS gerada (ex: https://abc123.ngrok.io) e use como hubspot.redirect-uri:

hubspot.redirect-uri=https://abc123.ngrok.io/hubspot/callback


✅ Opção 2: Hospedar em nuvem
Caso publique em um ambiente cloud, use a URL da aplicação como redirect-uri no portal do HubSpot e no application.properties.

---

## ✅ Como Usar (Passo a Passo)

1. Inicie a aplicação com `./mvnw spring-boot:run`
2. Acesse `http://localhost:8080/hubspot/login` no navegador
3. Faça login na sua conta HubSpot e aceite as permissões
4. O HubSpot irá redirecionar para `http://localhost:8080/hubspot/callback?code=...`
5. O token será obtido e Informado como Json
6. Com o token obtido se pode fazer requisições via Curl ou Postman
7. Agora você pode fazer requisições para `POST /hubspot/contact` com os dados do novo contato



---

## 🧪 Endpoints da API

### 🔐 1. Iniciar Login com HubSpot (OAuth2)
**GET** `http://localhost:8080/hubspot/login`

Retorna a URL de autorização OAuth2 do HubSpot para iniciar o login.

**Exemplo de resposta:**
```
https://app.hubspot.com/oauth/authorize?client_id=...&redirect_uri=...&scope=contacts&response_type=code
```

---

### 🔁 2. Callback OAuth2 (Troca de código por token)
**GET** `http://localhost:8080/hubspot/callback?code={authorization_code}`

Este endpoint é chamado automaticamente após o login. Ele troca o `code` pelo `access_token`.

**Exemplo:**
```
GET http://localhost:8080/hubspot/callback?code=123456
```

O token é retornado em Json, Exemplo:
```Json
{
"access_token":"CM_bpqjkMhIHAAEAQAAAARjlj9gXIL6M8CUoyrukBTIUyJv8zk42efT9LvfWHFtGHjbZiIk6MAAAAEEAAAAAAAAAAAAAAAAAgAAAAAAAAAAAACAAAAAAAOABAAAAAAAAAAAAAAAQAkIU08PUFsFyIybYw6ExHdfOQO3DgCNKA25hMVIAWgBgAGi-jPAlcAA",
"refresh_token":"na1-4be5-bd39-4155-9037-cdb5e4f31131",
"expires_in":1800,
"token_type":"bearer"
}
```
---

### 👤 3. Criar Contato
**POST** `http://localhost:8080/hubspot/contact`

Cria um novo contato no HubSpot usando o token OAuth2.

**Body JSON:**
```json
{
  "email": "john.doe@example.com",
  "firstName": "John",
  "lastName": "Doe"
}
```

**Requisitos:**
- O token de acesso já deve ter sido obtido via `/hubspot/login` e `/hubspot/callback`

---

### 📥 4. Receber Webhooks (opcional)
**POST** `http://localhost:8080/hubspot/webhook`

Esse endpoint processa notificações automáticas do HubSpot.

**Exemplo de payload:**
```json
{
  "appId": 11083210,
  "eventId": 100,
  "subscriptionId": 3474423,
  "portalId": 49675933,
  "occurredAt": 1744912092265,
  "subscriptionType": "contact.creation",
  "attemptNumber": 0,
  "objectId": 123,
  "changeSource": "CRM",
  "changeFlag": "NEW"
}
```

---


## 🔧 Configurações Necessárias

No arquivo `application.yml`:
```yaml
hubspot:
  client-id: SEU_CLIENT_ID
  client-secret: SEU_CLIENT_SECRET
  redirect-uri: http://localhost:8080/hubspot/callback
  access-token: SEU_TOKEN (opcional)
```

> Você deve registrar previamente seu app no [HubSpot Developer Portal](https://developers.hubspot.com/), para esta API foi utilizado uma conta de testes.

## Informações

Para esse código foi utilizado uma conta de testes do Hubspot.
Foi desenvolvido localmente com a utilização de Ngrok, mas também testado em ambiente AWS.

## 📚 Documentação Técnica
1. Decisões de Arquitetura
Escolha do Spring WebFlux (WebClient)
Motivação:

Assíncrono/não-bloqueante para chamadas HTTP ao HubSpot (evita gargalos em alta concorrência).

Integração natural com OAuth2 do HubSpot (fluxo reativo).

Alternativas consideradas:

RestTemplate (depreciado em versões recentes do Spring).

Feign Client (mais simples, mas síncrono).

Padrão de Projeto
Estrutura modular:

Separação clara entre:

Camada de autenticação (OAuth2Service)

Camada de negócios (ContactService)

Camada de webhooks (WebhookHandler)

Justificativa:

Facilita testes unitários.

Isola responsabilidades (SOLID).

2. Bibliotecas-Chave e Justificativas
Biblioteca	Motivo
Lombok	Reduz boilerplate (getters/setters, builders).
Spring Security	Gerenciamento seguro de tokens OAuth2 e validação de webhooks.
Jackson	(Padrão do Spring) Parsing eficiente de JSON para webhooks/contatos.
WebClient	(Spring WebFlux) Chamadas HTTP não-bloqueantes ao HubSpot API.

3. Melhorias Futuras (Roadmap)


Prioritárias
Refresh Token Automático

Implementar lógica para renovar tokens expirados usando refresh_token.

Validação de Webhooks

Adicionar assinatura HMAC para verificar origem dos webhooks (doc HubSpot).

Recomendadas
Métricas/Monitoramento

Integrar Micrometer + Prometheus para métricas de chamadas à API HubSpot.

Cache de Tokens

Usar Redis ou Caffeine para evitar nova autenticação a cada request.

SDK Client

Gerar cliente TypeScript/Java via OpenAPI Generator para consumidores da API.

Opcionais
Suporte a Batch Operations

Criar/atualizar múltiplos contatos em uma única chamada.

Integração com SQS/SNS

Publicar eventos de webhooks em filas AWS para processamento assíncrono.

**4. Trade-offs Reconhecidos**
Decisão	Prós	Contras
WebFlux (reativo)	Escalabilidade para muitas requisições.	Curva de aprendizado mais acentuada.
Sem banco de dados	Simplicidade inicial.	Dependência total do cache em memória.
Ngrok para dev	Rápido setup local.	URL temporária (requer atualização).


5. Lições Aprendidas


OAuth2 no HubSpot:

O fluxo authorization_code exige HTTPS (por isso Ngrok é essencial para desenvolvimento local).

Webhooks:

HubSpot envia payloads com headers específicos (X-HubSpot-Signature), mas a validação foi omitida na versão inicial por simplicidade.

**6. Contexto de Desenvolvimento**


Versão Inicial (MVP):
Focada em:

Autenticação OAuth2 funcional.

CRUD básico de contatos.

Recebimento de webhooks (sem validação).

Ambiente de Teste:

Conta developer do HubSpot (limite de 1000 contatos).

---

## 🧪 Testes

Execute os testes com:
```
./mvnw test
```

---



## 📫 Contato

- Email: rafa.cavalcant@gmail.com
- LinkedIn: https://www.linkedin.com/in/rafael-cavalcante-97849b1b6/
