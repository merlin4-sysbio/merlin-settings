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

@Operation(name="GPRConfigSettings", description="Configure GPR settings")
public class GPRConfigSettings {

	private String similarity;
	private String reference_taxo;
	private String threshold;
	private String compareToFullGenome;
	private String identifyGPRs;
	private String generateGPRs;
	private String keepReactionsWithNotes;
	private String keepManualReactions;
	private String integrateToDatabase;
	private String removeReactions;


	/**
	 * @param similarity
	 */
	@Port(direction=Direction.INPUT, name="Similarity", order=1)
	public void setSimilarity(String similarity) {

		this.similarity = similarity;

	}

	/**
	 * @param reference_taxo
	 */
	@Port(direction=Direction.INPUT, name="Reference_taxo", order=2)
	public void setReference_taxo(String reference_taxo) {

		this.reference_taxo = reference_taxo;

	}

	/**
	 * @param threshold
	 */
	@Port(direction=Direction.INPUT, name="Threshold", order=3)
	public void setThreshold(String threshold) {

		this.threshold = threshold;

	}


	/**
	 * @param compareToFullGenome
	 */
	@Port(direction=Direction.INPUT, name="CompareToFullGenome", order=4)
	public void setCompareToFullGenome(String compareToFullGenome) {

		this.compareToFullGenome = compareToFullGenome;

	}

	/**
	 * @param identifyGPRs
	 */
	@Port(direction=Direction.INPUT, name="identifyGPRs", order=5)
	public void setidentifyGPRs(String identifyGPRs) {

		this.identifyGPRs = identifyGPRs;
	}

	/**
	 * @param generateGPRs
	 */
	@Port(direction=Direction.INPUT, name="generateGPRs", order=6)
	public void setGenerateGPRs(String generateGPRs) {

		this.generateGPRs = generateGPRs;
	}

	/**
	 * @param keepReactionsWithNotes
	 */
	@Port(direction=Direction.INPUT, name="keepReactionsWithNotes", order=7)
	public void setKeepReactionsWithNotes(String keepReactionsWithNotes) {

		this.keepReactionsWithNotes = keepReactionsWithNotes;
	}

	/**
	 * @param keepManualReactions
	 */
	@Port(direction=Direction.INPUT, name="keepManualReactions", order=8)
	public void setKeepManualReactions(String keepManualReactions) {

		this.keepManualReactions = keepManualReactions;
	}

	/**
	 * @param integrateToDatabase
	 */
	@Port(direction=Direction.INPUT, name="integrateToDatabase", order=9)
	public void setIntegrateToDatabase(String integrateToDatabase) {

		this.integrateToDatabase = integrateToDatabase;
	}

	/**
	 * @param removeReactions
	 * @return 
	 */
	@Port(direction=Direction.INPUT, name="RemoveReactions", order=10)
	public void setRemoveReactions(String removeReactions) {

		this.removeReactions = removeReactions;
		saveConfigurations();
	}


	/**
	 * Save new configurations in .conf file
	 */
	public void saveConfigurations() {

		String validation = "(?<=^| )\\d+(\\.\\d+)?(?=$| )";
		if(!similarity.equals("") && !reference_taxo.equals("") && !threshold.equals("")) {
			if (similarity.matches(validation) && reference_taxo.matches(validation) && threshold.matches(validation)) {

				String path = FileUtils.getConfFolderPath().concat("gpr_settings.conf");
				System.out.println(path);

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

					if(listLines.get(i).startsWith("similarity_threshold")) {
						String newline = "similarity_threshold: ".concat(similarity.trim());
						listLines.remove(i);
						listLines.add(i, newline);
					}
					if(listLines.get(i).startsWith("referenceTaxonomyThreshold")) {
						String newline = "referenceTaxonomyThreshold: ".concat(reference_taxo.trim());
						listLines.remove(i);
						listLines.add(i, newline);
					}
					if(listLines.get(i).startsWith("threshold")) {
						String newline = "threshold: ".concat(threshold.trim());
						listLines.remove(i);
						listLines.add(i, newline);
					}
					if(listLines.get(i).startsWith("compareToFullGenome")) {
						String newline = "compareToFullGenome: ".concat(compareToFullGenome);
						listLines.remove(i);
						listLines.add(i, newline);
					}
					if(listLines.get(i).startsWith("identifyGPRs")) {
						String newline = "identifyGPRs: ".concat(identifyGPRs);
						listLines.remove(i);
						listLines.add(i, newline);
					}
					if(listLines.get(i).startsWith("generateGPRs")) {
						String newline = "generateGPRs: ".concat(generateGPRs);
						listLines.remove(i);
						listLines.add(i, newline);
					}
					if(listLines.get(i).startsWith("keepReactionsWithNotes")) {
						String newline = "keepReactionsWithNotes: ".concat(keepReactionsWithNotes);
						listLines.remove(i);
						listLines.add(i, newline);
					}
					if(listLines.get(i).startsWith("keepManualReactions")) {
						String newline = "keepManualReactions: ".concat(keepManualReactions);
						listLines.remove(i);
						listLines.add(i, newline);
					}
					if(listLines.get(i).startsWith("integrateToDatabase")) {
						String newline = "integrateToDatabase: ".concat(integrateToDatabase);
						listLines.remove(i);
						listLines.add(i, newline);
					}
					if(listLines.get(i).startsWith("removeReactions")) {
						String newline = "removeReactions: ".concat(removeReactions);
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

			}else {
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