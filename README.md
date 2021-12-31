# Conference System
A fully-featured backend developed in Java for a Conference Management System.
![Conference System Documentation](https://i.imgur.com/4yBaEWA.png])

## Index
- [Features](#features)
- [Tools And Libraries](#tools-and-libraries)
- [High Level Architecture](#high-level-architecture)
  - [Components](#components)
    - [Conference Management](#conference-management)
- [How to run](#how-to-run)

### Features
* Authentication/Authorization using Json Web Tokens
* Create and Management Speakers
* Create and Management Sessions
* Create and Management Conferences
* Subscribe to a Conference
* Send reminder email to the participants 

### Tools and Libraries
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Spring MVC](https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/mvc.html)
* [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
* [Spring Email](https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/mail.html)
* [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
* [Springdoc](https://springdoc.org/)
* [PostgreSQL](https://www.postgresql.org/)
* [JWT](https://jwt.io/)
* [Jacoco](https://www.eclemma.org/jacoco/)
* [Mailhog](https://github.com/mailhog/MailHog)

### High Level Architecture
This is a brief description of project structure

#### Components
![Conference System Components](https://i.imgur.com/CTfbv74.png)

* Authentication: this component is responsible for user sign in, sign up and contains all the code related to authorization.
* Conference Management: this component is responsible for creating managing entities like Speaker, Session, Conference and Participant. It is directly used for participants when subscribing to a conference.
* Notifier: this component is responsible for sending email to the participants reminding then about the upcoming conferences.

##### Conference Management
![Conference Management Modules](https://i.imgur.com/8h3yGRd.png)
* Web: this module contains the Spring MVC controller. It relies on the usecase module, but no other module references it.
* Usecase: this module contains the application specific business rules. It relies on the Exception and Entity modules, and it is referenced by the Web module. I tried my best to keep the usecase(and entity) completely decoupled from implementation details of external libraries (Spring included).
* Persistence: this module contains the code necessary to persist and retrieve data from the database. It relies on the Usecase and Entity modules, but no other module references it. I've opted to place the JPA separately from the domain code (Usecase and Entity modules) because I want to be able to change to a complete difference persistence approach without having to touch on the other modules.
* Exception: this module contains the customer exceptions that will be used in the web module that render specific HTTP responses.
* Entity: this module contains the domain entities.

### How to run

1. Execute docker containers

```bash
docker-compose up -d
```

2. Execute the app in your favorite IDE