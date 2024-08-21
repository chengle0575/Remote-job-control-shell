public class FileRequest implements Message{

    private String filename; //the requested file's path


    public FileRequest(String filename){
        this.filename=filename;
    }
    @Override
    public String stringToSend(){
        return filename+'\n';
    }
}
