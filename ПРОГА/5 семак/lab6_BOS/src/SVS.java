import java.util.*;

public class SVS {
    static public List<Object> listObj;
    static public List<Container> listCont;
    static public List<Subject> listSub;
    static public List<String> listRoles;
    static public HashMap<String, String> convert = new HashMap<String, String>();
    static public HashMap<String, Integer> convertToINT = new HashMap<String, Integer>();
    static public List<String> history = new ArrayList<>();

    public static void main(String[] args) {
        convertToINT.put("NS", 0);
        convertToINT.put("S", 1);
        convertToINT.put("SS", 2);


        convert.put("R", "NS");
        convert.put("S", "NS");
        convert.put("L", "S");
        convert.put("G", "SS");
        convert.put("SO", "SS");
        listRoles = new ArrayList<>();
        listRoles.add("R");
        listRoles.add("S");
        listRoles.add("L");
        listRoles.add("G");
        listRoles.add("SO");

        listSub = new ArrayList<>();
        listObj = new ArrayList<>();
        listCont = new ArrayList<>();

        Subject offS = new Subject("Admin", "SO");
        listSub.add(offS);
        System.out.println("Admin: ");
        System.out.println("create sub: name role\n" +
                "stop: end");
        System.out.println("roles: " + listRoles.toString());
        String str = StdIn.readLine();
        while (!str.equals("end")) {
            String[] keys = str.split(" ");
            Subject s = new Subject(keys[0], keys[1]);
            listSub.add(s);
            str = StdIn.readLine();
        }
        /*
        crO
        crC
        crOinC
        rO
        wO
        rC
        wC
        delOinC
        show
        history

         */
        while (true) {
            System.out.println("login: ");
            String log_name = StdIn.readLine();
            Subject cur_log = null;
            for (Subject s : listSub) {
                if (s.getName().equals(log_name)) {
                    cur_log = s;
                    history.add("login: " + cur_log.getName());
                }
            }
            if (cur_log != null) {
                while (cur_log != null) {
                    System.out.println("Вход " + cur_log.getName());
                    String[] command = StdIn.readLine().split(" ");
                    switch (command[0]) {
                        case "crO":
                            if (cur_log.getRole().equals("R")) {
                                System.out.println("У вас недостаточно прав для создания объекта");
                            } else {
                                System.out.println("Enter:\n" +
                                        "name value policy(default role)");
                                String[] param = StdIn.readLine().split(" ");
                                String policy = convert.get(cur_log.getRole());
                                Object o;
                                if (param.length == 2) {
                                    o = new Object(param[0], param[1], policy);
                                } else {
                                    if (convertToINT.get(convert.get(cur_log.getRole())) >= convertToINT.get(param[2]))
                                        o = new Object(param[0], param[1], param[2]);
                                    else {
                                        o = new Object(param[0], param[1], policy);
                                        System.out.println("Невозможно добавление объекта с такой меткой, метка меняется на по умолчанию для вашей роли");
                                    }
                                }
                                listObj.add(o);
                                history.add(cur_log.getName() + " " + cur_log.getRole() + " " + " create obj name = " +
                                        "" + o.getName() + " value = " + o.getValue() + " policy " + o.getPolicy());
                            }
                            break;
                        case "crC":
                            if (cur_log.getRole().equals("R")) {
                                System.out.println("У вас недостаточно прав для создания контейнера");
                            } else {
                                System.out.println("Enter data container:\n" +
                                        "name SSR(ALL or ONE");
                                String[] param = StdIn.readLine().split(" ");
                                String policy = convert.get(cur_log.getRole());
                                Container c = new Container(param[0], param[1]);
                                System.out.println("Enter number obj container:");
                                int number = StdIn.readInt();
                                StdIn.readLine();
                                String all_obj = "";
                                for (int i = 0; i < number; i++) {
                                    System.out.println("Enter objects container:\n" +
                                            "name value policy(default role)");
                                    param = StdIn.readLine().split(" ");
                                    Object o;
                                    if (param.length == 2) {
                                        o = new Object(param[0], param[1], policy);
                                    } else {
                                        if (convertToINT.get(convert.get(cur_log.getRole())) >= convertToINT.get(param[2]))
                                            o = new Object(param[0], param[1], param[2]);
                                        else {
                                            o = new Object(param[0], param[1], policy);
                                            System.out.println("Невозможно добавление объекта с такой меткой, метка меняется на по умолчанию для вашей роли");
                                        }
                                    }
                                    c.setOneList(o);
                                    all_obj += " name = " +
                                            "" + o.getName() + " value = " + o.getValue() + " policy " + o.getPolicy() + " ";
                                }
                                listCont.add(c);
                                history.add(cur_log.getName() + " " + cur_log.getRole() + " " + " create container name = " +
                                        "" + c.getName() + " SSR = " + c.SSR + " " + c.getPolicy() + " value: " + all_obj);
                            }
                            break;
                        case "crOinC":
                            if (cur_log.getRole().equals("R")) {
                                System.out.println("У вас недостаточно прав для создания объектов");
                            } else {
                                System.out.println("Enter data container:\n" +
                                        "name \"numberObj\"");
                                String[] param = StdIn.readLine().split(" ");
                                String policy = convert.get(cur_log.getRole());
                                Container container = null;
                                for (Container c : listCont) {
                                    if (c.getName().equals(param[0])) {
                                        container = c;
                                    }
                                }
                                int number = Integer.parseInt(param[1]);
                                String all_obj = "";
                                if (container != null) {
                                    if (container.SSR.equals("ALL")) {
                                        if (convertToINT.get(convert.get(cur_log.getRole())) >= convertToINT.get(container.getPolicy())) {
                                            for (int i = 0; i < number; i++) {
                                                System.out.println("Enter objects container:\n" +
                                                        "name value policy(default role)");
                                                param = StdIn.readLine().split(" ");
                                                Object o;
                                                if (param.length == 2) {
                                                    o = new Object(param[0], param[1], policy);
                                                } else {
                                                    if (convertToINT.get(convert.get(cur_log.getRole())) >= convertToINT.get(param[2]))
                                                        o = new Object(param[0], param[1], param[2]);
                                                    else {
                                                        o = new Object(param[0], param[1], policy);
                                                        System.out.println("Невозможно добавление объекта с такой меткой, метка меняется на по умолчанию для вашей роли");
                                                    }
                                                }
                                                container.setOneList(o);
                                                all_obj += " name = " +
                                                        "" + o.getName() + " value = " + o.getValue() + " policy " + o.getPolicy() + " ";
                                            }
                                            history.add(cur_log.getName() + " " + cur_log.getRole() + " " + " update container name = " +
                                                    "" + container.getName() + " SSR = " + container.SSR + " (update) " + container.getPolicy() + " (update) value: " + all_obj);
                                        } else {
                                            System.out.println("Невозможно добавление объекта, ваша роль не позволяет добавления в данный контейнер");
                                        }
                                    } else {
                                        for (int i = 0; i < number; i++) {
                                            System.out.println("Enter objects container:\n" +
                                                    "name value policy(default role)");
                                            param = StdIn.readLine().split(" ");
                                            Object o;
                                            if (param.length == 2) {
                                                o = new Object(param[0], param[1], policy);
                                            } else {
                                                if (convertToINT.get(convert.get(cur_log.getRole())) >= convertToINT.get(param[2]))
                                                    o = new Object(param[0], param[1], param[2]);
                                                else {
                                                    o = new Object(param[0], param[1], policy);
                                                    System.out.println("Невозможно добавление объекта с такой меткой, метка меняется на по умолчанию для вашей роли");
                                                }
                                            }
                                            container.setOneList(o);
                                            all_obj += " name = " +
                                                    "" + o.getName() + " value = " + o.getValue() + " policy " + o.getPolicy() + " ";
                                        }
                                        history.add(cur_log.getName() + " " + cur_log.getRole() + " " + " update container name = " +
                                                "" + container.getName() + " SSR = " + container.SSR + " (update) " + container.getPolicy() + " (update) value: " + all_obj);
                                    }
                                } else {
                                    System.out.println("Не существует " + param[0]);
                                }
                            }
                            break;
                        case "rO":
                            System.out.println("Enter data obj:\n" +
                                    "name");
                            String param = StdIn.readLine();
                            for (Object o : listObj) {
                                if (o.getName().equals(param)) {
                                    if (convertToINT.get(o.getPolicy()) <= convertToINT.get(convert.get(cur_log.getRole()))) {
                                        System.out.println(o.getName() + " " + o.getPolicy() + " value: " + o.getValue());
                                        history.add(cur_log.getName() + " " + cur_log.getRole() + " readObj " + o.getName() + " " + o.getPolicy());
                                    } else {
                                        System.out.println("У вас недостаточно прав для чтения объектов");
                                    }
                                }
                            }
                            break;
                        case "wO":
                            if (cur_log.getRole().equals("R")) {
                                System.out.println("У вас недостаточно прав для записи R");
                            } else {
                                System.out.println("Enter data obj:\n" +
                                        "name");
                                param = StdIn.readLine();
                                for (Object o : listObj) {
                                    if (o.getName().equals(param)) {
                                        if (convertToINT.get(o.getPolicy()) <= convertToINT.get(convert.get(cur_log.getRole()))) {
                                            System.out.println("Enter value obj:\n" +
                                                    "value");
                                            String value = StdIn.readLine();
                                            o.setValue(value);
                                            o.setPolicy(convert.get(cur_log.getRole()));
                                            history.add(cur_log.getName() + " " + cur_log.getRole() + " writeObj " + o.getName() + " (update) " + o.getPolicy() + " (update) " + o.getValue());
                                        } else {
                                            System.out.println("У вас недостаточно прав для записи M");
                                        }
                                    }
                                }
                            }
                            break;
                        case "rC":

                                System.out.println("Enter data cont:\n" +
                                        "name");
                                param = StdIn.readLine();
                                for (Container c : listCont) {
                                    if (c.getName().equals(param)) {
                                        if (c.SSR.equals("ALL")) {
                                            if (convertToINT.get(convert.get(cur_log.getRole())) >= convertToINT.get(c.getPolicy())) {
                                                System.out.println("Enter name obj");
                                                String name = StdIn.readLine();
                                                for (Object o : c.getList()) {
                                                    if (o.getName().equals(name)) {
                                                        System.out.println(o.getName() + " " + o.getPolicy() + " value: " + o.getValue());
                                                        history.add(cur_log.getName() + " " + cur_log.getRole() + " readObj " + o.getName() + " " + o.getPolicy() + " in cont " + c.getName() + " " +
                                                                "" + c.SSR + " " + c.getPolicy());
                                                        break;
                                                    }
                                                }
                                            } else {
                                                System.out.println("Невозможно чтение объекта, ваша роль не позволяет читать данные в контейнере");
                                            }
                                        } else {
                                            System.out.println("Enter name obj");
                                            String name = StdIn.readLine();
                                            for (Object o : c.getList()) {
                                                if (o.getName().equals(name)) {
                                                    if (convertToINT.get(convert.get(cur_log.getRole())) >= convertToINT.get(o.getPolicy())) {
                                                        System.out.println(o.getName() + " " + o.getPolicy() + " value: " + o.getValue());
                                                        history.add(cur_log.getName() + " " + cur_log.getRole() + " readObj " + o.getName() + " " + o.getPolicy() + " in cont " + c.getName() + " " +
                                                                "" + c.SSR + " " + c.getPolicy());
                                                        break;
                                                    } else {
                                                        System.out.println("Невозможно чтение данного объекта");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            break;
                        case "wC":
                            if (cur_log.getRole().equals("R")) {
                                System.out.println("У вас недостаточно прав для записи R");
                            } else {
                                System.out.println("Enter data cont:\n" +
                                        "name");
                                param = StdIn.readLine();
                                for (Container c : listCont) {
                                    if (c.getName().equals(param)) {
                                        if (c.SSR.equals("ALL")) {
                                            if (convertToINT.get(convert.get(cur_log.getRole())) >= convertToINT.get(c.getPolicy())) {
                                                System.out.println("Enter name obj");
                                                String name = StdIn.readLine();
                                                for (Object o : c.getList()) {
                                                    if (o.getName().equals(name)) {
                                                        System.out.println("Enter value obj:\n" +
                                                                "value");
                                                        String value = StdIn.readLine();
                                                        o.setValue(value);
                                                        o.setPolicy(convert.get(cur_log.getRole()));
                                                        c.updatePolicy();
                                                        history.add(cur_log.getName() + " " + cur_log.getRole() + " writeObj " + o.getName() + " (update) " + o.getPolicy() + " (update) " + o.getValue() + "" +
                                                                " in cont " + c.getName() + " " + c.SSR + " " + c.getPolicy());
                                                    }
                                                }
                                            } else {
                                                System.out.println("Невозможна запись, ваша роль не позволяет запись в контейнер");
                                            }
                                        } else {
                                            System.out.println("Enter name obj");
                                            String name = StdIn.readLine();
                                            for (Object o : c.getList()) {
                                                if (o.getName().equals(name)) {
                                                    if (convertToINT.get(convert.get(cur_log.getRole())) >= convertToINT.get(o.getPolicy())) {
                                                        System.out.println("Enter value obj:\n" +
                                                                "value");
                                                        String value = StdIn.readLine();
                                                        o.setValue(value);
                                                        o.setPolicy(convert.get(cur_log.getRole()));
                                                        c.updatePolicy();
                                                        history.add(cur_log.getName() + " " + cur_log.getRole() + " writeObj " + o.getName() + " (update) " + o.getPolicy() + " (update) " + o.getValue() + "" +
                                                                " in cont " + c.getName() + " " + c.SSR + " " + c.getPolicy());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        case "delOinC":
                            if (cur_log.getRole().equals("R")) {
                                System.out.println("У вас недостаточно прав для удаления R");
                            } else {
                                System.out.println("Enter data cont:\n" +
                                        "name");
                                param = StdIn.readLine();
                                for (Container c : listCont) {
                                    if (c.getName().equals(param)) {
                                        if (c.SSR.equals("ALL")) {
                                            if (convertToINT.get(convert.get(cur_log.getRole())) >= convertToINT.get(c.getPolicy())) {
                                                System.out.println("Enter name obj");
                                                String name = StdIn.readString();
                                                for (Object o : c.getList()) {
                                                    if (o.getName().equals(name)) {
                                                        c.removeOneList(o);
                                                        if(c.getList().size() == 0) {
                                                            listCont.remove(c);
                                                            break;
                                                        }
                                                        c.updatePolicy();
                                                        history.add(cur_log.getName() + " " + cur_log.getRole() + " delObj " + o.getName() + " " + o.getPolicy() + " " + o.getValue() + "" +
                                                                " in cont " + c.getName() + " " + c.SSR + " " + c.getPolicy());
                                                        break;
                                                    }
                                                }
                                            } else {
                                                System.out.println("Невозможна запись, ваша роль не позволяет запись в контейнер");
                                            }
                                        } else {
                                            System.out.println("Enter name obj");
                                            String name = StdIn.readString();
                                            for (Object o : c.getList()) {
                                                if (o.getName().equals(name)) {
                                                    if (convertToINT.get(convert.get(cur_log.getRole())) >= convertToINT.get(o.getPolicy())) {
                                                        c.removeOneList(o);
                                                        if(c.getList().size() == 0) {
                                                            listCont.remove(c);
                                                            break;
                                                        }
                                                        c.updatePolicy();
                                                        history.add(cur_log.getName() + " " + cur_log.getRole() + " delObj " + o.getName() + " " + o.getPolicy() + " " + o.getValue() + "" +
                                                                " in cont " + c.getName() + " " + c.SSR + " " + c.getPolicy());
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if(listCont.isEmpty()) break;
                                }
                            }
                            break;
                        case "show":
                            for (Subject s : listSub) {
                                System.out.print(s.getName() + " " + s.getRole() + " ");
                            }
                            System.out.println();
                            for (Object o : listObj) {
                                System.out.print(o.getName() + " " + o.getValue() + " " + o.getPolicy() + " ");
                            }
                            System.out.println();
                            for (Container c : listCont) {
                                System.out.print("Container: " + c.getName() + " " + c.SSR + " " + c.getPolicy() + " objects: ");
                                for (Object o : c.getList()) {
                                    System.out.print(o.getName() + " " + o.getValue() + " " + o.getPolicy() + " ");
                                }
                                System.out.println();
                            }
                            break;
                        case "logout":
                            history.add("logout: " + cur_log.getName());
                            cur_log = null;
                            break;
                        case "history":
                            for (String s : history) {
                                System.out.println(s);
                            }

                    }
                }
            } else {
                System.out.println("Нет пользователя, идентификатор не верный");
            }
        }
    }

}
