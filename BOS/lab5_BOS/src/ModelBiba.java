import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ModelBiba {
    static List<Subject> listSub;
    static List<String> listSub_;
    static List<Object> listObj;
    static List<String> listObj_;

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";

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
            else if(keys.length < 2){
                System.out.println("Неверный ввод");
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
                            "showOS\n" +
                            "dataToCSV\n" +
                            "dataFromCSV");
            String[] keys = console.nextLine().split(" ");
            if(keys.length == 1) {
                System.out.println(RED+"Неверный ввод"+RESET);
                continue;
            }
            String sub = keys[0];
            System.out.println("!"+RED + sub + RESET+"!");
            String key = keys[1];
            switch (key){
                case "addO":
                    String nameObj = keys[2];
                    if(listSub_.indexOf(sub) == -1) {
                        System.out.println(RED+ sub+" не существует в системе!"+RESET);
                        break;
                    }
                    String privacylabelSub = listSub.get(listSub_.indexOf(sub)).getPrivacy_label();
                    StdOut.println(RED+"Enter value obj="+nameObj+": "+RESET);
                    String value = console.nextLine();
                    listObj.add(new Object(nameObj, privacylabelSub, value, listObj.size()));
                    listObj_.add(nameObj);
                    DM.matrix[listSub_.indexOf(sub)][listObj_.indexOf(nameObj)] = "rwo";
                    break;
                case "w":
                    nameObj = keys[2];
                    if(checkPolicy(listObj.get(listObj_.indexOf(nameObj)).getPrivacy_label(), "write").contains(listSub.get(listSub_.indexOf(sub)).getPrivacy_label())){
                        if(DM.matrix[listSub_.indexOf(sub)][listObj_.indexOf(nameObj)].contains("w")) {
                            StdOut.println(RED+"Enter value obj=" + nameObj + ": "+RESET);
                            value = console.nextLine();

                            listObj.get(listObj_.indexOf(nameObj)).setValue(value);
                        }
                        else System.out.println(RED+"Отказанно в доступе: по матрице доступа вы не имеете право WRITE в файл"+RESET);
                    }
                    /*else if(rules_system.indexOf(listObj.get(listObj.indexOf(nameObj)).getPrivacy_label())
                            > rules_system.indexOf(listSub.get(listSub.indexOf(sub)).getPrivacy_label())) System.out.println("Отказанно в доступе");

                     */
                    else if(rules_system.indexOf(listObj.get(listObj_.indexOf(nameObj)).getPrivacy_label())
                            > rules_system.indexOf(listSub.get(listSub_.indexOf(sub)).getPrivacy_label())) System.out.println(RED+"Вы не можете записывать " +
                            "информацию в файл мандат которого выше вашего!"+RESET);
                    break;
                case "r":
                    nameObj = keys[2];
                    if(checkPolicy(listObj.get(listObj_.indexOf(nameObj)).getPrivacy_label(), "read").contains(listSub.get(listSub_.indexOf(sub)).getPrivacy_label())){
                        if(DM.matrix[listSub_.indexOf(sub)][listObj_.indexOf(nameObj)].contains("r")) {
                            System.out.printf(RED+"Value file-%s \n", listObj.get(listObj_.indexOf(nameObj)).getValue()+RESET);
                        }
                        else System.out.println(RED+"Отказанно в доступе: по матрице доступа вы не имеете право READ файл"+RESET);
                    }
                    /*else if(rules_system.indexOf(listObj.get(listObj.indexOf(nameObj)).getPrivacy_label())
                            > rules_system.indexOf(listSub.get(listSub.indexOf(sub)).getPrivacy_label())) System.out.println("Отказанно в доступе");

                     */
                    else if(rules_system.indexOf(listObj.get(listObj_.indexOf(nameObj)).getPrivacy_label())
                            < rules_system.indexOf(listSub.get(listSub_.indexOf(sub)).getPrivacy_label())) System.out.println(RED+"Вы не можете читать " +
                            "информацию из файла мандат которого ниже вашего!"+RESET);
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
                    System.out.println();
                }
                    break;
                case "adminCreate":
                    if(sub.equals("Admin")){
                        adminCreate();
                    }
                    break;
                case "dataToCSV":
                    if(sub.equals("Admin")){
                        dataToCSV();
                    }
                    break;
                case "dataFromCSV":
                    if(sub.equals("Admin")){
                        dataFromCSV();
                    }
                    break;
            }
        }
    }

    static public void dataFromCSV(){
        try {
            File file = new File("structure.csv");
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            int count = 0;
            while (line != null) {
                String[] args = line.split(",");
                switch (count){
                    case 0:
                        for(int i = 0; i < args.length; i++){
                            String[] strings = args[i].split(" ");
                            System.out.print(RED+" Субъект "+RESET+strings[0]+RED+" Мандат "+RESET+strings[1]);
                        }
                        System.out.println();
                        break;
                    case 1:
                        for(int i = 0; i < args.length; i++){
                            String[] strings = args[i].split(" ");
                            System.out.print(RED+" Объект "+RESET+strings[0]+RED+" Мандат "+RESET+strings[1]+RED+
                                    " Содержимое "+ RESET + strings[2]);
                        }
                        System.out.println();
                        break;

                }
                count++;
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static public void dataToCSV(){
        try {
            File file = new File("structure.csv");
            FileWriter writer = new FileWriter(file);
            StringBuilder sb = new StringBuilder();
            String prefix = "";
            for(Subject sub_ : listSub){
                sb.append(prefix);
                prefix = ",";
                sb.append(sub_.getName() + " "+ sub_.getPrivacy_label());
            }
            sb.append(System.getProperty("line.separator"));
            prefix = "";
            for(Object obj_ : listObj) {
                sb.append(prefix);
                prefix = ",";
                sb.append(obj_.getName() + " "+ obj_.getPrivacy_label() + " "+ obj_.getValue());
            }
            System.out.println(RED + "Информация о системе:\n"+RESET+sb.toString());
            writer.write(sb.toString());
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public List<String> checkPolicy(String policy, String operation){
        List<String> result_label = new ArrayList<>();
        if(operation.equals("read")){
            for(String label : rules_system){
                if(rules_system.indexOf(label) <= rules_system.indexOf(policy)){
                    result_label.add(label);
                }
            }
        }
        else if(operation.equals("write")){
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


