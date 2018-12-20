import com.weka.ml.ModelGenerator;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

import java.io.File;

public class compareModel {
    static void compareModel(String datasetPath, String testDataPath) throws Exception {
        File data = new File(datasetPath);
        File testData = new File(testDataPath);
        ModelGenerator mg = new ModelGenerator();
        Instances dataset = mg.loadDataset(datasetPath);
        Filter filter = new Normalize();

        //Normalize dataset
        filter.setInputFormat(dataset);
        Instances datasetnor = Filter.useFilter(dataset, filter);

        Instances testdataset = new Instances(new Instances(mg.loadDataset(testDataPath)));

        // build classifier with train dataset
        System.out.println("model: " + data.getName() + "\noutput random forest model");
        System.out.println("target: " + testData.getName() + "\n\n");
        RandomForest rf = new RandomForest();
        rf.buildClassifier(datasetnor);
        // Evaluate classifier with test dataset
        Evaluation eval = mg.evaluateModel(rf, datasetnor, testdataset);
        runWeka.representation(eval);
    }
}
