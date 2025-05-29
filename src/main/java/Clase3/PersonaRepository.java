package Clase3; // Defines the package where this interface resides.

import org.springframework.data.jpa.repository.JpaRepository; // Imports JpaRepository from Spring Data JPA.
import org.springframework.stereotype.Repository; // Imports the @Repository annotation.

/**
 * PersonaRepository.java (The Data Access Layer / Repository)
 *
 * This interface represents the Data Access Layer (DAL) of your application.
 * While not one of the core "MVC" components, it is a crucial part of a Spring Boot application's
 * architecture that directly interacts with the database to persist and retrieve "Model" (Persona) data.
 *
 * It extends `JpaRepository`, which is part of Spring Data JPA. Spring Data JPA drastically simplifies
 * database operations by providing a robust set of CRUD (Create, Read, Update, Delete) methods
 * automatically, requiring very little boilerplate code from you.
 */
@Repository // This annotation indicates that this interface is a "Repository" component in the
// Spring application context. It's primarily used for:
// 1. Component Scanning: Spring will automatically discover and create a bean (an instance
//    that Spring manages) for this repository.
// 2. Exception Translation: It enables Spring's data access exception translation, converting
//    database-specific exceptions (like `SQLException`) into Spring's unified
//    `DataAccessException` hierarchy, making error handling more consistent.
public interface PersonaRepository extends JpaRepository<Persona, Integer> {
    // This interface extends `JpaRepository`, which takes two generic type arguments:
    // 1. `Persona`: The type of the entity (the Model) that this repository will manage.
    // 2. `Integer`: The data type of the primary key of the `Persona` entity. (Since `id` in `Persona.java` is `int`,
    //    and `JpaRepository` expects a wrapper type, `Integer` is used).

    // By simply extending `JpaRepository`, you automatically inherit a rich set of methods without writing any code:
    // - `save(S entity)`: Saves a given entity (both new and existing ones).
    // - `findById(ID id)`: Retrieves an entity by its ID. Returns an `Optional<T>` to handle cases where the entity might not exist.
    // - `findAll()`: Retrieves all entities.
    // - `deleteById(ID id)`: Deletes an entity by its ID.
    // - `count()`: Returns the number of entities available.
    // ... and many more useful methods for pagination, sorting, etc.

    // You can also define custom query methods by simply declaring method signatures,
    // and Spring Data JPA will automatically generate the query implementation for them (e.g., `findByNombre(String nombre)`).
    // For this example, the inherited methods are sufficient.
}