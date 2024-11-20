package org.example;

public class Argument {
    private String name;

    public Argument(String name){
        this.name=name;
        if(this.name.equals("--help") || this.name.equals("-h")){
            help();
        }
        if(this.name.equals("-d") || this.name.equals("--directory")){
            help();
        }
    }

    public void help(){
        String s="ce prog est un projet";
        System.out.println(s);
    }


}
