package testing;

import java.io.File;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.featureselection.scoring.GainRatio;
import net.sf.javaml.featureselection.ranking.RecursiveFeatureEliminationSVM;
import net.sf.javaml.distance.PearsonCorrelationCoefficient;
import net.sf.javaml.featureselection.subset.GreedyForwardSelection;
import net.sf.javaml.featureselection.ensemble.LinearRankingEnsemble;
import net.sf.javaml.tools.data.FileHandler;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.ASSearch;
import weka.attributeSelection.GainRatioAttributeEval;
import weka.attributeSelection.Ranker;
import net.sf.javaml.tools.weka.WekaAttributeSelection;

public class Feature {
	/**
     * Shows the basic steps to create use a feature scoring algorithm.
     * 
     * @author Thomas Abeel
     * 
     */
    public static void main(String[] args) throws Exception {
    	
    	/*SCORING*/
    	System.out.println("SCORIING:");
    	/* Load the iris data set */
        Dataset data = FileHandler.loadDataset(new File("iris.data"), 4, ",");

        GainRatio ga = new GainRatio();
        /* Apply the algorithm to the data set */
        ga.build(data);
        /* Print out the score of each attribute */
        for (int i = 0; i < ga.noAttributes(); i++)
            System.out.println(ga.score(i));
        
        /*RANKING*/
    	System.out.println("RANKING:");
        data = FileHandler.loadDataset(new File("iris.data"), 4, ",");
        /* Create a feature ranking algorithm */
        RecursiveFeatureEliminationSVM svmrfe = new RecursiveFeatureEliminationSVM(0.2);
        /* Apply the algorithm to the data set */
        svmrfe.build(data);
        /* Print out the rank of each attribute */
        for (int i = 0; i < svmrfe.noAttributes(); i++)
            System.out.println(svmrfe.rank(i));

        /*SUBSSET SELECTION*/
    	System.out.println("SUBSET SELECTION:");
        data = FileHandler.loadDataset(new File("iris.data"), 4, ",");
        /*
         * Construct a greedy forward subset selector that will use the Pearson
         * correlation to determine the relation between each attribute and the
         * class label. The first parameter indicates that only one, i.e. 'the
         * best' attribute will be selected.
         */
        GreedyForwardSelection gfs = new GreedyForwardSelection(1, new PearsonCorrelationCoefficient());
        /* Apply the algorithm to the data set */
        gfs.build(data);
        /* Print out the attribute that has been selected */
        System.out.println(gfs.selectedAttributes());
        
        /*ENSEMLE RANKING*/
    	System.out.println("ENSEMLE RANKING:");
        data = FileHandler.loadDataset(new File("iris.data"), 4, ",");
        /* Create a feature ranking algorithm */
        RecursiveFeatureEliminationSVM[] svmrfes = new RecursiveFeatureEliminationSVM[10];
        for (int i = 0; i < svmrfes.length; i++)
            svmrfes[i] = new RecursiveFeatureEliminationSVM(0.2);
        LinearRankingEnsemble ensemble = new LinearRankingEnsemble(svmrfes);
        /* Build the ensemble */
        ensemble.build(data);
        /* Print out the rank of each attribute */
        for (int i = 0; i < ensemble.noAttributes(); i++)
            System.out.println(ensemble.rank(i));
        
        /*WEKA ATTRIBUTE SELECTION*/
    	System.out.println("WEKA ATTRIBUTE SELECTION:");
        data = FileHandler.loadDataset(new File("iris.data"), 4, ",");
        /*Create a Weka AS Evaluation algorithm */
        ASEvaluation eval = new GainRatioAttributeEval();
        /* Create a Weka's AS Search algorithm */
        ASSearch search = new Ranker();
        /* Wrap WEKAs' Algorithms in bridge */
        WekaAttributeSelection wekaattrsel = new WekaAttributeSelection(eval,search);
        /* Apply the algorithm to the data set */
        wekaattrsel.build(data);
        /* Print out the score and rank  of each attribute */
        for (int i = 0; i < wekaattrsel.noAttributes(); i++) 
            System.out.println("Attribute  " +  i +  "  Ranks  " + wekaattrsel.rank(i) + " and Scores " + wekaattrsel.score(i) );
    }
}
