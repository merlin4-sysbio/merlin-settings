package pt.ceb.biosystems.merlin.merlin_settings.system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;

@Operation(name="HeapSpaceSettings",description= "Set heap space size")
public class HeapSpaceSettings {

	private int size;

	@Port(direction = Direction.INPUT, name="Size",description="", order = 1)
	public void setSize (String size) throws Exception{
		
		size = size.split(" ")[0];
		this.size= Integer.parseInt(size);
		
		String path = FileUtils.getConfFolderPath().concat("main.conf");
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
		int i = 0;
		boolean go = true;
		
		while (i < listLines.size()) { 
			
			String line = listLines.get(i);
			
			StringBuffer result = new StringBuffer();
			Matcher m = Pattern.compile("-Xmx*").matcher(line);
			while (m.find()) {
			    if (m.group(0) != null) {
			        m.appendReplacement(result, "-Xmx"+this.size+"G\"/>");
			    }
			}
			if (result.toString().equals("")) {
				listLines.remove(i);
				listLines.add(i, line);
			}
			else {
				listLines.remove(i);
				listLines.add(i, result.toString());
			}
				
			i++;
		
	}	
		PrintWriter confFile = new PrintWriter(path); 
		for (String line : listLines) {
			confFile.println(line);


		}
		confFile.close();
}
}
