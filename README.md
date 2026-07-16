# OrçaCast

API de orçamento pessoal que usa IA para processar comandos de voz relacionados a transações financeiras.

> 🚧 Projeto em construção. Este README reflete o estado atual do desenvolvimento e é atualizado a cada etapa.

## Sobre o projeto

O OrçaCast é minha implementação para o desafio de projeto "Spring AI" da [trilha DIO Spring Boot](https://github.com/digitalinnovationone/dio-spring-boot-learning-track). O projeto foi construído do zero — não é um fork — mas segue os mesmos princípios arquiteturais ensinados na trilha (DDD em camadas, Clean Architecture, tipos fortes de identificador, padrão repositório).

A ideia central: o usuário envia um comando de voz (ex: *"gastei 50 reais em mercado"*), a IA transcreve, interpreta a intenção, aciona o caso de uso correspondente na aplicação, e devolve uma resposta.

## Diferencial em relação ao projeto original da trilha

O projeto de referência da DIO usa a OpenAI como único provedor de IA. Aqui, a proposta é usar soluções gratuitas para os três papéis de IA envolvidos:

| Capacidade | Provedor | Modelo |
|---|---|---|
| Chat / tool calling | Groq | `llama-3.3-70b-versatile` |
| Transcrição de voz (STT) | Groq (Whisper) | `whisper-large-v3` |
| Texto-para-voz (TTS) | ElevenLabs | `eleven_multilingual_v2` |

A Groq expõe uma API compatível com o formato da OpenAI, então o mesmo starter `spring-ai-starter-model-openai` cobre chat e transcrição — só a `base-url` aponta para a Groq em vez da OpenAI.

## Arquitetura

O projeto segue separação em camadas, com dependência sempre apontando para dentro (`infrastructure` → `application` → `domain`):

```
src/main/java/rshinna/orcacast/
├── domain/                 # Entidades, identificadores fortemente tipados e contratos de repositório.
│                            # Não depende de nenhum framework.
├── application/             # Casos de uso (use cases), com modelos de Input/Output próprios.
│                            # Não conhecem detalhes de infraestrutura (JPA, HTTP, IA).
└── infrastructure/
    ├── persistence/          # Entidade JPA, repositório Spring Data e adapter que implementa
    │                        # a interface de repositório do domínio.
    ├── web/                  # Controllers REST, DTOs de request/response e tratamento
    │                        # global de erros (ProblemDetail / RFC 7807).
    └── ai/                   # Integração com Groq (chat + STT) e ElevenLabs (TTS):
                             # ferramentas de tool-calling, configuração do ChatClient
                             # e orquestração do fluxo de comando de voz.
```

### Por que essa separação

Trocar um detalhe técnico — como o provedor de IA ou o banco de dados — não deve exigir alterar regra de negócio. Por isso `domain` e `application` não sabem nada sobre Spring, JPA ou qualquer provedor externo; essas dependências ficam isoladas em `infrastructure`.

### Modelos de Input/Output na camada de aplicação

Os casos de uso não recebem nem devolvem a entidade de domínio diretamente — usam `PersistTransactionInput` (entrada) e `TransactionOutput` (saída), próprios da camada `application`. Isso vai um pouco além do que a trilha propõe originalmente: garante que a `application` nunca vaze o objeto de domínio para fora dela, mesmo que aumente o número de classes.

## Funcionalidades implementadas

- Registrar uma transação financeira (categoria, descrição, valor)
- Listar transações filtradas por categoria
- Processar comandos de voz de ponta a ponta: recebe um áudio, transcreve (Groq/Whisper), interpreta a intenção via IA com tool-calling (Groq/Llama), executa a ação correspondente automaticamente (criar ou consultar transações), e devolve a resposta também em áudio (ElevenLabs)

## Endpoints

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/transactions` | Cria uma transação a partir de um corpo JSON |
| `GET` | `/transactions?category=` | Lista transações de uma categoria |
| `POST` | `/voice-commands` | Recebe um arquivo de áudio (`multipart/form-data`, campo `file`) com um comando em linguagem natural e devolve a resposta também em áudio (`audio/mpeg`) |

Categorias válidas: `GROCERIES`, `PHARMA`, `AUTO`.

## Stack

- Java 21
- Spring Boot 4.0.7
- Spring AI 2.0.0
- Spring Data JPA
- PostgreSQL (via Docker Compose, subida automática em desenvolvimento)
- Lombok
- Gradle

## Tratamento de erros

Erros de validação e de requisição usam o padrão `ProblemDetail` (RFC 7807), com mensagens específicas por campo — por exemplo, um valor de transação negativo retorna detalhamento do motivo da rejeição, em vez de um erro genérico.

## Como rodar

Pré-requisitos: JDK 21 e Docker em execução (o Spring Boot sobe o container do Postgres automaticamente via Docker Compose Support).

Configure as variáveis de ambiente com suas próprias chaves antes de rodar:

```bash
export GROQ_API_KEY=sua_chave_groq
export ELEVENLABS_API_KEY=sua_chave_elevenlabs
./gradlew bootRun
```

Chaves gratuitas podem ser obtidas em [console.groq.com](https://console.groq.com/keys) e [elevenlabs.io](https://elevenlabs.io).

## Limitações conhecidas

- **TTS da Groq não está disponível em português** — o único endpoint de texto-para-voz hospedado pela Groq (modelo Orpheus) suporta apenas inglês e árabe. Por isso o TTS usa a ElevenLabs em vez da Groq.
- **Plano gratuito da ElevenLabs restringe vozes via API a vozes padrão (`premade`)** — vozes da Voice Library (biblioteca compartilhada da comunidade) só ficam disponíveis via API em planos pagos, mesmo que estejam salvas na conta.
- **Categorias são um enum fechado** (`GROCERIES`, `PHARMA`, `AUTO`) — não são extensíveis pelo usuário no estado atual.
- **`ddl-auto=update`** é usado para criação automática de tabelas em desenvolvimento; não é adequado para produção (idealmente seria substituído por migrações versionadas, ex: Flyway).

## Roadmap

- [x] Modelagem do domínio (`Transaction`, `Category`, `TransactionId`)
- [x] Casos de uso de criação e consulta de transações
- [x] Persistência JPA e endpoints REST
- [x] Tratamento de erros com `ProblemDetail`
- [x] Integração com Groq para chat / tool calling
- [x] Integração com Groq (Whisper) para transcrição de voz
- [x] Integração com ElevenLabs para texto-para-voz
- [x] Integrar o TTS ao fluxo de comando de voz (ciclo completo: áudio → transcrição → IA → ação → resposta em áudio)
- [ ] Testes automatizadoss
- [ ] Migrações versionadas (Flyway)

## Créditos

Inspirado no desafio [05 - Spring AI](https://github.com/digitalinnovationone/dio-spring-boot-learning-track/blob/main/05-spring-ai/README.md) da trilha DIO Spring Boot.