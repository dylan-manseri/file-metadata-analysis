public class Arguments {
    private String[] arguments;

    public Arguments(String[] args){
        this.arguments=args;
        if(this.arguments[0].equals("--help") || this.arguments[0].equals("-h")){
            help();
        }
        if(this.arguments[0].equals("-d") || this.arguments[0].equals("--directory")){
            help();
        }
    }

    public void help(){
        String s="ce prog est un projet";
        System.out.println(s);
    }


}
