/*
* should always listen to the slaves connection*/
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Remote extends Node{


    public static void main(String[] args)
    {
        try{
                /*create a listrning socket*/
                ServerSocket serversocket=new ServerSocket(5110);//start the slave to listen to all incoming connection request
                System.out.println("Waiting to be connected......");

                while(true){
                    Socket socket=serversocket.accept(); /*this is the 'socket' connected to remote client*///will block until a connection is made to this machine
                    if(socket.isConnected()){
                        /*create a new thread preparing for command from client*/
                        RemoteServeThread thread=new RemoteServeThread(socket);
                        thread.run();
                    }
                }
            }catch (Exception e){
                System.out.println(e);
        }



    }


    public static Socket waitingConnection(ServerSocket serverSocket) throws Exception{

        Socket socket=serverSocket.accept();
        return socket;
    }








}


