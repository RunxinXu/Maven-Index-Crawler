package org.runxin.crawl;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.JarFile;

import org.apache.maven.index.cli.NexusIndexerCli;
import com.alibaba.fastjson.JSONWriter;

public class crawl{

    public static void main(String[] args) throws Exception {
        final int usageThreshold = 300;
        try{
            startCrawl(usageThreshold);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void startCrawl(int usageThreshold) throws Exception
    {
        final String downloadPath = "JarsRepo";
        final String crawltTmpResultFolder = "IndexResult_tmp";
        final String crawltResultFolder = "IndexResult";
        final String indexZipFilePath = Paths.get(crawltTmpResultFolder, "IndexFiles").toString();
        final String indexDataPath = Paths.get(crawltTmpResultFolder, "IndexData").toString();
        final String indexPath = Paths.get(indexDataPath, "index").toString();
        final String jsonPath  = Paths.get(indexDataPath, "ArtifactUsage.json").toString();
        Map<String, Integer> artifactUsage = new HashMap<>();

        prepareDir(crawltTmpResultFolder, indexDataPath, indexZipFilePath);

        /* crawl with depth of 30 */
        MyCrawler crawler = new MyCrawler("crawl", false, downloadPath, usageThreshold, artifactUsage);
        crawler.start(30);

        writeArtifactUsage(jsonPath, artifactUsage);
        checkBadJars(downloadPath);
        createIndex(downloadPath, indexPath, indexZipFilePath);
        fileReplace(crawltResultFolder, crawltTmpResultFolder);
    }

    private static void prepareDir(String crawltTmpResultFolder, String indexDataPath, 
        String indexZipFilePath) throws Exception {
        File folder = new File(crawltTmpResultFolder);
        if(!folder.exists()){
            folder.mkdirs();
        }else{
            boolean isSuccess = deleteDir(folder);
            if(!isSuccess){
                throw new Exception("Fail to delete old \"IndexResult_tmp\" file!");
            }
            folder.mkdirs();
        }
        folder = new File(indexDataPath);
        folder.mkdirs();
        folder = new File(indexZipFilePath);
        folder.mkdirs();
    }

    private static void writeArtifactUsage(String jsonPath, Map<String, Integer> artifactUsage) 
            throws IOException {
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
    }

    private static void checkBadJars(String downloadPath) {
        System.out.println("Checking files...");
        System.out.println("It will take about ten minutes. Please wait patiently..");
        File downloadJars = new File(downloadPath);
        processFile(downloadJars);
    }

    private static void createIndex(String downloadPath, String indexPath, String indexZipFilePath) {
        NexusIndexerCli cli = new NexusIndexerCli();
        String[] s = {"--repository", downloadPath,
        "--index", indexPath,
        "--destination", indexZipFilePath,
        "--type","full"};
        cli.execute(s);
    }

    private static void fileReplace(String crawltResultFolder, String crawltTmpResultFolder) 
            throws Exception {
        File file1 = new File(crawltResultFolder);
        File file2 = new File(crawltTmpResultFolder);
        if(file1.exists()){
            boolean isSuccess = deleteDir(file1);
            if(!isSuccess){
                throw new Exception("Fail to delete old \"IndexResult\" file!");
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