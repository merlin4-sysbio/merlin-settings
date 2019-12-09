package pt.ceb.biosystems.merlin.merlin_settings.system;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.sing_group.gc4s.dialog.AbstractInputJDialog;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.combobox.ExtendedJComboBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uvigo.ei.aibench.Util;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.InputGUI;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.gui.jpanels.CustomGUI;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.CreateImageIcon;
import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;


public class HeapSpaceSettingsGUI extends AbstractInputJDialog implements InputGUI{

	private static final long serialVersionUID = 1L;

	private ParamsReceiver rec;

	protected Object project;

	protected ExtendedJComboBox<String> sizes;
	
	final static Logger logger = LoggerFactory.getLogger(HeapSpaceSettingsGUI.class);
	
	public HeapSpaceSettingsGUI() {

		super(new JFrame());

		//fill();
	}

	private InputParameter[] getInputParameters() {
		InputParameter[] parameters = new InputParameter[1];
		parameters[0] = 

				new InputParameter(
						"Size (Gigabytes)", 
						sizes, 
						"Set heap space size"
						);
		return parameters;
	}

	public String getDialogTitle() {
		return "Heap Space";
	}

	public String getDescription() {
		return "Set heap space size";
	}

	@Override
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		this.rec = arg0;
		this.setTitle(arg1.getName());
		this.setVisible(true);	

	}

	@Override
	public void onValidationError(Throwable t) {

	}

	@Override
	public void finish() {

	}

	@Override
	protected Component getButtonsPane() {
		final JPanel buttonsPanel = new JPanel(new FlowLayout());

		okButton = new JButton("save");
		okButton.setEnabled(true);
		okButton.setToolTipText("save");
		okButton.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Save.png")), 0.1).resizeImageIcon());
		ActionListener listener= new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				
				logger.info("Heap space memory to set: " + sizes.getSelectedItem().toString() + "G");

				rec.paramsIntroduced(
						new ParamSpec[]{
								new ParamSpec("Size (Gigabytes)", String.class,sizes.getSelectedItem().toString(),null),

						}
						);

				String[] options = new String[2];
				options[0] = "yes";
				options[1] = "no";

				int result = CustomGUI.stopQuestion("cancel confirmation", "You have to reboot merlin to perform changes.\n"
						+ "Do you want to do it now?", options);

				if(result == 0) {
					logger.info("Rebooting...");
					dispose();
					Util.restart();
				}
				else {
					logger.info("Reboot later.");
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
				logger.info("Set new heap space operation canceled.");
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

		return buttonsPanel;
	}

	@Override
	protected Component getInputComponentsPane() {

		@SuppressWarnings("restriction")
		com.sun.management.OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean)
		java.lang.management.ManagementFactory.getOperatingSystemMXBean();
		@SuppressWarnings("restriction")

		Integer physicalMemorySize = (int) ((os.getTotalPhysicalMemorySize() * 0.000000000931322574615478515625)/2)+1;
		
		logger.info("Approximate memory size: " + (physicalMemorySize*2));

		String[] list = new String[physicalMemorySize];

		for (int i = 1; i<= physicalMemorySize; i++) {
			list[i-1]=Integer.toString(i);
		}

		sizes = new ExtendedJComboBox<String>(list);

		Integer currentMemory = getMemorySelected();
		
		if(currentMemory > physicalMemorySize)
			currentMemory = 1;
		
		sizes.setSelectedItem(currentMemory+"");
		
		InputParameter[] inPar = getInputParameters();
		return new InputParametersPanel(inPar);
	}

	private static Integer getMemorySelected(){
		Integer pattern = null;

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
			Workbench.getInstance().error(e);
			e.printStackTrace();
		}
		int i = 0;

		while (i < listLines.size()) { 

			String line = listLines.get(i);

			Pattern p = Pattern.compile(".*-Xmx(\\d+).*");
			Matcher m = p.matcher(line);

			if(m.find()) {
				try {
					logger.info("Current heap space memory in use: " + line);
					pattern = Integer.valueOf(m.group(1));		

				} 
				catch (Exception e) {
					pattern = 1;
				}
			}
			i++;
		}	
		
		return pattern;
	}

}
