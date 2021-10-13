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

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;

public class Handler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader reader;
    private String message;


    public Handler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            message = reader.readLine();
            String method = message.split(" ")[0];
            String filePath = message.split(" ")[1];

            if (filePath.equals("/") || filePath.isEmpty())
                filePath = "/index.html";

            //handle get requests
            if (method.equals(HTTPServer.SUPPORTED_METHODS[0])) {
                System.out.println("requesting: " + filePath);

                // check if the file exists
                // if not send a 404 response
                if (!(new File(HTTPServer.ROOT_FOLDER + filePath)).exists()) {
                    String response = generateTextHeader(false, filePath);
                    PrintWriter pw = new PrintWriter(out);
                    pw.print(response);
                    pw.close();
                }


                // handle text files requests
                if (HTTPServer.TEXT_EXTENSIONS.contains((filePath.split("[.]")[1]).toLowerCase())) {
                    String response = generateTextHeader(true, filePath);
                    PrintWriter pw = new PrintWriter(out);
                    pw.print(response);
                    pw.close();
                }

                // handle images
                else if (HTTPServer.IMAGES_EXTENSIONS.contains((filePath.split("[.]")[1]).toLowerCase())) {
                    FileInputStream is = new FileInputStream(HTTPServer.ROOT_FOLDER + filePath);
                    OutputStream os = socket.getOutputStream();
                    String header = generateHeader(true, (new File(HTTPServer.ROOT_FOLDER + filePath).length()));
                    int a;
                    // write the header
                    for (char c : header.toCharArray()) {
                        os.write(c);
                    }
                    // write the image data
                    while ((a = is.read()) > -1) {
                        os.write(a);
                    }
                    os.flush();
                    is.close();
                    os.close();
                }

            }

            // handle post requests
            else if (HTTPServer.SUPPORTED_METHODS[1].equals(method)) {

                // just a consumer
                while (!(message = reader.readLine()).equals(""));


                StringBuilder builder = new StringBuilder();
                while (reader.ready())
                    builder.append((char) reader.read());

                // decode the url -- convert the special characters
                System.out.println(URLDecoder.decode(builder.toString(), "UTF-8"));

                String response = generatePostResponse(URLDecoder.decode(builder.toString(), "UTF-8"));
                out.write(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
            try {
                reader.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * return a 200 response header + the file data if the file already exists --OK is true
     * or a 404 response + the file data if the file does not exists --OK == false
     *
     *
     * @param OK is the file exists or not
     * @param filepath the file path
     * @return string
     * @throws IOException
     */
    private String generateTextHeader(boolean OK, String filepath) throws IOException {
        if (!OK) {
            return "HTTP/1.1 404 \r\n" + "Apache Server /1.0";
        }

        File returnFile = new File(HTTPServer.ROOT_FOLDER + filepath);
        FileInputStream fileInputStream = new FileInputStream(returnFile);

        String response = "";
        response += "HTTP/1.1 200 \r\n";
        response += "Apache Server /1.0";
        response += "Content-Type:TEXT \r\n";
        response += "Connection: close \r\n";
        response += "Content-Length:" + returnFile.length() + "\r\n";
        response += "\r\n";

        int s;
        while ((s = fileInputStream.read()) != -1) {
            response += (char) s;
        }

        fileInputStream.close();
        return response;
    }


    /**
     * returns an abstract header
     *
     * @param OK is the file exists or not
     * @param length the content length
     * @return String: a 200 header if OK = true or 404 reader if OK = false
     * @throws IOException
     */
    private String generateHeader(boolean OK, long length) throws IOException {
        if (!OK) {
            return "HTTP/1.1 404 \r\n";
        }

        String response = "";

        response += "HTTP/1.1 200 \r\n";
        response += "Apache Server /1.0";
        response += "Content-Type:TEXT/html \r\n";
        response += "Connection: close \r\n";
        response += "Content-Length:" + length + "\r\n";
        response += "\r\n";

        return response;
    }


    /**
     * return the header + html response
     * TODO: replace the += operator by a string Builder to improve performance
     *
     *
     * @param url the decoded parameters
     * @return String
     */
    private String generatePostResponse(String url) {
        String response = "";

        response += "HTTP/1.1 200 \r\n";
        response += "Apache Server /1.0\n";
        response += "Content-Type:TEXT/html \r\n";
        response += "Connection: close \r\n";
        response += "\r\n";

        response += "<html><body><div><h1>you added this values</h1><p>";

        HashMap<String, String> values = new HashMap<>();

        try {
            String decoded = java.net.URLDecoder.decode(url, "UTF-8");
            String[] pairs = decoded.split("&");
            for (String pair : pairs) {
                //values.put(pair.split("=")[0], pair.split("=")[1]);
                response += pair + "<br>";
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        response += "</p></div></body></html>";
        System.out.println(response);

        return response;
    }

}
