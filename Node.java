import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Node {

    public static void sendMessage(Socket socket,Message message) throws IOException{
        Writer w=new OutputStreamWriter(socket.getOutputStream());
        w.write(message.getClass().getName()+"\n");
        w.write(message.stringToSend()+"\n"); /*\n as end mark recognized by readline()*/
        w.flush();

    }

    public static Message recieveMessage(Socket socket) throws IOException{
        BufferedReader r=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String messagetype=r.readLine();

        if(messagetype.equals("Command")){
            String message1=r.readLine();
            String message2=r.readLine();
            return new Command(message1,message2);
        }else if (messagetype.equals("Info")){
            String message1=r.readLine();
            return new Info(message1);
        }else{
            String message1=r.readLine();
            String[] content=message1.replace("[","").replace("]","").split(",");
            return new Result(Arrays.stream(content).toList());
        }

    }


}
