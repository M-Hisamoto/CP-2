# Biblioteca Comunitária 

Aplicação Spring Boot (Web, MVC, JPA, Thymeleaf, MySQL) para gerenciar livros, usuários e empréstimos.

- Java 17 instalado (`java -version`)
- MySQL em execução 
- Maven instalado (`mvn -version`) ou use o Maven Wrapper (se adicionado posteriormente)
 
Abra `src/main/resources/application.properties` e altere usuário e senha para os do seu banco de dados.
(O banco será criado automaticamente por `createDatabaseIfNotExist=true`.)

Com Maven instalado:
```bash
mvn clean spring-boot:run
```

Para acessar abra a porta abaixo no navegador:
```
http://localhost:8080
```