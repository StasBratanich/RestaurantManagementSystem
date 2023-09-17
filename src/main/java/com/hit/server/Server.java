package com.hit.server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private final int port;
    private boolean isRunning;

    public Server(int port) {
        this.port = port;
        this.isRunning = false;
    }

    public void run() {
        try {
            java.net.ServerSocket serverSocket = new java.net.ServerSocket(port);
            System.out.println("Server started on port " + port);
            isRunning = true;

            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                HandleRequest handleRequest = new HandleRequest(clientSocket);
                handleRequest.start();
            }

            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Error occurred while running the server: " + e.getMessage());
        }
    }

    public void stop() {
        isRunning = false;
        System.out.println("Server is closed");
    }

    private class HandleRequest extends Thread {
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
                // Read client request
                String request = reader.nextLine();
                System.out.println("Received request from client: " + request);

                // Process the request and generate a response
                String response = processRequest(request);

                // Send the response back to the client
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

    public static void main(String[] args) {
        int port = 1234;
        Server server = new Server(port);
        server.run();
    }
}


