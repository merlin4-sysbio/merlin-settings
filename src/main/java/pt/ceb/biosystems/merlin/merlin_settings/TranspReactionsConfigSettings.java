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

@Operation(name="TranspReactionsSettings", description="Configure create transport reactions settings")
public class TranspReactionsConfigSettings {

	private String alphaValue;
	private String cutoffThreshold;
	private String symportCurrencyMetabolites;
	
	/**
	 * @param alphaValue
	 */
	@Port(direction=Direction.INPUT, name="alphaValue", order=1)
	public void setalphaValue(String alphaValue) {

		this.alphaValue = alphaValue;

	}
	
	/**
	 * @param cutoffThreshold
	 */
	@Port(direction=Direction.INPUT, name="cutoffThreshold", order=2)
	public void setcutoffThreshold(String cutoffThreshold) {

		this.cutoffThreshold = cutoffThreshold;

	}
	
	/**
	 * @param symportCurrencyMetabolites
	 */
	@Port(direction=Direction.INPUT, name="symportCurrencyMetabolites", order=3)
	public void setsymportCurrencyMetabolites(String symportCurrencyMetabolites) {

		this.symportCurrencyMetabolites = symportCurrencyMetabolites;
		saveConfigurations();
		
	}

	
	
	/**
	 * Save new configurations in .conf file. 
	 */
	public void saveConfigurations() {
		String validation = "(?<=^| )\\d+(\\.\\d+)?(?=$| )";
		if(!alphaValue.equals("") && !cutoffThreshold.equals("") && !symportCurrencyMetabolites.equals("")) {
		if (alphaValue.matches(validation) && cutoffThreshold.matches(validation)) {

		String path = FileUtils.getConfFolderPath().concat("transp_reactions_settings.conf");

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
		

		for (int i = 0; i < listLines.size(); i++) { 
			
			if(listLines.get(i).startsWith("alpha Value")) {
				String newline = "alpha Value: ".concat(alphaValue.trim());
				listLines.remove(i);
				listLines.add(i, newline);
				
			}
			
			if(listLines.get(i).startsWith("cut-off Threshold")) {
				String newline = "cut-off Threshold:".concat(cutoffThreshold.trim());
				listLines.remove(i);
				listLines.add(i, newline);
				
			}
			
			if(listLines.get(i).startsWith("symport Currency Metabolites")) {
				String newline = "symport Currency Metabolites:".concat(symportCurrencyMetabolites.trim());
				listLines.remove(i);
				listLines.add(i, newline);
				
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

		}
		else {
			Exception ex = new Exception("Please insert valid values for these fields!");
			onValidationError(ex);
		}
		}
		else {
			Exception ex = new Exception("All fields must be filled!");
			onValidationError(ex);
		}
		
		
	}
	
	public void onValidationError(Throwable arg0) {

		Workbench.getInstance().error(arg0);
	}


		
	}
