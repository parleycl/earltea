# Earlgrey
[![Build Status][build-badge]][build]
[![version][version-badge]][package]
[![MIT License][license-badge]][LICENSE]
![tea love](https://raw.githubusercontent.com/acalvoa/EARLGREY/extra/resources/Earl_Grey.jpg)

A Lightweight Java Services Framework inspired in Nodejs Express, Sails.js &amp; Phalcon PHP for quick develop of apps based in 
client server architecture. Build apps with frontend frameworks like Angular and Reactjs. Code and build faster and leave the machine's work to machines.

## Requeriments 

TO DO

## Compilation and distribution

To compile and package this project you need to use Maven [Apache Maven](https://maven.apache.org/).

### Installing Apache Maven

- Linux Debian Distribution
   
   ```bash
   $ sudo apt-get install ant
   ```
- Linux Redhat Distribution
   
   ```bash
   $ sudo yum install ant
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
    	  Kernel earlgrey = new Kernel(arg0.getServletContext());
    }
}
```

## Contributors

Thanks to all beautiful people than make be possible this project

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore -->
[<img src="https://avatars3.githubusercontent.com/u/806569?s=400&v=4" width="100px;"/><br /><sub><b>Sebastian Gonzalez V.</b></sub>](https://github.com/brutalchrist)<br />[📖](https://github.com/acalvoa/EARLGREY/commits?author=brutalchrist "Documentation") [💻](https://github.com/acalvoa/EARLGREY/commits?author=brutalchrist)
<!-- ALL-CONTRIBUTORS-LIST:END -->
