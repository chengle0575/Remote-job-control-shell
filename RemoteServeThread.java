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




    public Message executeCommand(Command c) throws NullPointerException{
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
                    if(file.canRead()){
                        result.add(sendLocalTextFile());
                    }

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


    public static String sendLocalTextFile() {
        try{
            InputStreamReader r=new InputStreamReader(new FileInputStream("./README.md"));
            char[] buffer=new char[1024];
            r.read(buffer);

            String s=new String(buffer);
            // System.out.println(s);
/*
            sendMessage(socket,new Info(s));*/
            return s;
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }



    public static void zipCompress(String aftcompressName, String filepathToCompress) throws IOException{
        ZipOutputStream zout=new ZipOutputStream(new FileOutputStream(aftcompressName+".zip"));
        compressFile(new File(filepathToCompress),filepathToCompress,zout);
        zout.close();

    }
    public static void compressFile(File file,String filename,ZipOutputStream zout) throws IOException{
        if(file.isHidden())
            return;
        if(file.isDirectory()){ //directory itself is treated as an empty zipentry
            if(filename.endsWith("/")){
                System.out.println("next entry:"+filename);
                zout.putNextEntry(new ZipEntry(filename));
                zout.closeEntry();
            }else{
                System.out.println("next entry:"+filename);
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
        System.out.println("next entry:"+filename);
        //write into the zip
        byte[] bytes=new byte[1024];
        int length;
        while((length=fin.read(bytes))>0){
            zout.write(bytes,0,length);
        }
        zout.closeEntry();

        fin.close(); //close the file when finish reading
    }














}
