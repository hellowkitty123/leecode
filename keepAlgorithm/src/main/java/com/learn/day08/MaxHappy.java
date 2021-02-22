package com.learn.day08;

import java.util.ArrayList;

public class MaxHappy {
    public static class Employee{
        ArrayList<Employee> nexts;
        int happy;
        Employee(int happy){
            this.happy = happy;
        }
    }
    // yes x 来的最大快乐值
    // no  x 不来的最大快乐值
    public static class Info{
        int yes;
        int no ;
        Info(int yes ,int no){
            this.yes = yes;
            this.no = no;
        }
    }

    public static int getMaxHappy(Employee x){
        if (x == null){
            return 0;
        }
        Info info = process(x);

        return Math.max(info.yes,info.no);
    }

    public static Info process(Employee x){
        if (x.nexts == null){
            return new Info(x.happy,0);
        }
        int yes = x.happy;
        int no = 0;
        // x 下属 在x做完决定之后 happy贡献值
        //    x 来的 nexts 都会来
        //    x 不来 nexts 可能来 ，可能不来
        for (Employee e : x.nexts){
            Info info = process(e);
            yes += info.no;
            no += Math.max(info.yes,info.no);
        }

        return new Info(yes,no);
    }


    public static void main(String[] args) {

    }
}
