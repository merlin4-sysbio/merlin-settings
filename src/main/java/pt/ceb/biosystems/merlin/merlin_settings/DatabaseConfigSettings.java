package pt.ceb.biosystems.merlin.merlin_settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;

@Operation(name="DatabaseConfigSettings", description="Configure database settings")
public class DatabaseConfigSettings {

	private String host;
	private String port;
	private String username;
	private String password;
	private String dbType;


	/**
	 * @param host
	 */
	@Port(direction=Direction.INPUT, name="Host", order=1)
	public void setHost(String host) {

		this.host = host;

	}
	
	/**
	 * @param port
	 */
	@Port(direction=Direction.INPUT, name="Port", order=2)
	public void setPort(String port) {

		this.port = port;

	}
	
	/**
	 * @param username
	 */
	@Port(direction=Direction.INPUT, name="User", order=3)
	public void setUsername(String username) {

		this.username = username;

	}

	
	/**
	 * @param password
	 */
	@Port(direction=Direction.INPUT, name="Password", order=4)
	public void setPassword(String password) {

		this.password = password;

	}
	
	/**
	 * @param dbType
	 * @return 
	 */
	@Port(direction=Direction.INPUT, name="Database Type", order=5)
	public void setDatabaseType(String dbType) {
		
		this.dbType = dbType;
		saveConfigurations();
	}
	
	
	
	/**
	 * 
	 */
	public void saveConfigurations(){

		if(dbType.toString().equals("H2")) {
			if(!username.equals("")&& !password.equals("")) {
				
			
		
		String path = FileUtils.getConfFolderPath().concat("database_settings.conf");
		
		ArrayList<String> listLines= new ArrayList<>(); 
		File configFile= new File(path); 
		try {
			Scanner file= new Scanner(configFile); 
			while(file.hasNextLine()==true) {
				listLines.add(file.nextLine());
			}
			file.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(dbType.equals("MySQL")) {
			for (int i = 0; i < listLines.size(); i++) { 
				if(listLines.get(i).startsWith("username")) {
					String newline = "username: ".concat(username.trim());
					listLines.remove(i);
					listLines.add(i, newline);
				}
				if(listLines.get(i).startsWith("password")) {
					String newline = "password: ".concat(this.password.trim());
					listLines.remove(i);
					listLines.add(i, newline);
				}
				if(listLines.get(i).startsWith("host")) {
					String newline = "host: ".concat(host.trim());
					listLines.remove(i);
					listLines.add(i, newline);
				}
				if(listLines.get(i).startsWith("port")) {
					String newline = "port: ".concat(port.trim());
					listLines.remove(i);
					listLines.add(i, newline);
				}
				if(listLines.get(i).startsWith("dbtype")) {
					String newline = "dbtype: ".concat("mysql");
					listLines.remove(i);
					listLines.add(i, newline);
				}
			}

		}
		if (dbType.equals("H2")) {

			for (int i = 0; i < listLines.size(); i++) { 

				if(listLines.get(i).startsWith("h2_username")) {
					String newline = "h2_username: ".concat(username.trim());
					listLines.remove(i);
					listLines.add(i, newline);

				}
				if(listLines.get(i).startsWith("h2_password")) {
					String newline = "h2_password: ".concat(password.trim());
					listLines.remove(i);
					listLines.add(i, newline);

				}
				if(listLines.get(i).startsWith("dbtype")) {
					String newline = "dbtype: ".concat("h2");
					listLines.remove(i);
					listLines.add(i, newline);

				}
			}
		}

		try {
			PrintWriter confFile = new PrintWriter(path); 
			for (String line : listLines) {
				confFile.println(line);


			}
			confFile.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		}else {
			Exception ex = new Exception("Please insert username and password!");
			onValidationError(ex);
		}
	}
		
		if(dbType.toString().equals("MySQL")) {
			if(!username.equals("")&& !password.equals("")&&!port.equals("")&&!host.equals("")) {
				if (port.matches("[0-9]+")) {
			
		
		String path = FileUtils.getConfFolderPath().concat("database_settings.conf");

		ArrayList<String> listLines= new ArrayList<>(); 
		File configFile= new File(path); 
		try {
			Scanner file= new Scanner(configFile); 
			while(file.hasNextLine()==true) {
				listLines.add(file.nextLine());
			}
			file.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(dbType.equals("MySQL")) {
			for (int i = 0; i < listLines.size(); i++) { 
				if(listLines.get(i).startsWith("username")) {
					String newline = "username: ".concat(username.trim());
					listLines.remove(i);
					listLines.add(i, newline);
				}
				if(listLines.get(i).startsWith("password")) {
					String newline = "password: ".concat(this.password.trim());
					listLines.remove(i);
					listLines.add(i, newline);
				}
				if(listLines.get(i).startsWith("host")) {
					String newline = "host: ".concat(host.trim());
					listLines.remove(i);
					listLines.add(i, newline);
				}
				if(listLines.get(i).startsWith("port")) {
					String newline = "port: ".concat(port.trim());
					listLines.remove(i);
					listLines.add(i, newline);
				}
				if(listLines.get(i).startsWith("dbtype")) {
					String newline = "dbtype: ".concat("mysql");
					listLines.remove(i);
					listLines.add(i, newline);
				}
			}

		}
		if (dbType.equals("H2")) {

			for (int i = 0; i < listLines.size(); i++) { 

				if(listLines.get(i).startsWith("h2_username")) {
					String newline = "h2_username: ".concat(username.trim());
					listLines.remove(i);
					listLines.add(i, newline);

				}
				if(listLines.get(i).startsWith("h2_password")) {
					String newline = "h2_password: ".concat(password.trim());
					listLines.remove(i);
					listLines.add(i, newline);

				}
				if(listLines.get(i).startsWith("dbtype")) {
					String newline = "dbtype: ".concat("h2");
					listLines.remove(i);
					listLines.add(i, newline);

				}
			}
		}

		try {
			PrintWriter confFile = new PrintWriter(path); 
			for (String line : listLines) {
				confFile.println(line);


			}
			confFile.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		}else {
			Exception ex = new Exception("Port field accepts only numbers!");
			onValidationError(ex);
			
		}
		}else {
			Exception ex = new Exception("All fields must be filled!");
			onValidationError(ex);
		}
	}
		

	}
	
	public void onValidationError(Throwable arg0) {

		Workbench.getInstance().error(arg0);
	}

}