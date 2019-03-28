package pt.ceb.biosystems.merlin.merlin_settings;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import pt.uminho.ceb.biosystems.merlin.core.gui.CustomGUI;
import pt.uminho.ceb.biosystems.merlin.core.utilities.CreateImageIcon;
import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;


public class GprSettingsConfigGUI extends AbstractInputJDialog implements InputGUI{
	private static final long serialVersionUID = 1L;

	public static String[] items = {
			"true",
			"false", 
	};
	public static JTextField similarity = new JTextField();
	public static JTextField reference_taxo = new JTextField();
	public static JTextField threshold = new JTextField();
	public static ExtendedJComboBox<String> compareToFullGenome = new ExtendedJComboBox<String>(items);
	public static ExtendedJComboBox<String> identifyGPRs = new ExtendedJComboBox<String>(items);
	public static ExtendedJComboBox<String> generateGPRs = new ExtendedJComboBox<String>(items);
	public static ExtendedJComboBox<String> keepReactionsWithNotes = new ExtendedJComboBox<String>(items);
	public static ExtendedJComboBox<String> keepManualReactions = new ExtendedJComboBox<String>(items);
	public static ExtendedJComboBox<String> integrateToDatabase = new ExtendedJComboBox<String>(items);
	public static ExtendedJComboBox<String> removeReactions = new ExtendedJComboBox<String>(items);
	private ParamsReceiver rec;

	public GprSettingsConfigGUI() {
		super(new JFrame());
		fill();
	}

	public String getDialogTitle() {
		return "GPR Settings";
	}

