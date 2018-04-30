![tea love](https://github.com/acalvoa/earlgrey/blob/master/console/src/client/assets/images/logoearlgrey2.png)

A Lightweight Java Services Framework inspired in Nodejs Express, Sails.js &amp; Phalcon PHP and Loopback for quick develop of apps based in client server architecture. Build apps with frontend frameworks like Angular, React and Vue.js. Code and build faster and leave the machine's work to machines.

## Why Earlgrey? 

One day, Earlgrey born of the need to develop code and apps in java quickly. The java language and their frameworks, needs a lot of time and dependencies to make a little aplication that provide a RESTful API. In adition sometimes the dependencies generate conflicts and other own problems of apps container, make JAVA in not a great solution for quicks apps, smalls or hackaton sessions, or any project that you needs with agile coding, where you need a develop enviroment in less 5 minutes with real results, without a great knowllege about the language.

Also, the low standardization of coding rules and multiple styles using by the java developers to make web apps, in a large project maybe can generate a great problem, if don't have  a structure where a good practice of coding are apply.

Earlgrey is proposed like a solution to enhanced the developer experience making apps thats use the services architecture and the client-server architecture. With Earlgrey, you can develop an Restful API in less 5 minutes with a lot of posibilities embed in the framework like ORM, Cache, Sessions, Hot Configuratios, Admin Console, Realtime logs in a web platform, etc.

Earlgrey is designed thinking in the simplicity. Each feature was born from needs of a group of developers, that needed write quickly apps in java language with a strong base that allow deploy this apps in a apps container without matter which.
Each feature was design with love thinking in the coding simplicity, we want you write less code to make great results with and friendly and beatifull coding form.

The framework propose two principles used to make this possible. 
- The first is **"One Framework"**, no matter what you use to deploy your app, the same code works in all them. if you use a app container like JBOSS, Tomcat, Weblogic, GlassFish, it's ok you can use anyone with the same code. If you decide use the server embeded in earlgrey, you can use with the same code.

- The second is **"LTMWTM"** or **"Let the machine's work to machines"**. We are obsessed with write less code, bringing part of responsibility to machine, executing automatically multiple tasks, that assist to developer in the deveping work. A lot of functions of mapping and reflection of the code provide a great admin console, when the developer can configure and extend the functions of app, with realtime functions, Hot reloading properties, etc. Also Earlgrey core provide a lot of functions to write faster API's with Database interations, using "Models with blueprints", providing a function similar to "Loopback" of IBM, using a Model oriented architecture.

Earlgrey is good to use in any type of proyect, but it's perfect to use in any small or quick project oriented to services. Earlgrey it's a great solution to use in adition with Angular, React or Vue.js when you need a API to communicate Backend with Frontend. It's a secret but Earlgrey originally was made to use with apps build in Angular and React, when we need the frontend and backend in only one "WAR or EAR" packet.

**Try Earlgrey, and brew the new JAVA revolution.**

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

### Compiling Console

Ealgrey use a admin console to control and configurating the operation of the framework like a subsystem. This console is built in Angular using [Angular-seed](https://github.com/mgechev/angular-seed) and is inyected in the jar while maven made the packet. To do this posible, We need compile the console, but sometimes only it's necesary the first time when the console no have a changes in his functionallity. To build the Earlgrey with the console use this command.

```bash
mvn clean package -P console
```

## Get Started

To use Earlgrey only you need import the jar to your web project and start the Earlgrey Kernel in a Servlet listener in the contextInitialized event. The Earlgrey System automatically read the project structure and load the Earlgrey structures to implement a lightweight system based on services architecture with an admin console with hot configuration options.

```java
@WebListener
public class Testapp implements ServletContextListener, ServletRequestListener {
	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
    	  Ealgrey earlgrey = new Ealgrey(arg0.getServletContext());
    }
}
```

## Ealgrey Controllers

Write a controllers and API endpoints it's really easy with Earlgrey. Only you need put the controller class in a package you choose, extends the ControllerBase class. Aditional you must add the "Controller" annotation. Earlgrey System automatically recognize the controllers and map them to use in the app. To declare an Controller you need put the description, name and versión of this controller. An example of this can be.

```java
@Controller(description = "A test of Controller", name = "HelloWorld", version = 1)
public class HelloWorld extends ControllerBase {
	/**
     * The controller actions are code here
     */
}
```

The controllers have an options to extend his behaviors like Routes, Cache, UserCache, Policies, Properties, etc. If you wan't to define a context route for this endpoint only you need to use the annotation @Route like this.

```java
@Controller(description = "A test of Controller", name = "HelloWorld", version = 1)
@Route(route = "/hello")
public class HelloWorld extends ControllerBase {
	/**
     * The controller actions are code here
     */
}
```

### Controller actions and routing.

In this case, the controllers Actions are the way to write endpoint actions or anything you needs. To define a controller action you need declare a @ControllerAction annotation in a public static method defined into controller. Earlgrey automatically recognize the action and map them. This method receive two parameters to handle any http request and response. An example of this can be.

```java
@Controller(description = "A test of Controller", name = "HelloWorld", version = 1)
@Route(route = "/hello")
public class HelloWorld extends ControllerBase {
    
    @ControllerAction(description = "A test action to write hello world", name = "Test", version = 1)
    public static void test(HttpRequest req, HttpResponse res) {
        System.out.println("Hello world");
        return;
    }
}
```

#### Route a Controller Action

You can add diferents options to define and endpoint. All RESTFul operations are soported, and you can use like an annotation. Also you can define a Route for this endpoint with @Route annotation like the controllers. The actions support multiple HTTP methods declaration, but it's not recomended following the semantic API guidelines. An example of this can be.

```java
@Controller(description = "A test of Controller", name = "HelloWorld", version = 1)
@Route(route = "/hello")
public class HelloWorld extends ControllerBase {
    
    @ControllerAction(description = "A test action to write hello world", name = "Test", version = 1)
    @Route(route = "/world")
    @GET
    public static void test(HttpRequest req, HttpResponse res) {
        System.out.println("Hello world");
        res.ok();
        return;
    }
}
```

This definitiion make automatically an endpoint, following the next definition.

```bash
[http/https]://[HOST]:[PORT]/CONTEXT/api/[CONTEXT ENDPOINT]/[ACTION ROUTE]
```

For the test controller action the url is defined by.

```bash
[http/https]://[HOST]:[PORT]/CONTEXT/api/hello/world
```

#### Restful methods soported

The next HTTP method are supported in Earlgrey. If your application container not support a method, not problem buddy, Earlgrey bypass the application server and bring support to your app to use this method following the RESTful guidelines defined in [Restfull Cookbook](http://restcookbook.com)

- [X] POST (@POST)
- [X] PUT (@PUT)
- [X] GET (@GET)
- [X] PATCH (@PATCH)
- [X] DELETE (@DELETE)
- [X] OPTIONS (@OPTIONS)

#### Cache Support

Ealrgrey support cache for the controller actions in two levels, The cache can be general using the @Cache Annotation or by User using the @UserCache annotation. The general cache is a global cache for all users, while the UserCache is a cache for each user that make petitions to system, normally with diferentns results by user. Both ways define the lifetime of cache; If the lifetime are cero, Earlgrey automatically remove the register of cache memory. If the cache is not set, in the first request of client made, the cache will the take the result of petition and save into the memory. the next petitions take the result of the cache memory, enhancing the speed of response.  

The way to use the cache are the next for global cache.

```java
@Controller(description = "A test of Controller", name = "HelloWorld", version = 1)
@Route(route = "/hello")
public class HelloWorld extends ControllerBase {
    
    @ControllerAction(description = "A test action to write hello world", name = "Test", version = 1)
    @Route(route = "/world")
    @GET
    @Cache(time = 3600) // Time in seconds
    public static void test(HttpRequest req, HttpResponse res) {
        System.out.println("Hello world");
        res.ok();
        return;
    }
}
```
The way to use the cache are the next for user cache.

```java
@Controller(description = "A test of Controller", name = "HelloWorld", version = 1)
@Route(route = "/hello")
public class HelloWorld extends ControllerBase {
    
    @ControllerAction(description = "A test action to write hello world", name = "Test", version = 1)
    @Route(route = "/world")
    @GET
    @UserCache(time = 3600) // Time in seconds
    public static void test(HttpRequest req, HttpResponse res) {
        System.out.println("Hello world");
        res.ok();
        return;
    }
}
```

## Earlgrey ORM Functions.

Earlgrey implement a lightweight ORM to operate with databases in a common way. This ORM works based on Models definitions in the code write by the apps developers. Earlgrey recognize the models and map them to interact with the controllers and the configuration console. You can define Blueprints Models that implement the CRUD operations in the REST API based only in the model. The ORM provide a powerfull function set to operate with the database, with a custom querys, transaction suport and multi tenancy.

### Datasbase Suport

* [X] Mysql
* [X] Oracle
* [X] Postgres

### Model Declaration

To DO

### Blueprint Model

To DO

### ORM Operations

To DO

## Ealgrey Console

To admin Earlgrey, you only need a web browser. Earlgrey provide a Admin interface to config the main posibilities. In this console you can config the properties, controllers, models, routes, view the logs, config the custer configuration and everything you needs to generate a great app.

To access to the console only put in your web browser the next url.

```bash
[http/https]://[HOST]:[PORT]/CONTEXT/console/
```

If your first time use this credentials:

```bash
Username: admin
Password: earlgrey
```

For example, if you need access to the console with a www.test.com domain in the 8080 port, with the tessapp context, without a ssl connection, the url should be. 

```bash
http://www.test.com:8080/testapp/console/
```

If your app run on the 80 port, you can skip the port. An example should be.

```bash
http://www.test.com/testapp/console/
```


## Earlgrey Seed

Use Earlgrey is simple with the common archetype developed by us. Only you need code your controller and models to make quickly a strongs API's, based on REST Arquitecture. Earlgrey provide a RESTFul support, and the posibility to write Blueprints Models to get the CRUD operations with a minimal coding operations. Also you can extend the archetype to build a large projects in java technology with types, custom errors and policies, and a lot of operations in the earlgrey console.

Visit [Earlgrey Seed](https://github.com/acalvoa/Ealgrey_seed) to more information.

## Earlgrey CLI

Visit [Earlgrey CLI](https://github.com/brutalchrist/earlgrey-cli) to more information.

## Contributors

Thanks to all beautiful people than make be possible this project

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore -->
| [<img src="https://avatars3.githubusercontent.com/u/806569?s=400&v=4" width="100px;"/><br /><sub><b>Sebastian Gonzalez V.</b></sub>](https://github.com/brutalchrist)<br />[📖](https://github.com/acalvoa/EARLGREY/commits?author=brutalchrist "Documentation") [💻](https://github.com/acalvoa/EARLGREY/commits?author=brutalchrist) | [<img src="https://avatars3.githubusercontent.com/u/20507724?s=460&v=4" width="100px;"/><br /><sub><b>Pablo Jeldres</b></sub>](https://github.com/pjeldres)<br />[🐛](https://github.com/acalvoa/earlgrey/issues/created_by/pjeldres "Bug reports") | [<img src="https://avatars0.githubusercontent.com/u/31521040?s=460&v=4" width="100px;"/><br /><sub><b>David Silva</b></sub>](https://github.com/dsilvap)<br />[🐛](https://github.com/acalvoa/earlgrey/issues/created_by/dsilvap "Bug reports") [💻](https://github.com/acalvoa/EARLGREY/commits?author=dsilvap)|
|:----:|:----:|:----:|
<!-- ALL-CONTRIBUTORS-LIST:END -->

**Made with ♥ in Chile by all us.**

## LICENSE

MIT

[license-badge]: https://img.shields.io/npm/l/all-contributors.svg?style=flat-square
