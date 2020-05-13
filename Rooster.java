package com.barcode.rfd.Adapter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by prashant.chaudhary on 5/8/2017.
 */
public class Rooster {


    public static void main(String args[]){
        DataOutputStream    dataOutputStream    = null;
        DataInputStream     dataInputStream     = null;
        try {
            Socket socket               = new Socket("192.168.1.125",6068);
            dataInputStream             = new DataInputStream(socket.getInputStream());
            dataOutputStream            = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String user;
        Scanner sc = new Scanner(System.in);

        while (true){
            user = "";
            try {
                assert dataInputStream != null;
                user = dataInputStream.readUTF();

                if (user.equalsIgnoreCase("GM")) {
                    assert dataOutputStream != null;
                    dataOutputStream.writeUTF("GM");
                }
/*                if (user.equalsIgnoreCase("Successfully")) {
                    assert dataOutputStream != null;
                    dataOutputStream.writeUTF("Wait");
                }*/
                if (user.equalsIgnoreCase("INCOMING")){
                    assert dataOutputStream != null;
                    dataOutputStream.writeUTF("TO~2~HiFromJohn~1");
                }
                System.out.println(user);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (user.equalsIgnoreCase("BYE")) break;
            String userInput = sc.next();
            try {
                assert dataOutputStream != null;
                dataOutputStream.writeUTF(userInput);
                /*switch (userInput){
                    case "1":
                        dataOutputStream.writeUTF("HI");
                        break;
                    case "2":
                        dataOutputStream.writeUTF("DB");
                        break;
                    case "3":
                        dataOutputStream.writeUTF("BYE");
                        break;
                }*/
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
