# AdharshMart - E-Commerce Marketplace

A multi-seller e-commerce web application running on Java Servlets, JDBC, and Apache Tomcat 9, compliant with Anna University R2025 Semester 3 specifications.

## Architecture
- **Presentation**: Vanilla JavaScript fetch client, CSS3 3D Glassmorphism.
- **Controller**: Java Servlets (`javax.servlet.*`).
- **Service**: Business logic and validation.
- **Data Access**: DAOs using raw SQL PreparedStatements.
- **Database**: H2 Database via HikariCP connection pooling.