package earlgrey.core;

import earlgrey.annotations.ControllerAction;
import earlgrey.interfaces.ModelInterface;

public class ControllerCore implements ModelInterface{
	// DEFINIMOS LAS CLASES RESTFULL EN CASO DE ASOCIACION DE UN MODELO DE DATOS
	// AL ASOCIAR UN MODELO DE DATOS A UN CONTROLADOR GENERICO O CUSTOMIZADO
	// GANA LA CAPACIDAD DE EJECUTAR OPERACIONES RESTFULL SOBRE DICHO MODELO DE DATOS
	// EN ESE CONTEXTO SE PROVEEN DE FUNCIONES CRUD
	// ES IMPORTANTE DEFINIR QUE ES POSIBLE INDICAR AL CONTROLADOR CUSTOMIZADO QUE FUNCIONES RESTFULL ESTAN DISPONBILE A TRAVES DE LAS ANOTACIONES
	// POR DEFECTO UN CONTROLADOR PROVEE TODOS LOS METODOS DISPONBILES
	
	//DECLARAMOS EL MODELO DE DATOS INVOLUCRADO
	protected Class<ModelCore> modelo;
	//DECLARAMOS LAS ACTIONS
	@Override
	public void create(HttpRequest req, HttpResponse res) {
		if(modelo == null) res.notAllowed();
	}

	@Override
	// METODO CREATE SOLO ESTA DISPONIBLE SI EL CONTROLADOR PROVEE EL METODO GET
	public void find(HttpRequest req, HttpResponse res) {
		if(modelo == null) res.notAllowed();
	}

	@Override
	// METODO CREATE SOLO ESTA DISPONIBLE SI EL CONTROLADOR PROVEE EL METODO GET
	public void findOne(HttpRequest req, HttpResponse res) {
		if(modelo == null) res.notAllowed();
	}

	@Override
	// METODO CREATE SOLO ESTA DISPONIBLE SI EL CONTROLADOR PROVEE EL METODO PUT
	public void update(HttpRequest req, HttpResponse res) {
		if(modelo == null) res.notAllowed();
	}

	@Override
	// METODO CREATE SOLO ESTA DISPONIBLE SI EL CONTROLADOR PROVEE EL METODO DELETE
	public void destroy(HttpRequest req, HttpResponse res) {
		if(modelo == null) res.notAllowed();
	}

	@Override
	public void setModel(Class<ModelCore> modelo) {
		this.modelo = modelo;
	}

}
