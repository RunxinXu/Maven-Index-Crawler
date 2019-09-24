package org.runxin.crawl;

import org.eclipse.aether.resolution.ArtifactResolutionException;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.rocks.BreadthCrawler;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;


public class MyCrawler extends BreadthCrawler {

    private String downloadPath;
    public Map<String,Integer> artifactUsage;
    private static Pattern patternG = Pattern.compile("<groupId>(.+)</groupId>");
    private static Pattern patternA = Pattern.compile("<artifactId>(.+)</artifactId>");
    private static Pattern patternV = Pattern.compile(" <version>(.+)</version>");
    private int threshold;

    public MyCrawler(String crawlPath, boolean autoParse, String downloadPath, int threshold,
                    Map<String, Integer> artifactUsage) {
        super(crawlPath, autoParse);
        this.downloadPath = downloadPath;
        this.threshold = threshold;
        this.artifactUsage = artifactUsage;
       
        this.addSeedAndReturn("https://mvnrepository.com/tags").type("tagCloudPage");

        // Azure 
        final String azureLibraryList[] = {
            "https://mvnrepository.com/artifact/com.microsoft.azure/adal4j",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-mgmt-appservice",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-batch",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-mgmt-batchai",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-mgmt-cdn",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-mgmt-containerinstance",
            "https://mvnrepository.com/artifact/com.microsoft.azure.cognitiveservices/azure-cognitiveservices-computervision",
            "https://mvnrepository.com/artifact/com.microsoft.azure.cognitiveservices/azure-cognitiveservices-videosearch",
            "https://mvnrepository.com/artifact/com.microsoft.azure.cognitiveservices/azure-cognitiveservices-parent",
            "https://mvnrepository.com/artifact/com.microsoft.azure.cognitiveservices/azure-cognitiveservices-websearch",
            "https://mvnrepository.com/artifact/com.microsoft.azure.cognitiveservices/azure-cognitiveservices-customimagesearch",
            "https://mvnrepository.com/artifact/com.microsoft.azure.cognitiveservices/azure-cognitiveservices-visualsearch",
            "https://mvnrepository.com/artifact/com.microsoft.azure.cognitiveservices/azure-cognitiveservices-newssearch",
            "https://mvnrepository.com/artifact/com.microsoft.azure.cognitiveservices/azure-cognitiveservices-customsearch",
            "https://mvnrepository.com/artifact/com.microsoft.azure.cognitiveservices/azure-cognitiveservices-autosuggest",
            "https://mvnrepository.com/artifact/com.microsoft.azure.cognitiveservices/azure-cognitiveservices-spellcheck",
            "https://mvnrepository.com/artifact/com.microsoft.azure.cognitiveservices/azure-cognitiveservices-faceapi",
            "https://mvnrepository.com/artifact/com.microsoft.azure.cognitiveservices/azure-cognitiveservices-contentmoderator",
            "https://mvnrepository.com/artifact/com.microsoft.azure.cognitiveservices/azure-computervision",
            "https://mvnrepository.com/artifact/com.microsoft.azure.cognitiveservices/azure-cognitiveservices-luis-runtime",
            "https://mvnrepository.com/artifact/com.microsoft.azure.cognitiveservices/azure-faceapi",
            "https://mvnrepository.com/artifact/com.microsoft.azure.cognitiveservices/azure-cognitiveservices-entitysearch",
            "https://mvnrepository.com/artifact/com.microsoft.azure.cognitiveservices/azure-cognitiveservices-imagesearch",
            "https://mvnrepository.com/artifact/com.microsoft.azure.cognitiveservices/azure-cognitiveservices-textanalytics",
            "https://mvnrepository.com/artifact/com.microsoft.azure.cognitiveservices/azure-cognitiveservices-language",
            "https://mvnrepository.com/artifact/com.microsoft.azure.cognitiveservices/azure-cognitiveservices-luis-authoring",
            "https://mvnrepository.com/artifact/com.microsoft.azure.cognitiveservices/azure-cognitiveservices-customvision-training",
            "https://mvnrepository.com/artifact/com.microsoft.azure.cognitiveservices/azure-cognitiveservices-customvision-prediction",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-mgmt-containerregistry",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-mgmt-containerservice",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-documentdb",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-mgmt-datalake-analytics",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-data-lake-store-sdk",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-mgmt-dns",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-svc-mgmt",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-eventgrid",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-eventhubs",
            "https://mvnrepository.com/artifact/com.microsoft.azure.functions/azure-functions-java-library",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-client-authentication",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-arm-client-runtime",
            "https://mvnrepository.com/artifact/com.microsoft.azure.hdinsight.v2018_06_01_preview/azure-mgmt-hdinsight",
            "https://mvnrepository.com/artifact/com.microsoft.azure.sdk.iot/iot-service-client",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-keyvault",
            "https://mvnrepository.com/artifact/com.microsoft.azure.mixedreality.v2019_02_28_preview/azure-mgmt-mixedreality",
            "https://mvnrepository.com/artifact/com.microsoft.azure.labservices.v2018_10_15/azure-mgmt-labservices",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-mgmt-monitor",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-mgmt-network",
            "https://mvnrepository.com/artifact/com.microsoft.azure.privatedns.v2018_09_01/azure-mgmt-privatedns",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-mgmt-redis",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-mgmt-resources",
            "https://mvnrepository.com/artifact/com.microsoft.azure.resourcegraph.v2019_04_01/azure-mgmt-resourcegraph",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-servicebus",
            "https://mvnrepository.com/artifact/com.microsoft.servicefabric/sf",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-mgmt-streamanalytics",
            "https://mvnrepository.com/artifact/com.microsoft.sqlserver/mssql-jdbc",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-storage-blob",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-storage-queue",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-mgmt-trafficmanager",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-mgmt-compute",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure",
            "https://mvnrepository.com/artifact/com.microsoft.azure/azure-core",
        };

        // Additional popular tags
        final String additionalTags[] = {
            "https://mvnrepository.com/tags/android",
            "https://mvnrepository.com/tags/apache",
            "https://mvnrepository.com/tags/api",
            "https://mvnrepository.com/tags/client",
            "https://mvnrepository.com/tags/cloud",
            "https://mvnrepository.com/tags/example",
            "https://mvnrepository.com/tags/extension",
            "https://mvnrepository.com/tags/framework",
            "https://mvnrepository.com/tags/gradle",
            "https://mvnrepository.com/tags/groovy",
            "https://mvnrepository.com/tags/http",
            "https://mvnrepository.com/tags/io",
            "https://mvnrepository.com/tags/jboss",
            "https://mvnrepository.com/tags/library",
            "https://mvnrepository.com/tags/osgi",
            "https://mvnrepository.com/tags/platform",
            "https://mvnrepository.com/tags/rlang",
            "https://mvnrepository.com/tags/scala",
            "https://mvnrepository.com/tags/service",
            "https://mvnrepository.com/tags/starter",
            "https://mvnrepository.com/tags/streaming",
            "https://mvnrepository.com/tags/ui",
            "https://mvnrepository.com/tags/web",
            "https://mvnrepository.com/tags/webapp",
        };

        for(String s : azureLibraryList) {
            this.addSeedAndReturn(s).type("versionPage");
        }
        for(String s : additionalTags) {
            this.addSeedAndReturn(s+"?p=1").type("tagListPage");
        }

        setThreads(1);  
        getConf().setTopN(20000);  
        getConf().setExecuteInterval(3100);   //safe interval 
        getConf().setConnectTimeout(20*1000);
        getConf().setReadTimeout(20*1000);
        //disable resumable mode
        setResumable(false);
    }

