package com.colaman.kproject;

/**
 * Author   : kyle
 * Date     : 2020/4/4
 * Function : test
 */
public class Test {
    public static void main(String[] args) {
        int i=5,j=1;
        j=i--;
        System.out.println("i = "+i);
        System.out.println("j = "+j);
        String value = "123";
        change(value);
        System.out.println("value = "+value);
    }

    public static void  change(String value){
        value="b";
    }
}
