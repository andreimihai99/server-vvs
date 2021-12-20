package webserver;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class WebServerTest
{
    File root= new  File("TestSite");
    File fileHTML= new File(root,"typefile.html");
    File fileCSS= new File(root,"typefile.css");
    File fileJS= new File(root,"typefile.js");
    File fileN1= new File(root,"typefile.js..d");

    WebServer server;

    @Test
    public void testGetStatus()
    {
        server.setStateServer(1);
        assertEquals(1,server.getStateServer());

    }

    @Test
    public void testGetSocketClient() {
        Socket sock = new Socket();
        server.setClientSocket(sock);
        assertEquals(sock,server.getClientSocket());
    }


    @Before
    public void setup() {
        server=new WebServer();
    }

    @Test
    public void testSetPort1()
    {
        int port=0;
        server.setPort(port);
        assertEquals(false,server.acceptServerPort());
    }

    @Test
    public void testAcceptServerPort1()
    {
        server.setPort(10005);
        assertEquals(true,server.acceptServerPort());
    }

    @Test
    public void testAcceptServerPort2()
    {
        server.setPort(100000);
        assertEquals(false,server.acceptServerPort());
    }

    @Test
    public void testAcceptServerPort3()
    {
        server.setPort(-10);
        assertEquals(false,server.acceptServerPort());
    }

    @Test
    public void testAcceptServerPort4()
    {
        server.setPort(1024);
        assertEquals(false,server.acceptServerPort());
    }


    @Test
    public void testAcceptServerPort5()
    {
        server.setPort(65000);
        assertEquals(false,server.acceptServerPort());
    }

    @Test
    public void testListen()
    {
        server.setPort(3600);
        server.acceptServerPort();
        int srv = server.conectionClient;
        assertEquals(0,srv);

    }
    
    @Test
    public void verifyIfServerIsRunning(){
        server.setStateServer(1);
        assertEquals(1, server.getStateServer());
    }

    @Test
    public void verifyIfServerIsStopped(){
    	server.setStateServer(3);
        assertEquals(3, server.getStateServer());
    }

    @Test
    public void verifyIfServerIsInMaintenance(){
    	server.setStateServer(2);
        assertEquals(2, server.getStateServer());
    }


}