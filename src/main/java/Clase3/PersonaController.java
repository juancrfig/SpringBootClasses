package Clase3; // Defines the package where this class resides.

import org.springframework.beans.factory.annotation.Autowired; // Imports @Autowired for dependency injection.
import org.springframework.http.HttpStatus; // Imports HttpStatus for setting HTTP response status codes (e.g., 200 OK, 404 Not Found).
import org.springframework.http.ResponseEntity; // Imports ResponseEntity, which allows full control over the HTTP response (body, status, headers).
import org.springframework.web.bind.annotation.*; // Imports various Spring Web annotations like @RestController, @RequestMapping, @GetMapping,
// @PostMapping, @PutMapping, @DeleteMapping, @RequestBody, @PathVariable.

import java.util.List; // Imports List for collections of objects.
import java.util.Optional; // Imports Optional to handle the potential absence of data.

/**
 * PersonaController.java (The Controller)
 *
 * This class represents the "Controller" component in the Model-View-Controller (MVC) design pattern.
 * In a Spring Boot REST API, the controller is the entry point for handling incoming HTTP requests.
 *
 * Its responsibilities include:
 * - **Request Mapping:** Deciding which method handles which URL and HTTP method (GET, POST, PUT, DELETE).
 * - **Request Processing:** Extracting data from the request (e.g., path variables, request body).
 * - **Delegation:** Passing the request to the appropriate Service layer method for business logic execution.
 * - **Response Handling:** Constructing and sending the HTTP response back to the client, including data (often JSON) and status codes.
 *
 * The controller should be "thin," meaning it should mainly delegate tasks to the Service layer and not contain complex business logic itself.
 */
@RestController // This is a convenience annotation that combines @Controller and @ResponseBody.
// - @Controller: Marks this class as a Spring MVC controller capable of handling web requests.
// - @ResponseBody: Indicates that the return value of the methods should be directly
//   bound to the web response body. For REST APIs, this means Spring will automatically
//   convert Java objects (like `Persona` or `List<Persona>`) into JSON (the default,
//   or XML if configured) format for the client.
@RequestMapping("/personas") // This annotation maps all HTTP requests starting with "/personas" to this controller.
// For example, if your application runs on `http://localhost:8080`,
// then `http://localhost:8080/personas` will be handled by this controller.
public class PersonaController {

    // --- Dependency Injection ---
    private final PersonaService personaService; // Declares a final field for PersonaService.

    @Autowired // Spring will automatically "inject" an instance of `PersonaService` into this constructor.
    // This allows the controller to access the business logic defined in the service layer
    // without directly managing the service's lifecycle. It exemplifies the Dependency Injection pattern.
    public PersonaController(PersonaService personaService) {
        this.personaService = personaService; // Initializes the personaService field with the injected instance.
    }

    // --- Endpoint Methods (API Endpoints) ---

    /**
     * Handles HTTP GET requests to retrieve all personas.
     * URL: `GET /personas`
     *
     * @return A `ResponseEntity` containing a `List` of `Persona` objects (which will be converted to JSON),
     *         and an HTTP status of `200 OK`.
     */
    @GetMapping // This annotation maps HTTP GET requests to the `/personas` URL (inherited from `@RequestMapping`).
    public ResponseEntity<List<Persona>> getAllPersonas() {
        // Delegates the call to the `PersonaService` to retrieve all personas.
        List<Persona> personas = personaService.getAllPersonas();
        // Returns a `ResponseEntity`. This gives explicit control over the HTTP response,
        // allowing you to set the response body and the HTTP status code.
        // `HttpStatus.OK` corresponds to HTTP status code 200.
        return new ResponseEntity<>(personas, HttpStatus.OK);
    }

