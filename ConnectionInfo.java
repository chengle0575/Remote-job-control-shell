import java.nio.file.FileSystemNotFoundException;

public class ConnectionInfo implements Message{
    private String info;


    public ConnectionInfo(String info){
        this.info= info;
    }
    @Override
    public String stringToSend() {
        return info+'\n';
    }
}
