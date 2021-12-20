package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;


class Implementation implements Runnable{

    static boolean runServer=true;
    static String  statusServer;
    static int port;
    static boolean connectionOk=false;
    static WebServer server;

    public static void main(String[] args){

        server = new WebServer();
        setPortInterface();


        Thread interfaceThread = new Thread(new Implementation());
        interfaceThread.start();


        try {
            readComands();
        } catch (IOException e) {
            System.out.println("\n Invalid command !");
        }
    }

    @Override
    public void run() {
        while(runServer) {
            server.handleClient();
        }
    }


    public static void readComands() throws IOException {
        while(true)
        {
            System.out.print("+ Enter command :");
            BufferedReader readerCommand =  new BufferedReader(new InputStreamReader(System.in));
            String comandaLinie = readerCommand.readLine();
            verifCommand(comandaLinie);
        }
    }

    public static void verifCommand(String cmd) throws IOException {
        if (cmd.equals("status")) {
            System.out.println("Status WebServer is :"+statusServer);
        }else if (cmd.equals("systeminfo")) {
            System.out.println("Status server : "+statusServer);
            System.out.println("Port : 10008 ");
            InetAddress IP = InetAddress.getLocalHost();
            System.out.println("Host : "+IP.getHostAddress());
        }else if (cmd.equals("pause")){
            server.setStateServer(2);
            statusServer="MAINTENANCE";
            System.out.println("Server is now in maintenance mode!");
        }else if (cmd.equals("start")) {
            server.setStateServer(1);
            statusServer="RUNNING";
            System.out.println("Server started");
        }else if (cmd.equals("stop")){
            server.setStateServer(3);
            statusServer="STOP";
            System.out.println("Server stopped!");
        }else{
            System.out.println("This command is not defined!");
        }
    }

    public static void setPortInterface() {
        while(!connectionOk)
        {
			if (!server.acceptServerPort()) {
				System.out.println("Cannot connect on this port!\n");
			} else {
				System.out.println("Port  was accepted! Enter 'start' command in order to start server\n");
				connectionOk = true;
				statusServer = "STOP";
			}
		} 
    }

}
