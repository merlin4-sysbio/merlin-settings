package pt.ceb.biosystems.merlin.merlin_settings;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import org.sing_group.gc4s.dialog.AbstractInputJDialog;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.combobox.ExtendedJComboBox;
import org.sing_group.gc4s.ui.icons.Icons;
import org.sing_group.gc4s.utilities.builder.JButtonBuilder;
import org.sing_group.gc4s.visualization.VisualizationUtils;

import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.InputGUI;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.aibench.gui.CustomGUI;
import pt.uminho.ceb.biosystems.merlin.aibench.utilities.CreateImageIcon;
import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;

public class TranspReactionsConfigGUI extends AbstractInputJDialog implements InputGUI {

	private static final long serialVersionUID = 1L;

	public static JTextField alphavalue = new JTextField();
	public static JTextField cutoffthreshold = new JTextField();
	public static JTextField symportcurrencymetabolites = new JTextField();
	private ParamsReceiver rec;

	public TranspReactionsConfigGUI() {
		super(new JFrame());
		fill();
	}

	public String getDialogTitle() {
		return "Create transport reactions settings";
	}

	public String getDescription() {
		return "Change create transport reactions settings in the configuration file.";
	}

	public JPanel getInputComponentsPane() {
		return new InputParametersPanel(getInputParameters());
	}

	@Override
	protected Component getButtonsPane() {
		final JPanel buttonsPanel = new JPanel(new FlowLayout());

		okButton = new JButton("save");
		okButton.setEnabled(true);
		okButton.setToolTipText("Save");
		okButton.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Save.png")),0.1).resizeImageIcon());
		ActionListener listener= new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				rec.paramsIntroduced(
						new ParamSpec[]{
								new ParamSpec("alphavalue",String.class,alphavalue.getText(),null),
								new ParamSpec("cutoffthreshold",String.class,cutoffthreshold.getText(),null),
								new ParamSpec("symportcurrencymetabolites",String.class,symportcurrencymetabolites.getText(),null),

						}
						);
				String validation = "(?<=^| )\\d+(\\.\\d+)?(?=$| )";
				if(!alphavalue.getText().equals("")&&!cutoffthreshold.getText().equals("") &&!symportcurrencymetabolites.getText().equals("")) {
					if (alphavalue.getText().matches(validation) && cutoffthreshold.getText().matches(validation)) {
						dispose();
					}

				}
			}
		};
		okButton.addActionListener(listener);

		cancelButton = new JButton("cancel");
		cancelButton.setToolTipText("Cancel");
		cancelButton.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Cancel.png")),0.1).resizeImageIcon());
		cancelButton.addActionListener(event -> {
			String[] options = new String[2];
			options[0] = "yes";
			options[1] = "no";
			int result = CustomGUI.stopQuestion("Cancel Confirmation", "Are you sure you want to cancel the operation?", options);
			
			if (result == 0) {
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

	private InputParameter[] getInputParameters() {
		InputParameter[] parameters = new InputParameter[3];
		parameters[0] = 
				new InputParameter(
						"Alpha value", 
						alphavalue, 
						"Insert alpha value"
						);

		parameters[1] = 
				new InputParameter(
						"Cut-off threshold", 
						cutoffthreshold, 
						"Insert cut-off threshold"
						);

		parameters[2] = 
				new InputParameter(
						"Symport currency metabolites", 
						symportcurrencymetabolites, 
						"Insert symport currency metabolites"
						);

		return parameters;
	}

	public void fill () {

		ArrayList<String> listLines= new ArrayList<>();
		String confPath = FileUtils.getConfFolderPath().concat("transp_reactions_settings.conf");
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

			if(item.startsWith("alpha Value")) {

				String[]parts=item.split(":");
				alphavalue.setText(parts[1].trim());
				continue;

			}
			if(item.startsWith("cut-off Threshold")) {
				String[]parts=item.split(":");
				cutoffthreshold.setText(parts[1].trim());
				continue;

			}
			if(item.startsWith("symport Currency Metabolites")) {
				String[]parts=item.split(":");
				symportcurrencymetabolites.setText(parts[1].trim());
				continue;

			}
		}
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
