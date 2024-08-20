import java.net.InetAddress;

/*contains all the config info used in object*/
/*Use singleton to make sure only one instance will be initiated, and can be accessed globally*/
public class SlaveConfigs{
    
    private static final SlaveConfigs instance=new SlaveConfigs();
    private String centerhostname="localhost";
    private InetAddress centeripaddr;

    private SlaveConfigs(){
    }

    public static String getCenterHostname(){
        return instance.centerhostname;
    }
    public static InetAddress getCenterIpaddr(){
        return instance.centeripaddr;
    }
}
