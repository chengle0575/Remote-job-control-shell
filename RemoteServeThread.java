import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

                System.out.println("--------*Waiting message from client..*--------");
                while(true){
                    Message recievmessage=recieveMessage(this.socket);

                    if(recievmessage instanceof Command){
                        Command command=(Command) recievmessage;
                        System.out.println("Reciev command: "+command);

                        if(checkFilePathValid(command.getFilepath())){
                            if(command.getCommand().startsWith("getFile")){//deal with getFile command
                                sendFile(this.socket, command.getCommand().substring(8),command.getFilepath()+command.getCommand().substring(8));
                                System.out.println("file is sent");
                            }else{ //deal with executable command
                                Message result=executeCommand(command);
                                sendMessage(this.socket,result);
                            }
                        }else{
                            System.out.println("invalid filepath");
                            sendMessage(this.socket,new Info("INVALID FILE PATH"));
                        }


                    }else{// is an info
                        System.out.println(recievmessage.stringToSend());
                    }

                }


            }catch (IOException e){
                System.out.println(e);
            }
    }


    public static String getCurDirectory() throws IOException{
        return System.getProperty("user.dir");
    }

    public static boolean checkFilePathValid(String filepath){ //using bash scripting to check if filepath valid
        File f=new File(filepath);
        return f.exists();
    }

    public Result executeCommand(Command c) throws NullPointerException{
        String command=c.getCommand();
        String filepath=c.getFilepath();


        File file=new File(filepath);
        Result result=new Result();

        try{
            Process p = Runtime.getRuntime().exec(command,new String[0],file);
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = r.readLine();
            while (line != null) {
                result.add(line);
                line = r.readLine();
            }

            return result;
        }catch (IOException e){
            return null;

        }


    }


    public static void sendFile(Socket socket,String filename,String filepath) throws IOException{

        System.out.println("want to get file: "+filepath);
        BufferedOutputStream w=new BufferedOutputStream(socket.getOutputStream());

       //compress the file
        String aftcompressName=new File(filepath).getName();
        zipCompress(aftcompressName,filename,filepath);


        //zip file to transfer
        File aftzip=new File(aftcompressName+".zip");
        FileInputStream fin=new FileInputStream(aftzip);

        //send byte number to client
        System.out.println("file size:"+fin.available());
        DataOutputStream dataw=new DataOutputStream(w);
        dataw.writeInt(fin.available());

        System.out.println(aftzip.getAbsolutePath());
        byte[] buffer=new byte[1024];
        int length=1024;
        while((length=fin.read(buffer,0,length))>0){
            System.out.println(length+"is sending...");
            w.write(buffer);
        }
        w.flush();
        fin.close();

    }



    public static void zipCompress(String aftcompressName, String filename, String filepathToCompress) throws IOException{
        ZipOutputStream zout=new ZipOutputStream(new FileOutputStream(aftcompressName+".zip"));
        compressFile(new File(filepathToCompress),filename,zout);
        zout.close();

    }
    public static void compressFile(File file,String filename,ZipOutputStream zout) throws IOException{
        if(file.isHidden())
            return;
        if(file.isDirectory()){ //directory itself is treated as an empty zipentry
            if(filename.endsWith("/")){
               // System.out.println("next entry:"+filename);
                zout.putNextEntry(new ZipEntry(filename));
                zout.closeEntry();
            }else{
               // System.out.println("next entry:"+filename);
                zout.putNextEntry(new ZipEntry(filename+"/"));
                zout.closeEntry();
            }

            for(File child:file.listFiles()){
                /*the contents inside inner directory is maintained using zipEntry's path */
                compressFile(child,filename+"/"+child.getName(),zout);
            }
            return;
        }

        /*FOR FILES*/
        FileInputStream fin=new FileInputStream(file);
        //put a zip entry refer to the source file into zip archive
        ZipEntry zipEntry=new ZipEntry(filename);
        zout.putNextEntry(zipEntry);
       // System.out.println("next entry:"+filename);
        //write into the zip
        byte[] bytes=new byte[1024];
        int length=1024;
        while((length=fin.read(bytes))>0){
            zout.write(bytes,0,length);
        }
        zout.closeEntry();

        fin.close(); //close the file when finish reading
    }

}
