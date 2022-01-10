import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Program {


    public static void main(String[] args){
        int n = 0;
        String string = StdIn.readLine();
        /* Если необходимо удалить знаки препинания
        String Rstring = string.replaceAll("[^A-Za-zА-Яа-я0-9 ]", "").toLowerCase(Locale.ROOT);
        System.out.println(Rstring);
        //System.exit(-1);

         */
        List<String> list = new ArrayList<>();
        char[] charArray = string.toCharArray();
        System.out.println("Dlina "+(charArray.length));
        String newStr = "";
        System.out.println("Kajday 6 stroka ");
        for(int i = 1; i < charArray.length; i++){
            newStr+=charArray[i];
            if((i%6 == 0 || i == charArray.length-1) && i != 0){
                list.add(newStr);
                newStr = "";
            }
        }
        for(String s : list){
            System.out.println(s);
        }
        System.out.println("Частота всего");
        HashMap<Character, Integer> chastota = new HashMap<>();
        for(int i = 1; i < charArray.length;i++){
            if(chastota.get(charArray[i]) == null){
                chastota.put(charArray[i], 1);
            }
            else chastota.put(charArray[i], chastota.get(charArray[i])+1);
        }
        System.out.println(chastota.toString());
        HashMap<Character, Integer> chastota1 = new HashMap<>();
        HashMap<Character, Integer> chastota2 = new HashMap<>();
        HashMap<Character, Integer> chastota3 = new HashMap<>();
        HashMap<Character, Integer> chastota4 = new HashMap<>();
        HashMap<Character, Integer> chastota5 = new HashMap<>();
        HashMap<Character, Integer> chastota6 = new HashMap<>();
        HashMap<Character, Integer> chastota7 = new HashMap<>();
        for(String s : list) {
            char[] charArray_ = s.toCharArray();
            for (int i = 0; i < charArray_.length; i++) {
                switch (i){
                    case 0:
                        if (chastota1.get(charArray_[i]) == null) {
                            chastota1.put(charArray_[i], 1);
                        } else chastota1.put(charArray_[i], chastota1.get(charArray_[i]) + 1);
                        break;
                    case 1:
                        if (chastota2.get(charArray_[i]) == null) {
                            chastota2.put(charArray_[i], 1);
                        } else chastota2.put(charArray_[i], chastota2.get(charArray_[i]) + 1);
                        break;
                    case 2:
                        if (chastota3.get(charArray_[i]) == null) {
                            chastota3.put(charArray_[i], 1);
                        } else chastota3.put(charArray_[i], chastota3.get(charArray_[i]) + 1);
                        break;
                    case 3:
                        if (chastota4.get(charArray_[i]) == null) {
                            chastota4.put(charArray_[i], 1);
                        } else chastota4.put(charArray_[i], chastota4.get(charArray_[i]) + 1);
                        break;
                    case 4:
                        if (chastota5.get(charArray_[i]) == null) {
                            chastota5.put(charArray_[i], 1);
                        } else chastota5.put(charArray_[i], chastota5.get(charArray_[i]) + 1);
                        break;
                    case 5:
                        if (chastota6.get(charArray_[i]) == null) {
                            chastota6.put(charArray_[i], 1);
                        } else chastota6.put(charArray_[i], chastota6.get(charArray_[i]) + 1);
                        break;
                    case 6:
                        if (chastota7.get(charArray_[i]) == null) {
                            chastota7.put(charArray_[i], 1);
                        } else chastota7.put(charArray_[i], chastota7.get(charArray_[i]) + 1);
                        break;
                }

            }
        }
        System.out.println("_______________________________________________");
        System.out.println(chastota1.toString());
        System.out.println(chastota2.toString());
        System.out.println(chastota3.toString());
        System.out.println(chastota4.toString());
        System.out.println(chastota5.toString());
        System.out.println(chastota6.toString());
//        System.out.println(chastota7.toString());
        System.out.println("_______________________________________________");
    }
}
