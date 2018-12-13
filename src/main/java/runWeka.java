import com.weka.ml.ModelGenerator;
import weka.attributeSelection.*;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Debug;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

import java.util.Arrays;
import java.util.Random;

public class runWeka {

    public static final String DATASETPATH = "/Users/lynn/jt/test/ant_Extract.arff";
    public static final String MODElPATH = "/Users/lynn/jt/test/rf.model";

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

        //Save model
        mg.saveModel(rf, MODElPATH);

        AttributeSelection ranker = wekaRanker(datasetnor);

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

//        loadModel(datasetPath, MODElPATH);

    }

    private static void representation(Evaluation eval) {
        System.out.println("\nEvaluation: " + eval.toSummaryString());
        System.out.println("f-measure\t" + eval.fMeasure(1));
        System.out.println("precision about true\t" + eval.precision(1));
        System.out.println("recall about true\t" + eval.recall(1));
    }

    private static AttributeSelection wekaRanker(Instances trainDataSet) throws Exception {
        AttributeSelection selector = new AttributeSelection();
        InfoGainAttributeEval eval = new InfoGainAttributeEval();
        Ranker ranker = new Ranker();
//        ranker.setNumToSelect(Math.min(500, trainDataSet.numAttributes() - 1));
        selector.setEvaluator(eval);
        selector.setSearch(ranker);
        selector.SelectAttributes(trainDataSet);
        return selector;
    }

    public static void loadModel(String datasetPath, String modelPath) throws Exception {
        Classifier cls = (Classifier) SerializationHelper.read(modelPath);
        ModelGenerator mg = new ModelGenerator();
        Instances dataset = mg.loadDataset(datasetPath);
        cls.buildClassifier(dataset);
        Evaluation eval = new Evaluation(dataset);
        eval.crossValidateModel(cls, dataset, 10, new Random(1));
        System.out.println("\noutput result about load model");
        representation(eval);
    }
}
