import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoteServeThread extends Node implements Runnable{

    private final Socket socket;
    public RemoteServeThread(Socket socket){
        this.socket=socket;
    }

    @Override
    public void run(){
            try{
                System.out.println("A client is connected");
                sendMessage(socket,new Info(getCurDirectory())); //send current directory path to the client

                System.out.println("Waiting command from client......");
                while(true){
                    Command command=recieveCommand(this.socket);//get command from this socket and run command
                    System.out.println(command);

                    if(checkFilePathValid(command.getFilepath())){
                        System.out.println("valid filepath");
                        Message result=executeCommand(command);
                        sendMessage(this.socket,result);
                    }else{
                        System.out.println("invalid filepath");
                        sendMessage(this.socket,new Info("INVALID FILE PATH"));
                    }
                }


            }catch (IOException e){
                System.out.println(e);
            }
    }


    public static boolean checkFilePathValid(String filepath){ //using bash scripting to check if filepath valid
        File f=new File(filepath);
        return f.exists();
    }




    public static Message executeCommand(Command c) throws NullPointerException{
        String command=c.getCommand();
        String filepath=c.getFilepath();

        if("gFile".equals(command)){//deal with getfile command here
        }

        File file=new File(filepath);

        Result result=new Result();


        try{
            Process p = Runtime.getRuntime().exec(command,new String[0],file);

            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = r.readLine();
            while (line != null) {
                result.add(line);
                System.out.println(line);
                line = r.readLine();
            }

            return result;
        }catch (IOException e){
            //deal with command not able to execute directly in remote shell

            if(command.matches("^getFile {1}")){
                //try to find file name in the given pathname
                if(file.isFile()){ //is normal file

                }else if(file.isDirectory()){ //is directory

                }else{ //eg/ symbolic links

                }
            }
            return null;

        }


    }



    public static String getCurDirectory() throws IOException{
        return System.getProperty("user.dir");
    }

    public static void sendLocalByteFile(Socket socket,String filepath) throws IOException{
        //assuming the filepath refer to 'raw byte file', not text file ,not directory
        FileInputStream f=new FileInputStream("./blue.png");
        f.read();


        BufferedWriter w=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
       // w.write();

    }

    public static void sendLocalTextFile() throws IOException{

    }



    public static boolean isTextFile(String pathname) throws IOException{
        String regrex="\\.(pdf|doc|java|md)$";
        Pattern p= Pattern.compile(regrex);
        Matcher m=p.matcher(pathname);
        return m.matches();

    }







}
