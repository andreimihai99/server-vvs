package webserver;

import java.net.*;
import java.util.StringTokenizer;
import java.io.*;

public class WebServer extends Thread {
	protected Socket clientSocket;
	
	private static final String INDEX = "B:\\Andrei\\Faculta\\An4\\LabVVS\\project\\server-vvs\\TestSite\\index.html";
	private static final String ERROR = "B:\\Andrei\\Faculta\\An4\\LabVVS\\project\\server-vvs\\TestSite\\error.html";
	private static final String STOPPED = "B:\\Andrei\\Faculta\\An4\\LabVVS\\project\\server-vvs\\TestSite\\stopped.html";
	private static final String MAINTENANCE = "B:\\Andrei\\Faculta\\An4\\LabVVS\\project\\server-vvs\\TestSite\\maintenance.html";
	private static int status = 1;
	
	public static int getStatus() {
		return status;
	}

	public Socket getClientSocket() {
		return clientSocket;
	}

	public static String getError() {
		return ERROR;
	}

	public static String getStopped() {
		return STOPPED;
	}

	public static String getMaintenance() {
		return MAINTENANCE;
	}

	public static String getIndex() {
		return INDEX;
	}

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(10008);
			System.out.println("Connection Socket Created");
			try {
				while (true) {
					System.out.println("Waiting for Connection");
					new WebServer(serverSocket.accept());
				}
			} catch (IOException e) {
				System.err.println("Accept failed.");
				System.exit(1);
			}
		} catch (IOException e) {
			System.err.println("Could not listen on port: 10008.");
			System.exit(1);
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				System.err.println("Could not close port: 10008.");
				System.exit(1);
			}
		}
	}

	WebServer(Socket clientSoc) {
		clientSocket = clientSoc;
		start();
	}

	public void run() {
		System.out.println("New Communication Thread Started");

		try {
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
					true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));

			String inputLine = in.readLine();
			
			StringTokenizer parse = new StringTokenizer(inputLine);
			
			String method = parse.nextToken().toUpperCase();
			
			String requestedFile = parse.nextToken().toLowerCase();
			
			switch(status) {
			case 1: running(out, method, requestedFile);
			case 2: stopped(out);
			case 3: maintenance(out);
			}
			
			out.close();
			in.close();
			clientSocket.close();
		} catch (IOException e) {
			System.err.println("Problem with Communication Server");
			System.exit(1);
		}
	}
	
	private void running(PrintWriter out, String method, String requestedFile){
		try{
			if(method.equals("GET")){
				if(requestedFile.endsWith("/") || requestedFile.endsWith("index.html") ){
					File file = new File(INDEX);
					byte[] fileData = new byte[(int)file.length()];
					FileInputStream inFile = new FileInputStream(file);

					inFile.read(fileData);
					out.println("HTTP/1.0 200 OK");
					out.println("Content-Type: text/html");
					out.println("\r\n");
					out.flush();
					inFile.close();

					String fileContent = new String(fileData);
					out.println(fileContent);
					out.close();
				}
				else{
					File file = new File(ERROR);
					byte[] fileData = new byte[(int)file.length()];
					FileInputStream inFile = new FileInputStream(file);
					inFile.read(fileData);

					out.println("HTTP/1.1 404 File Not Found");
					out.println("Content-Type: text/html");
					out.println("\r\n");
					out.flush();
					inFile.close();

					String responseFileContent = new String(fileData);
					out.println(responseFileContent);
					out.close();
				}
			}
		}catch (IOException e) {
			System.err.println("Problem with Communication Server");
			System.exit(1);
		}
	}
	
	private void maintenance(PrintWriter out){
		try{
			File file = new File(MAINTENANCE);
			
			byte[] fileData = new byte[(int)file.length()];
			FileInputStream inFile = new FileInputStream(file);
			inFile.read(fileData);

			out.println("HTTP/1.1 200 OK");
			out.println("Content-Type: text/html");
			out.println("\r\n");
			out.flush();
			inFile.close();

			String responseFileContent = new String(fileData);
			out.println(responseFileContent);
			out.close();

		}catch(IOException e){
			System.err.println("Problem with Communication Server");
			System.exit(2);
		}
	}
	
	private void stopped(PrintWriter out){
		try{
			File file = new File(STOPPED);
			
			byte[] fileData = new byte[(int)file.length()];
			FileInputStream inFile = new FileInputStream(file);
			inFile.read(fileData);

			out.println("HTTP/1.1 522 Connection Timeout");
			out.println("Content-Type: text/html");
			out.println("\r\n");
			out.flush();
			inFile.close();

			String responseFileContent = new String(fileData);
			out.println(responseFileContent);
			out.close();

		}catch(IOException e){
			System.err.println("Problem with Communication Server");
			System.exit(2);
		}
	}
}