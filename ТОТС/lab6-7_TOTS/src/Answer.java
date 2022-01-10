public class Answer {
    private int name;
    private String type;

    public Answer(int name, String type){
        this.name = name;
        this.type = type;
    }


    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
