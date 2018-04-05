# Earlgrey
![tea love](https://raw.githubusercontent.com/acalvoa/EARLGREY/extra/resources/Earl_Grey.jpg)

A Lightweight Java Services Framework inspired in Nodejs Express, Sails.js &amp; Phalcon PHP and Loopback for quick develop of apps based in client server architecture. Build apps with frontend frameworks like Angular and Reactjs. Code and build faster and leave the machine's work to machines.

## Requeriments 

TO DO

## Compilation and distribution

To compile and package this project you need to use Maven [Apache Maven](https://maven.apache.org/).

### Installing Apache Maven

- Linux Debian Distribution
   
   ```bash
   $ sudo apt-get install mvn
   ```
- Linux Redhat Distribution
   
   ```bash
   $ sudo yum install mvn
   ```

### Compiling and Packaging Earlgrey

```bash
mvn clean package
```

## Using Earlgrey

To use Earlgrey only you need import the jar to your web project and start the Earlgrey Kernel in a Servlet listener in the contextInitialized event. The Earlgrey System automatically read the project structure and load the Earlgrey structures to implement a lightweight system based on services architecture with an admin console with hot configuration options.

```java
@WebListener
public class SO implements ServletContextListener, ServletRequestListener {
	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
    	  Ealgrey earlgrey = new Ealgrey(arg0.getServletContext());
    }
}
```
## Earlgrey ORM functions.

Earlgrey implement a lightweight ORM to operate with databases in a common way. This ORM works based on Models definitions in the code write by the apps developers. Earlgrey recognize the models and map them to interact with the controllers and the configuration console. You can define Blueprints Models that implement the CRUD operations in the REST API based only in the model. The ORM provide a powerfull function set to operate with the database, with a custom querys, transaction suport and multi tenancy.

### Datasbase suport

* [X] Mysql
* [X] Oracle
* [X] Postgres

### Model Declaration

To DO

### Blueprint Model

To DO

## Ealgrey Controllers

To DO

## Ealgrey Console

To DO

## Earlgrey Seed

Use Earlgrey is simple with the common archetype developed by us. Only you need code your controller and models to make quickly a strongs API's, based on REST Arquitecture. Earlgrey provide a RESTFull suport, and the posibility to write Blueprints Models to get the CRUD operations with a minimal coding operations. Also you can extend the archetype to build a large projects in java technology with types, custom errors and policies, and a lot of operations in the earlgrey console.

Visit [Earlgrey Seed](https://github.com/acalvoa/Ealgrey_seed) to more information.

## Earlgrey CLI

Visit [Earlgrey CLI](https://github.com/brutalchrist/earlgrey-cli) to more information.

## Contributors

Thanks to all beautiful people than make be possible this project

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore -->
[<img src="https://avatars3.githubusercontent.com/u/806569?s=400&v=4" width="100px;"/><br /><sub><b>Sebastian Gonzalez V.</b></sub>](https://github.com/brutalchrist)<br />[📖](https://github.com/acalvoa/EARLGREY/commits?author=brutalchrist "Documentation") [💻](https://github.com/acalvoa/EARLGREY/commits?author=brutalchrist)
<!-- ALL-CONTRIBUTORS-LIST:END -->
