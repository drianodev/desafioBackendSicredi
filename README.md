# Desafio back-end Sicredi

## Descrição

### Objetivo
No cooperativismo, cada associado possui um voto e as decisões são tomadas em assembleias,
por votação. Imagine que você deve criar uma solução backend para gerenciar essas sessões de
votação.

Essa solução deve ser executada na nuvem e promover as seguintes funcionalidades através de
uma API REST:

- Cadastrar uma nova pauta
- Abrir uma sessão de votação em uma pauta (a sessão de votação deve ficar aberta por um
tempo determinado na chamada de abertura ou 1 minuto por default)
- Receber votos dos associados em pautas (os votos são apenas 'Sim'/'Não'. Cada
associado é identificado por um id único e pode votar apenas uma vez por pauta)
- Contabilizar os votos e dar o resultado da votação na pauta

Para fins de exercício, a segurança das interfaces pode ser abstraída e qualquer chamada para as
interfaces pode ser considerada como autorizada. A escolha da linguagem, frameworks e
bibliotecas é livre (desde que não infrinja direitos de uso).

É importante que as pautas e os votos sejam persistidos e que não sejam perdidos com o restart
da aplicação.

### Tarefas bônus
- Tarefa Bônus 1 - Integração com sistemas externos
  - Integrar com um sistema que verifique, a partir do CPF do associado, se ele pode
votar
    - GET https://run.mocky.io/v3/57f23672-c15f-48f8-90d3-d84ce00250b8/users/
    - Caso o CPF seja inválido, a API retornará o HTTP Status 404 (Not found)
    - Você pode usar geradores de CPF para gerar CPFs válidos
    - Caso o CPF seja válido, a API retornará se o usuário pode
(ABLE_TO_VOTE) ou não pode (UNABLE_TO_VOTE) executar a operação
    - Exemplo de retorno do serviço:
    ````json
    // GET /users/19839091069
    // 200 OK
    {
        "status": "ABLE_TO_VOTE"
    }
    
    // GET /users/62289608068
    // 200 OK
    {
        "status": "UNABLE_TO_VOTE"
    }

- Tarefa Bônus 2 - Mensageria e filas
  - O resultado da votação precisa ser informado para o restante da plataforma, isso
deve ser feito preferencialmente através de mensageria. Quando a sessão de
votação fechar, poste uma mensagem com o resultado da votação
- Tarefa Bônus 3 - Performance
  - Imagine que sua aplicação possa ser usada em cenários que existam centenas de
milhares de votos. Ela deve se comportar de maneira performática nesses cenários
  - Testes de performance são uma boa maneira de garantir e observar como sua
aplicação se comporta
- Tarefa Bônus 4 - Versionamento da API
  - Como você versionaria a API da sua aplicação? Que estratégia usar?

---
Vamos criar um README básico para sua aplicação. Certifique-se de personalizar as seções conforme necessário para fornecer as informações específicas da sua aplicação.

---
# Orientações

## Servidor: https://sessoes-votacao-api.onrender.com

## Tecnologias utilizadas:
- Java
- SpringBoot
- PostgresSql
- Swagger
- JUnit

## Pré-requisitos
- Java JDK 17
- Maven
- Docker
- PostgreSql

## Configuração do Banco de Dados
A aplicação utiliza o PostgreSQL como banco de dados. 
Configure as propriedades no arquivo `application.yml` para apontar para o seu banco de dados local.

```yaml
# Configurações BD local
spring.datasource.url=jdbc:postgresql://localhost:5432/desafiosicredi
spring.datasource.username=postgres
spring.datasource.password=1234

# Configurações BD nuvem
#spring.datasource.url=${DATABASE_URL}
#spring.datasource.username=${DATABASE_USERNAME}
#spring.datasource.password=${DATABASE_PASSWORD}
```

## Construção e Execução

### Local
1. Certifique-se de ter o JDK 17 e o Maven instalados.
2. Execute o seguinte comando na raiz do projeto:
    ```bash
    mvn spring-boot:run
    ```
3. Acesse a aplicação em [http://localhost:8080](http://localhost:8080)

### Docker
1. Construa a imagem Docker:
    ```bash
    docker build -t backend-api:latest .
    ```
2. Execute o contêiner Docker:
    ```bash
    docker run -p 8080:8080 backend-api
    ```
3. Acesse a aplicação em [http://localhost:8080](http://localhost:8080)

## Documentação da API
- [Documentação da API no Swagger](https://app.swaggerhub.com/apis-docs/ADRIANOMENDES661/sessoes-votacao-api/1.0.0)
- [Collection Postman](https://github.com/drianodev/desafioBackendSicredi/tree/main/postman)

---

# Decisões técnicas

Primeiramente gostaria de agradecer a incrivel oportunidade!

No início tive uma pequena dificuldade para imaginar a implementação, 
logo busquei a caneta e o papel e comecei a fazer um rascunho de como deveria ser implementando.
Durante a implementação fiz diversas mudanças e fui documentando melhorias e correções no próprio github.

### Escolha do Banco de Dados:
Optei pelo PostgreSQL devido à minha familiaridade com a plataforma e à facilidade de integração com projetos Spring.

### Tarefa Bônus 1 - Integração com sistemas externos
Concluí com sucesso a implementação da validação de CPF utilizando a API externa: https://api-validador-cpf.vercel.app/validarcpf/.

### Tarefa Bônus 2 - Mensageria e filas
Embora a implementação da mensageria e filas não tenha sido possível devido à limitação de tempo, a base do projeto pode ser encontrada em [feature/mensageria](https://github.com/drianodev/desafioBackendSicredi/tree/feature/mensageria).

### Tarefa Bônus 3 - Performance
Infelizmente, devido a restrições de prazo, não foi possível abordar testes de performance neste momento.

### Tarefa Bônus 4 - Versionamento da API
Para versionamento da API, adotei a prática de incluir a versão no header de cada controller: @GetMapping(headers = "Api-Version=1").

### Publicar API
Optei pela plataforma Render devido à sua gratuidade e facilidade de integração. A API pode ser acessada em: [Link da API](https://sessoes-votacao-api.onrender.com).
