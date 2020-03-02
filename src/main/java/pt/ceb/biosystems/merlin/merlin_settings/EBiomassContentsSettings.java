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

@Operation(name="EBiomassContentsSettings", description="Configure EBiomass Contents settings")
public class EBiomassContentsSettings {

	private String proteincontents;
	private String dnacontents;
	private String rnacontents;
	private String mrnacontents;
	private String trnacontents;
	private String rrnacontents;


	/**
	 * @param proteincontents
	 */
	@Port(direction=Direction.INPUT, name="ProteinContents", order=1)
	public void setProteinContents(String proteincontents) {

		this.proteincontents = proteincontents;

	}
	
	/**
	 * @param dnacontents
	 */
	@Port(direction=Direction.INPUT, name="DnaContents", order=2)
	public void setDnaContents(String dnacontents) {

		this.dnacontents = dnacontents;

	}
	
	/**
	 * @param rnacontents
	 */
	@Port(direction=Direction.INPUT, name="RnaContents", order=3)
	public void setRnaContents(String rnacontents) {

		this.rnacontents = rnacontents;

	}

	
	/**
	 * @param mrnacontents
	 */
	@Port(direction=Direction.INPUT, name="MrnaContents", order=4)
	public void setMrnaContents(String mrnacontents) {

		this.mrnacontents = mrnacontents;

	}
	
	/**
	 * @param trnacontents
	 * @return 
	 */
	@Port(direction=Direction.INPUT, name="TrnaContents", order=5)
	public void setTrnaContents(String trnacontents) {
		
		this.trnacontents = trnacontents;
	}
	
	/**
	 * @param rrnacontents
	 * @return 
	 */
	@Port(direction=Direction.INPUT, name="RrnaContents", order=6)
	public void setRrnaContents(String rrnacontents) {
		
		this.rrnacontents = rrnacontents;
		saveConfigurations();
	}
	
	
	/**
	 * 
	 */
	public void saveConfigurations() {
		String validation = "(?<=^| )\\d+(\\.\\d+)?(?=$| )";
		if(!proteincontents.equals("") && !dnacontents.equals("") && !rnacontents.equals("")
				&& !mrnacontents.equals("")&& !trnacontents.equals("")&& !rrnacontents.equals(""))
				 {
		if (proteincontents.matches(validation) && dnacontents.matches(validation) && 
				rnacontents.matches(validation)  && mrnacontents.matches(validation) 
				&& trnacontents.matches(validation) && rrnacontents.matches(validation)) {

		String path = FileUtils.getConfFolderPath().concat("ebiomass_contents.conf");

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
			
			if(listLines.get(i).startsWith("proteinContents")) {
				String newline = "proteinContents: ".concat(proteincontents.trim());
				listLines.remove(i);
				listLines.add(i, newline);
				
			}
			if(listLines.get(i).startsWith("dnaContents")) {
				String newline = "dnaContents: ".concat(dnacontents.trim());
				listLines.remove(i);
				listLines.add(i, newline);
				
			}
			if(listLines.get(i).startsWith("rnaContents")) {
				String newline = "rnaContents: ".concat(rnacontents.trim());
				listLines.remove(i);
				listLines.add(i, newline);
				
			}
			if(listLines.get(i).startsWith("mRNA_Contents")) {
				String newline = "mRNA_Contents: ".concat(mrnacontents.trim());
				listLines.remove(i);
				listLines.add(i, newline);
				
			}
			if(listLines.get(i).startsWith("tRNA_Contents")) {
				String newline = "tRNA_Contents: ".concat(trnacontents.trim());
				listLines.remove(i);
				listLines.add(i, newline);
				
			}
			if(listLines.get(i).startsWith("rRNA_Contents")) {
				String newline = "rRNA_Contents: ".concat(rrnacontents.trim());
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