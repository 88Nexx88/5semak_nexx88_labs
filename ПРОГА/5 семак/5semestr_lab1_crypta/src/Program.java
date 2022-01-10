import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Program {
    public static void main(String[] args){
        String str = "Журавлёв Данила Андреевич";
        //String str = "Море";
        System.out.println("Current str: "+str);
        String[] result = encryptPermutation(str);
        System.out.println("Key: "+result[0]);
        System.out.println("SecretStr: "+result[1]);
        System.out.println("Reverse operation");
        String currentStr = decryptPermutation(result);
        System.out.println("Current str: "+currentStr);

    }
    public static String[] encryptPermutation(String str){
        char[] currentStr = str.toLowerCase().toCharArray();
        //System.out.println(currentStr.length);
        ArrayList<Integer> list = new ArrayList<Integer>();
        String secretStr = "";
        for (int i=1; i<=currentStr.length; i++) {
            list.add(new Integer(i));
        }
        Collections.shuffle(list);
        String key = "";
        for (int i=0; i < list.size(); i++) {
            int k = list.get(i)-1;
            key += k+" ";
            secretStr += currentStr[k];
        }
        //System.out.println(secretStr);
        //System.out.println(key);
        String[] result = {key, secretStr};
        return result;
    }
    public static String decryptPermutation(String[] parametrs){
        String[] strkey = parametrs[0].split(" ");
        String result = "";
        int[] key = new int[strkey.length];
        for(int i = 0; i <key.length; i++){
            key[i] = Integer.parseInt(strkey[i]);
            //System.out.print(key[i]+" ");
        }
        char[] strchar = parametrs[1].toCharArray();
        char[] currentStr = new char[strchar.length];
        for (int i=0; i<strchar.length; i++) {
            currentStr[key[i]] = strchar[i];

        }
        for(int i=0;i<strchar.length;i++){
            result+=currentStr[i];
        }

        return result;
    }
}

