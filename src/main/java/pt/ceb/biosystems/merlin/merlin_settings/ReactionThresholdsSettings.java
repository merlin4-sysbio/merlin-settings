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

@Operation(name="ReactionThresholdsSettings", description="Configure reaction thresholds settings")
public class ReactionThresholdsSettings {

	private String similarity;
	private String reference_taxo;


	/**
	 * @param similarity
	 */
	@Port(direction=Direction.INPUT, name="Similarity", order=1)
	public void setHost(String similarity) {

		this.similarity = similarity;

	}
	
	/**
	 * @param reference_taxo
	 */
	@Port(direction=Direction.INPUT, name="ReferenceTaxo", order=2)
	public void setPort(String reference_taxo) {

		this.reference_taxo = reference_taxo;
		saveConfigurations();
	}
	
	
	
	
	/**
	 * 
	 */
	public void saveConfigurations() {
		
		String validation = "(?<=^| )\\d+(\\.\\d+)?(?=$| )";
		if(!similarity.equals("") && !reference_taxo.equals("")) {
			
		if (similarity.matches(validation) && reference_taxo.matches(validation)) {
		
		ArrayList<String> listLines= new ArrayList<>(); 
		String path = FileUtils.getConfFolderPath().concat("reaction_thresholds.conf");
		File configfile= new File(path);
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
		
		for (int i = 0; i < listLines.size(); i++) { 
			
			if(listLines.get(i).startsWith("similarity_threshold")) {
				String newline = "similarity_threshold: ".concat(similarity.trim());
				listLines.remove(i);
				listLines.add(i, newline);
				
			}
			if(listLines.get(i).startsWith("reference_taxo_threshold")) {
				String newline = "reference_taxo_threshold: ".concat(reference_taxo.trim());
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
		}else {
			Exception ex = new Exception("All fields must be filled!");
			onValidationError(ex);
		}
		
		
		
	}
	
	public void onValidationError(Throwable arg0) {

		Workbench.getInstance().error(arg0);
	}


		

	}


