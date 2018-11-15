import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

class parseXML {
    public static void parseXML(File folder) throws IOException, SAXException, ParserConfigurationException {
        java.lang.String parentPath = folder.getParent();
        String separater = "#";
        String[] metricsList = {"package", "class", "method", "returnType",
                "CBO", "DIT", "LOCM", "classLOC", "MAX_CC", "MAX_LOC", "MAX_MNON", "MAX_NOAFD",
                "MAX_NOAMD", "MAX_NOEMD", "MAX_NOPT", "MAX_NOST", "MAX_NOVL", "NOACL", "classNOAMD", "NOC",
                "NOECL", /*"classNOEFD",*/ "classNOEMD", "NOFD", "NOMD", "NOMF", "NOPF", "classNOST", "RFC", "WMC",
                "CC", "LOC", "MNON", "NOAMD", /*"NOEFD",*/ "NOEMD", "NOPT", "NOST", "NOVL"};

        File xmlDir = new File(parentPath + "/" + folder.getName() + "_data/xml/");
        File csvDir = new File(xmlDir.getParent() +  "/csv/");
        if(!xmlDir.exists()){
            System.err.println(xmlDir + " doesn't exist.");
        }
        if(!csvDir.exists()){
            csvDir.mkdir();
        }
        File[] files = xmlDir.listFiles();
        for(File file: files){
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);

            try{
                FileWriter f = new FileWriter(csvDir.getPath() + "/" + file.getName().replaceFirst("\\.xml", "\\.csv"));
                PrintWriter p = new PrintWriter(new BufferedWriter(f));

                for (int i = 0; i < metricsList.length-1; i++){
                    p.print(metricsList[i]);
                    p.print(separater);
                }
                p.print(metricsList[metricsList.length-1]);
                p.println();

                Element root = document.getDocumentElement();
                if(root.getNodeName().equals("project")){
                    NodeList projectChildren = root.getChildNodes();

                    for (int i = 0; i < projectChildren.getLength(); i++){
                        Node projectChild = projectChildren.item(i);
                        if(!projectChild.getNodeName().equals("package")){
                            continue;
                        }

                        NodeList packageChildren = projectChild.getChildNodes();
                        for (int j = 0; j < packageChildren.getLength(); j++){
                            Node packageChild = packageChildren.item(j);
                            String classMetrics = "##########################";
                            if(!packageChild.getNodeName().equals("class")){
                                continue;
                            }
//                            if (packageChild.getAttributes().getNamedItem("name").getNodeValue().equals(".UNKNOWN")){
//                                continue;
//                            }

                            NodeList classChildren = packageChild.getChildNodes();
                            for (int k = 0; k < classChildren.getLength(); k++){
                                Node classChild = classChildren.item(k);

                                if (classChild.getNodeName().equals("metrics")){
                                    classMetrics = classChild.getAttributes().getNamedItem("CBO").getNodeValue()
                                            + separater + classChild.getAttributes().getNamedItem("DIT").getNodeValue()
                                            + separater + classChild.getAttributes().getNamedItem("LCOM").getNodeValue()
                                            + separater + classChild.getAttributes().getNamedItem("LOC").getNodeValue()
                                            + separater + classChild.getAttributes().getNamedItem("MAX_CC").getNodeValue()
                                            + separater + classChild.getAttributes().getNamedItem("MAX_LOC").getNodeValue()
                                            + separater + classChild.getAttributes().getNamedItem("MAX_MNON").getNodeValue()
                                            + separater + classChild.getAttributes().getNamedItem("MAX_NOAFD").getNodeValue()
                                            + separater + classChild.getAttributes().getNamedItem("MAX_NOAMD").getNodeValue()
                                            + separater + classChild.getAttributes().getNamedItem("MAX_NOEMD").getNodeValue()
                                            + separater + classChild.getAttributes().getNamedItem("MAX_NOPT").getNodeValue()
                                            + separater + classChild.getAttributes().getNamedItem("MAX_NOST").getNodeValue()
                                            + separater + classChild.getAttributes().getNamedItem("MAX_NOVL").getNodeValue()
                                            + separater + classChild.getAttributes().getNamedItem("NOACL").getNodeValue()
                                            + separater + classChild.getAttributes().getNamedItem("NOAMD").getNodeValue()
                                            + separater + classChild.getAttributes().getNamedItem("NOC").getNodeValue()
                                            + separater + classChild.getAttributes().getNamedItem("NOECL").getNodeValue()
//                                            + separater + classChild.getAttributes().getNamedItem("NOEFD").getNodeValue()
                                            + separater + classChild.getAttributes().getNamedItem("NOEMD").getNodeValue()
                                            + separater + classChild.getAttributes().getNamedItem("NOFD").getNodeValue()
                                            + separater + classChild.getAttributes().getNamedItem("NOMD").getNodeValue()
                                            + separater + classChild.getAttributes().getNamedItem("NOMF").getNodeValue()
                                            + separater + classChild.getAttributes().getNamedItem("NOPF").getNodeValue()
                                            + separater + classChild.getAttributes().getNamedItem("NOPM").getNodeValue()
                                            + separater + classChild.getAttributes().getNamedItem("NOST").getNodeValue()
                                            + separater + classChild.getAttributes().getNamedItem("RFC").getNodeValue()
                                            + separater + classChild.getAttributes().getNamedItem("WMC").getNodeValue() + separater;
                                }

                                if (classChild.getNodeName().equals("method")){
//                                    if (classChild.getAttributes().getNamedItem("name").getNodeValue().equals(".UNKNOWN")){
//                                        continue;
//                                    }
                                    StringBuilder buf = new StringBuilder();

                                    buf.append(projectChild.getAttributes().getNamedItem("name").getNodeValue());
                                    buf.append(separater);
                                    buf.append(classChild.getAttributes().getNamedItem("fqn").getNodeValue());
                                    buf.append(separater);
                                    buf.append(classChild.getAttributes().getNamedItem("type").getNodeValue());
                                    buf.append(separater);

                                    Node methodChild = classChild.getFirstChild();
                                    while (methodChild != null){
                                        if (methodChild.getNodeName().equals("metrics")){
                                            buf.append(classMetrics);
                                            buf.append(methodChild.getAttributes().getNamedItem("CC").getNodeValue());
                                            buf.append(separater);
                                            buf.append(methodChild.getAttributes().getNamedItem("LOC").getNodeValue());
                                            buf.append(separater);
                                            buf.append(methodChild.getAttributes().getNamedItem("MNON").getNodeValue());
                                            buf.append(separater);
                                            buf.append(methodChild.getAttributes().getNamedItem("NOAMD").getNodeValue());
                                            buf.append(separater);
//                                            buf.append(methodChild.getAttributes().getNamedItem("NOEFD").getNodeValue());
//                                            buf.append(separater);
                                            buf.append(methodChild.getAttributes().getNamedItem("NOEMD").getNodeValue());
                                            buf.append(separater);
                                            buf.append(methodChild.getAttributes().getNamedItem("NOPT").getNodeValue());
                                            buf.append(separater);
                                            buf.append(methodChild.getAttributes().getNamedItem("NOST").getNodeValue());
                                            buf.append(separater);
                                            buf.append(methodChild.getAttributes().getNamedItem("NOVL").getNodeValue());
                                            buf.append(System.getProperty("line.separator"));
                                            p.print(buf.toString());
                                        }
                                        methodChild = methodChild.getNextSibling();
                                    }
                                }
                            }
                        }
                    }
                }
                p.close();

            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
}
