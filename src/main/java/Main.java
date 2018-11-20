
import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
//        args = new String[2];
//        args[0] = "-c";
//        args[1] = "/Users/lynn/java-exp/ant/ant";
        if(args.length < 1){
            throw new IllegalArgumentException("Type 'RefactorRec -h' to show usage");
        }
        String option = args[0];
        if (option.equalsIgnoreCase("-h") || option.equalsIgnoreCase("--h")
                || option.equalsIgnoreCase("-help") || option.equalsIgnoreCase("--help")) {
            printTips();
        } else if(option.equalsIgnoreCase("-r")){
            fromGetRefactor(args);
        } else if(option.equalsIgnoreCase("-m")){
            fromGetMetrics(args);
        } else if(option.equalsIgnoreCase("-p")){
            fromParseXML(args);
        } else if (option.equalsIgnoreCase("-c")){
            fromCorresponsive(args);
        } else {
            printTips();
        }
    }

    private static void fromGetRefactor(String[] args) throws Exception {
        System.out.println("start with Refactoring detection.");
        getData.getRefData(new File(args[1]));
        fromGetMetrics(args);
    }

    private static void fromGetMetrics(String[] args) throws Exception {
        System.out.println("start with metrics collection.");
        getData.getMetrics(new File(args[1]));
        fromParseXML(args);
    }

    private static void fromParseXML(String[] args) throws Exception {
        System.out.println("start with parse XML files.");
        parseXML.parseXML(new File(args[1]));
        fromCorresponsive(args);
    }

    private static void fromCorresponsive(String[] args) throws Exception {
        System.out.println("start with correspond the output file of Refactoring Miner with the output file of jxMetrics.");
        corresponsive cor = new corresponsive();
        if (args.length == 3) {
            cor.corresponsive(new File(args[1]), Integer.parseInt(args[2]));
        }
        cor.corresponsive(new File(args[1]));
    }

    private static void printTips(){
        System.out.println("-h\t\t\tShow tips");
        System.out.println("-r <full path of target directory>\tRefactoring detection, when there is no file in which Refactoring has been detected.");
        System.out.println("-m <full path of target directory>\tMetrics calculator, when you already have a file of detected Refactoring");
        System.out.println("-p <full path of target directory>\tparseXML, when you already have a file of metrics(.xml)");
        System.out.println("-c <full path of target directory>\tcorresponsive, when you already have a file of metrics(.csv)");
    }
}