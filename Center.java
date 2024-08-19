/*
* should always listen to the slaves connection*/
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Center{



    public Center(){

    }

    public static void main(String[] args)
    {
        try{
            /*create a listrning socket*/
            ServerSocket serversocket=new ServerSocket(5110);

            //start the center to listen to all incoming connection request
            while(true){
                /*this is the 'socket' connected to remote machine*/
                Socket socket=serversocket.accept(); //will block until a connection is made to this machine
                System.out.println("connected!");

                //send message to the slave
                Writer w=new OutputStreamWriter(socket.getOutputStream());
                w.write("hello salve, are you good today");
                w.flush();
                System.out.println("already sent");


                Reader r=new InputStreamReader(socket.getInputStream());
                char[] readinng=new char[2];
                int i=r.read(readinng);
                System.out.println(i);
                System.out.println(readinng);

            }


        }catch (Exception e){
            System.out.println(e);
        }


        //connet to multipple server
    }






}


