package geos.gateway;

import geos.core.Packet;

public class Gateway extends Thread {
	/*ESTANDARIZAMOS LOS ELEMENTOS DE ENTRADA TANTO PARA EL CANAL WEBSOCKET COMO PARA EL CANAL REST*/
	private Rest rest;
	private Websocket websocket;
	/*INICIALIZAMOS LOS CONSTRUCTORES*/
	public Gateway(){
		
	}
	public Gateway(Websocket websocket){
		this.websocket = websocket;
	}
	public Gateway(Rest rest){
		this.rest = rest;
	}
	public void response(Packet p){
		
	}
	public void responsePacket(Packet p){
		
	}
	public void kill(){
		
	}
	
}
