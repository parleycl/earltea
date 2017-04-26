package geos.interfaces;

import geos.core.ModelCore;
import geos.core.HttpRequest;
import geos.core.HttpResponse;

public interface ModelInterface {
	public void create(HttpRequest req, HttpResponse res);
	public void find(HttpRequest req, HttpResponse res);
	public void findOne(HttpRequest req, HttpResponse res);
	public void update(HttpRequest req, HttpResponse res);
	public void destroy(HttpRequest req, HttpResponse res);
	void setModel(Class<ModelCore> modelo);
}
