import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Container {
    public String[] rul = {"NS", "S", "SS"};
    private String name;
    private String policy = "NS";
    private List<Object> list;
    public String SSR;
    static public HashMap<String, Integer> convertToINT = new HashMap<String, Integer>();

    public Container(String name, String SSR){
        convertToINT.put("NS", 0);
        convertToINT.put("S", 1);
        convertToINT.put("SS", 2);
        this.name = name;
        this.SSR = SSR;
        list = new ArrayList<>();
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public List<Object> getList() {
        return list;
    }


    public void setList(List<Object> list) {
        this.list = list;
    }

    public void setOneList(Object o){
        list.add(o);
        if(convertToINT.get(o.getPolicy()) > convertToINT.get(this.getPolicy()))
                this.policy = o.getPolicy();
    }

    public void updatePolicy(){
        policy = list.get(0).getPolicy();
        for(Object o : list){
            if(convertToINT.get(o.getPolicy()) > convertToINT.get(this.getPolicy())){
                this.policy = o.getPolicy();
            }
        }
    }

    public void removeOneList(Object o){
        list.remove(o);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int checkPolicy(String policy){
        if(this.policy.equals("SS")){
            if(policy.equals(this.policy)){
                return 0;
            }
            else if(policy.equals("S")){
                return -1;
            }
            else {
                return -1;
            }
        }
        else if(this.policy.equals("S")){
            if(policy.equals(this.policy)){
                return 0;
            }
            else if(policy.equals("SS")){
                return 1;
            }
            else {
                return -1;
            }
        }
        else {
            if(policy.equals(this.policy)){
                return 0;
            }
            else if(policy.equals("S")){
                return 1;
            }
            else {
                return 1;
            }
        }
    }
}
