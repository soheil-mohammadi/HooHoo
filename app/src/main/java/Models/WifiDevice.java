package Models;

/**
 * Created by soheilmohammadi on 7/21/18.
 */

public class WifiDevice {

    public String Address ;
    public String Name ;
    public int Sex ;
    public String UniqId;


    public WifiDevice(String address  , String name, int sex , String uniqId) {
        Address = address;
        Name = name;
        Sex = sex;
        UniqId = uniqId;
    }
}
