import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RemoteServeThread extends Node implements Runnable{

    private final Socket socket;
    public RemoteServeThread(Socket socket){
        this.socket=socket;
    }

    @Override
    public void run(){


            try{
                String command=recieveCommand(this.socket);//get command from this socket and run command
                System.out.println(command);
                List<String> result=executeCommand(command);
                System.out.println(result);
                sendResult(this.socket,result);

            }catch (Exception e){
                System.out.println(e);
            }




    }





    public static List<String> executeCommand(String command) throws IOException {
        Process p = Runtime.getRuntime().exec(command);

        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

        List<String> result=new ArrayList<>();

        String line = r.readLine();
        while (line != null) {
            result.add(line);
            System.out.println(line);
            line = r.readLine();
        }

        return result;

    }

}
