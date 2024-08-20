import java.net.*;
import java.io.*;
import java.util.List;

public abstract class Client extends Node{

    public static void main(String[] args){

        try{
            Socket socket=connectRemote(5110);//create a socket to connect to the remote

            //sendmessage(socket,"client connected");//write data to remote machine

            String command=commandFromUser();//send command to remote machine
            sendmessage(socket,command);

            List<String> result=recieveResult(socket);

            System.out.println(result);

        }catch (Exception e){
            System.out.println(e);
        }




    }


    public static Socket connectRemote(int portnum) throws IOException{
        Socket socket=new Socket();

        SocketAddress centeradd=new InetSocketAddress(SlaveConfigs.getCenterHostname(),portnum);
        socket.connect(centeradd);

        return socket;
    }







}
