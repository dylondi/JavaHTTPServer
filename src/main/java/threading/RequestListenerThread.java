package threading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.StringsRepository;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Used to listen for new connections to server. Once a connection is established,
 * it is processed on another thread using RequestProcessingRunnable
 */
public class RequestListenerThread extends Thread{

    private int port;
    private ServerSocket serverSocket;
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestListenerThread.class);

    /**
     * Constructor
     * @param port
     * @param
     */
    public RequestListenerThread(int port) {
        this.port = port;
        try {
            this.serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Listens for new requests. Once a connection is established, a RequestProcessingRunnable object
     * is executed on a CachedThreadPool defined at start of method. The RequestProcessingRunnable
     * object is responsible for the processing of each connection. This method is just responsible
     * for accepting new connections.
     */
    @Override
    public void run() {

        try{

            ExecutorService pool = Executors.newCachedThreadPool();
            while(!serverSocket.isClosed() && serverSocket.isBound()) {
                Socket socket = serverSocket.accept();

                LOGGER.info(StringsRepository.NEW_CONNECTION_ACCEPTED + socket.getInetAddress());

                RequestProcessingRunnable req = new RequestProcessingRunnable(socket);
                pool.execute(req);
            }

        } catch(IOException e) {
            e.printStackTrace();
            LOGGER.error(StringsRepository.ISSUE_WITH_SOCKET, e);
        } finally {
            if(serverSocket!=null){
                try {
                    serverSocket.close();
                } catch (IOException e) {

                }
            }
        }
    }
}
