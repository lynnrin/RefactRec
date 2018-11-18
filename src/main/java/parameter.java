public class parameter {

    String separater = "#";

    String[] methodTypeList = {"package", "class", "method", "returnType"};

    String[] methodMetricsList = {"CC", "LOC", "MNON", "NOAMD", /*"NOEFD",*/ "NOEMD", "NOPT", "NOST", "NOVL"};

    String[] classMetricsList = {"CBO", "DIT", "LOCM", "classLOC", "MAX_CC", "MAX_LOC", "MAX_MNON", "MAX_NOAFD",
            "MAX_NOAMD", "MAX_NOEMD", "MAX_NOPT", "MAX_NOST", "MAX_NOVL", "NOACL", "classNOAMD", "NOC",
            "NOECL", /*"classNOEFD",*/ "classNOEMD", "NOFD", "NOMD", "NOMF", "NOPF", "classNOST", "RFC", "WMC"};

    String[] metricsList;


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
}
