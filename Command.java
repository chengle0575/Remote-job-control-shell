public class Command implements Message{

    private final String command;
    private final String filepath;//the directory to run the command

    public Command(String command,String filepath){
        this.command=command;
        this.filepath=filepath;
    }

    @Override
    public String stringToSend(){
        return command+'\n'+filepath+'\n';
    }

    @Override
    public String toString(){
        return "command: "+command+"/n"+"working directory: "+filepath;
    }

    public String getCommand() {
        return command;
    }

    public String getFilepath() {
        return filepath;
    }
}
