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

        java.lang.String parentPath = folder.getParent();
        File allRef = new File(parentPath + "/all_refactorings.csv");
        BufferedReader br = new BufferedReader(new FileReader(allRef));

        File dataDir = new File(parentPath + "/" + folder.getName() + "_data/csv/");
        try {
            if (!dataDir.exists()) {
                throw new Exception();
            }
            FileWriter f = new FileWriter(parentPath + "/" + folder.getName() + "/" + type + ".csv");
            PrintWriter p = new PrintWriter(new BufferedWriter(f));

            while ((line = br.readLine()) != null) {
                data = line.split("#", 0);
                if (data[0].length() == 40 && data[1].equals(type)) {
                    File csvFile = new File(dataDir + data[1] + ".csv");
                    BufferedReader csvbr = new BufferedReader(new FileReader(csvFile));
                    while ((csvLine = csvbr.readLine()) != null){
                        csvData = csvLine.split("#", 0);
                        if (csvData[0].equals("(default)") || csvData[1].equals(".UNKNOWN")){
                            continue;
                        }
                        if (data[2].equals(csvData[0] + "." + csvData[1])){
                            String methodData = data[3].substring(data[3].indexOf(" ")+1, data[3].lastIndexOf(":"));
                            String methodName = methodData.substring(0, methodData.indexOf("("));

                            refCount = (int) count(methodData, ',');
                            dataCount = (int) count(csvData[2], ',');
                            if (methodName.equals(csvData[2].substring(0, csvData[2].indexOf("("))) && refCount == dataCount){

                            }
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
