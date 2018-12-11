import com.weka.ml.ModelClassifier;
import com.weka.ml.ModelGenerator;
import com.weka.ml.wekaRanker;
import weka.attributeSelection.AttributeSelection;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.trees.RandomForest;
import weka.core.Debug;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

import java.util.Arrays;

public class runWeka {

    public static final String DATASETPATH = "/Users/lynn/jt/test/ant_Extract.arff";
    public static final String MODElPATH = "/Users/lynn/jt/model.bin";

    public static void runWeka(String datasetPath) throws Exception {

        ModelGenerator mg = new ModelGenerator();

        Instances dataset = mg.loadDataset(datasetPath);

        Filter filter = new Normalize();

        // divide dataset to train dataset 80% and test dataset 20%
        int trainSize = (int) Math.round(dataset.numInstances() * 0.8);
        int testSize = dataset.numInstances() - trainSize;

        dataset.randomize(new Debug.Random(1));// if you comment this line the accuracy of the model will be droped from 96.6% to 80%

        //Normalize dataset
        filter.setInputFormat(dataset);
        Instances datasetnor = Filter.useFilter(dataset, filter);

        Instances traindataset = new Instances(datasetnor, 0, trainSize);
        Instances testdataset = new Instances(datasetnor, trainSize, testSize);

        // build classifier with train dataset
        RandomForest rf = new RandomForest();
        rf.buildClassifier(traindataset);

        // Evaluate classifier with test dataset
        Evaluation eval = mg.evaluateModel(rf, traindataset, testdataset);
        System.out.println("Evaluation: " + eval.toSummaryString());
        System.out.println("f-measure\t" + eval.fMeasure(1));
        System.out.println("precision about true\t" + eval.precision(1));
        System.out.println("recall about true\t" + eval.recall(1));

        AttributeSelection ranker = wekaRanker.wekaRanker(dataset);
        System.out.println("\noutput ranker");
        for (double[] a : ranker.rankedAttributes()) {
            System.out.println(Arrays.toString(a));
        }
        System.out.println("finished\n");

        //Save model
        mg.saveModel(rf, MODElPATH);

    }

}
