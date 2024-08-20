import java.io.*;
import java.net.Socket;
import java.util.List;

public abstract class Node {

    public static void sendmessage(Socket socket, String message) throws IOException{
        Writer w=new OutputStreamWriter(socket.getOutputStream());
        w.write(message+"\n"); /*\n as end mark recognized by readline()*/
        w.flush();
    }

    public static void sendResult(Socket socket,List<String> result) throws IOException{
        ObjectOutputStream w=new ObjectOutputStream(socket.getOutputStream()); //ObjectOutputStream used to write serializable object into outputstream
        w.writeObject(result);
        w.flush();
    }

    public static String recieveCommand(Socket socket) throws IOException {

        BufferedReader r=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String command=r.readLine();

        return command;
    }

    public static List<String> recieveResult(Socket socket) throws Exception{
        ObjectInputStream r=new ObjectInputStream(socket.getInputStream());
        List<String> result=(List<String>) r.readObject();
        return result;

    }

    public static String commandFromUser() throws IOException{
        BufferedReader r=new BufferedReader(new InputStreamReader(System.in));
        String command=r.readLine();
        return command;
    }
}
