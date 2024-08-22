import java.util.ArrayList;
import java.util.List;

public class Result implements Message{

    List<String> result;
    public Result(){
        this.result=new ArrayList<>();
    }

    public void add(String content){
        this.result.add(content);
    }


    @Override
    public String stringToSend() {
        return this.result.toString();
    }

    public List<String> getResult() {
        return result;
    }
}
