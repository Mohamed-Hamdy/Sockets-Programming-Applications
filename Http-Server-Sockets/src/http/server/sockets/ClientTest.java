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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientTest {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost",8080);
            OutputStream os = socket.getOutputStream();
            os.write("GET /image.jpg".getBytes());
            InputStream inputStream = socket.getInputStream();
            int a;
            StringBuilder builder = new StringBuilder();
            while ((a = inputStream.read()) > -1){
                builder.append((char) a);
            }
            System.out.println(builder.toString());
            inputStream.close();
            os.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
