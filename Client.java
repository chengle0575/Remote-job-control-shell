import java.net.*;
import java.io.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipOutputStream;

public abstract class Client extends Node{

    private static String remotePreFilePath;
    private static String remoteCurFilePath;

    public static void main(String[] args){

        try{

            Socket socket=null;
            while (true){

                String command=commandFromUser();//send command to remote machine

                if(socket==null){
                    if(command.startsWith("connect")){
                        socket=connectRemote(5110);//create a socket to connect to the remote

                        remoteCurFilePath=recieveMessage(socket).toString(); //get the current direcory of the remote machine
                        System.out.println("Remote machine is working in: "+remoteCurFilePath);
                    }else{
                        System.out.println("Not connected with remote machine. Type 'connect' ");
                    }
                } else if(command.equals("close")){
                    System.out.println("Remote connection is closed");
                    socket.close();
                    socket=null;
                } else if(command.startsWith("getFile")){
                    sendMessage(socket,new Command(command,remoteCurFilePath));
                    recieveFile(socket,"try.zip");// recived file name
                } else{
                    sendMessage(socket,new Command(command,remoteCurFilePath));
                    Message recievmessage=recieveMessage(socket);

                    if(recievmessage instanceof Result){
                        Result res=(Result)recievmessage;
                        System.out.println(res.result.toString());
                    }else{// is info
                        Info info=(Info) recievmessage;
                        System.out.println(info);
                        if("INVALID FILE PATH".equals(info.toString())){
                            System.out.println("checkout!!!!!!!!!!!");
                            remoteCurFilePath=remotePreFilePath; //invert the change of filepath
                        }
                    }
                }
            }

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
        System.out.println("Waiting command input.....");
        BufferedReader r=new BufferedReader(new InputStreamReader(System.in));
        String command=r.readLine();

        //deal with 'cd' command here
        if(command.startsWith("cd ")){ //change the current working filepath of the remote machine
            updateRemoteCurFilePath(command);
            command="ls";  //try 'ls' command to check if the new working filepath is valid for remote machine
        }

        return command;
    }


    public static void updateRemoteCurFilePath(String cdCommand){
        //assuming the cdcommand is reasonable, the requested folder exist
        remotePreFilePath=remoteCurFilePath; //store the copy to enable undo the changing of curfilepath
        String commandAftCd=cdCommand.replaceFirst("^cd","") ;
        String aimfile=commandAftCd.trim();

        int lenth=aimfile.length();

        if(aimfile.startsWith(".")){
            if(aimfile.startsWith("..")){ //means to go to to the parent directory
                int lastslash=remoteCurFilePath.lastIndexOf('/');
                if(lastslash>0){ //can only go to parent directory is not in the root
                    remoteCurFilePath=remoteCurFilePath.substring(0,lastslash+1);
                }
            }

            int firstslash=aimfile.indexOf('/');
            if(firstslash!=-1){ //excluding '.' and '..' these two situation
                remoteCurFilePath=remoteCurFilePath+aimfile.substring(firstslash+1);
            }

        }else{
            File f=new File(aimfile);
            if(f.isAbsolute()){
                remoteCurFilePath=aimfile;
            }else{
                remoteCurFilePath=remoteCurFilePath+aimfile;
            }

        }

    }


    public static void recieveFile(Socket socket,String zipfoldername) throws IOException{
        System.out.println("file is recieved");
        BufferedInputStream r=new BufferedInputStream(socket.getInputStream());

        //create a zip file
        File f=new File(zipfoldername);
        BufferedOutputStream w=new BufferedOutputStream(new FileOutputStream(f));

        DataInputStream datar=new DataInputStream(r);
        int byteToread= datar.readInt();
        System.out.println("all byte to read:"+byteToread);
        //wirte data to the zipfile
        int length=0;
        byte[] buffer=new byte[1024];

        while(byteToread>0){
            length=r.read(buffer);
            byteToread-=length;
            System.out.println("left to read: "+byteToread);
            w.write(buffer,0,length);
        }

        w.flush();
        w.close();
        System.out.println("file is fully recieved");

    }




}
