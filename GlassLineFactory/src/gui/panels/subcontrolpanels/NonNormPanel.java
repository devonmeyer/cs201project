
package gui.panels.subcontrolpanels;

import gui.panels.ControlPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import transducer.Transducer;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * The NonNormPanel is responsible for initiating and managing non-normative
 * situations. It contains buttons for each possible non-norm.
 * 
 * The non-normative situations are:
 * 1. Truck runs out of gas
 * 2.
 * 3.
 * 4.
 * 5.
 * 6.
 * 7.
 * 8.
 */
@SuppressWarnings("serial")
public class NonNormPanel extends JPanel
{
	/** The number of different havoc actions that exist */
	public static final int NUM_NON_NORMS = 8;

	/** The control panel this is linked to */
	ControlPanel parent;

	/** List of buttons for each non-norm */
	List<JButton> nonNormButtons;
	
	List<JTextField> nonNormIndex;

	/** Title label **/
	JLabel titleLabel;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;

	/**
	 * Creates a new HavocPanel and links the control panel to it
	 * 
	 * @param cp
	 *        the ControlPanel linked to it
	 */
	public NonNormPanel(ControlPanel cp)
	{
		parent = cp;
		
		Transducer transducer;

//		this.setBackground(Color.black);
		this.setForeground(Color.black);

		// set up layout
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// set up button panel
		JPanel buttonPanel = new JPanel();
		GridLayout grid = new GridLayout(NUM_NON_NORMS / 2, 4);
		grid.setVgap(2);
		grid.setHgap(2);
//		buttonPanel.setBackground(Color.black);
//		buttonPanel.setLayout(grid);

		// make title
		titleLabel = new JLabel("NON NORMATIVES");
		titleLabel.setForeground(Color.black);
		titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 22));
		JPanel titleLabelPanel = new JPanel();
		titleLabelPanel.add(titleLabel);
		// titleLabelPanel.setAlignmentY(JLabel.CENTER_ALIGNMENT);
//		titleLabelPanel.setBackground(Color.black);

		// make buttons
		nonNormButtons = new ArrayList<JButton>(NUM_NON_NORMS);
		nonNormIndex = new ArrayList<JTextField>();
//		nonNormButtons.add(new JButton("Truck Out of Gas"));
//		nonNormIndex.add(new JTextField(5));
//		nonNormButtons.add(new JButton("NON NORM 2"));
//		nonNormIndex.add(new JTextField(5));
//		nonNormButtons.add(new JButton("NON NORM 3"));
//		nonNormIndex.add(new JTextField(5));
//		nonNormButtons.add(new JButton("NON NORM 4"));
//		nonNormIndex.add(new JTextField(5));
//		nonNormButtons.add(new JButton("NON NORM 5"));
//		nonNormIndex.add(new JTextField(5));
//		nonNormButtons.add(new JButton("NON NORM 6"));
//		nonNormIndex.add(new JTextField(5));
//		nonNormButtons.add(new JButton("NON NORM 7"));
//		nonNormIndex.add(new JTextField(5));
//		nonNormButtons.add(new JButton("NON NORM 8"));
//		nonNormIndex.add(new JTextField(5));
		// add listeners
//		nonNormButtons.get(0).addActionListener(new NonNorm1Listener());
//		nonNormButtons.get(1).addActionListener(new NonNorm2Listener());
//		nonNormButtons.get(2).addActionListener(new NonNorm3Listener());
//		nonNormButtons.get(3).addActionListener(new NonNorm4Listener());
//		nonNormButtons.get(4).addActionListener(new NonNorm5Listener());
//		nonNormButtons.get(5).addActionListener(new NonNorm6Listener());
//		nonNormButtons.get(6).addActionListener(new NonNorm7Listener());
//		nonNormButtons.get(7).addActionListener(new NonNorm8Listener());

