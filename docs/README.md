# Biblioteca Comunitária (Spring Boot + MVC + JPA + Thymeleaf + MySQL)

Aplicação web simples para gerenciar livros, usuários e empréstimos.

## Requisitos

- Java 17+
- Maven 3.9+
- MySQL 8+

## Configuração

Edite `src/main/resources/application.properties` com as credenciais do seu MySQL:

```
spring.datasource.url=jdbc:mysql://localhost:3306/biblioteca?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=secret
```

A aplicação utiliza `spring.jpa.hibernate.ddl-auto=update` para criar/alterar o schema automaticamente.

## Executando

```bash
mvn spring-boot:run
```

Acesse: http://localhost:8080

## Funcionalidades

- Livros:
  - Cadastrar, listar, editar e excluir
  - Status começa como "DISPONIVEL"
  - Listagem de todos e apenas "disponíveis"
- Usuários:
  - Cadastrar e listar
  - Validação de e-mail
- Empréstimos:
  - Vincula Livro + Usuário + Datas (retirada, prevista de devolução)
  - Validação: data prevista deve ser posterior à retirada
  - Ao emprestar, livro fica "EMPRESTADO"
  - Devolução: livro volta para "DISPONIVEL"
  - Listagens: ativos e todos os empréstimos

## Estrutura

- `model`: entidades JPA (`Livro`, `Usuario`, `Emprestimo`, `LivroStatus`)
- `repository`: interfaces `JpaRepository`
- `controller`: controladores MVC e regras simples
- `templates`: páginas Thymeleaf

## Observações

- Bloqueia exclusão de livros com empréstimo ativo.
- Campos com validações Bean Validation (Jakarta).
- Ajuste o CSS conforme necessário.
