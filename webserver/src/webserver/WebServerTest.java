package webserver;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import org.junit.Test;

public class WebServerTest {
	
	private static WebServer webServer;
    private static ServerSocket serverSocket;
    private static int clientSocketPort;
    private File file = new File(WebServer.getIndex());
    public WebServerTest() throws IOException {
    }

    @Test
    public void verifyFileCreation() {
        assertNotNull(file);
    }

    @Test
    public void verifyIfServerIsRunning(){
        assumeTrue(WebServer.getStatus() == 1);
        assertEquals(1, WebServer.getStatus());
    }

    @Test
    public void verifyIfServerIsStopped(){
        assumeTrue(WebServer.getStatus() == 2);
        assertEquals(2, WebServer.getStatus());
    }

    @Test
    public void verifyIfServerIsInMaintenance(){
        assumeTrue(WebServer.getStatus() == 3);
        assertEquals(3, WebServer.getStatus());
    }
    
    @Test
    public void verifySocket() {
    	assertEquals(webServer.getClientSocket(), serverSocket);
    }

    @Test
    public void verifyIndexPagePath(){
        assertEquals("B:\\Andrei\\Faculta\\An4\\LabVVS\\project\\TestSite\\index.html", WebServer.getIndex());
    }

    @Test
    public void verifyErrorPagePath(){
        assertEquals("B:\\Andrei\\Faculta\\An4\\LabVVS\\project\\TestSite\\error.html", WebServer.getError());
    }

    @Test
    public void verifyStoppedPagePath(){
        assertEquals("B:\\Andrei\\Faculta\\An4\\LabVVS\\project\\TestSite\\stopped.html", WebServer.getStopped());
    }

    @Test
    public void verifyMaintenancePagePath(){
        assertEquals("B:\\Andrei\\Faculta\\An4\\LabVVS\\project\\TestSite\\maintenance.html", WebServer.getMaintenance());
    }

}