//		for (int i = 0; i < NUM_NON_NORMS; i++)
//		{
//			nonNormButtons.get(i).setBackground(Color.white);
//			nonNormButtons.get(i).setForeground(Color.black);
//			nonNormButtons.get(i).setFont(new Font("SansSerif", Font.BOLD, 14));
//			nonNormButtons.get(i).setOpaque(true);
//			nonNormButtons.get(i).setBorderPainted(false);
//			nonNormButtons.get(i).setSize(20, 30);
//			nonNormButtons.get(i).setMinimumSize(new Dimension(20, 40));
//			nonNormButtons.get(i).setMaximumSize(new Dimension(20, 40));
//			nonNormButtons.get(i).setPreferredSize(new Dimension(20, 40));
//		}
		// add to panel
		this.add(titleLabelPanel);

		JPanel colorLinesPanel1 = new JPanel();
		colorLinesPanel1.setPreferredSize(new Dimension(350, 2));
//		colorLinesPanel1.setBackground(Color.black);
		ImageIcon cl = new ImageIcon("imageicons/singleColoredLine.png");
		JLabel clLabel1 = new JLabel(cl);
		colorLinesPanel1.add(clLabel1);
		this.add(colorLinesPanel1);

		for (int i = 0; i < nonNormButtons.size(); i++)
		{
//			buttonPanel.add(nonNormButtons.get(i));
//			buttonPanel.add(nonNormIndex.get(i));
		}
		buttonPanel.setAlignmentY(JPanel.CENTER_ALIGNMENT);
		this.add(buttonPanel);
		GridBagLayout gbl_buttonPanel = new GridBagLayout();
		gbl_buttonPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_buttonPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_buttonPanel.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_buttonPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		buttonPanel.setLayout(gbl_buttonPanel);
		
		JButton btnTr = new JButton("Conveyor Jam");
		btnTr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
								
				String text = textField.getText();
				try{
					  int num = Integer.parseInt(text);
					  if(parent.parent.conveyors.containsKey(num)){
						  parent.parent.conveyors.get(num).msgToggleJamConveyor();

					  } else {
						  System.out.println("That conveyor index doesn't exist. Who taught you to count?");
					  }
					  
					} catch (NumberFormatException ex) {
					  System.out.println("Please enter an integer, dumbass.");
					}
				
			}
		});
		GridBagConstraints gbc_btnTr = new GridBagConstraints();
		gbc_btnTr.insets = new Insets(0, 0, 5, 5);
		gbc_btnTr.gridx = 1;
		gbc_btnTr.gridy = 1;
		buttonPanel.add(btnTr, gbc_btnTr);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 1;
		buttonPanel.add(textField, gbc_textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Glass Breaks");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 4;
		gbc_btnNewButton.gridy = 1;
		buttonPanel.add(btnNewButton, gbc_btnNewButton);
		
		textField_2 = new JTextField();
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 5);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 5;
		gbc_textField_2.gridy = 1;
		buttonPanel.add(textField_2, gbc_textField_2);
		textField_2.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("Truck Broke");
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_1.gridx = 7;
		gbc_btnNewButton_1.gridy = 1;
		buttonPanel.add(btnNewButton_1, gbc_btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Popup Jam");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
								
				String text = textField_1.getText();
				try{
					  int num = Integer.parseInt(text);
					  if(parent.parent.popups.containsKey(num)){
						  parent.parent.popups.get(num).msgToggleJamPopup();
					  } else  {
						  System.out.println("That popup index doesn't exist. Who taught you to count?");
					  }
					  
					} catch (NumberFormatException ex) {
					  System.out.println("Please enter an integer, dumbass.");
					}
				
			}
		});
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_2.gridx = 1;
		gbc_btnNewButton_2.gridy = 3;
		buttonPanel.add(btnNewButton_2, gbc_btnNewButton_2);
		
		textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 2;
		gbc_textField_1.gridy = 3;
		buttonPanel.add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);
		
		JButton btnNewButton_3 = new JButton("Machine Break");
		GridBagConstraints gbc_btnNewButton_3 = new GridBagConstraints();
		gbc_btnNewButton_3.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_3.gridx = 4;
		gbc_btnNewButton_3.gridy = 3;
		buttonPanel.add(btnNewButton_3, gbc_btnNewButton_3);
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				String text = textField_3.getText();
				try{
					  int num = Integer.parseInt(text);
					  if(parent.parent.machines.containsKey(num)){
						  parent.parent.machines.get(num).msgBreakMachine();
					  } else  {
						  System.out.println("That machine index doesn't exist. Who taught you to count?");
					  }
					  
					} catch (NumberFormatException ex) {
					  System.out.println("Please enter an integer, dumbass.");
					}
			}
		});
		
		textField_3 = new JTextField();
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.insets = new Insets(0, 0, 5, 5);
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.gridx = 5;
		gbc_textField_3.gridy = 3;
		buttonPanel.add(textField_3, gbc_textField_3);
		textField_3.setColumns(10);
		
		JButton btnNewButton_4 = new JButton("P-U Robot Off");
		GridBagConstraints gbc_btnNewButton_4 = new GridBagConstraints();
		gbc_btnNewButton_4.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_4.gridx = 7;
		gbc_btnNewButton_4.gridy = 3;
		buttonPanel.add(btnNewButton_4, gbc_btnNewButton_4);
		
		JButton btnMachineFix = new JButton("Machine Fix");
		GridBagConstraints gbc_btnMachineFix = new GridBagConstraints();
		gbc_btnMachineFix.insets = new Insets(0, 0, 5, 5);
		gbc_btnMachineFix.gridx = 4;
		gbc_btnMachineFix.gridy = 4;
		buttonPanel.add(btnMachineFix, gbc_btnMachineFix);
		btnMachineFix.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String text = textField_6.getText();
				try{
					  int num = Integer.parseInt(text);
					  if(parent.parent.machines.containsKey(num)){
						  parent.parent.machines.get(num).msgFixMachine();
					  } else  {
						  System.out.println("That machine index doesn't exist. Who taught you to count?");
					  }
					  
					} catch (NumberFormatException ex) {
					  System.out.println("Please enter an integer, dumbass.");
					}
			}
		});
		
		textField_6 = new JTextField();
		GridBagConstraints gbc_textField_6 = new GridBagConstraints();
		gbc_textField_6.insets = new Insets(0, 0, 5, 5);
		gbc_textField_6.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_6.gridx = 5;
		gbc_textField_6.gridy = 4;
		buttonPanel.add(textField_6, gbc_textField_6);
		textField_6.setColumns(10);
		
		JButton btnNewButton_6 = new JButton("Robot Break");
		GridBagConstraints gbc_btnNewButton_6 = new GridBagConstraints();
		gbc_btnNewButton_6.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_6.gridx = 1;
		gbc_btnNewButton_6.gridy = 5;
		buttonPanel.add(btnNewButton_6, gbc_btnNewButton_6);
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
								
				String text = textField_4.getText();
				try{
					  int num = Integer.parseInt(text);
					  if(parent.parent.robots.containsKey(num)){
						  parent.parent.robots.get(num).msgBreakRobot();
					  } else  {
						  System.out.println("That popup index doesn't exist. Who taught you to count?");
					  }
					  
					} catch (NumberFormatException ex) {
					  System.out.println("Please enter an integer, dumbass.");
					}
				
			}
		});
		
		textField_4 = new JTextField();
		GridBagConstraints gbc_textField_4 = new GridBagConstraints();
		gbc_textField_4.insets = new Insets(0, 0, 5, 5);
		gbc_textField_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_4.gridx = 2;
		gbc_textField_4.gridy = 5;
		buttonPanel.add(textField_4, gbc_textField_4);
		textField_4.setColumns(10);
		
		JButton btnNewButton_7 = new JButton("Robot Fix");
		GridBagConstraints gbc_btnNewButton_7 = new GridBagConstraints();
		gbc_btnNewButton_7.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_7.gridx = 1;
		gbc_btnNewButton_7.gridy = 6;
		buttonPanel.add(btnNewButton_7, gbc_btnNewButton_7);
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
								
				String text = textField_5.getText();
				try{
					  int num = Integer.parseInt(text);
					  if(parent.parent.robots.containsKey(num)){
						  parent.parent.robots.get(num).msgFixRobot();
					  } else  {
						  System.out.println("That popup index doesn't exist. Who taught you to count?");
					  }
					  
					} catch (NumberFormatException ex) {
					  System.out.println("Please enter an integer, dumbass.");
					}
				
			}
		});
		
		
		textField_5 = new JTextField();
		GridBagConstraints gbc_textField_5 = new GridBagConstraints();
		gbc_textField_5.insets = new Insets(0, 0, 5, 5);
		gbc_textField_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_5.gridx = 2;
		gbc_textField_5.gridy = 6;
		buttonPanel.add(textField_5, gbc_textField_5);
		textField_5.setColumns(10);
		
		JLabel label_4 = new JLabel("  ");
		GridBagConstraints gbc_label_4 = new GridBagConstraints();
		gbc_label_4.insets = new Insets(0, 0, 5, 5);
		gbc_label_4.gridx = 1;
		gbc_label_4.gridy = 7;
		buttonPanel.add(label_4, gbc_label_4);
		
		JLabel label_2 = new JLabel("");
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.insets = new Insets(0, 0, 5, 5);
		gbc_label_2.gridx = 1;
		gbc_label_2.gridy = 8;
		buttonPanel.add(label_2, gbc_label_2);
		
		JLabel label = new JLabel("");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 4;
		gbc_label.gridy = 9;
		buttonPanel.add(label, gbc_label);
		
		JLabel label_3 = new JLabel("  ");
		GridBagConstraints gbc_label_3 = new GridBagConstraints();
		gbc_label_3.insets = new Insets(0, 0, 5, 5);
		gbc_label_3.gridx = 1;
		gbc_label_3.gridy = 10;
		buttonPanel.add(label_3, gbc_label_3);
		
		JLabel label_1 = new JLabel("");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.insets = new Insets(0, 0, 0, 5);
		gbc_label_1.gridx = 1;
		gbc_label_1.gridy = 11;
		buttonPanel.add(label_1, gbc_label_1);

		JPanel colorLinesPanel2 = new JPanel();
		colorLinesPanel2.setPreferredSize(new Dimension(350, 40));
