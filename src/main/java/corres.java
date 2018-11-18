import java.io.*;

public class corres {
    private int count(java.lang.String str, char target){
        int count = 0;
        for (char x: str.toCharArray()){
            if (x == target){
                count++;
            }
        }
        return count;
    }

    public void corresMethod(File folder, String type) throws IOException {
        java.lang.String line;
        java.lang.String[] data;

        java.lang.String csvLine;
        java.lang.String[] csvData;
        int refCount = 0;
        int dataCount = 0;

        parameter pm = new parameter();
        pm.makeList();
        String[] metricsList = pm.getMetricsList();
        String separate = pm.getSeparater();

        java.lang.String parentPath = folder.getParent();
        File allRef = new File(parentPath + "/all_refactorings.csv");
        BufferedReader br = new BufferedReader(new FileReader(allRef));

        File dataDir = new File(parentPath + "/" + folder.getName() + "_data/csv/");
        try {
            if (!dataDir.exists()) {
                throw new Exception();
            }

            //*** define csv file for each type & output csv header
            FileWriter f = new FileWriter(parentPath + "/" + folder.getName() + "/" + type + ".csv");
            PrintWriter p = new PrintWriter(new BufferedWriter(f));
            for (int i = 0; i < metricsList.length; i++){
                p.print(metricsList[i]);
                p.print(separate);
            }
            p.print("label");
            ////*********

            while ((line = br.readLine()) != null) {
                data = line.split(separate, 0);
                if (data[0].length() == 40 && data[1].equals(type)) {
                    File csvFile = new File(dataDir + data[1] + ".csv");
                    BufferedReader csvBr = new BufferedReader(new FileReader(csvFile));
                    while ((csvLine = csvBr.readLine()) != null){
                        csvData = csvLine.split("#", 0);
                        if (csvData[0].equals("(default)") || csvData[1].equals(".UNKNOWN")){
                            continue;
                        }
                        if (data[2].equals(csvData[0] + "." + csvData[1])){ // exist in the same class
                            String methodData = data[3].substring(data[3].indexOf(" ")+1, data[3].lastIndexOf(":")); // name with arg
                            String methodName = methodData.substring(0, methodData.indexOf("(")); // name without arg

                            refCount = (int) count(methodData, ',');
                            dataCount = (int) count(csvData[2], ',');

                            // in any case, data required
                            p.print(csvFile.getName().substring(0, csvFile.getName().lastIndexOf('.'))); //output Commit ID
                            p.print(separate);
                            p.print(data);
                            p.print(separate);

                            if (methodName.equals(csvData[2].substring(0, csvData[2].indexOf("("))) && refCount == dataCount){
                                p.print(1); // if it is target output 1
                            } else {
                                p.print(0);
                            }
                            p.println();
                            //********

                        } else if (data[2].substring(0, data[2].lastIndexOf(".")).equals(csvData[0])){ //exist in the same package
                            p.print(csvFile.getName().substring(0, csvFile.getName().lastIndexOf('.'))); //output Commit ID
                            p.print(separate);
                            p.print(data);
                            p.print(separate);
                            p.print(0);
                            p.println();
                        }
                    }
                }
            }
        }
        catch (Exception e){
            System.out.println("csv directory is not exist");
        }
    }
}
