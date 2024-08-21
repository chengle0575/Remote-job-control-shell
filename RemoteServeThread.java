import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
                sendMessage(socket,new ConnectionInfo(getCurDirectory())); //send current directory path to the client

                System.out.println("Waiting command from client......");
                String command=recieveCommand(this.socket);//get command from this socket and run command
                System.out.println(command);

                List<String> result=executeCommand(command);
                System.out.println(result);
                sendResult(this.socket,result);

            }catch (Exception e){
                System.out.println(e);
            }




    }





    public static List<String> executeCommand(String command)  {
        List<String> result=new ArrayList<>();


        try{
            Process p = Runtime.getRuntime().exec(command);

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

            }

        }


    }



    public static String getCurDirectory() throws IOException{
        List<String> result=executeCommand("pwd");
        return result.get(0);
    }

    public static boolean isTextFile(String pathname) throws IOException{
        String regrex="\\.(pdf|doc|java|md)$";
        Pattern p= Pattern.compile(regrex);
        Matcher m=p.matcher(pathname);
        return m.matches();

    }







}
