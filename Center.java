/*
* should always listen to the slaves connection*/
import java.net.*;
import java.io.*;

public class Center{

    public static void main(String[] args)
    {
        try{
            //create a socket
            ServerSocket socket=new ServerSocket(5110);

            System.out.println(socket.getInetAddress());
            System.out.println(socket.getLocalSocketAddress());
            System.out.println(socket.getLocalPort());
            //start the center to listen to all incoming connection request
            Socket remotesocket=socket.accept(); //will block until a connection is made to this machine
            System.out.println("connected!");



        }catch (Exception e){
            System.out.println(e);
        }


        //connet to multipple server
    }
}


