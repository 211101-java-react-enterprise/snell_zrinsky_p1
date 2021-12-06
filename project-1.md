# Project 1 - Custom Object Relational Mapping Framework
## Functional Requirements
### ✅ CRUD operations are supported for one or more domain objects via the web application's exposed endpoints
### * 💻 [BookController](src/main/java/com/revature/p1/app/web/controllers/BookController.java)
### ✅ JDBC logic is abstracted away by the custom ORM
### * 🤝 [BookDAO](src/main/java/com/revature/p1/app/daos/BookDAO.java)
### ✅ Programmatic persistence of entities (basic CRUD support) using custom ORM
```
GET /books/{uuid}
PUT /books/{uuid}
    {
        "title": "The Hobbit",
        "author": "J.R.R. Tolkien",
        "genre": "Fantasy",
        "publisher": "Houghton Mifflin",
        "year": 1937
    }
POST /books/
    {
        "title": "The Hobbit",
        "author": "J.R.R. Tolkien",
        "genre": "Fantasy",
        "publisher": "Houghton Mifflin",
        "year": 1937
    }
DELETE /books/{uuid}
```
### ✅ File-based or programmatic configuration of entities
### * 📕 [Book](src/main/java/com/revature/p1/app/models/Book.java)

---

## Bonus Features
### ✅ Custom ORM supports connection pooling
### * 📞 [ConnectionPool](src/main/java/com/revature/p1/orm/data/ConnectionPool.java)

---

## Non-Functional Requirements
### * ✅  80% line coverage of all service layer classes
### * ✅  Generated Jacoco reports that display coverage metrics
### * 📊 [JaCoCo](target/site/jacoco/index.html)
### * ✅  Usage of the java.util.Stream API within your project.
### * ✅  Custom ORM source code should be included within the web application as a Maven dependency

---
