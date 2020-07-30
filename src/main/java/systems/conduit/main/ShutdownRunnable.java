package systems.conduit.main;

public class ShutdownRunnable implements Runnable {

    @Override
    public void run() {
        Conduit.getPluginManager().disablePlugins();
        System.exit(1);
    }
}
