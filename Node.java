import java.io.*;
import java.net.Socket;
import java.util.Collections;
import java.util.List;

public abstract class Node {

    public static void sendMessage(Socket socket,Message message) throws IOException{
        Writer w=new OutputStreamWriter(socket.getOutputStream());
        w.write(message.stringToSend()+"\n"); /*\n as end mark recognized by readline()*/
        w.flush();
    }

   /*
    public static void sendmessage(Socket socket, String message) throws IOException{
        Writer w=new OutputStreamWriter(socket.getOutputStream());
        w.write(message+"\n");
        w.flush();
    }

     public static void sendCommand(Socket socket,String command,String filepath) throws IOException{
        Writer w=new OutputStreamWriter(socket.getOutputStream());
        w.write(command+"\n");
        w.write(filepath+'\n');
        w.flush();
}

    */

    public static String recievemessgae(Socket socket) throws IOException{
        BufferedReader r=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String message=r.readLine();
        return message;
    }


    public static String recieveCommand(Socket socket) throws IOException {

        BufferedReader r=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String command=r.readLine();

        return command;
    }

    public static void sendResult(Socket socket,List<String> result) throws IOException{
        ObjectOutputStream w=new ObjectOutputStream(socket.getOutputStream()); //ObjectOutputStream used to write serializable object into outputstream
        w.writeObject(result);
        w.flush();
    }



    public static List<String> recieveResult(Socket socket) throws Exception{
        ObjectInputStream r=new ObjectInputStream(socket.getInputStream());
        List<String> result=(List<String>) r.readObject();
        return result;

    }




    public static void getlocalFile(String pathname) throws IOException{

        String filename="/home/le/Documents/javacode/GradeHelper.java";

        FileReader fileReader=new FileReader(filename);

        StringBuilder sb=new StringBuilder();
        int c=fileReader.read();
        while(c!=-1){
            sb.append((char)c);
            c= fileReader.read();;
        }

        System.out.println(sb.toString());



        if(filename.contains(".jpg")||filename.contains(".png")||filename.contains(".jpeg")){//use FileInputStream for image
           // FileInputStream fileInputStream=new FileInputStream("/home/le/Pictures/Screenshots/'Screenshot from 2024-08-07 11-47-11.png'");

        }


        //use Filereader for stream of characters
    }




}
