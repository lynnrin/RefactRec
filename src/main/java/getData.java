import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.jtool.jxmetrics.batch.MetricsCalculator;
import org.refactoringminer.RefactoringMiner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class getData {
    public static void getRefData(File folder) throws Exception {
        RefactoringMiner ref = new RefactoringMiner();
        ref.detectAll(folder.getPath());
    }

    public static void getMetrics(File folder) throws Exception {
        java.lang.String line;
        java.lang.String prev = "CommitId";
        java.lang.String[] data;

        Repository repo; //jgit
        Git git;
        RevCommit parent;
        RevWalk walk;

        MetricsCalculator calculator; //jxMetrics

        java.lang.String parentPath = folder.getParent();
        File allRef = new File(parentPath + "/all_refactorings.csv");
        BufferedReader br = new BufferedReader(new FileReader(allRef));

        File metricDir; //move metrics file
        File[] files;
        java.lang.String fileName;
        File srcFile;
        File destFile;

        File dataDir = new File(parentPath + "/" + folder.getName() + "_data/xml/");
        if(!dataDir.exists()){
            dataDir.mkdirs();
        }

        while((line = br.readLine()) != null){
            data = line.split("#", 0);

            if(!prev.equals(data[0]) && data[0].length() == 40) {
                prev = data[0];

                System.out.println("\ngit reset --hard " + data[0]);
                repo = new FileRepository((java.lang.String.valueOf(folder + "/.git")));
                git = new Git(repo);
                walk = new RevWalk(repo);
                parent = walk.parseCommit(ObjectId.fromString(data[0])).getParent(0);
                git.reset().setMode( ResetCommand.ResetType.HARD ).setRef(parent.getName()).call();

                System.out.println("\ncalculate the Metrics of " + data[0]);
                calculator = new MetricsCalculator("RefactoringMetrics", folder.getPath());
                calculator.run();

                files = folder.listFiles();
                for(int i = 0; i < files.length; i++){
                    fileName = files[i].getName();
                    if(fileName.startsWith("RefactoringMetrics")){
                        srcFile = new File(folder + "/" + fileName);
                        destFile = new File(dataDir.getPath() + "/" + data[0] + ".xml");
                        srcFile.renameTo(destFile);
                    }
                }
                repo.close(); //will be GC, isn't this necessary?
            }
        }
    }
}
