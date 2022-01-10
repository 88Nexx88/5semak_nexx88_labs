public class Object {
    private String name;
    private String privacy_label;
    private String value;
    private int indexObj;

    public Object(String name, String privacy_label, String value, int indexObj){
        this.name = name;
        this.privacy_label = privacy_label;
        this.value = value;
        this.indexObj = indexObj;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrivacy_label() {
        return privacy_label;
    }

    public void setPrivacy_label(String privacy_label) {
        this.privacy_label = privacy_label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getIndexObj() {
        return indexObj;
    }

    public void setIndexObj(int indexObj) {
        this.indexObj = indexObj;
    }
}
