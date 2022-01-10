import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ModelBLP {
    static List<Subject> listSub;
    static List<String> listSub_;
    static List<Object> listObj;
    static List<String> listObj_;

    static List<String> rules_system;
    static Scanner console = new Scanner(System.in);

    static public void adminCreate(){
        StdOut.println("|add sub|\n" +
                "name privacy_label\n" +
                "or\n" +
                "end");
        while (true){
            String[] keys = console.nextLine().split(" ");
            if(keys[0].equals("end")) {
                break;
            }
            else {
                createSub(keys[0], keys[1]);
                listSub_.add(keys[0]);
            }
        }
    }

    static public void createRules(){
        StdOut.println("|create policy|\n" +
                "from low to high\n" +
                "?privacy_label");
        while (true){
            String keys = StdIn.readString();
            if(keys.equals("end")) break;
            else {
                rules_system.add(keys);
            }
        }
    }

    public static void main(String[] args) {
        rules_system = new ArrayList<>();
        listObj_ = new ArrayList<>();
        listObj = new ArrayList<>();
        listSub_ = new ArrayList<>();
        listSub = new ArrayList<>();
        HRU DM = new HRU();
        for(int i = 0; i < DM.matrix.length; i++){
            DM.matrix[0][i] = "rwo";
        }
        createRules();
        createSub("Admin", rules_system.get(rules_system.size()-1));
        listSub_.add("Admin");
        adminCreate();

        while(true){
            StdOut.println
                    ("|sub command arguments|\n" +
                    "comand arguments\n" +
                            "addO name\n" +
                            "w nameObj\n" +
                            "r nameObj\n" +
                            "res nameObj");
            String[] keys = console.nextLine().split(" ");
            String sub = keys[0];
            System.out.println("!"+sub + "!");
            String key = keys[1];
            switch (key){
                case "addO":
                    String nameObj = keys[2];
                    String privacylabelSub = listSub.get(listSub_.indexOf(sub)).getPrivacy_label();
                    StdOut.println("Enter value obj="+nameObj+": ");
                    String value = console.nextLine();
                    listObj.add(new Object(nameObj, privacylabelSub, value, listObj.size()));
                    listObj_.add(nameObj);
                    DM.matrix[listSub_.indexOf(sub)][listObj_.indexOf(nameObj)] = "rwo";
                    break;
                case "w":
                    nameObj = keys[2];
                    if(checkPolicy(listObj.get(listObj_.indexOf(nameObj)).getPrivacy_label(), "write").contains(listSub.get(listSub_.indexOf(sub)).getPrivacy_label())){
                        if(DM.matrix[listSub_.indexOf(sub)][listObj_.indexOf(nameObj)].contains("w")) {
                            StdOut.println("Enter value obj=" + nameObj + ": ");
                            value = console.nextLine();
                            System.out.println("По политике law-watermark мандат файла \"" + nameObj + "\" меняется с " +
                                    "\"" + listObj.get(listObj_.indexOf(nameObj)).getPrivacy_label() + "\" на \"" + listSub.get(listSub_.indexOf(sub)).getPrivacy_label() + "\"");
                            listObj.get(listObj_.indexOf(nameObj)).setPrivacy_label(listSub.get(listSub_.indexOf(sub)).getPrivacy_label());
                            listObj.get(listObj_.indexOf(nameObj)).setValue(value);
                        }
                        else System.out.println("Отказанно в доступе: по матрице доступа вы не имеете право WRITE в файл");
                    }
                    /*else if(rules_system.indexOf(listObj.get(listObj.indexOf(nameObj)).getPrivacy_label())
                            > rules_system.indexOf(listSub.get(listSub.indexOf(sub)).getPrivacy_label())) System.out.println("Отказанно в доступе");

                     */
                    else if(rules_system.indexOf(listObj.get(listObj_.indexOf(nameObj)).getPrivacy_label())
                            < rules_system.indexOf(listSub.get(listSub_.indexOf(sub)).getPrivacy_label())) System.out.println("Вы не можете записывать " +
                            "информацию в файл мандат которого ниже вашего!");
                    break;
                case "r":
                    nameObj = keys[2];
                    if(checkPolicy(listObj.get(listObj_.indexOf(nameObj)).getPrivacy_label(), "read").contains(listSub.get(listSub_.indexOf(sub)).getPrivacy_label())){
                        if(DM.matrix[listSub_.indexOf(sub)][listObj_.indexOf(nameObj)].contains("r")) {
                            System.out.printf("Value file-%s \n", listObj.get(listObj_.indexOf(nameObj)).getValue());
                        }
                        else System.out.println("Отказанно в доступе: по матрице доступа вы не имеете право READ файл");
                    }
                    /*else if(rules_system.indexOf(listObj.get(listObj.indexOf(nameObj)).getPrivacy_label())
                            > rules_system.indexOf(listSub.get(listSub.indexOf(sub)).getPrivacy_label())) System.out.println("Отказанно в доступе");

                     */
                    else if(rules_system.indexOf(listObj.get(listObj_.indexOf(nameObj)).getPrivacy_label())
                            > rules_system.indexOf(listSub.get(listSub_.indexOf(sub)).getPrivacy_label())) System.out.println("Вы не можете читать " +
                            "информацию из файла мандат которого выше вашего!");
                    break;
                case "res":
                    nameObj = keys[2];
                    if(!checkPolicy(listObj.get(listObj_.indexOf(nameObj)).getPrivacy_label(), "write").contains(listSub.get(listSub_.indexOf(sub)).getPrivacy_label())){
                        if(DM.matrix[listSub_.indexOf(sub)][listObj_.indexOf(nameObj)].contains("o")) {
                            System.out.println("По политике law-watermark мандат файла \"" + nameObj + "\" меняется с " +
                                    "\"" + listObj.get(listObj_.indexOf(nameObj)).getPrivacy_label() + "\" на максимально возможный мандат \"" + rules_system.get(rules_system.size()-1) + "\"");
                            listObj.get(listObj_.indexOf(nameObj)).setPrivacy_label(rules_system.get(rules_system.size()-1));
                        }
                        else System.out.println("Отказанно в доступе: по матрице доступа вы не имеете право OWN файл");
                    }
                    /*else if(rules_system.indexOf(listObj.get(listObj.indexOf(nameObj)).getPrivacy_label())
                            > rules_system.indexOf(listSub.get(listSub.indexOf(sub)).getPrivacy_label())) System.out.println("Отказанно в доступе");

                     */
                    else if(rules_system.indexOf(listObj.get(listObj_.indexOf(nameObj)).getPrivacy_label())
                            > rules_system.indexOf(listSub.get(listSub_.indexOf(sub)).getPrivacy_label())) System.out.println("Вы не можете выполнить команду RESET к файлу " +
                            " мандат которого выше вашего!");
                    else System.out.println("Вы не можете выполнить команду RESET к файлу " +
                                " так как имеете мандат позволяющий WRITE в файл!");
                    break;
                case "showOS":
                    if(sub.equals("Admin")){
                    for(Subject sub_ : listSub){
                        System.out.print(sub_.getName() + " "+ sub_.getPrivacy_label()+"; ");
                    }
                    System.out.println();
                    for(Object obj_ : listObj) {
                        System.out.print(obj_.getName() + " " + obj_.getPrivacy_label() + " " + obj_.getValue() + "; ");
                    }
                }
                    break;
                case "adminCreate":
                    if(sub.equals("Admin")){
                        adminCreate();
                    }
            }
        }
    }
    static public List<String> checkPolicy(String policy, String operation){
        List<String> result_label = new ArrayList<>();
        if(operation.equals("write")){
            for(String label : rules_system){
                if(rules_system.indexOf(label) <= rules_system.indexOf(policy)){
                    result_label.add(label);
                }
            }
        }
        else if(operation.equals("read")){
            for(String label : rules_system){
                if(rules_system.indexOf(label) >= rules_system.indexOf(policy)){
                    result_label.add(label);
                }
            }
        }
        return result_label;

    }

    static public void createSub(String name, String privacy_label){
        Subject sub = new Subject(name, privacy_label, listSub.size());
        listSub.add(sub);
    }

}






class HRU {
    public String[][] matrix;
    public HRU() {
        matrix = new String[20][20];
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix.length; j++){
                matrix[i][j] = "rwo";
            }
        }
    }

    public void setNewRules(int indexSub, int indexObj, String rules, int indexWhoAdd) {
        if (indexWhoAdd == 0) {
            matrix[indexSub][indexObj] = rules;
        } else if (matrix[indexWhoAdd][indexObj].contains("o"))
            matrix[indexSub][indexObj] = rules;
        else
            System.out.println("Add rules" + rules + " by obj id=" + indexObj + " subject id=" + indexSub + " denied to id=" + indexWhoAdd);
    }

}


