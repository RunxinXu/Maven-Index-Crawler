package org.runxin.crawl;


import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;

public class Downloader {

	private static RepositorySystem newRepositorySystem()
    {
        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService( RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class );
        locator.addService( TransporterFactory.class, FileTransporterFactory.class );
        locator.addService( TransporterFactory.class, HttpTransporterFactory.class );
        return locator.getService( RepositorySystem.class);
    }

	private static RepositorySystemSession newSession( RepositorySystem system,String downloadPath )
    {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
        LocalRepository localRepo = new LocalRepository(downloadPath);
        session.setLocalRepositoryManager( system.newLocalRepositoryManager( session, localRepo ) );
        return session;
    }
    
    
    public static void DownLoad(String groupId, String artifactId, String version, 
                                String downloadPath) throws ArtifactResolutionException,StringIndexOutOfBoundsException
	{
        
		RepositorySystem repoSystem = newRepositorySystem();
        RepositorySystemSession session = newSession(repoSystem ,downloadPath);
        RemoteRepository central=null;
        central = new RemoteRepository.Builder( "central", "default", "http://repo1.maven.org/maven2" ).build();

        /* Download jar */
        Artifact artifact=new DefaultArtifact(groupId+":"+artifactId+":"+version);
        ArtifactRequest artifactRequest=new ArtifactRequest();
        artifactRequest.addRepository(central);
        artifactRequest.setArtifact(artifact);
        repoSystem.resolveArtifact(session, artifactRequest);
        System.out.println("Successfully download :" + groupId + ":" + artifactId);
	}
}