//		colorLinesPanel2.setBackground(Color.black);
		JLabel clLabel2 = new JLabel();
		colorLinesPanel2.add(clLabel2);
		this.add(colorLinesPanel2);
		
		transducer = parent.getTransducer();
	}

	/**
	 * Returns the parent panel
	 * 
	 * @return the parent panel
	 */
	public ControlPanel getGuiParent()
	{
		return parent;
	}

	/**
	 * Non-norm 1
	 */
	public class NonNorm1Listener implements ActionListener
	{
		/**
		 * Invoked whenever the button is pressed
		 */
		public void actionPerformed(ActionEvent ae)
		{
			
		}
	}

	/**
	 * Non-norm 2
	 */
	public class NonNorm2Listener implements ActionListener
	{
		/**
		 * Invoked whenever the button is pressed
		 */
		public void actionPerformed(ActionEvent ae)
		{

		}
	}

	/**
	 * Non-norm 3
	 */
	public class NonNorm3Listener implements ActionListener
	{
		/**
		 * Invoked whenever the button is pressed
		 */
		public void actionPerformed(ActionEvent ae)
		{

		}
	}

	/**
	 * Non-norm 4
	 */
	public class NonNorm4Listener implements ActionListener
	{
		/**
		 * Invoked whenever the button is pressed
		 */
		public void actionPerformed(ActionEvent ae)
		{

		}
	}

	/**
	 * Non-norm 5
	 */
	public class NonNorm5Listener implements ActionListener
	{
		/**
		 * Invoked whenever the button is pressed
		 */
		public void actionPerformed(ActionEvent ae)
		{

		}
	}

	/**
	 * Non-norm 6
	 */
	public class NonNorm6Listener implements ActionListener
	{
		/**
		 * Invoked whenever the button is pressed
		 */
		public void actionPerformed(ActionEvent ae)
		{

		}
	}

	/**
	 * Non-norm 7
	 */
	public class NonNorm7Listener implements ActionListener
	{
		/**
		 * Invoked whenever the button is pressed
		 */
		public void actionPerformed(ActionEvent ae)
		{

		}
	}

	/**
	 * Non-norm 8
	 */
	public class NonNorm8Listener implements ActionListener
	{
		/**
		 * Invoked whenever the button is pressed
		 */
		public void actionPerformed(ActionEvent ae)
		{
			
		}
	}

}
