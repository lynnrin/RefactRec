import com.weka.ml.ModelClassifier;
import com.weka.ml.ModelGenerator;
import com.weka.ml.wekaRanker;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Debug;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

import java.util.Arrays;
import java.util.Random;

public class runWeka {

    public static final String DATASETPATH = "/Users/lynn/jt/test/ant_Extract.arff";
    public static final String MODElPATH = "/Users/lynn/jt/test/model.bin";

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

//        Instances traindataset = new Instances(datasetnor, 0, trainSize);
//        Instances testdataset = new Instances(datasetnor, trainSize, testSize);

        // build classifier with train dataset
        RandomForest rf = new RandomForest();
        rf.buildClassifier(datasetnor);

        // Evaluate classifier with test dataset
//        Evaluation eval = mg.evaluateModel(rf, traindataset, testdataset);
        Evaluation eval = new Evaluation(datasetnor);
        eval.crossValidateModel(rf, datasetnor, 10, new Random(1));
        representation(eval);

        AttributeSelection ranker = wekaRanker.wekaRanker(datasetnor);

        System.out.println("\noutput ranker");
        for (double[] a : ranker.rankedAttributes()) {
            System.out.println(Arrays.toString(a));
        }
        System.out.println("finished\n");

        AttributeSelectedClassifier classifier = new AttributeSelectedClassifier();
        CfsSubsetEval ev = new CfsSubsetEval();
        GreedyStepwise search = new GreedyStepwise();
        search.setSearchBackwards(true);
        classifier.setClassifier(rf);
        classifier.setEvaluator(ev);
        classifier.setSearch(search);
        // 10-fold cross-validation
        Evaluation evaluation = new Evaluation(datasetnor);
        evaluation.crossValidateModel(classifier, datasetnor, 10, new Random(1));
        representation(evaluation);



        //Save model
        mg.saveModel(rf, MODElPATH);

    }

    private static void representation(Evaluation eval) {
        System.out.println("Evaluation: " + eval.toSummaryString());
        System.out.println("f-measure\t" + eval.fMeasure(1));
        System.out.println("precision about true\t" + eval.precision(1));
        System.out.println("recall about true\t" + eval.recall(1));
    }

}