	public String getDescription() {
		return "Change merlin GPR rules settings in the configuration file.";
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
								new ParamSpec("similarity",String.class,similarity.getText(),null),
								new ParamSpec("reference_taxo",String.class,reference_taxo.getText(),null),
								new ParamSpec("threshold",String.class,threshold.getText(),null),
								new ParamSpec("compareToFullGenome",String.class,compareToFullGenome.getSelectedItem().toString(),null),
								new ParamSpec("identifyGPRs",String.class,identifyGPRs.getSelectedItem().toString(),null),
								new ParamSpec("generateGPRs",String.class,generateGPRs.getSelectedItem().toString(),null),
								new ParamSpec("keepReactionsWithNotes",String.class,keepReactionsWithNotes.getSelectedItem().toString(),null),
								new ParamSpec("keepManualReactions",String.class,keepManualReactions.getSelectedItem().toString(),null),
								new ParamSpec("integrateToDatabase",String.class,integrateToDatabase.getSelectedItem().toString(),null),
								new ParamSpec("removeReactions",String.class,removeReactions.getSelectedItem().toString(),null),
						}
						);
				String validation = "(?<=^| )\\d+(\\.\\d+)?(?=$| )";
				if(!similarity.getText().equals("")&&!reference_taxo.getText().equals("") &&!threshold.getText().equals("")) {
					if (similarity.getText().matches(validation) && reference_taxo.getText().matches(validation) && threshold.getText().matches(validation)) {
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
			options[0]="yes";
			options[1]="no";

			int result=CustomGUI.stopQuestion("Cancel confirmation", "Are you sure you want to cancel the operation?", options);

			if(result==0) {
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
		InputParameter[] parameters = new InputParameter[10];
		parameters[0] = 
				new InputParameter(
						"Similarity threshold", 
						similarity, 
						"Insert similarity threshold"
						);

		parameters[1] = 
				new InputParameter(
						"Reference taxonomy threshold", 
						reference_taxo, 
						"Insert reference taxo threshold"
						);
		parameters[2] = 
				new InputParameter(
						"Compare to full genome", 
						compareToFullGenome, 
						"Select true or false"
						);

		parameters[3] = 
				new InputParameter(
						"Identify GPRs", 
						identifyGPRs, 
						"Select true or false"
						);
		parameters[4] = 
				new InputParameter(
						"Generate GPRs", 
						generateGPRs, 
						"Select true or false"
						);

		parameters[5] = 
				new InputParameter(
						"Keep reactions with notes", 
						keepReactionsWithNotes, 
						"Select true or false"
						);
		parameters[6] = 
				new InputParameter(
						"Keep manual reactions", 
						keepManualReactions, 
						"Select true or false"
						);

		parameters[7] = 
				new InputParameter(
						"Integrate to database", 
						integrateToDatabase, 
						"Select true or false"
						);
		parameters[8] = 
				new InputParameter(
						"Threshold", 
						threshold, 
						"Insert threshold"
						);

		parameters[9] = 
				new InputParameter(
						"Remove reactions", 
						removeReactions, 
						"Select true or false"
						);

		return parameters;
	}

	public void fill () {

		ArrayList<String> listLines= new ArrayList<>();
		String confPath = FileUtils.getConfFolderPath().concat("gpr_settings.conf");
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

			if(item.startsWith("similarity_threshold")) {

				String[]parts=item.split(":");
				similarity.setText(parts[1].trim());
				continue;

			}
			if(item.startsWith("referenceTaxonomyThreshold")) {
				String[]parts=item.split(":");
				reference_taxo.setText(parts[1].trim());
				continue;

			}
			if(item.startsWith("threshold")) {
				String[]parts=item.split(":");
				threshold.setText(parts[1].trim());
				continue;

			}

			//
			if(item.startsWith("compareToFullGenome")) {
				String[]parts=item.split(":");


				if(parts[1].trim().equals("true")) {
					for (String dbOption : items) {
						if(dbOption.equals("true")) {
							compareToFullGenome.setSelectedItem(dbOption);
							continue;
						}
					}

				}

				if(parts[1].trim().equals("false")) {
					for (String dbOption : items) {
						if(dbOption.equals("false")) {
							compareToFullGenome.setSelectedItem(dbOption);
							continue;
						}
					}
				}
			}

			//
			if(item.startsWith("identifyGPRs")) {
				String[]parts=item.split(":");


				if(parts[1].trim().equals("true")) {
					for (String dbOption : items) {
						if(dbOption.equals("true")) {
							identifyGPRs.setSelectedItem(dbOption);
							continue;
						}
					}

				}

				if(parts[1].trim().equals("false")) {
					for (String dbOption : items) {
						if(dbOption.equals("false")) {
							identifyGPRs.setSelectedItem(dbOption);
							continue;
						}
					}
				}
			}

			//
			if(item.startsWith("generateGPRs")) {
				String[]parts=item.split(":");


				if(parts[1].trim().equals("true")) {
					for (String dbOption : items) {
						if(dbOption.equals("true")) {
							generateGPRs.setSelectedItem(dbOption);
							continue;
						}
					}

				}

				if(parts[1].trim().equals("false")) {
					for (String dbOption : items) {
						if(dbOption.equals("false")) {
							generateGPRs.setSelectedItem(dbOption);
							continue;
						}
					}
				}
			}

			//
			if(item.startsWith("keepReactionsWithNotes")) {
				String[]parts=item.split(":");


				if(parts[1].trim().equals("true")) {
					for (String dbOption : items) {
						if(dbOption.equals("true")) {
							keepReactionsWithNotes.setSelectedItem(dbOption);
							continue;
						}
					}

				}

				if(parts[1].trim().equals("false")) {
					for (String dbOption : items) {
						if(dbOption.equals("false")) {
							keepReactionsWithNotes.setSelectedItem(dbOption);
							continue;
						}
					}
				}
			}
			//
			if(item.startsWith("keepManualReactions")) {
				String[]parts=item.split(":");


				if(parts[1].trim().equals("true")) {
					for (String dbOption : items) {
						if(dbOption.equals("true")) {
							keepManualReactions.setSelectedItem(dbOption);
							continue;
						}
					}

				}

				if(parts[1].trim().equals("false")) {
					for (String dbOption : items) {
						if(dbOption.equals("false")) {
							keepManualReactions.setSelectedItem(dbOption);
							continue;
						}
					}
				}
			}
			//
			if(item.startsWith("integrateToDatabase")) {
				String[]parts=item.split(":");


				if(parts[1].trim().equals("true")) {
					for (String dbOption : items) {
						if(dbOption.equals("true")) {
							integrateToDatabase.setSelectedItem(dbOption);
							continue;
						}
					}

				}

				if(parts[1].trim().equals("false")) {
					for (String dbOption : items) {
						if(dbOption.equals("false")) {
							integrateToDatabase.setSelectedItem(dbOption);
							continue;
						}
					}
				}
			}
			//
			if(item.startsWith("removeReactions")) {
				String[]parts=item.split(":");


				if(parts[1].trim().equals("true")) {
					for (String dbOption : items) {
						if(dbOption.equals("true")) {
							removeReactions.setSelectedItem(dbOption);
							continue;
						}
					}

				}

				if(parts[1].trim().equals("false")) {
					for (String dbOption : items) {
						if(dbOption.equals("false")) {
							removeReactions.setSelectedItem(dbOption);
							continue;
						}
					}
				}
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
