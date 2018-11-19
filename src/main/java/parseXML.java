import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Arrays;
import java.util.Iterator;

class parseXML {
    public static void parseXML(File folder) throws IOException, SAXException, ParserConfigurationException {
        String parentPath = folder.getParent();

        parameter pm = new parameter();
        String separate = pm.getSeparater();
        String methodTypeList[] = pm.getTypeList();
        String methodMetricsList[] = pm.getMethodMetricsList();
        String classMetricsList[] = pm.getClassMetricsList();

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
                for (int i = 0; i < methodTypeList.length; i++){
                    p.print(methodTypeList[i]);
                    p.print(separate);
                }
                for (int i = 0; i < classMetricsList.length; i++){
                    p.print(classMetricsList[i]);
                    p.print(separate);
                }
                for (int i = 0; i < methodMetricsList.length; i++){
                    p.print(methodMetricsList[i]);
                    if (i != methodMetricsList.length-1){
                        p.print(separate);
                    }
                }
                p.println();
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
                                    for (String s : classMetricsList){
                                        classBuf.append(classChild.getAttributes().getNamedItem(s).getNodeValue());
                                        classBuf.append(separate);
                                    }
                                    classMetrics = classBuf.toString();
                                }

                                if (classChild.getNodeName().equals("method")){
                                    if (classChild.getAttributes().getNamedItem("name").getNodeValue().equals(".UNKNOWN")){
                                        continue;
                                    }
                                    StringBuilder buf = new StringBuilder();

                                    buf.append(projectChild.getAttributes().getNamedItem("name").getNodeValue());
                                    buf.append(separate);
                                    buf.append(classChild.getAttributes().getNamedItem("fqn").getNodeValue());
                                    buf.append(separate);
                                    buf.append(classChild.getAttributes().getNamedItem("type").getNodeValue());
                                    buf.append(separate);

                                    Node methodChild = classChild.getFirstChild();
                                    while (methodChild != null){
                                        if (methodChild.getNodeName().equals("metrics")){
                                            buf.append(classMetrics);
                                            for (Iterator<String> iter = Arrays.asList(methodMetricsList).iterator(); iter.hasNext();){
                                                buf.append(methodChild.getAttributes().getNamedItem(iter.next()).getNodeValue());
                                                if (iter.hasNext()){
                                                    buf.append(separate);
                                                }
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
