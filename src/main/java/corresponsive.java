import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class corresponsive {
    public void corresponsive(File folder) throws IOException {

        corresponsive(folder, select.selection());
    }

    public void corresponsive(File folder, int x) throws IOException {
        parameter pm = new parameter();
        String[] refactoringType = pm.getRefactoringTypeList();

        switch (refactoringType[x]){
            case "Extract Method":
                corresMethod(folder, "Extract Method");
                break;
            case "Move Method":
                corresMethod(folder, "Move Method");
                break;
            case "Inline Method":
                corresMethod(folder, "Inline Method");
                break;
            default:
                corresMethod(folder, "Extract Method");
        }
    }

    private int count(java.lang.String str, char target){
        int count = 0;
        for (char x: str.toCharArray()){
            if (x == target){
                count++;
            }
        }
        return count;
    }

    private void corresMethod(File folder, String type) {
        java.lang.String line;
        java.lang.String csvLine;
        java.lang.String parentPath = folder.getParent();

        int pass = 4;
        // get parameters
        parameter pm = new parameter();
        pm.makeList();
        String[] metricsList = pm.getMetricsList();
        String[] methodMetricsList = pm.getMethodMetricsList();
        String[] classMetricsList = pm.getClassMetricsList();
        String separate = pm.getSeparater();
        String sepOutput = ",";
        //***

        try {
            File dataDir = new File(parentPath + "/" + folder.getName() + "_data/csv/");
            if (!dataDir.exists()) {
                throw new Exception();
            }

            // define csv file for each type & output csv header
            FileWriter f = new FileWriter(parentPath + "/" + folder.getName() + "_" + type.replace(" ", "_") + ".csv");
            PrintWriter p = new PrintWriter(new BufferedWriter(f));
//            p.print("CommitId");
//            p.print(sepOutput);
            for (String s : classMetricsList) {
                p.print("class");
                p.print(s);
                p.print(sepOutput);
            }
            for (String s : methodMetricsList) {
                p.print(s);
                p.print(sepOutput);
            }
            p.println("label");
            ////*********

            // for reading the file output by Refactoring Miner
            File allRef = new File(parentPath + "/all_refactorings.csv");
            BufferedReader br = new BufferedReader(new FileReader(allRef));
            //***

            while ((line = br.readLine()) != null) {
                java.lang.String[] data = line.split(separate, 0);
                if (data[0].length() == 40 && data[1].matches(".*" + type + ".*")) {
                    File csvFile = new File(dataDir + "/" + data[0] + ".csv");
                    BufferedReader csvBr = new BufferedReader(new FileReader(csvFile));

                    while ((csvLine = csvBr.readLine()) != null){ // check output file by jxMetrics
                        java.lang.String[] csvData = csvLine.split(separate, 0);
                        if (csvData[0].equals("(default)") || csvData[1].equals(".UNKNOWN")){
                            continue;
                        }
                        if (data[2].equals(csvData[1])){ // exist in the same class
                            String methodName = data[3].substring(data[3].indexOf(" ") + 1, data[3].indexOf("(")); // name without arg

                            int refCount = count(data[3], ',');
                            int dataCount = count(csvData[2], ',');

                            // in any case, data required
//                            p.print(data[0]); //output Commit ID
//                            p.print(sepOutput);
                            for (int m = metricsList.length-pass; m > 0; m--){
                                p.print(csvData[csvData.length - m]);
                                p.print(sepOutput);
                            }
                            if (methodName.equals(csvData[2].substring(0, csvData[2].indexOf("("))) && refCount == dataCount){
                                p.print("true"); // if it is target, output 1
                            } else {
                                p.print("false");
                            }
                            p.println();
                            //********

                        }
//                        else if (data[2].lastIndexOf(".") != -1 ? data[2].substring(0, data[2].lastIndexOf(".")).equals(csvData[0])
//                                : data[2].equals(csvData[0])){ //exist in the same package
////                            p.print(data[0]); //output Commit ID
////                            p.print(sepOutput);
//                            for (int m = metricsList.length-pass; m > 0; m--){
//                                p.print(csvData[csvData.length - m]);
//                                p.print(sepOutput);
//                            }
//                            p.print("0");
//                            p.println();
//                        }
                    }
                }
            }
            p.close();
        }
        catch (Exception e){
            System.out.println("csv directory is not exist");
            e.printStackTrace();
        }
    }
}
