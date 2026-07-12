# OrçaCast

API de orçamento pessoal que usa IA para processar comandos de voz relacionados a transações financeiras.

> ⚠️ Projeto em construção. Este README reflete o estado atual do desenvolvimento e será atualizado a cada etapa.

## Sobre o projeto

O OrçaCast é minha implementação para o desafio de projeto "Spring AI"
da [trilha DIO Spring Boot](https://github.com/digitalinnovationone/dio-spring-boot-learning-track). O projeto foi
construído do zero — não é um fork — mas segue os mesmos princípios arquiteturais ensinados na trilha (DDD em camadas,
Clean Architecture, tipos fortes de identificador, padrão repositório).

A ideia central: o usuário envia um comando de voz (ex: *"gastei 50 reais em mercado"*), a IA transcreve, interpreta a
intenção, aciona o caso de uso correspondente na aplicação, e devolve uma resposta — também em voz.

## Diferencial em relação ao projeto original da trilha

O projeto de referência da DIO usa a OpenAI como único provedor de IA. Aqui, a proposta é usar soluções gratuitas:

- **Transcrição de voz (STT):** Groq (Whisper)
- **Chat / tool calling:** Groq (modelos Llama/GPT-OSS)
- **Texto-para-voz (TTS):** ainda em avaliação — a Groq não oferece voz em português no momento, então essa etapa deve
  usar um provedor diferente

Essas integrações de IA ainda não foram implementadas — veja o roadmap abaixo.

## Arquitetura

O projeto segue separação em camadas, com dependência sempre apontando para dentro (`infrastructure` → `application` →
`domain`):

```
src/main/java/rshinna/orcacast/
├── domain/            # Entidades, identificadores fortemente tipados e contratos de repositório.
│                       # Não depende de nenhum framework.
├── application/        # Casos de uso (use cases). Orquestram o domínio.
│                       # Não conhecem detalhes de infraestrutura (JPA, HTTP, IA).
└── infrastructure/      # Adapters: controllers REST, implementação JPA dos repositórios,
                        # configuração de banco e (futuramente) integração com IA.
```

### Por que essa separação

Trocar um detalhe técnico — como o provedor de IA ou o banco de dados — não deve exigir alterar regra de negócio. Por
isso `domain` e `application` não sabem nada sobre Spring, JPA ou qualquer provedor externo; essas dependências ficam
isoladas em `infrastructure`.

## Stack

- Java 21
- Spring Boot 4.1
- Spring Data JPA
- PostgreSQL (via Docker Compose, subida automática em desenvolvimento)
- Lombok
- Gradle

## Como rodar

Pré-requisitos: JDK 21 e Docker em execução (o Spring Boot sobe o container do Postgres automaticamente via Docker
Compose Support).

```bash
./gradlew bootRun
```

## Roadmap

- [x] Modelagem do domínio (`Transaction`, `Category`, `TransactionId`)
- [x] Primeiro caso de uso (`ListTransactionByCategoryUseCase`)
- [ ] Casos de uso de criação e demais consultas de transação
- [ ] Camada de infraestrutura: persistência JPA e endpoints REST
- [ ] Integração com Groq para chat / tool calling
- [ ] Integração com Groq (Whisper) para transcrição de voz
- [ ] Integração de texto-para-voz em português
- [ ] Testes automatizados

## Créditos

Inspirado no
desafio [05 - Spring AI](https://github.com/digitalinnovationone/dio-spring-boot-learning-track/blob/main/05-spring-ai/README.md)
da trilha DIO Spring Boot.