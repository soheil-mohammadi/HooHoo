package Models;

public class FabricEventStrModel {

    public String attrName ;
    public String value;

    public FabricEventStrModel(String attrName, String value) {
        this.attrName = attrName;
        this.value = value;
    }


    public String getAttrName() {
        return attrName;
    }

    public String getValue() {
        return value;
    }
}
