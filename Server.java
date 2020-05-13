package com.barcode.rfd.Adapter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by prashant.chaudhary on 7/1/2016.
 */
public class Server{

    public static void main(String args[]) {
        ServerSocket serverSocket = null;
        Socket socket = null;
        EchoThread thread;


        try {
            serverSocket = new ServerSocket(6068);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                assert serverSocket != null;
                socket = serverSocket.accept();
                System.out.println(socket.getRemoteSocketAddress());
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            thread = new EchoThread(socket);
            thread.start();
        }

    }

    private static class EchoThread extends Thread{
        protected Socket socket;
        DataOutputStream    out;
        DataInputStream     dataInputStream;
        static String KEY = "";

        public EchoThread(Socket clientSocket) {
            this.socket     = clientSocket;
            try {
                out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF("GM");
                dataInputStream = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                return;
            }
        }

        @Override
        public void run() {
            while (true){
                try {
                    String line = dataInputStream.readUTF();
                    String[] response = line.split("~");
                    switch (response[0].toUpperCase().trim()){
                        case "LOGIN":
                            KEY = response[1];
                            Roosters.registerOnLineRooster(KEY,this);
                            out.writeUTF("Successfully");
                            break;
                        case "WAIT":
                            System.out.println("\nSEVER :\t"+response[0]);
                            break;
                        case "HI":
                            System.out.println("SEVER\t"+line);
                            out.writeUTF("Hello From Server");
                            break;
                        case "BYE":
                            System.out.println("BYE\t"+line);
                            out.writeUTF("BYE");
                            Roosters.removeRoster(KEY);
                            out.close();
                            dataInputStream.close();
                            socket.close();
                            return;
                        case "DB":
                            System.out.println("GOOD\t"+line);
                            out.writeUTF("Good To See You!!");
                            break;
                        case "GM":
                            System.out.println("HANDSHAKING\t"+line);
                            break;
                        case "TO":
                              System.out.println("\nTO : \t"+response[1] + "\tMSG : "+response[2]+"\t From : "+response[3]);
                              EchoThread rosterRecipient = (EchoThread) Roosters.getRecipient(response[1]);
                              rosterRecipient.pullUpRooster("INCOMING~"+response[2]+"~"+response[3]);
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
        private void pullUpRooster(String message){
            try {
                out.writeUTF(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Roosters{

       static HashMap<String ,Thread> register = new HashMap<>();

        public static void registerOnLineRooster(String id,Thread thread){
            register.put(id,thread);
            System.out.print("onRegister Rosters " + String.valueOf(register.size()));
        }

        public static Thread getRecipient(String id){
            return register.get(id);
        }

        public static void removeRoster(String key){
            register.remove(key);
            System.out.print("onRemove Register Rosters " + String.valueOf(register.size()));
        }
    }
}
