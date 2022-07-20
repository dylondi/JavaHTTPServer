import threading.RequestListenerThread;

public class Main {

    /**
     * Creates a RequestListenerThread on port 8080 and with an empty webroot. Then starts thread.
     * @param args
     */
    public static void main(String[]args){

        RequestListenerThread requestListenerThread = new RequestListenerThread(Integer.parseInt(args[0]));
        requestListenerThread.start();
    }
}
