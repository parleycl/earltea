# Earlgrey

![tea love](https://raw.githubusercontent.com/acalvoa/EARLGREY/master/console/src/client/assets/images/logo.jpg)

Earlgrey es un liviano framework java inspirado en Express, Sails.js, Phalcon PHP y Loopback para el desarrollo agil de aplicaciones orientadas a servicios y apps basadas en las arquitectura cliente servidor. Desarrolla aplicaciones con framework como Angular, React y Vue.js usando a Earlgrey como tu capa de servidor. Construye aplicaciones Y API's rapidamente y deja el trabajo de las maquinas a las maquinas.

## Porque Earlgrey? 

Un dia, Earlgrey nacio en base a la necesidad de escribir codigo y aplicaciones en java de una forma rapida. Normalmente el lenguaje JAVA y sus frameworks necesitan mucho tiempo y dependencias para desarrollar una aplicación pequeña que provea una API RESTful. Ademas muchas veces, el exceso de dependencias genera conflictos y existen otros problemas propios del contenedor de aplicaciones, haciendo que JAVA no sea la mejor solución para aplicaciones rapidas, pequeñas, sesiones de hackaton o cualquier proyecto en que necesites un codigo agil o donde necesites un entorno de desarrollo en menos de 5 minutos con resultados reales, sin conocimientos avanzados del lenguaje.

Ademas la baja estandarización en las normas de codifición y los mutliples estilos para fabricar aplicaciones usado por los desarrolladores JAVA, en un proyecto de gran mediana o gran escala puede generar grandes problemas, sino se tiene una estructura donde se apliquen buenas practicas de codificación.

Earlgrey es propuesto como una solución para mejorar la experiencia de los desarrolladores fabricando aplicaciones, que usan la arquitectura orientada a servicios y la arquitectura cliente servidor. Con Earlgrey, puedes desarrollar una API RESTful en menos de 5 minutos con muchas posibilidades que vienen embebidas en el framework como ORM, Cache, Sesiones, Configuraciones en Caliente, Consola de administración, Logs en tiempo real, entre otras.

Earlgrey fue diseñado a partir de la simplicidad y cada una de sus caracteristicas nacio de la necesidad de un grupo de desarrolladores que necesitaban escribir rapidamente aplicaciones en el lenguaje JAVA, que tuvieran una fuerte base que les permitiera desplegar la aplicacion en un contenedor de aplicaciones o donde quiera que ellos quisieran. Para esto, cada caractersitica fue diseñada con amor pensando en la simplicidad a la hora de programar. Queremos que escribas menos codigo para obtener grandes resultados a traves de una bella y amigable form de escribir codigo.

Para este fin el framework propone dos principios:

- El primero de ellos es **"One Framework"**, y propone que sin importar que uses para desplegar tu aplicación, el mismo codigo que fabriques funcionara en todos ellos. Si usas un contenedor como JBOSS, Tomcat, Weblogic, GlassFish, no hay ningun problema y puedes usar el que gustes, pero si decides usar el servidor embebido en earlgrey, no necesitas hacer ningun cambio, el mismo codigo te servira para ambos fines.

- El segundo es **"LTMWTM"** o **"Let the machine's work to machines"**. Estamos realmente obsecionados con escribir menos codigo, otorgandole parte de la responsabilidad a la maquina, para lo cual ejecutamos automaticamente multiples tareas, que ayudan al desarrollador en su tarea de construir aplicaciones. Muchas funciones

- The second is **"LTMWTM"** or **"Let the machine's work to machines"**. We are obsessed with write less code, bringing part of responsibility to machine, executing automatically multiple tasks, that assist to developer in the deveping work. A lot of functions of mapping and reflection of the code provide a great admin console, when the developer can configure and extend the functions of app, with realtime functions, Hot reloading properties, etc. Also Earlgrey core provide a lot of functions to write faster API's with Database interations, using "Models with blueprints", providing a function similar to "Loopback" of IBM, using a Model oriented architecture.

Earlgrey is good to use in any type of proyect, but it's perfect to use in any small or quick project oriented to services. Earlgrey it's a great solution to use in adition with Angular, React or Vue.js when you need a API to communicate Backend with Frontend. It's a secret but Earlgrey originally was made to use with apps build in Angular and React, when we need the frontend and backend in only one "WAR or EAR" packet.

**Try Earlgrey, and be part of a new JAVA revolution.**

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
