![tea love](https://raw.githubusercontent.com/acalvoa/earlgrey/master/console/src/client/assets/images/logoearlgrey2.png)

Earlgrey es un liviano framework java inspirado en Express, Sails.js, Phalcon PHP y Loopback para el desarrollo agil de aplicaciones orientadas a servicios y apps basadas en las arquitectura cliente servidor. Desarrolla aplicaciones con framework como Angular, React y Vue.js usando a Earlgrey como tu capa de servidor. Construye aplicaciones Y API's rapidamente y deja el trabajo de las maquinas a las maquinas.

## Porque Earlgrey? 

Un dia, Earlgrey nacio en base a la necesidad de escribir codigo y aplicaciones en java de una forma rapida. Normalmente el lenguaje JAVA y sus frameworks necesitan mucho tiempo y dependencias para desarrollar una aplicación pequeña que provea una API RESTful. Ademas muchas veces, el exceso de dependencias genera conflictos y existen otros problemas propios del contenedor de aplicaciones, haciendo que JAVA no sea la mejor solución para aplicaciones rapidas, pequeñas, sesiones de hackaton o cualquier proyecto en que necesites un codigo agil o donde necesites un entorno de desarrollo en menos de 5 minutos con resultados reales, sin conocimientos avanzados del lenguaje.

Ademas la baja estandarización en las normas de codifición y los mutliples estilos para fabricar aplicaciones usado por los desarrolladores JAVA, en un proyecto de gran mediana o gran escala puede generar grandes problemas, sino se tiene una estructura donde se apliquen buenas practicas de codificación.

Earlgrey es propuesto como una solución para mejorar la experiencia de los desarrolladores fabricando aplicaciones, que usan la arquitectura orientada a servicios y la arquitectura cliente servidor. Con Earlgrey, puedes desarrollar una API RESTful en menos de 5 minutos con muchas posibilidades que vienen embebidas en el framework como ORM, Cache, Sesiones, Configuraciones en Caliente, Consola de administración, Logs en tiempo real, entre otras.

Earlgrey fue diseñado a partir de la simplicidad y cada una de sus caracteristicas nacio de la necesidad de un grupo de desarrolladores que necesitaban escribir rapidamente aplicaciones en el lenguaje JAVA, que tuvieran una fuerte base que les permitiera desplegar la aplicacion en un contenedor de aplicaciones o donde quiera que ellos quisieran. Para esto, cada caractersitica fue diseñada con amor pensando en la simplicidad a la hora de programar. Queremos que escribas menos codigo para obtener grandes resultados a traves de una bella y amigable form de escribir codigo.

Para este fin el framework propone dos principios:

- El primero de ellos es **"One Framework"**, y propone que sin importar que uses para desplegar tu aplicación, el mismo codigo que fabriques funcionara en todos ellos. Si usas un contenedor como JBOSS, Tomcat, Weblogic, GlassFish, no hay ningun problema y puedes usar el que gustes, pero si decides usar el servidor embebido en earlgrey, no necesitas hacer ningun cambio, el mismo codigo te servira para ambos fines.

- El segundo es **"LTMWTM"** o **"Let the machine's work to machines"**. Estamos realmente obsecionados con escribir menos codigo, otorgandole parte de la responsabilidad a la maquina, para lo cual ejecutamos automaticamente multiples tareas, que ayudan al desarrollador en su tarea de construir aplicaciones. Earlgrey tiene muchas funciones de mapeo y reflexión entregan la información necesaria para proveer una gran consola de administración, donde los desarrolladores pueden configurar y extender las funciones de la app con funciones en tiempo real, recarga de properties sin reinicio, etc. Tambien, el nucleo de earlgrey provee muchas funcionalidades para escribir API's con interacción a base de datos, de una forma simple y rapida usando "Modelos con Blueprints", proporcionando una función similar a lo que entrega "Loopback" de IBM, usando una arquitectura orientada a modelos.

Earlgrey es adecuado para usar en cualquier tamaño de proyecto, pero es perfecto para usar en proyectos pequeños orientados a servicios que necesiten un desarrollo rapido o una implementación sencilla. Ademas Ealrgrey es perfecto para utilizar en adición con Angular, React o Vue.js, cuando necesitas usar como una API para comunicar Backend con Frontend. Es un secreto, pero Earlgrey fue originalmente hecho para construir aplicaciones con Angular y React, en donde necesitabamos que el frontend y el backend estuvieran en un solo un paquete "WAR o EAR" para generar deploys. 

**Prueba Earlgrey, y se parte de la nueva revolución de JAVA.**

## Compilación y distribución

Para compilar y empaquetar este proyecto necesitas maven. [Apache Maven](https://maven.apache.org/).

### Instalando Apache Maven

- Linux Debian Distribution
   
   ```bash
   $ sudo apt-get install mvn
   ```
- Linux Redhat Distribution
   
   ```bash
   $ sudo yum install mvn
   ```

### Configuraciónes en Maven

El proyecto utiliza como dependencia la libreria ODBC de la base de datos Oracle, para lo cual se debe configurar este repositorio en el maven local. Es importante señalar que el repositorio maven de Oracle requiere de autenticación, por lo cual se debera disponer de una cuenta en Oracle. Para mas información visita: [Configuring the Oracle Maven Repository](https://docs.oracle.com/middleware/1213/core/MAVEN/config_maven_repo.htm)

### Compilando y empaquetando Earlgrey

```bash
mvn clean package
```

### Compilando la consola

Earlgrey usa una consola de administración para controlar y configurar la operación del framework, actuando como un subsistema. La consola esta construida en Angular usando [Angular-seed](https://github.com/mgechev/angular-seed) y es inyectada en el jar mientras maven fabrica el paquete Earlgrey. Para lograr esto, necesitamos previamente compilar la consola, pero solo es necesario la primera vez mientras la consola no tenga cambios que afecten su funcionalidad. Para compilar Earlgrey junto a la consola se debe usar el siguiente comando.

```bash
mvn clean package -P console
```

## Comenzando con Earlgrey

Para usar Earlgrey solo necesitas importar el archivo JAR en tu proyecto web y inicializar el nucleo de Earlgrey en un Servlet Listener en el evento contextInitialized. El subsistema del framework automaticamente leera la estructura del proyecto y cargara las estructuras de Earlgrey para implementar un sistema ligero basado en una arquitectura orientada a servicios REST, con una consola de administración y opciones de configuración en caliente. Un ejemplo de lo señalado anteriormente es el siguiente.

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

## Controladores en Earlgrey

Escribir controladores y API's endpoints es muy sencillo en Earlgrey.
Solo necesitas escribir la clase del controlador en el package que tu elijas y extender la clase ControllerBase. Ademas, debes incluir la anotación "Controller". Earlgrey automaticamente reconocera el controlador y la mapeara para utilizarlo dentro del subsistema. Para declarar un controlador, se debe indicar el nombre, la descripción y la versión dell controlador en la anotación. un ejemplo de esto podria ser. 

```java
@Controller(description = "Un controlador de prueba", name = "HolaMundo", version = 1)
public class HelloWorld extends ControllerBase {
	/**
     * El codigo de las acciones de controlador va aqui
     */
}
```

Los controladores tienen opciones para extender su comportamiento, como rutas, cache, politicas, properties, etc. Si por ejemplo deseas definir un endpoint, solo necesitas usar la anotación @Route para asignarle una ruta.

```java
@Controller(description = "Un controlador de prueba", name = "HolaMundo", version = 1)
@Route(route = "/hello")
public class HelloWorld extends ControllerBase {
	/**
     * El codigo de las acciones de controlador va aqui
     */
}
```

### Acciones de controlador y enrutamiento.

En este caso, las acciones de controlador son una forma de escribir endpoints, variaciones RESTful o lo que necesites. Para definir una acción de controlador necesitas declarar la anotación @ControllerAction en un metodo estatico de ambito publico definido dentro del controlador. Earlgrey automaticamente reconocera esta acción y la mapeara. El metodo en todos A test of Controllerlos casos recibe dos parametros para gatillar una acción http. Un ejemplo de esto seria.

```java
@Controller(description = "Un controlador de prueba", name = "HolaMundo", version = 1)
@Route(route = "/hello")
public class HelloWorld extends ControllerBase {
    
    @ControllerAction(description = "Una acción de prueba para escribir hola mundo", name = "prueba", version = 1)
    public static void test(HttpRequest req, HttpResponse res) {
        System.out.println("Hello world");
        return;
    }
}
```

#### Rutas y acciones de controlador

Puedes añadir diferentes opciones para definir un endpoint. Todas las operacion RESTful esta soportadas y pueden ser utilizadas como una anotación, muy similar a como se hace en Jersey. Ademas puedes definir una ruta de contexto extra para esta acción con la anotación @Route como en los controladores. Las acciones soportan multiples declaraciones de metodos HTTP, pero no es muy recomendado si quieres usar las normas de API's semanticas. Un ejemplo podria ser.

```java
@Controller(description = "Un controlador de prueba", name = "HolaMundo", version = 1)
@Route(route = "/hello")
public class HelloWorld extends ControllerBase {
    
    @ControllerAction(description = "Una acción de prueba para escribir hola mundo", name = "prueba", version = 1)
    @Route(route = "/world")
    @GET
    public static void test(HttpRequest req, HttpResponse res) {
        System.out.println("Hello world");
        res.ok();
        return;
    }
}
```
Esta definición crea automaticamente un endpoint siguiendo la siguiente url.

```bash
[http/https]://[HOST]:[PORT]/CONTEXT/api/[CONTEXT ENDPOINT]/[ACTION ROUTE]
```
Para el controlador test y su acción la url estaria definida por.

```bash
[http/https]://[HOST]:[PORT]/CONTEXT/api/hello/world
```

#### Metodos RESTful soportados

Los siguientes metodos HTTP son soportados en Earlgrey. Si usas un contenedor de aplicaciones que no soporta alguno de los metodos, no hay problema amigo, porque Earlgrey efectua un bypass al servidor de aplicaciones y brinda soporte a tu app para utilizar el metodo para seguir las normas RESTful definidas en guias como [Restfull Cookbook](http://restcookbook.com) entre otras.

- [X] POST (@POST)
- [X] PUT (@PUT)
- [X] GET (@GET)
- [X] PATCH (@PATCH)
- [X] DELETE (@DELETE)
- [X] OPTIONS (@OPTIONS)

#### Soporte para cache

Earlgrey soporta cache para los controladores en dos niveles, el cache general usando la anotación @Cache o el cache por usuario usando la anotación @UserCache. El cache general es un cache global para todas las peticiones, mientras que el cache por usuario es un cache que solo afecta al usuario que lo genero. Ambas formas necesitan definir su tiempo de vida; Cuando el tiempo de vida llega a cero, Earlgrey automaticamente remueve el registro de memoria. Una vez que el cache es definido en una acción, queda a la espera de que sea gatillado. Para esto puede ser gatillado desde dentro de la aplicación por un Cronjob o en caso de que no exista ningun trigger, cuando el un cliente efectue la primera llamada a la acción, el cache tomara el resultado de la acción y automaticamente lo guardara en memoria. Las proximas peticiones tomaran el resultado directamente desde la memoria, mejorando drasticamente la velocidad de las respuestas.

La manera de usar el cache es la siguiente para el cache global.

```java
@Controller(description = "Un controlador de prueba", name = "HolaMundo", version = 1)
@Route(route = "/hello")
public class HelloWorld extends ControllerBase {
    
    @ControllerAction(description = "Una acción de prueba para escribir hola mundo", name = "prueba", version = 1)
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
La forma de usar el cache de usuario es la siguiente.

```java
@Controller(description = "Un controlador de prueba", name = "HolaMundo", version = 1)
@Route(route = "/hello")
public class HelloWorld extends ControllerBase {
    
    @ControllerAction(description = "Una acción de prueba para escribir hola mundo", name = "prueba", version = 1)
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

## Match ORM y sus funciones.

Earlgrey implementa un ORM ligero llamado Match para operar con bases de datos de una forma estandar. Este ORM funciona leyendo las estructuras definidas por los desarrolladores en los Modelos. Earlgrey reconoce los modelos y los mapea para interactuar con los controladores y la consola de configuración. Ademas, puedes definir "Blueprints models", los cuales implementan las operaciones CRUD en una API REST, basadas solamente en la definición del modelo. El ORM provee un set poderoso y reducido set de funciones para operar con las bases de datos, con soporte para consultas personalizadas, transacciones y multi tenancy.

### Bases de datos soportadas

* [X] Mysql
* [X] Oracle
* [X] Postgres

### Declaración de un Modelo

Para declarar un modelo se debe definir una clase que extienda de la clase ModelCore. Ademas se debe incluir la anotación descriptiva @Model para que Earlgrey pueda describirlo dentro de sus funciones de mapeo. Para declara un modelo sencillo se deben declarar campos, los cuales deben ser homologados al tipo de datos que desea alterar en la fuente de datos. Un ejemplo podria verse asi.

```java
@Model(name = "CATEGORIES", tableName = "CATEGORIES", version = 1)
public class Category extends ModelCore {
    @ModelField
    @PrimaryKey
    public Integer ID;
    @ModelField
    @Required
    public String NAME;
    @ModelField
    public String DESCRIPTION;
    @ModelField
    public String STATUS;
}
```
Como se puede apreciar, el modelo es definido como una clase con los atributos correspondientes. Earlgrey, utiliza todos los datos del ORM como objetos de tipo ModelCore. Cada atributo es una columna de una tabla en el origen de datos, dentro de las cuales es conveniente definir y designar cual de esta es la llave primaria o identificador. Todos los atributos deben llevar la anotación @ModelField para ser reconocidos como una columna en el origen de datos.

### Opciones declarativas

Matcha provee un juego de anotaciones que permite definir y extender los modelos de datos. Para esto, se utilizan las siguientes anotaciones.

* **@ModelField**: Es la anotación por defecto utilizada para declarar un atributo de un modelo de datos.
* **@Require**: La anotación Require indica que un atributo que posee la anotación ModelField es requerido para efectuar operación de tipo insert. Cuando un modelo es definido como Blueprint automaticamente se convierte en un parametro requerido.
* **@DefaultValue**: La anotación DefaultValue define un valor por defecto para un atributo. La anotación @DefaultValue es incompatible con la anotación @Require.
* **@Unique**: La anotación unique es utilizada para definir un campo que tiene como restricción que es de tipo unico. Es una anotación declarativa y se utiliza para la construcción de modelos. No es compatible con la anotación @Defaultvalue.
* **@AutoIncrement**: Permite que la columna sube incrementalmente su valor fila tras fila, para atributos que tienen definido tipos numericos. Al igual que la anotación @Unique, es una propiedad declarativa y no es compatible con la anotación @DefaultValue.
* **@PrimaryKey**: Esta anotación define la llave primaria de un modelo de datos. Solo puede existir una llave primaria por modelo de datos. La llave primaria debe ser un @Modelfield como todos los atributos.
* **@MultiTenant**: Es una anotación utilizada a nivel de modelo de datos para definir que este es de tipo multitenant. Un modelo de tipo multitenant no esta asociado a un datasource en particular, si no que tiene datasources dinamicos dependiendo el usuario que utiliza el modelo de datos.
* **@Route**: La anotación route es utilizada para definir un modelo de datos como Blueprint. Esto quiere decir que en la ruta especificada se suscribe un endpoint capaz de proveer las operaciones CRUD. Para mas información vease Modelos Blueprints. 
* **@ModelJoin**: Es utilizado para definir que un campo del modelo actual se relaciona con el campo de otro modelo. Debe ser utilizado sobre un atributo previamente definido como @ModelField.
* **@ModelRelation**: Es utilizado para definir campos en un modelo que pertenecen a otro modelo de datos que fue previamente asociado con un @ModelJoin.

### Modelos Blueprints 

Para definir un "Blueprint Model" solo es necesario agregar la anotación @Route, con esto Earlgrey automaticamente lo reconocera como un Endpoint CRUD en la ruta especificada. Los blueprint model contienen el siguiente set de funciones.

* **Get all data (GET):** Efectua una operación find recibiendo como parametro los campos que se deseen usar como criterio de busqueda. si es definido como criterio de bsuqueda un campo que no esta definido en el modelo, sera ignorado. retornara el codigo HTTP 200 [Ok](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/200) y los objetos del modelo si la petición es correcta.

```
curl -X GET [http/https]://[HOST]:[PORT]/CONTEXT/api/route?params
```
* **Get singular data (GET):** Efectua una operación findOne recibiendo como parametro de ruta el campo id. Retornara el codigo HTTP 200 [Ok](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/200) y el objeto que tenga como PrimaryKey el valor definido. Para esto es necesario que este definido el un atributo como @PrimaryKey, o retornara un error 400 [Bad Request](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400).

```
curl -X GET [http/https]://[HOST]:[PORT]/CONTEXT/api/route/:id
```

* **Insert (POST):** Efectua una operación insert recibiendo como parametro los campos que se desean agregar. Retornara el codigo 201 [Created](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/201) y el objeto creado, si la petición es exitosa. Si existe algun atributo definido como required y no es incluido en la petición, retornara un error 422 [Unprocessable Entity](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/422)

```
curl -H 'Content-Type: application/json' -X POST  -d '{JSON OBJECT}'
[http/https]://[HOST]:[PORT]/CONTEXT/api/route
```

* **Update (PATCH):** Efectua una operación update recibiendo como parametro de ruta el campo id. Retornara el codigo HTTP 200 [Ok](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/200) y el objeto que tenga como PrimaryKey el valor definido. Para esto es necesario que este definido el un atributo como @PrimaryKey, o retornara un error 400 [Bad Request](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400).

```
curl -H 'Content-Type: application/json' -X PATCH -d '{JSON OBJECT}'
[http/https]://[HOST]:[PORT]/CONTEXT/api/route/:id
```

* **Destroy (DELETE):** Efectua una operación delete recibiendo como parametro de ruta el campo id. Retornara el codigo HTTP 204 [No Content](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/204) en caso de ser exitoso. Para esto es necesario que este definido el un atributo como @PrimaryKey, o retornara un error 400 [Bad Request](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400).

```
curl -X DELETE [http/https]://[HOST]:[PORT]/CONTEXT/api/route/:id
```
### Operaciones ORM soportadas

* [X] find()
* [X] findOne()
* [X] match()
* [X] insert()
* [X] delete()
* [X] delete(Integer id)
* [X] update()
* [X] update(Integer id)
* [X] count()

## Consola Ealgrey

Para administrar Earlgrey, solamente necesitas un navegador web. Earlgrey provee un interfaz de administrador para configurar las principales posibilidades. En esta consola, puedes configurar properties, controladores, modelos, rutas, ver logs, configurar la disposición en cluster y todo lo que neceitas para generar una gran app.

Para acceder a la consola solo necesitas ingresar en tu navegador la siguiente url.

```bash
[http/https]://[HOST]:[PORT]/CONTEXT/console/
```

Si es tu primera vez, utiliza las siguientes credenciales.

```bash
Username: admin
Password: earlgrey
```

Por ejemplo, si necesitas acceder a la consola de una aplicación ubicada en el dominio www.test.com ubicada en el puerto 8080, con el contexto de aplicación testapp, sin un certificado ssl, la url podria ser.

```bash
http://www.test.com:8080/testapp/console/
```

Si utilizadas el puerto 80, puedes omitir el puerto. El ejemplo quedaria como el siguiente.

```bash
http://www.test.com/testapp/console/
```


## Earlgrey Seed

Usar Earlgrey es simple con el arquetipo comun desarrollado por nosotros. Solo necesitas escribir tus controladores y modelos para crear rapidamente robustas API's, basadas en arquitectura REST. Earlgrey provee soporte RESTful y la posibilidad de escribir "Blueprints Models" para obtener operaciones CRUD con un minimo de esfuerzo y escribiendo muy poco codigo JAVA. Ademas, puedes extender el arquetipo para crear grandes proyectos en tecnologia JAVA con tipos, errores y politicas personalizadas y un gran conjunto de operaciones en la consola earlgrey.  

Visita [Earlgrey Seed](https://github.com/acalvoa/Ealgrey_seed) Para obtener mas información.

## Earlgrey CLI

Earlgrey provee un CLI llamado "Infusión", que permite no solo crear nuevos proyectos a partir de un arquetipo, sino tambien automatizar gran parte de las tardes de creación de nuevos componentes y elementos en earlgrey, bajo normas estructuradas. Su uso es altamente recomendado en proyectos donde se requieren normas y estructuras de directorio para definir componentes. 

Visita [Earlgrey CLI](https://github.com/brutalchrist/earlgrey-cli) para obtener mas información.

## Contribuidores

Gracias a todas las hermosas personas que hacen posible este proyecto.

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore -->
| [<img src="https://avatars3.githubusercontent.com/u/806569?s=400&v=4" width="100px;"/><br /><sub><b>Sebastian Gonzalez V.</b></sub>](https://github.com/brutalchrist)<br />[📖](https://github.com/acalvoa/EARLGREY/commits?author=brutalchrist "Documentation") [💻](https://github.com/acalvoa/EARLGREY/commits?author=brutalchrist) | [<img src="https://avatars3.githubusercontent.com/u/20507724?s=460&v=4" width="100px;"/><br /><sub><b>Pablo Jeldres</b></sub>](https://github.com/pjeldres)<br />[🐛](https://github.com/acalvoa/earlgrey/issues/created_by/pjeldres "Bug reports") | [<img src="https://avatars0.githubusercontent.com/u/31521040?s=460&v=4" width="100px;"/><br /><sub><b>David Silva</b></sub>](https://github.com/dsilvap)<br />[🐛](https://github.com/acalvoa/earlgrey/issues/created_by/dsilvap "Bug reports") [💻](https://github.com/acalvoa/EARLGREY/commits?author=dsilvap)|
|:----:|:----:|:----:|
<!-- ALL-CONTRIBUTORS-LIST:END -->

**Hecho con ♥ en Chile por todos nosotros.**

## LICENCIA

MIT

[license-badge]: https://img.shields.io/npm/l/all-contributors.svg?style=flat-square