The Smart Clinic Management System follows a three-tier web architecture (Presentation, Application and Data layer).
Structure : Thymeleaf templates and REST API consumers. 
The Spring Boot backend that contains the controllers, services, and business logic.
MySQL for structured data and MongoDB for flexible, document-based data

Thymeleaf templates are used for the Admin and Doctor dashboards, while REST APIs serve all other modules. 
The application interacts with two databases—MySQL (for patient, doctor, appointment, and admin data) and MongoDB (for prescriptions). 
All controllers route requests through a common service layer, which in turn delegates to the appropriate repositories. 
MySQL uses JPA entities while MongoDB uses document models.

1. User accesses AdminDashboard or Appointment pages.
2. The action is routed to the appropriate Thymeleaf or REST controller.
3. The controller calls the service layer
4. Service layer access to MySQL repo or MongoDB Repo
5. Repo access to according database
6. Model binding : Data is mapped to Java model class
7. Use of model in response layer : MVC renders HMTL, REST flow send HTTP response
