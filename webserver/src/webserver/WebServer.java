package webserver;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.StringTokenizer;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
public class WebServer implements Runnable
{

    // serverSoket and port will be available for all instance of myServer

    static ServerSocket serverSocket;
    static int serverPort=0;
    public int conectionClient;
    static final File WEB_ROOT= new File("B:\\Andrei\\Faculta\\An4\\LabVVS\\project\\server-vvs\\TestSite");
    static final String DEFAULT_FILE= "a.html";
    static final String FILE_NOT_FOUND="error.html";
    static final String FILE_MAINTENANCE="maintenance.html";
    static int statusServerInt=0;

    private Socket clientSocket;

    public void setClientSocket(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }

    public Socket getClientSocket()
    {
        return this.clientSocket;
    }


    public void setStateServer(int state)
    {
        statusServerInt=state;
    }

    public int getStateServer()
    {
        return statusServerInt;
    }

    public boolean acceptServerPort()
    {
        try {
            serverSocket=new ServerSocket(10008);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public void setConnectionOK(int x)
    {
        this.conectionClient=x;
    }

    public void handleClient()
    {
        while(true)
        {
            WebServer connection;
            try {
                connection = new WebServer();
                connection.setClientSocket(serverSocket.accept());
                setConnectionOK(1);
                Thread thread = new Thread(connection);
                thread.start();
            }catch (IOException e)
            {
                setConnectionOK(0);
            }
        }
    }

    
    public  boolean setPort(int portNr)
    {
        try {
            try {
                serverPort = portNr;
                return true;
            } finally {
                new ServerSocket(portNr).close();
            }
        } catch (Exception e)
        {
            return false;
        }
    }

    @Override
    public void run() {


        BufferedReader in = null;
        PrintWriter out=null;
        BufferedOutputStream dataOut;
        dataOut = null;
        String fileRequested=null;


        try {

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out=new PrintWriter(clientSocket.getOutputStream());
            dataOut=new BufferedOutputStream(clientSocket.getOutputStream());

            //get first line of request
            String input = in.readLine();
            StringTokenizer parse= new StringTokenizer(input);
            String method= parse.nextToken().toUpperCase();

            //get file request
            fileRequested=URLDecoder.decode(parse.nextToken().toLowerCase(), "UTF-8");



            System.out.println("+ <<--REQUEST :  "+input);


            if(!method.equals("GET") && !method.equals("HEAD"))
            {
                System.out.println("501 Not implemented: "+method);
                //writeFileData(METHOD_NOT_SUPPORTED,out,dataOut,"HTTP/1.1 501 Not Implemented");

            }
            else
            {

                if(method.equals("GET"))
                {	if(statusServerInt==1)
                {
                    if(fileRequested.endsWith("/"))
                    {
                        fileRequested+=DEFAULT_FILE;
                    }
                    //return content
                    writeFileData(fileRequested,out,dataOut,"HTTP/1.1 200 OK");

                }
                else if(statusServerInt==2)
                {
                    fileRequested=FILE_MAINTENANCE;
                    writeFileData(fileRequested,out,dataOut,"HTTP/1.1 200 OK");
                }
                else if(statusServerInt==3)
                {
                    // close  server
                    in.close();
                    out.close();
                    dataOut.close();
                    clientSocket.close();
                }
                }

            }
        }
        catch(FileNotFoundException fnfe)
        {
            try
            {
                fileNotFound(out,dataOut,fileRequested);
            }
            catch(IOException ioe)
            {
                System.err.println("+ File : 404.html not found  "+ioe.getMessage());
            }
        }
        catch(IOException ioe)
        {
            System.err.println("+ Server error : "+ioe);
        }
        finally
        {
            try{
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (dataOut != null) {
                    dataOut.close();
                }
                clientSocket.close();
            }
            catch(Exception e)
            {
                System.err.println("+ Error closing stream:"+e.getMessage());
            }
        }
    }

    public byte[] readFileData(File file,int fileLength) throws IOException
    {
        FileInputStream fileIn=null;
        byte[] fileData= new byte[fileLength];
        try{
            fileIn=new FileInputStream(file);
            fileIn.read(fileData);
        }
        finally
        {
            if(fileIn!=null)
                fileIn.close();
        }
        return fileData;
    }

    void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException
    {
        writeFileData(FILE_NOT_FOUND,out,dataOut,"HTTP/1.1 404 Not found");
        System.out.println("File: "+fileRequested+" not found! ");
    }

    public void writeFileData(String nameOfFileRequested,PrintWriter hearderOut,OutputStream bodyOut,String headerText)
    {

        File file= new File(WEB_ROOT,nameOfFileRequested);
        int fileLength=(int)file.length();
       // String content=getFilePath(file);

        String inputString = "No file";
        Charset charset = StandardCharsets.US_ASCII;
        byte[] fileData =  inputString.getBytes(charset);
        try
        {
            fileData = readFileData(file,fileLength);
        } catch (IOException e)
        {
            System.out.println("Cannot read this file : "+file);
        }

        //send Header
        hearderOut.println(headerText);
        hearderOut.println("Server : Java HTTP Server  ");
        hearderOut.println("Date: "+new Date());
        //hearderOut.println("Content-type: "+content);
        hearderOut.println("Content-length: "+fileLength);
        hearderOut.println();//blamk line between head and content very important
        hearderOut.flush();

        //send data
        try
        {
            bodyOut.write(fileData,0,fileLength);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try
        {
            bodyOut.flush();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void listenForClients() {
    }
}