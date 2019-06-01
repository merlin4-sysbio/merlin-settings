package pt.ceb.biosystems.merlin.merlin_settings;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import org.sing_group.gc4s.dialog.AbstractInputJDialog;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.combobox.ExtendedJComboBox;

import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.InputGUI;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.aibench.gui.CustomGUI;
import pt.uminho.ceb.biosystems.merlin.aibench.utilities.CreateImageIcon;
import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;


public class DatabaseConfigGUI extends AbstractInputJDialog implements InputGUI{
	private static final long serialVersionUID = 1L;

	public static String[] items = {
			"H2",
			"MySQL", 
	};
	public static JTextField username = new JTextField();
	public static JPasswordField password = new JPasswordField();
	public static ExtendedJComboBox<String> dbType = new ExtendedJComboBox<String>(items);
	public static  JTextField host = new JTextField();
	public static  JTextField port = new JTextField();

	private ParamsReceiver rec;

	public DatabaseConfigGUI() {
		super(new JFrame());
		fill();
	}

	public String getDialogTitle() {
		return "database settings";
	}

	public String getDescription() {
		return "change database settings in the configuration file";
	}

	public JPanel getInputComponentsPane() {
		return new InputParametersPanel(getInputParameters());
	}

	@Override
	protected Component getButtonsPane() {
		final JPanel buttonsPanel = new JPanel(new FlowLayout());

		okButton = new JButton("save");
		okButton.setEnabled(true);
		okButton.setToolTipText("save");
		okButton.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Save.png")),0.1).resizeImageIcon());
		ActionListener listener= new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				String passText = new String(password.getPassword());

				rec.paramsIntroduced(
						new ParamSpec[]{
								new ParamSpec("Host",String.class,host.getText(),null),
								new ParamSpec("Port",String.class,port.getText(),null),
								new ParamSpec("User",String.class,username.getText(),null),
								new ParamSpec("Password",String.class,passText,null),
								new ParamSpec("Database Type",String.class,dbType.getSelectedItem().toString(),null),
						}
						);
				if(dbType.getSelectedItem().toString().equals("H2")) {
					if(!username.getText().equals("")&& !String.valueOf(password.getPassword()).equals("")) {

						dispose();

					}
				}
				if(dbType.getSelectedItem().toString().equals("MySQL")) {
					if(!username.getText().equals("")&& !String.valueOf(password.getPassword()).equals("")&&!port.getText().equals("")&&!host.getText().equals("")) {
						if (port.getText().matches("[0-9]+")) {
							dispose();
						}
					}
				}

			}
		};
		okButton.addActionListener(listener);

		cancelButton = new JButton("cancel");
		cancelButton.setToolTipText("cancel");
		cancelButton.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Cancel.png")),0.1).resizeImageIcon());
		cancelButton.addActionListener(event -> {

			String[] options = new String[2];
			options[0] = "yes";
			options[1] = "no";

			int result = CustomGUI.stopQuestion("cancel confirmation", "are you sure you want to cancel the operation?", options);

			if(result == 0) {
				canceled = true;
				dispose();

			}

		});


		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);

		getRootPane().setDefaultButton(okButton);
		InputMap im = okButton.getInputMap();
		im.put(KeyStroke.getKeyStroke("ENTER"), "pressed");
		im.put(KeyStroke.getKeyStroke("released ENTER"), "released");

		ActionListener updateDbType = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(dbType.getSelectedItem().toString().equals("MySQL")) {
					fillWithoutDbMySQL();
				}else {
					fillWithoutDbH2();
				}


			}
		};

		dbType.addActionListener(updateDbType);


		return buttonsPanel;


	}
	private InputParameter[] getInputParameters() {
		InputParameter[] parameters = new InputParameter[5];
		parameters[0] = 
				new InputParameter(
						"database type", 
						dbType, 
						"select database type"
						);

		parameters[1] = 
				new InputParameter(
						"username", 
						username, 
						"set username"
						);
		parameters[2] = 
				new InputParameter(
						"password", 
						password, 
						"set password"
						);
		parameters[3] = 
				new InputParameter(
						"host", 
						host, 
						"set host"
						);
		parameters[4] = 
				new InputParameter(
						"port", 
						port, 
						"set port"
						);


		return parameters;
	}

	public void fill () {

		ArrayList<String> listLines= new ArrayList<>();
		String confPath = FileUtils.getConfFolderPath().concat("database_settings.conf");
		File configfile= new File(confPath);
		try {
			Scanner file= new Scanner(configfile);
			while(file.hasNextLine()==true) {
				listLines.add(file.nextLine());
			}
			file.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (String item : listLines) {

			if(item.startsWith("dbtype")) {
				String[]parts=item.split(":");


				if(parts[1].trim().equals("h2")) {
					for (String dbOption : items) {
						if(dbOption.equals("H2")) {
							dbType.setSelectedItem(dbOption);
							fillWithoutDbH2();
							continue;
						}
					}

				}

				if(parts[1].trim().equals("mysql")) {
					for (String dbOption : items) {
						if(dbOption.equals("MySQL")) {
							dbType.setSelectedItem(dbOption);
							fillWithoutDbMySQL();
							continue;
						}
					}
				}
			}
		}
	}
	public void fillWithoutDbMySQL() {
		ArrayList<String> listLines= new ArrayList<>();
		String confPath = FileUtils.getConfFolderPath().concat("database_settings.conf");
		File configfile= new File(confPath);
		try {
			Scanner file= new Scanner(configfile);
			while(file.hasNextLine()==true) {
				listLines.add(file.nextLine());
			}
			file.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (String item : listLines) {

			if(item.startsWith("username")) {

				String[]parts=item.split(":");
				username.setText(parts[1].trim());
				continue;

			}
			if(item.startsWith("password")) {
				String[]parts=item.split(":");
				password.setText(parts[1].trim());
				continue;

			}
			if(item.startsWith("host")) {
				String[]parts=item.split(":");
				host.setText(parts[1].trim());
				continue;

			}
			if(item.startsWith("port")) {
				String[]parts=item.split(":");
				port.setText(parts[1].trim());
				continue;

			}
		}
		port.setEditable(true);
		host.setEditable(true);
	}
	public void fillWithoutDbH2() {
		ArrayList<String> listLines= new ArrayList<>();
		String confPath = FileUtils.getConfFolderPath().concat("database_settings.conf");
		File configfile= new File(confPath);
		try {
			Scanner file= new Scanner(configfile);
			while(file.hasNextLine()==true) {
				listLines.add(file.nextLine());
			}
			file.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (String item : listLines) {

			if(item.startsWith("h2_username")) {
				String[]parts=item.split(":");
				username.setText(parts[1].trim());
				continue;

			}
			if(item.startsWith("h2_password")) {
				String[]parts=item.split(":");
				password.setText(parts[1].trim());
				continue;

			}
			host.setText("");
			port.setText("");
		}
		port.setEditable(false);
		host.setEditable(false);
	}

	@Override
	public void setVisible(boolean b) {
		this.pack();
		super.setVisible(b);
	}

	@Override
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		this.rec = arg0;
		this.setTitle(arg1.getName());
		this.setVisible(true);		
	}

	/* (non-Javadoc)
	 * @see es.uvigo.ei.aibench.workbench.InputGUI#onValidationError(java.lang.Throwable)
	 */
	public void onValidationError(Throwable arg0) {

		Workbench.getInstance().error(arg0);
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}
}