    @Override
    public void visit(Page page, CrawlDatums next){
        final String url = page.url();
        if(page.matchType("versionPage")) {
            //  the latest version
            Elements contents = page.select("body>div#page>div#maincontent>div:not([class])").last().select(
                                            "div#snippets>div[class=tab_container]>div[class=tab_content active]"+
                                            ">div[class=gridcontainer]>table[class=grid versions]>tbody");
            String tmp_result = "";
            for(int i=0;i<contents.size();++i)
            {
                tmp_result = contents.get(i).select("tr").first().select("td:not([rowspan])").select("a").first().attr("href");
                if(tmp_result.toLowerCase().indexOf("alpha") == -1 && tmp_result.toLowerCase().indexOf("beta") == -1) {
                    break;
                }
            }
            String result = tmp_result;
            String NewUrl = url.substring(0, url.lastIndexOf("/") + 1) + result;
            next.add(new CrawlDatum(NewUrl).type("downloadPage"));
        } else if(page.matchType("downloadPage")){
            String contents = page.select("body>div#page>div#maincontent>div[id=snippets]").first().select(
                                "textarea[id=maven-a]").first().text();
            Elements UsageContent = page.select("body>div#page>div#maincontent>table[class=grid]>tbody>tr");
            Integer usage = 0;
            if(UsageContent.last().select("th").first().text().trim().startsWith("Used")){
                usage = Integer.valueOf(UsageContent.last().select("b").first().text().trim().split(" ")[0].replace(",",""));
            }
            Matcher mG = patternG.matcher(contents);
            Matcher mA = patternA.matcher(contents);
            Matcher mV = patternV.matcher(contents);
            String groupId = "";
            String artifactId = "";
            String version = "";
            if(mG.find()) groupId = mG.group(1);
            if(mA.find()) artifactId = mA.group(1);
            if(mV.find()) version = mV.group(1);
            try{
                if(groupId != "" && artifactId != "" && version != ""){
                    Downloader.DownLoad(groupId, artifactId, version, downloadPath);
                    artifactUsage.put(groupId+":"+artifactId, usage);
                }
            }catch(ArtifactResolutionException|StringIndexOutOfBoundsException e){
                e.printStackTrace();
            }
        } else if(page.matchType("tagCloudPage")){
            Elements contents = page.select("body>div#page>div#maincontent>center>a");
            for(int i = 0;i < contents.size();++i)
            {
                String result = contents.get(i).attr("href");
                next.add(new CrawlDatum("https://mvnrepository.com"+result+"?p=1").type("tagListPage"));
            }
        } else if(page.matchType("tagListPage")){
            String prefix = page.url().substring(0,page.url().lastIndexOf('?'));
            if(page.url().endsWith("p=1")){
                int pageNum = page.select("body>div#page>div#maincontent>ul[class=search-nav]>li").size()-2;
                for(int pageIndex = 2; pageIndex <= pageNum; pageIndex++) {
                    String seedUrl = String.format(prefix+"?p=%d", pageIndex);
                    this.addSeed(seedUrl, "tagListPage");
                }
            }
            Elements contents = page.select("body>div#page>div#maincontent>div[class=im]");
            int usage = 0;
            for(int i = 0;i < contents.size();++i)  
            {
                Elements e = contents.get(i).select("div[class=im-header]>h2[class=im-title]");
                if(e.size() == 0) continue;
                String result = e.select("a").first().attr("href");
                usage = Integer.valueOf(e.select("b").first().text().replace(",",""));
                if(usage < threshold) break;
                next.add(new CrawlDatum("https://mvnrepository.com"+ result).type("versionPage"));
            }
            // have to dig out 
            if(usage>=threshold){
                Elements elms = page.select("body>div#page>div#maincontent>ul[class=search-nav]>li")
                                            .last().select("a");
                if(elms.size() > 0) {
                    String NPage = elms.first().attr("href");
                    int NextPage = Integer.valueOf(NPage.substring(NPage.lastIndexOf('=')+1));
                    if(NextPage - 1 == Integer.valueOf(page.url().substring(page.url().lastIndexOf('=')+1))){
                        next.add(prefix+NPage, "tagListPage");
                    }
                }
            }
        }
    }
}