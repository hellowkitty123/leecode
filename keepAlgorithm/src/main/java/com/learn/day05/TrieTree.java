package com.learn.day05;

import java.util.HashMap;

public class TrieTree {
    public static class Node{
        HashMap<Integer,Node> nexts;
        int pass;
        int end;
        Node(){
            nexts = new HashMap<>();
            pass = 0;
            end = 0;
        }
    }

    public static class TireTree1{
        Node root;
        TireTree1(){
            root = new Node();
            root.pass = 0;
            root.end = 0;
        }

        public void insert(String word){
            if (word == null ||word.equals("")){
                return;
            }
            char[] str = word.toCharArray();
            Node node = this.root;
            node.pass +=1;
            for (char c : str) {
                if (!node.nexts.containsKey((int) c)) {
                    node.nexts.put((int) c, new Node());
                }
                Node t = node.nexts.get((int) c);
                t.pass += 1;
                node.nexts.put((int) c, t);
                node = node.nexts.get((int) c);
            }
            node.end += 1;
        }

        public int search(String word){
            if (word == null ||word.equals("")){
                return 0;
            }
            Node node = this.root;


            char[] str = word.toCharArray();
            for (char c : str) {
                if (!node.nexts.containsKey((int)c)){
                    return 0;
                }
                node = node.nexts.get((int)c);

            }
            return node.end;
        }


        public void delete(String word){
            if (word == null ||word.equals("")){
                return;
            }
            if (search(word) <= 0 ){
                return;
            }
            Node node = this.root;
            char[] str = word.toCharArray();
            for(char c: str){
                // 如果nexts（key） 后面没有了，直接断开
                Node t = node.nexts.get((int)c);
                if (--t.pass == 0 ){
                    node.nexts.put((int)c,null);
                    return;
                }
                node.nexts.put((int)c,t);
                node = t;
            }
            node.end -=1;

        }

        public int preifxNumber(String word){
            if (word == null ){
                return 0;
            }
            Node node = this.root;

            if (word.equals("")){
                return node.pass;
            }
            char[] str = word.toCharArray();
            for(char c : str){
                node = node.nexts.get((int)c);
           }
            return node.pass;
        }
    }
    public static void main(String[] args) {
        TireTree1 tireTree1 = new TireTree1();
        tireTree1.insert("ab");
        tireTree1.insert("abc");
        tireTree1.insert("abcdef");
        tireTree1.insert("bc");
        tireTree1.insert("bcde");
        tireTree1.insert("cde");

        int num = tireTree1.preifxNumber("");
        System.out.println(num);

        tireTree1.delete("ab");
        num = tireTree1.search("abc");
        System.out.println(num);


    }
}
