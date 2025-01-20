# algebrator-repo1a
A Java, Maven, Spring Boot, Vaadin-based algebraic calculator which uses Matheclipse to solve quadratic equations.

To deploy it locally, I do the following:

cd to algebrator project folder. e.g.: c:\projs\algebrator

Run: mvn clean install

Run: mvn spring-boot:run

In dev mode, this opens the browser. When in production mode, you can manually open the browser to http://localhost:8080/

This application makes full use of Vaadin's single page web application design, enabling the user to enter comma-separated equations and inequations, and to send them to Matheclipse to solve for variables.