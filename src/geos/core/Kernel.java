package geos.core;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import geos.admin.Communication;
import geos.error.Error700;

@WebListener
public class Kernel implements ServletContextListener {
	// LOGGING MAP
	private Logging log = new Logging(this.getClass().getName());
    Properties properties;
    Communication com;
    ResourceMaping resources;
    DatabasePool linkpool;
    Engine taskmanager;
    public Kernel() {
    	if(this.checkFileSystem()){
    		this.setAutors();
    		this.resources = new ResourceMaping();
    		this.taskmanager = new Engine();
        	this.com = new Communication();
        	this.properties = new Properties();
        	this.linkpool = new DatabasePool();
        	this.endLoading();
    	}
    	else
    	{
    		this.log.Critic("El aplicativo no tiene permisos para escribir en disco, favor corregir.", Error700.FILESYSTEM_WRITE_ERROR);
    	}
    }
    private void setAutors(){
    	this.log.Info("INICIALIZANDO SISTEMA GEOS.");
    	this.log.Info("********************************************************");
    	this.log.Info("");
    	this.log.Info("********************************************************");
    	this.log.Info("*     _________    _______    _______    ________      *");
    	this.log.Info("*    |  _______|  |  _____|  |   __  |  |  ______|     *");
    	this.log.Info("*    | |          |  |       |  |  | |  | |            *");
    	this.log.Info("*    | |  ______  |  |__     |  |  | |  | |______      *");
    	this.log.Info("*    | | |____ |  |   __|    |  |  | |  |______  |     *");
    	this.log.Info("*    | |     | |  |  |       |  |  | |         | |     *");
    	this.log.Info("*    | |_____| |  |  |____   |  |__| |   ______| |     *");
    	this.log.Info("*    |_________|  |_______|  |_______|  |________|     *");
    	this.log.Info("*                                                      *");
    	this.log.Info("*    BACKEND SYSTEM                  BY: CGR 2017      *");
    	this.log.Info("********************************************************");
    	this.log.Info("");
    	this.log.Info("Version : 0.5 Alpha");
    	this.log.Info("System Architech : Angelo Alejandro Calvo Alfaro");
    	this.log.Info("Code by : Angelo Alejandro Calvo Alfaro");
    	this.log.Info("Team Leader : Maria Alejandra Levill Leal");
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
    	this.log.Info("SISTEMA GEOS INICIALIZADO.");
    	this.log.Info("********************************************************");
    	this.log.Info("ESPERANDO PETICIONES");
    	this.log.Info("********************************************************");
    }
    public boolean checkFileSystem() {
    	// CHEQUEAMOS Y CREAMOS LAS CARPETAS
    	this.log.Info("Verificando sistema de archivos");
    	File geos = new File("kernel");
    	File logs = new File("kernel/logs");
    	File properties = new File("kernel/properties");
    	File backups = new File("kernel/backups");
    	File model = new File("kernel/model");
    	File controller = new File("kernel/controller");
    	File policies = new File("kernel/policies");
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
	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
        // TODO Auto-generated method stub
    	
    }
	
}
