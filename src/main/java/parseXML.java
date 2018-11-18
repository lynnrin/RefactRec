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
        String[] metricsList = {"package", "class", "method", "returnType"};

        String[] methodM = {"CC", "LOC", "MNON", "NOAMD", /*"NOEFD",*/ "NOEMD", "NOPT", "NOST", "NOVL"};

        String[] classM = {"CBO", "DIT", "LOCM", "classLOC", "MAX_CC", "MAX_LOC", "MAX_MNON", "MAX_NOAFD",
                "MAX_NOAMD", "MAX_NOEMD", "MAX_NOPT", "MAX_NOST", "MAX_NOVL", "NOACL", "classNOAMD", "NOC",
                "NOECL", /*"classNOEFD",*/ "classNOEMD", "NOFD", "NOMD", "NOMF", "NOPF", "classNOST", "RFC", "WMC"};

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

                //*********** output csv header
                for (int i = 0; i < metricsList.length; i++){
                    p.print(metricsList[i]);
                    p.print(separater);
                }
                for (int i = 0; i < classM.length; i++){
                    p.print(classM[i]);
                    p.print(separater);
                }
                for (int i = 0; i < methodM.length; i++){
                    p.print(methodM[i]);
                    if (i != methodM.length-1){
                        p.print(separater);
                    }
                }
                //************

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
                                    StringBuilder classBuf = new StringBuilder();
                                    for (int m = 0; i < classM.length; i++){
                                        classBuf.append(classChild.getAttributes().getNamedItem(classM[m]).getNodeValue());
                                        classBuf.append(separater);
                                    }
                                    classMetrics = classBuf.toString();
                                }

                                if (classChild.getNodeName().equals("method")){
                                    if (classChild.getAttributes().getNamedItem("name").getNodeValue().equals(".UNKNOWN")){
                                        continue;
                                    }
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
                                            for (int m = 0; i < methodM.length; i++){
                                                buf.append(methodChild.getAttributes().getNamedItem(methodM[m]).getNodeValue());
                                                buf.append(separater);
                                            }
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
