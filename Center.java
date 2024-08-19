/*
* should always listen to the slaves connection*/
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Center extends Node{



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
                sendmessage(socket,"hi slave, how are you!");
                System.out.println("already sent");

                char[] reading=new char[100];
                recievemessage(socket,reading);
                System.out.println(reading);



                //send command to slave
                sendmessage(socket,"ls");


            }


        }catch (Exception e){
            System.out.println(e);
        }


        //connet to multipple server
    }









}


