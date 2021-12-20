package webserver;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserInterface implements Runnable {
    WebServer server = new WebServer();

    public JPanel Form;
    public JButton Start;
    public JButton Stop;
    public JCheckBox checkBox = new JCheckBox("Switch to maintenance mode");

    boolean state = true;

    Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

    TitledBorder title = BorderFactory.createTitledBorder(loweredetched, "Web Server Control");
    //Form.SetBorder(title);

    public UserInterface() {
		Start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkBox.setSelected(false);
				if (Start.getText().equals("Start server")) {
					Stop.setText("Stop server");
					JOptionPane.showMessageDialog(null, "Server stoped!");
					server.setStateServer(3);
					Start.setEnabled(true);
					state = true;
				}

			}

			public void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException,
					InstantiationException, IllegalAccessException {
				JFrame frame = new JFrame("App");

				frame.setContentPane(new UserInterface().Form);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}

			public void startWebServer() {
					JOptionPane.showMessageDialog(null, "Server Start");
					Start.setEnabled(false);
					// oldPort = curentPort;
					server.setStateServer(1);
					Thread interfaceThread = null;
					interfaceThread = new Thread(new UserInterface());
					interfaceThread.start();
			}
		});
}
    public void run() {
        server.listenForClients();
    }
}