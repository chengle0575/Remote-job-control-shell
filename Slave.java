import java.net.*;
import java.io.*;
import java.rmi.server.ExportException;

public abstract class Slave extends Node{

    public static void main(String[] args){

        //getting shell command from center
        executeCommand("ls");


        //create a socket to connect to the center
        Socket socket=connectCenter();


        //write data to center machine
        sendmessage(socket,"slave is cconnected");

        //get data from the center machine
        char[] contents=new char[10];
        recievemessage(socket,contents);




    }


    public static Socket connectCenter(){
        Socket socket=new Socket();

        try{
            SocketAddress centeradd=new InetSocketAddress(SlaveConfigs.getCenterHostname(),5110);
            socket.connect(centeradd);
        }catch (Exception e){
            System.out.println(e);
        }

        return socket;
    }


    public static void executeCommand(String command){
        try{
            Process p=Runtime.getRuntime().exec(command);

            BufferedReader r=new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line=r.readLine();
            System.out.println(line);

        }catch (Exception e){
            System.out.println(e);
        }

    }




}
