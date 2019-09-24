# Maven-Index-Crawler
This Maven-Index-Crawler is used to crawl the popular jars and their usage information in https://mvnrepository.com/.

## Quick start

It is easy to start the crawler. 

First, set up a proper usage threshold (the jar will be crawled only then its usage is higher than the threshold) in ***crawl.java*** , e.g.
```
public static void main(String[] args) throws Exception {
        final int usageThreshold = 300;
        ...
}
```

Then, run the main function and wait patiently.

## Result
The crawler will produce the following files. The ***IndexData*** directory is exactly what we want.

+---- JarsRepo <br>
+---- IndexResult <br>
| &nbsp; &nbsp; &nbsp; &nbsp; +---- IndexFiles <br>
| &nbsp; &nbsp; &nbsp; &nbsp; +---- **IndexData** <br>
| &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; +---- ArtifactUsage.json <br>
| &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; +---- index <br>


The detail description is as follows:

| File/Directory   | Description  |
|  ----  | ----  |
| JarsRepo  | The crawled jars based on which the index is constructed |
| IndexFiles  | The zip file of the index |
| ArtifactUsage.json  | The usage information of the jars |
| index  | The constructed index |
