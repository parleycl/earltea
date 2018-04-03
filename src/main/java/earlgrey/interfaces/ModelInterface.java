package earlgrey.interfaces;

import earlgrey.core.HttpRequest;
import earlgrey.core.HttpResponse;
import earlgrey.core.ModelCore;

public interface ModelInterface {
	public void create(HttpRequest req, HttpResponse res);
	public void find(HttpRequest req, HttpResponse res);
	public void findOne(HttpRequest req, HttpResponse res);
	public void update(HttpRequest req, HttpResponse res);
	public void destroy(HttpRequest req, HttpResponse res);
	void setModel(Class<ModelCore> modelo);
}
