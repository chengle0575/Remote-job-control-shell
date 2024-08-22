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


            }catch (IOException e){
                System.out.println(e);
            }




    }


    public static boolean checkFilePathValid(String filepath){ //using bash scripting to check if filepath valid
        Process p=null;
        try{
            List<String> s=new ArrayList<>();
            s.add("bash");
            s.add("CheckFilePathExist.sh");

            ProcessBuilder pb=new ProcessBuilder(s);
            p=pb.redirectInput(ProcessBuilder.Redirect.PIPE).redirectOutput(ProcessBuilder.Redirect.PIPE).start();

            //write into process pipe
            Writer w=new OutputStreamWriter(p.getOutputStream());
            w.write(filepath);
            w.flush();
            w.close(); //close the writer ,to avoid process input pipe waiting for input

            //get output from process pipe
            BufferedReader r=new BufferedReader(new InputStreamReader(p.getInputStream()));
            String checkresult=r.readLine();

            if(checkresult.equals("valid")){
                // System.out.println("!!!!valid");
                return true;
            }else{
                //System.out.println("!!!inlavlid!!!");
                return false;
            }

        }catch (Exception e){
            System.out.println(e);
            return false;
        }finally {//will always happen. before the 'return statement' in try/catch block
            if(p!=null){
                p.destroy();//kill the process manually, to release resources.
            }
        }
    }




    public static Message executeCommand(Command c) throws NullPointerException{
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
                System.out.println(line);
                line = r.readLine();
            }

            return result;
        }catch (IOException e){
            //deal with command not able to execute directly in remote shell

            if(command.matches("^getFile {1}")){
                //try to find file name in the given pathname

            }
            return null;

        }


    }



    public static String getCurDirectory() throws IOException{
        Result result=(Result)executeCommand(new Command("pwd","."));
        return result.getResult().get(0);
    }

    public static boolean isTextFile(String pathname) throws IOException{
        String regrex="\\.(pdf|doc|java|md)$";
        Pattern p= Pattern.compile(regrex);
        Matcher m=p.matcher(pathname);
        return m.matches();

    }







}
