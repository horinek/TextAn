package testing;

import java.io.File;
import java.util.Map;
import java.util.Random;

import weka.classifiers.functions.SMO;
import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.KNearestNeighbors;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.classification.evaluation.EvaluateDataset;
import net.sf.javaml.classification.evaluation.PerformanceMeasure;
import net.sf.javaml.classification.evaluation.CrossValidation;
import net.sf.javaml.tools.weka.WekaClassifier;
import net.sf.javaml.tools.data.FileHandler;

public class Classification {
	public static void main(String[] args)throws Exception {

		String dataPath = "data/arcene/ARCENE/arcene_train.data";
		
		/*BASICS*/
        /* Load a data set */
        Dataset data = FileHandler.loadDataset(new File(dataPath), 4, ",");
        /*
         * Contruct a KNN classifier that uses 5 neighbors to make a decision.
         */
        Classifier knn = new KNearestNeighbors(5);
        knn.buildClassifier(data);

        /*
         * Load a data set for evaluation, this can be a different one, but we
         * will use the same one.
         */
        Dataset dataForClassification = FileHandler.loadDataset(new File(dataPath), 4, ",");
        /* Counters for correct and wrong predictions. */
        int correct = 0, wrong = 0;
        /* Classify all instances and check with the correct class values */
        for (Instance inst : dataForClassification) {
            Object predictedClassValue = knn.classify(inst);
            Object realClassValue = inst.classValue();
            if (predictedClassValue.equals(realClassValue))
                correct++;
            else
                wrong++;
        }
        System.out.println("Correct predictions  " + correct);
        System.out.println("Wrong predictions " + wrong);

        /*EVALUATE CLASSIFIER*/
        data = FileHandler.loadDataset(new File(dataPath), 4, ",");
        /*
         * Contruct a KNN classifier that uses 5 neighbors to make a decision.
         */
        knn = new KNearestNeighbors(5);
        knn.buildClassifier(data);

        /*
         * Load a data set for evaluation, this can be a different one, but for
         * this example we use the same one.
         */
        dataForClassification = FileHandler.loadDataset(new File(dataPath), 4, ",");

        Map<Object, PerformanceMeasure> pm = EvaluateDataset.testDataset(knn, dataForClassification);
        for (Object o : pm.keySet())
            System.out.println(o + ": " + pm.get(o).getAccuracy());
        
        /*CLASSIFICATION CROSS VALIDATON*/
        /* Load data */
        data = FileHandler.loadDataset(new File(dataPath), 4, ",");
        /* Construct KNN classifier */
        knn = new KNearestNeighbors(5);
        /* Construct new cross validation instance with the KNN classifier, */
        CrossValidation cv = new CrossValidation(knn);
        /*
         * Perform 5-fold cross-validation on the data set with a user-defined
         * random generator
         */
        Map<Object, PerformanceMeasure> p = cv.crossValidation(data, 5, new Random(1));

        Map<Object, PerformanceMeasure> q = cv.crossValidation(data, 5, new Random(1));

        Map<Object, PerformanceMeasure> r = cv.crossValidation(data, 5, new Random(25));

        System.out.println("Accuracy=" + p.get("Iris-virginica").getAccuracy());
        System.out.println("Accuracy=" + q.get("Iris-virginica").getAccuracy());
        System.out.println("Accuracy=" + r.get("Iris-virginica").getAccuracy());
        System.out.println(p);
        System.out.println(q);
        System.out.println(r);
        
        /*WEKA CLASSIFIER*/
        System.out.println();
        System.out.println("WEKA:");
        /* Load data */
        data = FileHandler.loadDataset(new File(dataPath), 4, ",");
        /* Create Weka classifier */
        SMO smo = new SMO();
        /* Wrap Weka classifier in bridge */
        Classifier javamlsmo = new WekaClassifier(smo);
        /* Initialize cross-validation */
        cv = new CrossValidation(javamlsmo);
        /* Perform cross-validation */
        pm = cv.crossValidation(data);
        /* Output results */
        System.out.println(pm);
        
    }
}
