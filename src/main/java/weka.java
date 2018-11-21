import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.FileReader;
import java.util.Random;

public class weka {
    public static void weka(File folder) throws Exception {
        java.lang.String parentPath = folder.getParent();
        File targetFolder = new File(parentPath + "/" + "ant_Extract_Method.csv");
        File arffFile = csv2arff(targetFolder);
        randomForest(arffFile);
    }

    private static File csv2arff(File folder) throws Exception {
        // load CSV
        CSVLoader loader = new CSVLoader();
        loader.setSource(folder);
        Instances data = loader.getDataSet();
        // save ARFF
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        File arffFile = new File(folder.getParent() + "/test.arff");
        saver.setFile(arffFile);
        saver.writeBatch();
        // .arff file will be created in the output location
        return arffFile;
    }

    private static void randomForest(File folder) {
        try {
            Instances structure = new Instances(new FileReader(folder));
            structure.setClassIndex(structure.numAttributes() - 1);
            System.out.println("Loaded data from arff file...");

            RandomForest randomForest = new RandomForest();
            randomForest.setNumFeatures(32);

            Evaluation eval = new Evaluation(structure);
            eval.crossValidateModel(randomForest, structure, 10, new Random(1));
            System.out.println("Estimated Accuracy: "+Double.toString(eval.pctCorrect()));



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}