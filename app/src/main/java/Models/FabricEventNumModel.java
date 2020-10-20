package Models;

public class FabricEventNumModel {

    public String attrName;
    public int value ;

    public FabricEventNumModel(String attrName, int value) {
        this.attrName = attrName;
        this.value = value;
    }


    public String getAttrName() {
        return attrName;
    }

    public int getValue() {
        return value;
    }
}
