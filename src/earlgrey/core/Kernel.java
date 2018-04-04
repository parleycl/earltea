package earlgrey.core;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;
import earlgrey.*;
import earlgrey.error.Error700;
import earlgrey.gateway.Rest;

public class Kernel{
	// LOGGING MAP
	private Logging log;
	private static Kernel instance = null;
	public String kernelname;
	public String rootPackageName;
    Properties properties;
    Communication com;
    ResourceMaping resources;
    DatasourceManager linkpool;
    Engine taskmanager;
    Session session;
    ServletContext context;
    // CONSTRUCTOR DEL KERNEL DE APLICATIVO
    public Kernel(ServletContext context) {
    	this.rootPackageName = Thread.currentThread().getStackTrace()[2].getClassName().split("\\.")[0];
    	instance = this;
    	this.context = context;
    	this.kernelname = "kernel_"+this.context.getContextPath().substring(1);
    	this.log = new Logging(this.getClass().getName());
    	if(this.checkFileSystem()){
    		this.setAutors();
			this.resources = new ResourceMaping(this.rootPackageName);
    		this.taskmanager = new Engine();
        	this.com = new Communication();
        	this.properties = new Properties();
        	this.linkpool = new DatasourceManager();
        	this.session = new Session();
        	this.endLoading();
    	}
    	else
    	{
    		this.log.Critic("El aplicativo no tiene permisos para escribir en disco, favor corregir.", Error700.FILESYSTEM_WRITE_ERROR);
    	}
    }
	public static synchronized Kernel getInstance(){
		return instance;
	}
    private void setAutors(){
    	this.log.Info("INICIALIZANDO SISTEMA EARLGREY.");
    	this.log.Info("********************************************************");
    	this.log.Info("");
    	this.log.Info("****************************************************************************************");
    	this.log.Info("*     _______   ______  _______   __      _______    ______   _______  __      __      *");
    	this.log.Info("*    |   ____| |  __  | |  __  | |  |    |   ____|  | ___  | |  _____||  |    |  |     *");
    	this.log.Info("*    |  |      | |  | | | |  | | |  |    |  |       | |  | | | |       |  |  |  |      *");
    	this.log.Info("*    |  |___   | |__| | | |__| | |  |    |  |  ___  | |__| | | |____    |  ||  |       *");
    	this.log.Info("*    |   ___|  |  __  | | __  _| |  |    |  | |   | |   ___| |  ____|    |    |        *");
    	this.log.Info("*    |  |      | |  | | | | |_|  |  |    |  | |_  | | |_|    | |          |  |         *");
    	this.log.Info("*    |  |____  | |  | | | | |_|  |  |___ |  |___| | | | |_|  | |_____     |  |         *");
    	this.log.Info("*    |_______| |_|  |_| |_|  |_| |______||________| |_|  |_| |_______|    |__|         *");
    	this.log.Info("*                                                                                      *");
    	this.log.Info("*    BACKEND SYSTEM                                                                    *");
    	this.log.Info("****************************************************************************************");
    	this.log.Info("");
    	this.log.Info("Version : 0.5 Alpha");
    	this.log.Info("System Architect : Angelo Alejandro Calvo Alfaro");
    	this.log.Info("Code by : Angelo Alejandro Calvo Alfaro, Sebastian Gonzalez Villena");
    	this.log.Info("");
    	this.log.Info("");
    	this.log.Info("LICENCE : MIT");
    	this.log.Info("Copyright 2017 - Contraloria General de la Republica de Chile");
    	this.log.Info("Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the 'Software'), to deal in the");
    	this.log.Info("Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, ");
    	this.log.Info("and to permit persons to whom the Software is furnished to do so, subject to the following conditions:");
    	this.log.Info("");
    	this.log.Info("The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.");
    	this.log.Info("THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF ");
    	this.log.Info("MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ");
    	this.log.Info("ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH ");
    	this.log.Info("THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.");
    	this.log.Info("");
    	this.log.Info("********************************************************");
    	this.log.Info("");
    	this.log.Info("EJECUTANDO TAREAS.");
    	this.log.Info("********************************************************");
    }
    private void endLoading(){
    	this.log.Info("");
    	this.log.Info("********************************************************");
    	this.log.Info("SISTEMA EARLGREY INICIALIZADO.");
    	this.log.Info("********************************************************");
    	this.log.Info("ESPERANDO PETICIONES");
    	this.log.Info("********************************************************");
    }
    public boolean checkFileSystem() {
    	// CHEQUEAMOS Y CREAMOS LAS CARPETAS
    	this.log.Info("Verificando sistema de archivos");
    	File geos = new File(this.kernelname);
    	File logs = new File(this.kernelname+"/logs");
    	File properties = new File(this.kernelname+"/properties");
    	File backups = new File(this.kernelname+"/backups");
    	File model = new File(this.kernelname+"/model");
    	File controller = new File(this.kernelname+"/controller");
    	File policies = new File(this.kernelname+"/policies");
    	//CHEQUEAMOS
    	boolean status = true;
    	if(!geos.exists()) status = geos.mkdir();
    	if(status && !logs.exists()) status = logs.mkdir(); 
    	if(status && !properties.exists()) status = properties.mkdir(); 
    	if(status && !backups.exists()) status = backups.mkdir(); 
    	if(status && !model.exists()) status = model.mkdir(); 
    	if(status && !controller.exists()) status = controller.mkdir();
    	if(status && !policies.exists()) status = policies.mkdir();
    	return status;
    }
}
