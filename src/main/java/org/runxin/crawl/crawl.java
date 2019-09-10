package org.runxin.crawl;

import java.io.FileWriter;
import java.nio.file.Paths;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.JarFile;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.apache.maven.index.cli.NexusIndexerCli;
import com.alibaba.fastjson.JSONWriter;

public class crawl{
    public static void main(String[] args) throws Exception {
        Options options = new Options();
        options.addOption("u",  true, "the lower bound of usage");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine result = null;
        try {
            result = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            formatter.printHelp("crawl", options, true);
            System.exit(1);
        }

        int usageThreshold = 300;

        if(result.hasOption("u")){
            try{
                usageThreshold = Integer.parseInt(result.getOptionValue("u"));
            }catch(Exception e){
                System.err.println(e.toString());
                formatter.printHelp("crawl", options, true);
                System.exit(1);
            }
        }
        
        try{
            startCrawl(usageThreshold);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void startCrawl(int usageThreshold) throws Exception
    {
        final String downloadPath = "jarsRepo";
        final String crawltTmpResultFolder = "indexResult_tmp";
        final String crawltResultFolder = "indexResult";
        final String indexZipFilePath = Paths.get(crawltTmpResultFolder, "indexFiles").toString();
        final String indexDataPath = Paths.get(crawltTmpResultFolder, "IndexData").toString();
        final String indexPath = Paths.get(indexDataPath, "index").toString();
        final String jsonPath  = Paths.get(indexDataPath, "ArtifactUsage.json").toString();
        Map<String, Integer> artifactUsage = new HashMap<>();

        /* prepare target directory */
        File folder = new File(crawltTmpResultFolder);
        if(!folder.exists()){
            folder.mkdirs();
        }else{
            boolean isSuccess = deleteDir(folder);
            if(!isSuccess){
                throw new Exception("Fail to delete old \"indexResult_tmp\" file!");
            }
            folder.mkdirs();
        }
        folder = new File(indexDataPath);
        folder.mkdirs();
        folder = new File(indexZipFilePath);
        folder.mkdirs();

        /* crawl with depth of 20 */
        MyCrawler crawler = new MyCrawler("crawl", false, downloadPath, usageThreshold, artifactUsage);
        crawler.start(20);

        /* write artifactUsage information */
        JSONWriter writer = new JSONWriter(new FileWriter(jsonPath)); 
        writer.startObject();
        Iterator<Map.Entry<String, Integer>> entries = artifactUsage.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, Integer> entry = entries.next();
            writer.writeKey(entry.getKey());
            writer.writeValue(entry.getValue());
        }
        writer.endObject();
        writer.close();

        /* Delete bad jar files*/
        System.out.println("Checking files...");
        System.out.println("It will take about ten minutes. Please wait patiently..");
        File downloadJars = new File(downloadPath);
        processFile(downloadJars);

        /* create index files */
        NexusIndexerCli cli = new NexusIndexerCli();
        String[] s = {"--repository", downloadPath,
        "--index", indexPath,
        "--destination", indexZipFilePath,
        "--type","full"};
        cli.execute(s);


        /* file replacement */
        File file1 = new File(crawltResultFolder);
        File file2 = new File(crawltTmpResultFolder);
        if(file1.exists()){
            boolean isSuccess = deleteDir(file1);
            if(!isSuccess){
                throw new Exception("Fail to delete old \"indexResult\" file!");
            }
        }
        file2.renameTo(file1);
    }

    private static void processFile(File file){
        File[] fs = file.listFiles();
        for(File f:fs){
            if(f.isDirectory()) processFile(f);
            if(f.isFile()&&f.getName().endsWith("jar")){
                try{
                    JarFile jarFile = new JarFile(f); 
                    jarFile.close();
                }catch(Exception e){
                    f.delete();
                    System.out.println("Delete "+f.getName());
                }
            }
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}