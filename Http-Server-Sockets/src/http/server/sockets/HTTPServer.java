/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http.server.sockets;

/**
 *
 * @author Mohamed_El_Laithy
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class HTTPServer {
    public static ArrayList<Socket> ConnectionArray = new ArrayList<>();

    public static final int PORT = 8080;
    public static final String ROOT_FOLDER = "www";
    public static final String[] SUPPORTED_METHODS = {"GET", "POST"};
    public static final ArrayList<String> IMAGES_EXTENSIONS = createImageExtensions();
    public static final ArrayList<String> TEXT_EXTENSIONS = createTextExtensions();

    /**
     * generate a list of the supported file extensions
     *
     * @return Arraylist<String>
     */
    public static ArrayList<String> createTextExtensions() {
        ArrayList<String> text = new ArrayList<>();
        text.add("html");
        text.add("css");
        text.add("js");
        text.add("txt");
        return text;
    }


    /**
     * generate a list of the supported images extensions
     *
     * @return Arraylist<String>
     */
    private static ArrayList<String> createImageExtensions() {
        ArrayList<String> images = new ArrayList<>();
        images.add("jpg");
        images.add("png");
        images.add("gif");
        images.add("ico");
        return images;
    }

    /**
     * opens the socket
     * block waiting for every new request
     * when a new request comes, accept it and pass it to new thread to handle it
     *
     * @throws IOException
     */
    public void startChatServer() throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("waiting for Clients ");
        while (true){
            // listn for new Connections
            Socket socket = server.accept();
            ConnectionArray.add(socket);
            System.out.println("Client Connected From : " +socket.getLocalAddress().toString());

            //start a new handler for each new user to serve more users at the same time
            Handler handler = new Handler(socket);
            Thread X = new Thread(handler);
            X.start();
        }
    }

    public static void main(String[] args) throws IOException {
        //starts the server non-static
        new HTTPServer().startChatServer();
    }
}
