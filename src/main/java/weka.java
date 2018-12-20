import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.File;

public class weka {
    public static void weka(File folder, int sel) throws Exception{
        java.lang.String parentPath = folder.getParent();
        File dir = new File(parentPath);
        File[] files = dir.listFiles();
        parameter pm = new parameter();
        String[] refactoringType = pm.getRefactoringTypeList();
        String targetFile;
        switch (refactoringType[sel]){
            case "Extract Method":
                System.out.println("Extract Method===============");
                targetFile = searchTargetFile(files, "Extract");
                break;
            case "Move Method":
                targetFile = searchTargetFile(files, "Move");
                System.out.println("Move Method===============");
                break;
            case "Inline Method":
                targetFile = searchTargetFile(files, "Inline");
                System.out.println("Inline Method===============");
                break;
            default:
                targetFile = searchTargetFile(files, "Extract");
        }
        try {
            File targetFolder = new File(targetFile);

            File arffFile = csv2arff(new File(targetFile));
            runWeka.runWeka(arffFile.getPath());

        } catch (Exception e) {
            System.err.println("don't find target file");
        }
    }
    public static void weka(File folder) throws Exception {
        int sel = select.selection();
        weka(folder, sel);
    }

    private static String searchTargetFile(File[] files, String target){
        for (File file : files) {
            if (file.getName().matches(".*" + target + ".*" + ".csv")) {
                return file.getPath();
            }
        }
        return null;
    }

    private static File csv2arff(File target) throws Exception {
        // load CSV
        CSVLoader loader = new CSVLoader();
        loader.setSource(target);
        Instances data = loader.getDataSet();
        // save ARFF
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        File arffFile = new File(target.getPath().replaceFirst("\\.csv", "\\.arff"));
        saver.setFile(arffFile);
        saver.writeBatch();
        // .arff file will be created in the output location
        return arffFile;
    }
}