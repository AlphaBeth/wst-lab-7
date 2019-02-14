# Web service technologies, Laboratory work #7

## Task description

> Требуется разработать приложение, осуществляющее регистрацию сервиса в
  реестре jUDDI, а также поиск сервиса в реестре и обращение к нему. Рекомендуется
  реализовать консольное приложение, которое обрабатывает 2 команды. Итог работы
  первой команды – регистрация сервиса в реестре; вторая команда должна
  осуществлять поиск сервиса, а также обращение к нему. 

## Requirements

- Java 8
- Maven 3+
- Glassfish 4.0
- Postgresql 9.3+

## Getting started

Start with typing 

`mvn clean install`

in project root directory.

## Project structure

The project consists of some modules:

- data-access -- all database-related code (entity classes, data access objects, utilities for query generation)
- exterminatus-service -- implementation of JAX-WS service
- standalone-jaxws -- standalone version of exterminatus service
- jaxws-client -- console client for web service
