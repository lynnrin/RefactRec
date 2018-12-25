import com.weka.ml.ModelGenerator;
import weka.attributeSelection.*;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Debug;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Random;

public class runWeka {

    public static final String DATASETPATH = "/Users/lynn/jt/test/ant_Extract.arff";
    public static final String MODElPATH = "/Users/lynn/jt/test/rf.model";

    public static void runWeka(String datasetPath) throws Exception {
        File data = new File(datasetPath);
        String modelPath = data.getPath().replaceFirst("\\.arff", "\\.model");
        String txtPath = data.getPath().replaceFirst("\\.arff", "\\.txt");
        FileWriter writer = new FileWriter(new File(txtPath));
        StringBuilder buf = new StringBuilder();

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
        System.out.println(data.getName() + "\noutput random forest model");
        buf.append(data.getName() + "\noutput random forest model");
        RandomForest rf = new RandomForest();
        rf.buildClassifier(datasetnor);
        // Evaluate classifier with test dataset
        Evaluation eval = mg.evaluateModel(rf, traindataset, testdataset);
//        Evaluation eval = new Evaluation(datasetnor);
//        eval.crossValidateModel(rf, datasetnor, 10, new Random(1));
        buf.append(bufRepresentation(eval));
        representation(eval);

        //Save model
        mg.saveModel(rf, modelPath);

        System.out.println("\n\n\n" + data.getName() + "\noutput Multi layer perceptron");
        buf.append("\n\n\n" + data.getName() + "\noutput Multi layer perceptron");
        MultilayerPerceptron multi = new MultilayerPerceptron();
        multi.buildClassifier(datasetnor);
        eval = mg.evaluateModel(multi, traindataset, testdataset);
        buf.append(bufRepresentation(eval));
        representation(eval);

        System.out.println("\n\n\n" + data.getName() + "\noutput bayes net");
        buf.append("\n\n\n" + data.getName() + "\noutput bayes net");
        BayesNet bayes = new BayesNet();
        bayes.buildClassifier(datasetnor);
        eval = mg.evaluateModel(bayes, traindataset, testdataset);
        buf.append(bufRepresentation(eval));
        representation(eval);

        System.out.println("\n\n\n" + data.getName() + "\noutput SMO(SVM)");
        buf.append("\n\n\n" + data.getName() + "\noutput SMO(SVM)");
        SMO smo = new SMO();
        smo.buildClassifier(datasetnor);
        eval = mg.evaluateModel(smo, traindataset,testdataset);
        buf.append(bufRepresentation(eval));
        representation(eval);

        AttributeSelection ranker = wekaRanker(datasetnor);

        System.out.println("\n\n\noutput ranker");
        buf.append("\n\n\noutput ranker");
        for (double[] a : ranker.rankedAttributes()) {
            buf.append("\n" + Arrays.toString(a));
            System.out.println(Arrays.toString(a));
        }
        buf.append("\n\n\n");
        System.out.println("\n\n\n");

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
        System.out.println(data.getName() + "\nattributed selectioned");
        buf.append("\n" + data.getName() + "\nattributed selectioned");
        representation(evaluation);
        buf.append(bufRepresentation(eval));

        writer.write(buf.toString());
        writer.close();

//        loadModel(datasetPath, MODElPATH);

    }

    static void representation(Evaluation eval) {
        System.out.println("\nEvaluation: " + eval.toSummaryString());
        System.out.println("f-measure\t" + eval.fMeasure(0));
        System.out.println("precision about true\t" + eval.precision(0));
        System.out.println("recall about true\t" + eval.recall(0));
        System.out.println("f-measure\t" + eval.fMeasure(1));
        System.out.println("precision about true\t" + eval.precision(1));
        System.out.println("recall about true\t" + eval.recall(1));
    }

    static String bufRepresentation(Evaluation eval) {
        StringBuilder buf = new StringBuilder();
        buf.append("\nEvaluation: " + eval.toSummaryString());
        buf.append("\nf-measure\t" + eval.fMeasure(0));
        buf.append("\nprecision about true\t" + eval.precision(0));
        buf.append("\nrecall about true\t" + eval.recall(0));
        buf.append("\nf-measure\t" + eval.fMeasure(1));
        buf.append("\nprecision about true\t" + eval.precision(1));
        buf.append("\nrecall about true\t" + eval.recall(1));
        return buf.toString();
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
