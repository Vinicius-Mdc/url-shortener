# Url Shortener

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![AWS](https://img.shields.io/badge/aws-%23232F3E.svg?style=for-the-badge&logo=amazonwebservices&logoColor=white)

Este projeto é uma API desenvolvida usando Java, AWS Lambda e AWS S3 para criar e disponibilizar urls encurtadas, desenvolvido durante o curso disponibilizado pela Rocketseat.

## Índice

- [Instalação](#instalação)
- [Endpoints](#endpoints)
- [Autenticação](#autenticação)
- [Banco de dados](#banco-de-dados)

## Instalação

1. Clone este projeto para seu próprio repositório.

2. Crie uma conta na AWS caso não tenha.

3. Crie duas funções lambda chamadas 'GenerateShortnerUrl' e 'RedirectUrlShortener' (caso deseje nomes diferentes será necessário realizar o deploy manual ou a alteração do arquivo ci-cd.yaml).

4. Crie um bucket S3 na AWS.

5. Adicione uma variável de ambiente 'S3_BUCKET_NAME' para ambas as funções lambda, com o nome dado ao bucket S3 criado no passo anterior.
   
6. Adicione as variáveis de ambiente 'AWS_ACCESS_KEY_ID' e 'AWS_SECRET_ACCESS_KEY' no Github com as chaves de acesso para o deploy automático e, caso necessário, faça as devidas alterações no arquivo ci-cd.yaml.
   
7. Crie um API Gateway e faça o roteamento das funções lambda para os caminhos desejados.
   
8. Execute a ação de deploy pelo Github Actions.

## Endpoints
Os endpoints usados dependem da configuração feita no AWS API Gateway. Os serviços executados ao chamar cada função lambda é definido a seguir:

```markdown
RedirectUrlShortener - Caso seja um id válido e ainda não expirado, redireciona para a url definida

GenerateShortnerUrl - Cria uma nova url encurtada

```

## Autenticação
A API não utiliza nenhum tipo de autenticação no momento.

## Banco de dados
A aplicação salva as informações sobre as urls encurtadas criadas em arquivos JSON dentro de um bucket S3 na AWS, não existe conexão direta a nenhum banco de dados no momento.
