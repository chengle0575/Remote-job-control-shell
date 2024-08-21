public class Command implements Message{

    private String command;
    private String filepath;//the directory to run the command

    public Command(String command,String filepath){
        this.command=command;
        this.filepath=filepath;
    }
    @Override
    public String stringToSend(){
        return command+'\n'+filepath+'\n';
    }

}
