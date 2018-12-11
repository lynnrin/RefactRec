package com.weka.ml;

import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instances;

public class wekaRanker {
    public static AttributeSelection wekaRanker(Instances trainDataSet) throws Exception {
        AttributeSelection selector = new AttributeSelection();
        InfoGainAttributeEval eval = new InfoGainAttributeEval();
        Ranker ranker = new Ranker();
//        ranker.setNumToSelect(Math.min(500, trainDataSet.numAttributes() - 1));
        selector.setEvaluator(eval);
        selector.setSearch(ranker);
        selector.SelectAttributes(trainDataSet);
        return selector;
    }
}
