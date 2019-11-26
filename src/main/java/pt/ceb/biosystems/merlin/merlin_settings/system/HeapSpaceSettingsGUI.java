package pt.ceb.biosystems.merlin.merlin_settings.system;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.IntStream;

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

import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.InputGUI;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import pt.uminho.ceb.biosystems.merlin.aibench.gui.CustomGUI;
import pt.uminho.ceb.biosystems.merlin.aibench.utilities.CreateImageIcon;


public class HeapSpaceSettingsGUI extends AbstractInputJDialog implements InputGUI{

	private static final long serialVersionUID = 1L;

	private ParamsReceiver rec;

	protected Object project;

	ExtendedJComboBox<String> sizes;


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
		// TODO Auto-generated method stub

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

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

				rec.paramsIntroduced(
						new ParamSpec[]{
								new ParamSpec("Size (Gigabytes)", String.class,sizes.getSelectedItem().toString(),null),

						}
						);

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
		
		okButton.addActionListener(event -> {

			String[] options = new String[2];
			options[0] = "yes";
			options[1] = "no";

			int result = CustomGUI.stopQuestion("cancel confirmation", "You have to reboot merlin to perform changes.\n"
					+ "Do you want to do it now?", options);

			if(result == 0) {
				System.out.println("POE AQUI A OPERAÇAO DE REBOOT");
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
		long physicalMemorySize = os.getTotalPhysicalMemorySize();
		
		physicalMemorySize = (long) (physicalMemorySize * 0.000000000931322574615478515625);
		
		String[] list = new String[(int) physicalMemorySize+1];
		
		for (int i = 1; i<= physicalMemorySize; i++) {
			list[i]=Integer.toString(i);
		}
		
		sizes = new ExtendedJComboBox<String>(list);
		
		InputParameter[] inPar = getInputParameters();
		return new InputParametersPanel(inPar);
	}

}
