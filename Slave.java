import java.net.*;
import java.io.*;

public abstract class Slave {

    public static void main(String[] args){

        //create a socket to connect to the center
        connectCenter();

        //getting shell command from center


    }


    public static void connectCenter(){
        try{
            Socket socket=new Socket();

            SocketAddress centeradd=new InetSocketAddress(SlaveConfigs.getCenterHostname(),5110);
            socket.connect(centeradd);

            //write data to center machine
            Writer w=new OutputStreamWriter(socket.getOutputStream());
            w.write("slave is coming!");
            System.out.println("slave finish writing");
            //get data from the center machine
            Reader r=new InputStreamReader(socket.getInputStream());
            char[] contents=new char[2];
            System.out.println("finish reading");

            System.out.println(contents[0]);

        }catch (Exception e){
            System.out.println(e);
        }


    }




}
