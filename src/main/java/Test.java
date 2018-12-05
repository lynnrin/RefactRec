import com.weka.ml.ModelClassifier;
import com.weka.ml.ModelGenerator;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.trees.RandomForest;
import weka.core.Debug;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

public class Test {

    public static final String DATASETPATH = "/Users/lynn/jt/test/ant_Extract.arff";
    public static final String MODElPATH = "/Users/lynn/jt/model.bin";

    public static void main(String[] args) throws Exception {

        ModelGenerator mg = new ModelGenerator();

        Instances dataset = mg.loadDataset(DATASETPATH);

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

        //Save model
        mg.saveModel(rf, MODElPATH);

        //classifiy a single instance
//        ModelClassifier cls = new ModelClassifier();
//        String classname =cls.classifiy(Filter.useFilter(cls.createInstance(1.6, 0.2, 0), filter), MODElPATH);
//        System.out.println("\n The class name for the instance with petallength = 1.6 and petalwidth =0.2 is  " +classname);

    }

}
