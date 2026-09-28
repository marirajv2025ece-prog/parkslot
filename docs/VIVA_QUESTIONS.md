# Viva questions and simple answers

**1. Why did you use Spring Boot?**
It removes a lot of setup. It has an embedded Tomcat server, auto-configuration and ready starters for web, JPA and validation, so I could focus on the parking logic instead of XML configuration.

**2. Why did you use JPA?**
JPA lets me work with Java objects instead of writing SQL for every operation. Hibernate creates the tables and converts objects to rows. Spring Data JPA gives methods such as `save()` and `findById()` automatically.

**3. What is a REST API?**
A way for the browser and the server to talk using HTTP. URLs represent resources (`/api/slots`), HTTP methods represent actions (GET read, POST create, PUT update, DELETE delete), and data is sent as JSON.

**4. What is layered architecture?**
Splitting the code into layers with one job each: Controller (HTTP) → Service (business rules) → Repository (database) → MySQL. It makes the code easy to read, test and change.

**5. Why is business logic in the Service layer?**
Rules such as "an occupied slot cannot be assigned" must work for every caller, not only for one screen. The controller only handles requests, the repository only handles data, so the service is the right place. Rules are not put in the frontend because anyone can bypass the frontend with Postman.

**6. Why did you create ParkingRecord?**
A slot is used by many vehicles over time and a vehicle uses many slots over time. If I linked vehicle and slot permanently, I would lose the history. One ParkingRecord = one visit, with entry time, exit time and status, so I can build the daily log.

**7. How do you prevent duplicate slot allocation?**
`ParkingService.ensureSlotAvailable()` checks the slot status before saving. If it is OCCUPIED, I throw `SlotAlreadyOccupiedException` (409). The slot row is also loaded with a database lock (`PESSIMISTIC_WRITE`), so two requests at the same moment cannot both take it.

**8. How do you prevent a vehicle from being parked twice?**
`ensureVehicleNotParked()` asks the repository whether an ACTIVE record exists for that vehicle. If yes, `VehicleAlreadyParkedException` is thrown.

**9. What is @ControllerAdvice / @RestControllerAdvice?**
A class that handles exceptions for all controllers in one place. My `GlobalExceptionHandler` converts exceptions to clean JSON with the right status code, so users never see a Java stack trace.

**10. What is @ManyToOne?**
Many rows of one table point to one row of another. Many ParkingRecords belong to one VisitorVehicle / Flat / ParkingSlot. The foreign key (`vehicle_id`, `flat_id`, `slot_id`) is stored in `parking_record`.

**11. What is @OneToMany?**
One row is linked to many rows. One VisitorVehicle has many ParkingRecords. I used `mappedBy` because the foreign key already lives on the ParkingRecord side.

**12. Why do you use DTOs?**
DTOs are the objects sent to and from the API. They let me validate input, hide fields I do not want to expose, and avoid problems from returning entities with lazy relationships. The frontend JSON is exactly the DTO fields.

**13. What HTTP status do you return when a slot is occupied?**
409 Conflict, because the request is valid but conflicts with the current state of the data.

**14. How is the daily report generated?**
The frontend sends `GET /api/parking/report/daily?date=2026-09-28`. The service searches records whose entry time is between 00:00 and 23:59:59 of that date and counts total, completed and currently inside.

**15. What happens when a vehicle exits?**
`recordExit()` finds the record, rejects it if it is already COMPLETED, sets the exit time, changes the status to COMPLETED and changes the slot to AVAILABLE — all in one transaction.

**16. How does the frontend communicate with Spring Boot?**
JavaScript `fetch()` calls the REST URLs and receives JSON. The HTML files are served by the same Spring Boot application, so there is no CORS problem.

**17. Why is validation required?**
To reject wrong or empty data before it reaches the database (for example an invalid vehicle number or mobile number). I use `@NotBlank`, `@Pattern`, `@Size` with `@Valid`, and return a 400 with a message per field.

**18. What happens if the database is unavailable?**
Spring throws a database exception. `GlobalExceptionHandler` catches it, writes the details in the server log and returns a simple 500 JSON message: "Database is not available...".

**19. How does login work?**
The guard sends username and password. The password is compared with a BCrypt hash stored in `staff_user`. On success the username is stored in the HTTP session. `AuthInterceptor` checks the session for every `/api/**` request and returns 401 if there is none.

**20. Why BCrypt?**
Passwords must never be stored as plain text. BCrypt is a one-way hash with a random salt, so even the database owner cannot read the password.

**21. How are the 10 slots created without duplicates?**
`DataInitializer` (a `CommandLineRunner`) runs at start-up and inserts P01–P10 only when `existsBySlotNumber()` is false.

**22. What is @Transactional?**
It makes a method run as one database transaction. If a business rule fails halfway, everything is rolled back, so no partial data is saved.

**23. What does `ddl-auto=update` do?**
Hibernate creates missing tables/columns at start-up and keeps the existing data.
