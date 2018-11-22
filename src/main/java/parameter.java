public class parameter {

    String separater;
    String[] methodTypeList;
    String[] methodMetricsList;
    String[] classMetricsList;
    String[] metricsList;
    String[] refactoringType;

    public parameter(){
        this.separater = "#";
        this.methodTypeList = new String[]{"package", "class", "method", "returnType"};
        this.methodMetricsList = new String[]{"CC", "LOC", "MNON", "NOAMD", /*"NOEFD",*/ "NOEMD", "NOPT", "NOST", "NOVL"};
        this.classMetricsList = new String[]{"CBO", "DIT", /*"LOCM",*/ "LOC", "MAX_CC", "MAX_LOC", "MAX_MNON", "MAX_NOAFD",
                "MAX_NOAMD", "MAX_NOEMD", "MAX_NOPT", "MAX_NOST", "MAX_NOVL", "NOACL", "NOAMD", "NOC",
                "NOECL", /*"NOEFD",*/ "NOEMD", "NOFD", "NOMD", "NOMF", "NOPF", "NOST", "RFC", "WMC"};
        this.refactoringType = new String[]{"Extract Method", "Move Method", "Inline Method"};
    }

    public void makeList(){
        metricsList = new String[methodTypeList.length + methodMetricsList.length + classMetricsList.length];
        System.arraycopy(methodTypeList, 0, metricsList, 0, methodTypeList.length);
        System.arraycopy(classMetricsList, 0, metricsList, methodTypeList.length, classMetricsList.length);
        System.arraycopy(methodMetricsList, 0, metricsList, methodTypeList.length + classMetricsList.length, methodMetricsList.length);
    }

    public String[] getTypeList(){
        return methodTypeList;
    }

    public String[] getMethodMetricsList(){
        return methodMetricsList;
    }

    public String[] getClassMetricsList(){
        return classMetricsList;
    }

    public String getSeparater(){
        return separater;
    }

    public String[] getMetricsList(){
        return metricsList;
    }

    public String[] getRefactoringTypeList(){
        return refactoringType;
    }
}
