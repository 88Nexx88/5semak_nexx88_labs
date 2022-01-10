public class Subject {
    private String name;
    private String privacy_label;
    private int indexSub;

    public Subject(String name, String privacy_label, int indexSub){
        this.name = name;
        this.privacy_label = privacy_label;
        this.indexSub = indexSub;
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

    public int getIndexSub() {
        return indexSub;
    }

    public void setIndexSub(int indexSub) {
        this.indexSub = indexSub;
    }
}