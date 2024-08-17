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

        }catch (Exception e){
            System.out.println(e);
        }
    }
}
