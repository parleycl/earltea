package earlgrey.database;

public interface DatabaseDriver {
	public void find();
	public void update();
	public void delete();
	public void create();
	public void count();
}
