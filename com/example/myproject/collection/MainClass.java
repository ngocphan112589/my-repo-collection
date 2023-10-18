package com.example.myproject.collection;

public class MainClass {
    public static void main(String[] args) {
        CLinkedList<String> list = new CLinkedList<String>();
        list.add("a1");
        list.add("a3");
        list.add("a2");
        list.add("a8");
        list.add("a2");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
        //test addFirst - add Last
        list.addFirst("First");
        list.addLast("element Last");
        System.out.println("after:");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
        // testing push
        list.push("a9");
        System.out.println("List after remove:");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
        //testing iterator
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()){
            String a = iterator.next();
            if (a.equals("a2")){
                iterator.remove();
            }
        }
        System.out.println("------------------");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }
}
