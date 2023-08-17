# Shape API App

Shapes API - Spring Boot Example Project

**Shape API** is a Spring Boot application that demonstrates the implementation of Inheritance, Factory Pattern, and Strategy Pattern. The application also includes features such as Spring Validation, Spring MVC, Spring Data JPA, Security with JWT, and more.

## Technologies Used

- **Java 11**
- **Spring Framework:** Utilized for various functionalities including Validation, MVC architecture, Data JPA, Security with JWT, and Boot Testing.
- **Querydsl:** Employed for advanced searching of figures by parameters.
- **Maven:** Dependency management and build tool.
- **Flyway:** Facilitates database schema management and version control.
- **Mockito:** Framework for creating and managing mock objects during testing.
- **ModelMapper:** Simplifies object-to-object mapping.

## Getting Started

1. **Clone the Repository:** Use Git to clone the repository to your local machine.

    ```bash
    git clone https://github.com/your-username/shape-api-app.git
    ```

2. **Build the Project:** Navigate to the project directory and build the project using Maven.

    ```bash
    cd shape-api-app
    mvn clean install
    ```

3. **Run the Application:** Execute the built JAR file to start the Spring Boot application.

    ```bash
    java -jar target/shape-api-app.jar
    ```
4. **After starting:** The application, it will be accessible at: http://localhost:8080.
## Usage

The Shapes API application provides endpoints to manage various shapes, allowing you to create shapes.

- **Register a User** Register a new user to get authentication access. You can use API endpoints like  ```/api/v1/users/register```to create a user.

- **Create a Shape:** Use the provided API endpoint, such as ```/api/v1/shapes``` to create a new shape by providing the necessary parameters

- **Retrieve Shapes:** Search for shapes using different parameters through the API.

Users can perform a complex filtered search of shapes by multiple parameters, for example:
   ```bash
  createdBy=...&type=...&dateFrom=...&dateTo...&areaFrom=...&areaTo...&perimeterFrom=...&perimeterTo=...&widthFrom=...&widthTo=...&radiusFrom=...&radiusTo=...
  ```
## Contributing
Feel free to customize it further according to your project's specific details.
