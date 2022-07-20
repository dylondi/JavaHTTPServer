package threading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.StringsRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

/**
 * Sends the response for each request processed in RequestListenerThread
 */
public class RequestProcessingRunnable implements Runnable{

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestProcessingRunnable.class);

    private Socket socket;

    /**
     * Constructor
     * @param socket
     */
    public RequestProcessingRunnable(Socket socket) {
        this.socket = socket;
    }

    /**
     * Sends response to OutputStream object containing random 32 character HEX value
     */
    @Override
    public void run() {

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            String hex = "0x" + getRandomHexString();

            String html = "<html><head><title>Java HTTP Server</title></head><body><h1>" + hex + "</h1></body></html>";

            final String CRLF = "\r\n";

            String response =
                    "HTTP/1.1 200 OK" + CRLF +
                            "Content-Length: " + html.getBytes().length + CRLF +
                            CRLF +
                            html +
                            CRLF + CRLF;

            outputStream.write(response.getBytes());



        } catch (IOException e) {
            LOGGER.error(StringsRepository.ISSUE_WITH_COMMUNICATION, e);
            e.printStackTrace();
        } finally {

            if(inputStream!=null) {
                try {
                    inputStream.close();

                } catch (IOException e) {}
            }
            if(outputStream!=null) {
                try {
                    outputStream.close();
                } catch (IOException e) {}
            }
            if(socket!=null) {
                try {
                    socket.close();
                } catch (IOException e) {}
            }
        }

        LOGGER.info(StringsRepository.CONNECTION_PROCESSING_COMPLETE);
    }


    /**
     * Generates a random hexadecimal number as a 32 character String
     * @return
     */
    private String getRandomHexString(){

        LOGGER.info(StringsRepository.GENERATING_RANDOM_HEX);

        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < 32){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, 32);
    }
}
