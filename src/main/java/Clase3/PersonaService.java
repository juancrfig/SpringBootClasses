package Clase3; // Defines the package where this class resides.

import org.springframework.beans.factory.annotation.Autowired; // Imports @Autowired for dependency injection.
import org.springframework.stereotype.Service; // Imports the @Service annotation.

import java.util.List; // Imports List for collections of objects.
import java.util.Optional; // Imports Optional to handle cases where a value might be null or absent.

/**
 * PersonaService.java (The Service Layer)
 *
 * This class represents the "Service" layer in the application's architecture.
 * It sits between the Controller and the Repository, acting as the primary location for your
 * application's business logic.
 *
 * The responsibilities of a Service layer include:
 * - **Business Logic:** Implementing specific rules and calculations related to `Persona` objects.
 * - **Orchestration:** Coordinating calls to one or more repositories (if an operation requires multiple data access points).
 * - **Transaction Management:** Defining transactional boundaries (e.g., using `@Transactional` to ensure data consistency for multiple operations).
 * - **Validation:** Performing validation checks on data before it's persisted (though dedicated validation classes can also handle this).
 *
 * By isolating business logic here, the Controller remains "thin" (focused on HTTP concerns),
 * and the Repository remains focused solely on data persistence.
 */
@Service // This annotation marks this class as a "Service" component in the Spring application context.
// Like @Repository, it allows Spring to detect and register this class as a Spring Bean,
// making it available for dependency injection into other components (like the Controller).
public class PersonaService {

    // --- Dependency Injection ---
    private final PersonaRepository personaRepository; // Declares a final field for PersonaRepository.
    // 'final' indicates that this field must be initialized once (via constructor)
    // and its reference cannot be changed later.

    @Autowired // This annotation is used for automatic dependency injection.
    // When Spring creates an instance of `PersonaService`, it looks for a suitable
    // bean (an instance managed by Spring) of type `PersonaRepository` and automatically
    // "injects" it into this constructor parameter. This is a core concept of Spring's
    // Inversion of Control (IoC) container, reducing boilerplate code and making components
    // loosely coupled.
    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository; // Initializes the personaRepository field with the injected instance.
    }

    // --- Service Methods (Encapsulating Business Logic) ---

    /**
     * Retrieves all Persona entities from the database.
     * @return A `List` of all `Persona` objects found in the database.
     */
    public List<Persona> getAllPersonas() {
        return personaRepository.findAll(); // Delegates the call to the `PersonaRepository`.
        // `findAll()` is one of the methods inherited from `JpaRepository`.
    }

    /**
     * Retrieves a single Persona entity by its ID.
     * @param id The integer ID of the persona to retrieve.
     * @return An `Optional<Persona>`:
     *         - If a persona with the given ID is found, it returns an `Optional` containing that `Persona` object.
     *         - If no persona is found, it returns an empty `Optional`.
     *         Using `Optional` helps prevent `NullPointerExceptions` and encourages explicit handling of
     *         the absence of a value.
     */
    public Optional<Persona> getPersonaById(int id) {
        return personaRepository.findById(id); // Delegates the call to the `PersonaRepository`.
        // `findById()` is inherited from `JpaRepository`.
    }

    /**
     * Saves a new Persona entity or updates an existing one.
     * - If the `persona` object has an `id` that matches an existing record in the database,
     *   it updates that existing record.
     * - If the `persona` object has a null `id` or an `id` that does not exist, it creates a new record.
     * @param persona The `Persona` object to be saved or updated.
     * @return The saved/updated `Persona` object. If it was a new entity, this returned object will have its `id` populated by the database.
     */
    public Persona savePersona(Persona persona) {
        return personaRepository.save(persona); // Delegates the call to the `PersonaRepository`.
        // `save()` is inherited from `JpaRepository`.
    }

    /**
     * Deletes a Persona entity by its ID.
     * @param id The integer ID of the persona to delete.
     */
    public void deletePersona(int id) {
        personaRepository.deleteById(id); // Delegates the call to the `PersonaRepository`.
        // `deleteById()` is inherited from `JpaRepository`.
    }

    // You could add more complex business logic here, for example:
    // public List<Persona> findPersonasByNameStartingWith(String prefix) {
    //     // This would require a custom method in PersonaRepository, e.g., 'List<Persona> findByNombreStartingWith(String prefix);'
    //     return personaRepository.findByNombreStartingWith(prefix);
    // }

    // public Persona updatePersonaAge(int id, int newAge) {
    //     Optional<Persona> personaOptional = personaRepository.findById(id);
    //     if (personaOptional.isPresent()) {
    //         Persona persona = personaOptional.get();
    //         if (newAge >= 0) { // Simple validation
    //             persona.setEdad(newAge);
    //             return personaRepository.save(persona);
    //         } else {
    //             throw new IllegalArgumentException("Age cannot be negative.");
    //         }
    //     } else {
    //         throw new RuntimeException("Persona not found with ID: " + id); // Or throw a custom exception
    //     }
    // }
}