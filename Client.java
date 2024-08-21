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


    public static String commandFromUser() throws IOException{
        BufferedReader r=new BufferedReader(new InputStreamReader(System.in));
        String command=r.readLine();

        //deal with 'cd' command here
        if(command.matches("^cd {1}")){
            //change the filepath

            remoteCurFilePath=remoteCurFilePath+"/"+
        }
        return command;
    }



    public static void updateRemoteCurFilePath(String cdCommand){
        //assuming the cdcommand is reasonable, the requested folder exist

        String commandAftCd=cdCommand.replaceFirst("^cd","") ;
        String aimfile=commandAftCd.trim();

        int lenth=aimfile.length();

        if(aimfile.startsWith(".")){
            if(aimfile.startsWith("..")){ //means to go to to the parent directory
                int lastslash=remoteCurFilePath.lastIndexOf('/');
                if(lastslash>0){ //can only go to parent directory is not in the root
                    remoteCurFilePath=remoteCurFilePath.substring(0,lastslash);
                }
            }

            int firstslash=aimfile.indexOf('/');
            if(firstslash!=-1){ //excluding '.' and '..' these two situation
                remoteCurFilePath=remoteCurFilePath+"/"+aimfile.substring(firstslash+1);
            }

        }else{
            remoteCurFilePath=remoteCurFilePath+"/"+aimfile;
        }

    }



}
