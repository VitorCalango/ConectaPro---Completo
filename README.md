# ConectaPro

## Project Structure

This project follows a standard Maven web application structure with JPA Hibernate as the persistence framework.

### Directory Structure

```
ConectaPro/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── conectapro/
│   │   │           ├── controller/  # Servlet controllers
│   │   │           ├── model/       # JPA entity classes
│   │   │           ├── repository/  # Data access layer
│   │   │           ├── service/     # Business logic
│   │   │           └── util/        # Utility classes
│   │   ├── resources/
│   │   │   └── META-INF/
│   │   │       └── persistence.xml  # JPA configuration
│   │   └── webapp/
│   │       ├── resources/
│   │       │   ├── css/             # CSS files
│   │       │   ├── js/              # JavaScript files
│   │       │   └── images/          # Image files
│   │       ├── WEB-INF/
│   │       │   ├── views/           # HTML/JSP pages
│   │       │   └── web.xml          # Web application configuration
│   │       └── index.html           # Welcome page
│   └── test/
│       ├── java/                    # Test classes
│       └── resources/               # Test resources
└── pom.xml                          # Maven configuration
```

## Technology Stack

- Java
- JPA/Hibernate for ORM
- Maven for dependency management
- HTML/CSS/JavaScript for frontend

## Getting Started

1. Clone the repository
2. Build the project with Maven: `mvn clean install`
3. Deploy to a servlet container like Tomcat or Jetty