    /**
     * Handles HTTP GET requests to retrieve a single persona by ID.
     * URL: `GET /personas/{id}`
     * Example: `GET /personas/1` will fetch the persona with ID 1.
     *
     * @param id The ID of the persona, extracted from the URL path.
     * @return A `ResponseEntity` containing the `Persona` object and HTTP status `200 OK` if found,
     *         or HTTP status `404 NOT FOUND` if no persona with the given ID exists.
     */
    @GetMapping("/{id}") // Maps GET requests to `/personas/{id}`, where `{id}` is a path variable.
    public ResponseEntity<Persona> getPersonaById(@PathVariable int id) {
        // `@PathVariable` annotation binds the value from the URL path segment (e.g., '1' from `/personas/1`)
        // to the `id` method parameter.
        // Delegates the call to the `PersonaService` to find a persona by its ID.
        // `Optional` is used to gracefully handle cases where the persona might not be found.
        return personaService.getPersonaById(id)
                .map(persona -> new ResponseEntity<>(persona, HttpStatus.OK)) // If `persona` is present in `Optional`, return 200 OK with the persona data.
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));        // If `Optional` is empty (persona not found), return 404 NOT FOUND.
    }

    /**
     * Handles HTTP POST requests to create a new persona.
     * URL: `POST /personas`
     * Request Body: A JSON representation of a Persona object (e.g., `{"nombre": "Juan", "apellido": "Perez", "edad": 30}`).
     * Note: The `id` should generally not be provided in the request body for new entities, as it's auto-generated.
     *
     * @param persona The `Persona` object received in the request body. Spring automatically converts
     *                the incoming JSON (or XML) payload into a `Persona` Java object.
     * @return A `ResponseEntity` containing the newly created `Persona` object (which will now have its ID assigned),
     *         and an HTTP status of `201 CREATED`.
     */
    @PostMapping // Maps HTTP POST requests to `/personas` to this method.
    public ResponseEntity<Persona> createPersona(@RequestBody Persona persona) {
        // `@RequestBody` annotation binds the entire HTTP request body (the JSON payload)
        // to the `persona` method parameter. Spring's message converters handle the JSON-to-Java conversion.
        // Delegates the call to the `PersonaService` to save the new persona.
        Persona savedPersona = personaService.savePersona(persona);
        // Returns `201 CREATED` status, which is the standard HTTP status code for successful resource creation.
        // The response body contains the saved persona, including its newly generated ID.
        return new ResponseEntity<>(savedPersona, HttpStatus.CREATED);
    }

    /**
     * Handles HTTP PUT requests to update an existing persona.
     * URL: `PUT /personas/{id}`
     * Request Body: A JSON representation of the `Persona` object with updated details.
     *
     * @param id The ID of the persona to update, extracted from the URL path.
     * @param personaDetails The `Persona` object with updated details received in the request body.
     * @return A `ResponseEntity` containing the updated `Persona` object and HTTP status `200 OK` if found and updated,
     *         or HTTP status `404 NOT FOUND` if the persona with the given ID does not exist.
     */
    @PutMapping("/{id}") // Maps HTTP PUT requests to `/personas/{id}` to this method.
    public ResponseEntity<Persona> updatePersona(@PathVariable int id, @RequestBody Persona personaDetails) {
        // First, attempt to find the existing persona in the database using its ID.
        Optional<Persona> existingPersona = personaService.getPersonaById(id);

        if (existingPersona.isPresent()) {
            // If the persona exists, retrieve it from the Optional.
            Persona personaToUpdate = existingPersona.get();

            // Update the attributes of the existing persona with the values from the request body.
            // Note: The ID should not be updated as it's the primary key and should remain constant.
            personaToUpdate.setNombre(personaDetails.getNombre());     // Update the name.
            personaToUpdate.setApellido(personaDetails.getApellido()); // Update the last name.
            personaToUpdate.setEdad(personaDetails.getEdad());         // Update the age.

            // Save the updated persona back to the database. This will overwrite the existing record.
            Persona updatedPersona = personaService.savePersona(personaToUpdate);
            // Return 200 OK with the fully updated persona object in the response body.
            return new ResponseEntity<>(updatedPersona, HttpStatus.OK);
        } else {
            // If no persona with the given ID is found, return 404 NOT FOUND.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Handles HTTP DELETE requests to delete a persona by ID.
     * URL: `DELETE /personas/{id}`
     *
     * @param id The ID of the persona to delete, extracted from the URL path.
     * @return A `ResponseEntity` with HTTP status `204 NO CONTENT` if the deletion was successful (meaning no body is returned),
     *         or HTTP status `404 NOT FOUND` if the persona to delete did not exist.
     */
    @DeleteMapping("/{id}") // Maps HTTP DELETE requests to `/personas/{id}` to this method.
    public ResponseEntity<Void> deletePersona(@PathVariable int id) {
        // It's good practice to check if the resource exists before attempting to delete it,
        // so you can return a more informative HTTP status code (like 404).
        if (personaService.getPersonaById(id).isPresent()) {
            personaService.deletePersona(id); // Delegates the deletion operation to the `PersonaService`.
            // Return `204 NO CONTENT` for a successful deletion. This status code indicates that the request
            // was successful but the client doesn't need to navigate away from its current page or receive
            // any new content.
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            // If the persona was not found, return 404 NOT FOUND.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /*
     * Important Note on the "View" in REST APIs:
     *
     * In a traditional MVC web application (e.g., using Thymeleaf or JSP templates),
     * the Controller methods would return a `String` representing the name of a view template (e.g., "persona-list").
     * The Controller would also use Spring's `Model` interface to pass data (like `List<Persona>`) to that view:
     *
     * Example of a traditional MVC Controller method returning a View:
     * @GetMapping("/web/personas")
     * public String showPersonas(Model model) { // Spring's Model interface to pass data to the view
     *     List<Persona> personas = personaService.getAllPersonas();
     *     model.addAttribute("personas", personas); // Add list of personas to the model for the view
     *     return "persona-list"; // This string is the logical view name (e.g., persona-list.html in `src/main/resources/templates`)
     * }
     *
     * However, for a Spring Boot REST API built with `@RestController`, we are primarily serving
     * data (JSON, in this case) directly. The "View" is implicitly the client application (e.g., a JavaScript
     * frontend built with React, Angular, Vue; a mobile application; or even another backend service)
     * that consumes this JSON data and then renders it in its own way to the end-user.
     *
     * The `@RestController` annotation, along with returning Java objects in `ResponseEntity`, automatically
     * serializes these objects into JSON in the HTTP response body, effectively serving as the "View"
     * for any API consumer.
     */
}