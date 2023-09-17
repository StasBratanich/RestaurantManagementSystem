package com.hit.server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class HandleRequest extends Thread {
    private final Socket clientSocket;
    private final Scanner reader;
    private final PrintWriter writer;

    public HandleRequest(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.reader = new Scanner(new InputStreamReader(clientSocket.getInputStream()));
        this.writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
    }

    @Override
    public void run() {
        try {
            String request = reader.nextLine();
            System.out.println("Received request from client: " + request);

            String response = processRequest(request);

            writer.println(response);
            writer.flush();
            reader.close();
            writer.close();
            clientSocket.close();

        } catch (IOException e) {
            System.out.println("Error occurred while handling client request: " + e.getMessage());
        }
    }

    private String processRequest(String request) {
        return "Response to client: " + request;
    }
}
