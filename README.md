# Earlgrey
![tea love](https://raw.githubusercontent.com/acalvoa/EARLGREY/extra/resources/Earl_Grey.jpg)

A Lightweight Java Services Framework inspired in Nodejs Express, Sails.js &amp; Phalcon PHP for quick develop of apps based in 
client server architecture. Build apps with frontend frameworks like Angular and Reactjs. Code and build faster and leave the machine's work to machines.

## Requisitos 

TO DO

## Compilación y distribución

Para la compilación y distribución del framework se utiliza la herramienta [Apache Ant](http://ant.apache.org/).

### Instalar Apache Ant

- Linux
   
   ```bash
   $ sudo apt-get install ant
   ```

### Uso

```bash
$ ant -p
Buildfile: /home/sgonzalezvi/git/earlgrey/build.xml

Main targets:

 clean    Clean up
 compile  Compile the source
 dist     Generate the distribution
Default target: dist

```

### Compilar

```bash
$ ant compile
$ ll build
total 20K
drwxrwxr-x  3 sgonzalezvi sgonzalezvi 4,0K jul  4 18:28 com
drwxrwxr-x 15 sgonzalezvi sgonzalezvi 4,0K jul  4 18:28 earlgrey
drwxrwxr-x  3 sgonzalezvi sgonzalezvi 4,0K jul  4 18:28 javax
drwxrwxr-x  9 sgonzalezvi sgonzalezvi 4,0K jul  4 18:28 oracle
drwxrwxr-x  6 sgonzalezvi sgonzalezvi 4,0K jul  4 18:28 org

```

### Distribuir

```bash
$ ant dist
$ ll dist 
total 5,6M
-rw-rw-r-- 1 sgonzalezvi sgonzalezvi 5,6M jul  4 18:28 earlgrey.jar
```


