/*
* should always listen to the slaves connection*/
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Center{

    static List<Slave> slaves;

    public Center(){
        Center.slaves=new ArrayList<>();
    }

    public static void main(String[] args)
    {
        try{
            //create a socket
            ServerSocket socket=new ServerSocket(5110);

            //start the center to listen to all incoming connection request
            while(true){
                Socket remotesocket=socket.accept(); //will block until a connection is made to this machine
                System.out.println("connected!");
                //Center.slaves.add(remotesocket);

            }


        }catch (Exception e){
            System.out.println(e);
        }


        //connet to multipple server
    }



}


