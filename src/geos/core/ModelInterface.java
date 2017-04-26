package geos.core;

public interface ModelInterface {
	public void create(Request req, Response res);
	public void find(Request req, Response res);
	public void findOne(Request req, Response res);
	public void update(Request req, Response res);
	public void destroy(Request req, Response res);
	void setModel(Class<ModelCore> modelo);
}
