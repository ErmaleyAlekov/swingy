package app;

public class Singleton {
    private Singleton() {}
    private static volatile Controler instance;
    public static Controler getInstance() {
        Controler localInstance = instance;
        if (localInstance == null) {
			synchronized (Controler.class) {
				localInstance = instance;
				if (localInstance == null) {
					instance = localInstance = new Controler();
				}
			}
		}
        return localInstance;
    }
}
