import java.io.*;
import java.net.Socket;

public abstract class Node {

    public static void sendmessage(Socket socket, String message) {
        try{
            Writer w=new OutputStreamWriter(socket.getOutputStream());
            w.write(message);
            w.flush();
        }catch (Exception e){
            System.out.println(e);
        }

    }

    public static void recievemessage(Socket socket,char[] message) {
        try {
            Reader r=new InputStreamReader(socket.getInputStream());
            int i=r.read(message);
        }catch (Exception e){
            System.out.println(e);
        }

    }
}
