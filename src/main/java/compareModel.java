import com.weka.ml.ModelGenerator;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class compareModel {
    static void compareModel(String datasetPath, String testDataPath) throws Exception {
        File data = new File(datasetPath);
        File testData = new File(testDataPath);
        Instances dataset = new Instances(new BufferedReader((new FileReader(datasetPath))));
        dataset.setClassIndex(dataset.numAttributes()-1);
        Instances testset = new Instances(new BufferedReader((new FileReader(testDataPath))));
        testset.setClassIndex(testset.numAttributes()-1);
        Filter filter = new Normalize();

        //Normalize dataset
        filter.setInputFormat(dataset);
        Instances datasetnor = Filter.useFilter(dataset, filter);

        // build classifier with train dataset
        System.out.println("model: " + data.getName() + "\noutput random forest model");
        System.out.println("target: " + testData.getName() + "\n\n");
        RandomForest rf = new RandomForest();
        rf.buildClassifier(datasetnor);
        // Evaluate classifier with test dataset
        Evaluation eval = new Evaluation(datasetnor);
        eval.evaluateModel(rf, testset);
        runWeka.representation(eval);
    }
}
