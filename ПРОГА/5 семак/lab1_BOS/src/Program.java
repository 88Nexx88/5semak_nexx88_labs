import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class Program {
    static public void main(String[] args) {
        discretionaryModel dm = new discretionaryModel();
        dm.addSub("root");
        while (true) {
            boolean log = false;
            StdOut.println("login");
            String login = StdIn.readLine();
            if(login != "") {
                if(dm.sub.indexOf(login) == -1)
                    dm.addSub(login);
                log = true;
            }
            while (log) {
                StdOut.println("command arguments\n" +
                        "createObj nameObj\n" +
                        "deleteObj nameObj\n" +
                        "addRule rule nameObj nameSub\n" +
                        "removeRule rule nameObj nameSub\n" +
                        "show\n" +
                        "logout\n" +
                        "test");
                String str= StdIn.readLine();
                String[] keys = str.split(" ");
                switch (keys[0]) {
                    case ("createObj"):
                        String c_obj= keys[1];
                        dm.addObj(c_obj);
                        dm.addRight("rwo", dm.sub.indexOf(login), dm.obj.indexOf(c_obj), login, c_obj, "root");
                        break;
                    case ("deleteObj"):
                        String d_obj= keys[1];
                        dm.removeObj(d_obj);
                        break;
                    case ("addRule"):
                        String rul= keys[1];
                        String nameObj= keys[2];
                        String nameSub= keys[3];
                        dm.addRight(rul, dm.sub.indexOf(nameSub), dm.obj.indexOf(nameObj), nameSub, nameObj, login);
                        break;
                    case ("removeRule"):
                        rul= keys[1];
                        nameObj= keys[2];
                        nameSub= keys[3];
                        dm.removeRight(dm.sub.indexOf(nameSub), dm.obj.indexOf(nameObj), rul.toCharArray(), login);
                        break;
                    case ("logout"):
                        log = false;
                        break;
                    case ("show"):
                        System.out.print("      ");
                        for (int j = 0; j < dm.obj.size(); j++) {
                            System.out.print(dm.obj.get(j) + "      ");
                        }
                        System.out.println();
                        for (int i = 0; i < dm.sub.size(); i++) {
                            System.out.print(dm.sub.get(i) + " ");
                            for (int j = 0; j < dm.obj.size(); j++) {
                                Access a = dm.getRight(i, j);
                                if (a != null) System.out.print(a.r + "" + a.w +""+a.o+ "      ");
                                else System.out.print("         ");
                            }
                            System.out.println();
                        }
                        System.out.println();
                        break;
                    case ("test"):
                        dm.removeSub(login);
                        test(dm);
                        break;
                    default:
                        break;
                }


            }
        }
    }
    static void test(discretionaryModel dm){
        System.out.println("!");
        dm.addSub("Admin");
        dm.addSub("User");
        dm.addObj("File");
        dm.addObj("File0");
        dm.addRight("rwo", dm.sub.indexOf("Admin"), dm.obj.indexOf("File"), "Admin", "File", "root");
        dm.addRight("rw-", dm.sub.indexOf("User"), dm.obj.indexOf("File"), "User", "File", "root");
        dm.addRight("rwo", dm.sub.indexOf("Admin"), dm.obj.indexOf("File0"), "Admin", "File0", "root");
        dm.addRight("rw-", dm.sub.indexOf("User"), dm.obj.indexOf("File0"), "User", "File0", "root");
        System.out.print("      ");
        for (int j = 0; j < dm.obj.size(); j++) {
            System.out.print(dm.obj.get(j) + " ");
        }
        System.out.println();
        for (int i = 0; i < dm.sub.size(); i++) {
            System.out.print(dm.sub.get(i) + " ");
            for (int j = 0; j < dm.obj.size(); j++) {
                Access a = dm.getRight(i, j);
                if (a != null) System.out.print(a.r + "" + a.w +""+a.o+ "      ");
                else System.out.print("         ");
            }
            System.out.println();
        }

        //проверка
        System.out.println();
        System.out.println("remove [rw-] User File0 <- Admin");
        System.out.print(dm.removeRight(2, 1, "rw-".toCharArray(), "Admin")); // удаление права
        System.out.print("  ");
        for (int j = 0; j < dm.obj.size(); j++) {
            System.out.print(dm.obj.get(j) + " ");
        }
        System.out.println();
        for (int i = 0; i < dm.sub.size(); i++) {
            System.out.print(dm.sub.get(i) + " ");
            for (int j = 0; j < dm.obj.size(); j++) {
                Access a = dm.getRight(i, j);
                if (a != null) System.out.print(a.r + "" + a.w +""+a.o+ "      ");
                else System.out.print("         ");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("add [-w-] User File0 <- Admin");
        System.out.print(dm.addRight("-w-", 2, 1, "User", "File0", "Admin"));
        System.out.print("  ");
        for (int j = 0; j < dm.obj.size(); j++) {
            System.out.print(dm.obj.get(j) + " ");
        }
        System.out.println();
        for (int i = 0; i < dm.sub.size(); i++) {
            System.out.print(dm.sub.get(i) + " ");
            for (int j = 0; j < dm.obj.size(); j++) {
                Access a = dm.getRight(i, j);
                if (a != null) System.out.print(a.r + "" + a.w +""+a.o+ "      ");
                else System.out.print("         ");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("remove [rw-] User File <- Admin");
        System.out.print(dm.removeRight(2, 0, "rw-".toCharArray(), "Admin")); // удаление права
        System.out.print("  ");
        for (int j = 0; j < dm.obj.size(); j++) {
            System.out.print(dm.obj.get(j) + " ");
        }
        System.out.println();
        for (int i = 0; i < dm.sub.size(); i++) {
            System.out.print(dm.sub.get(i) + " ");
            for (int j = 0; j < dm.obj.size(); j++) {
                Access a = dm.getRight(i, j);
                if (a != null) System.out.print(a.r + "" + a.w +""+a.o+ "      ");
                else System.out.print("         ");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("add [rw-] User File <- Admin");
        System.out.print(dm.addRight("rw-", 2, 0, "User", "File", "Admin")); // add права
        System.out.print("  ");
        for (int j = 0; j < dm.obj.size(); j++) {
            System.out.print(dm.obj.get(j) + " ");
        }
        System.out.println();
        for (int i = 0; i < dm.sub.size(); i++) {
            System.out.print(dm.sub.get(i) + " ");
            for (int j = 0; j < dm.obj.size(); j++) {
                Access a = dm.getRight(i, j);
                if (a != null) System.out.print(a.r + "" + a.w +""+a.o+ "      ");
                else System.out.print("         ");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("remove [rw-] User File <- User");
        System.out.print(dm.removeRight(2, 1, "rw-".toCharArray(), "User")); // удаление права
        System.out.print("  ");
        for (int j = 0; j < dm.obj.size(); j++) {
            System.out.print(dm.obj.get(j) + " ");
        }
        System.out.println();
        for (int i = 0; i < dm.sub.size(); i++) {
            System.out.print(dm.sub.get(i) + " ");
            for (int j = 0; j < dm.obj.size(); j++) {
                Access a = dm.getRight(i, j);
                if (a != null) System.out.print(a.r + "" + a.w +""+a.o+ "      ");
                else System.out.print("         ");
            }
            System.out.println();
        }


        System.out.println();
        System.out.println("add [rw-] User File0 <- User");
        System.out.print(dm.addRight("rw-", 1, 1, "User", "File0", "User")); // add права
        System.out.print("  ");
        for (int j = 0; j < dm.obj.size(); j++) {
            System.out.print(dm.obj.get(j) + " ");
        }
        System.out.println();
        for (int i = 0; i < dm.sub.size(); i++) {
            System.out.print(dm.sub.get(i) + " ");
            for (int j = 0; j < dm.obj.size(); j++) {
                Access a = dm.getRight(i, j);
                if (a != null) System.out.print(a.r + "" + a.w +""+a.o+ "      ");
                else System.out.print("         ");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("add [rw-] User Dr Disk <- Admin");
        System.out.print(dm.addRight("rw-", 1, 1, "User", "Disk drive0", "Admin")); // add права
        System.out.print("  ");
        for (int j = 0; j < dm.obj.size(); j++) {
            System.out.print(dm.obj.get(j) + " ");
        }
        System.out.println();
        for (int i = 0; i < dm.sub.size(); i++) {
            System.out.print(dm.sub.get(i) + " ");
            for (int j = 0; j < dm.obj.size(); j++) {
                Access a = dm.getRight(i, j);
                if (a != null) System.out.print(a.r + "" + a.w +""+a.o+ "      ");
                else System.out.print("         ");
            }
            System.out.println();
        }
    }
}

class discretionaryModel{
    public Vector<String> obj;
    public Vector<String> sub;
    public ArrayList<Access> ListRight;
    public int sizeObj;
    public int sizeSub;
    public discretionaryModel(){
        this.obj = new Vector<String>();
        this.sub = new Vector<String>();
        this.sizeObj = 0;
        this.sizeSub = 0;
        ListRight = new ArrayList<>();
    }
    public void addObj(String obj_){
        obj.add(sizeObj, obj_);
        addRight("rwo", 0, obj.indexOf(obj_), "root", obj_, "root");
        sizeObj++;
    }
    public void removeObj(String obj_){
        obj.remove(obj_);
        for (int i = 0; i < ListRight.size(); i++) {
            if(ListRight.get(i).nameObj.equals(obj_)){
                removeRight(ListRight.get(i).indexSub, ListRight.get(i).indexObj, new char[]{ListRight.get(i).r, ListRight.get(i).w, ListRight.get(i).o}, "root");
            }
        }
        sizeObj--;
    }
    public void addSub(String sub_){
        sub.add(sizeSub, sub_);
        sizeSub++;
    }
    public void removeSub(String sub_){
        sub.remove(sub_);
        sizeSub--;
    }
    public boolean addRight(String right, int isub_, int iobj_, String nameSub, String nameObj, String whoAdd){
        //Проверка на наличие в матрице суб и объекта
        int check = 0;
        for (int i = 0; i < sub.size(); i++) {
            if (sub.get(i).equals(nameSub)) check++;
        }
        for (int i = 0; i < obj.size(); i++) {
            if (obj.get(i).equals(nameObj)) check++;
        }

        //Проверка на наличия права на добавление
        if(!(whoAdd == "root")) {
            int c = 0;
            for (int i = 0; i < ListRight.size(); i++) {
                if (ListRight.get(i).o == 'o' && ListRight.get(i).nameObj.equals(nameObj) && ListRight.get(i).nameSub.equals(whoAdd)) {
                    c++;
                }
            }
                if (c == 0) {
                    return false;
                }

        }

        char[] rt = right.toCharArray();
            if (check == 2) {
                for (int i = 0; i < ListRight.size(); i++) {
                    //Проверка на наличие в ячейке существуещего права...если есть удаляем / если нет дальше / если удалить не получилось ошибка
                    if (ListRight.get(i).indexSub == isub_ && ListRight.get(i).indexObj == iobj_ ) {
                        removeRight(isub_, iobj_, new char[]{ListRight.get(i).r, ListRight.get(i).w, ListRight.get(i).o}, whoAdd);
                    }
                }
                //Добавление права
                Access ac = new Access(rt[0], rt[1], rt[2],  isub_, iobj_, nameSub, nameObj);
                ListRight.add(ac);
                return true;
            }
            else return false;

    }
    public Access getRight(int isub_, int iobj_){
        for(int i = 0; i < ListRight.size();i++){
            if(ListRight.get(i).indexSub == isub_ && ListRight.get(i).indexObj == iobj_){
                return ListRight.get(i);
            }
        }
        return null;

    }
    public boolean removeRight(int isub_, int iobj_, char[] right, String whoRemove){
        //System.out.println(isub_+" "+iobj_+" "+whoRemove+" "+Arrays.toString(right));
        for(int i = 0; i < ListRight.size();i++){
            //System.out.println(ListRight.get(i).indexSub+" "+ListRight.get(i).indexObj+" "+ListRight.get(i).nameSub+" "+ListRight.get(i).o+" "+ListRight.get(i).alfa);
            if(ListRight.get(i).indexSub == isub_ && ListRight.get(i).indexObj == iobj_ && ListRight.get(i).r == right[0]
                    && ListRight.get(i).w == right[1] && ListRight.get(i).o == right[2]){
                for(int j = 0; j < ListRight.size();j++){
                    if(ListRight.get(j).o == 'o' && ListRight.get(j).nameSub == whoRemove) {
                        ListRight.remove(i);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

class Access{
    char r, w, o;
    int indexSub;
    int indexObj;
    String nameSub;
    String nameObj;
    public Access(char r, char w, char o, int iSub, int iObj, String nameSub, String nameObj){
        this.r = r;
        this.w = w;
        this.o = o;
        this.indexObj = iObj;
        this.indexSub = iSub;
        this.nameSub = nameSub;
        this.nameObj = nameObj;
    }
}

