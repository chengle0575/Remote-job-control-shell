/*
* should always listen to the slaves connection*/
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

public class Remote extends Node{


    public static void main(String[] args)
    {
        try{
            /*create a listrning socket*/
            ServerSocket serversocket=new ServerSocket(5110);//start the slave to listen to all incoming connection request
            while(true){

                Socket socket=serversocket.accept(); /*this is the 'socket' connected to remote client*///will block until a connection is made to this machine
                System.out.println("connected!");

                /*create a new thread preparing for command from client*/
                RemoteServeThread thread=new RemoteServeThread(socket);
                thread.run();
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


