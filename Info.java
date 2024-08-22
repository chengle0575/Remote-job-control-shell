public class Info implements Message{
    private String info;


    public Info(String info){
        this.info= info;
    }
    @Override
    public String stringToSend() {
        return info+'\n';
    }

    @Override
    public String toString(){
        return info;
    }
}
