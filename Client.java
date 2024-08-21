import java.net.*;
import java.io.*;
import java.util.List;

public abstract class Client extends Node{

    private static String remoteCurFilePath;

    public static void main(String[] args){

        try{
            Socket socket=connectRemote(5110);//create a socket to connect to the remote

            remoteCurFilePath=recievemessgae(socket); //get the current direcory of the remote machine


            System.out.println("Remote machine is working in: "+remoteCurFilePath);

            String command=commandFromUser();//send command to remote machine
            sendMessage(socket,new Command(command,remoteCurFilePath));

            List<String> result=recieveResult(socket);
            System.out.println(result);


            //get file from the remote machine


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
