
package gui.panels.subcontrolpanels;

import glassLine.Glass;
import gui.panels.ControlPanel;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;

import javax.swing.JScrollPane;

/**
 * The GlassInfoPanel class displays information on glass in production
 */
@SuppressWarnings("serial")
public class GlassInfoPanel extends JPanel
{
	/** The ControlPanel this is linked to */
	private ControlPanel parent;
	
	/** GUI components */
	JLabel glassID;
	JLabel recipe;

	/**
	 * Creates a new GlassInfoPanel and links it to the control panel
	 * @param cp
	 *        the ControlPanel linked to it
	 */
	public GlassInfoPanel(ControlPanel cp)
	{
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		glassID = new JLabel("Glass ID:");
		GridBagConstraints gbc_lblGlassId = new GridBagConstraints();
		gbc_lblGlassId.insets = new Insets(0, 0, 5, 0);
		gbc_lblGlassId.gridx = 1;
		gbc_lblGlassId.gridy = 1;
		add(glassID, gbc_lblGlassId);
		
		JLabel label = new JLabel("Recipe:");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 0);
		gbc_label.gridx = 1;
		gbc_label.gridy = 2;
		add(label, gbc_label);
		
		recipe = new JLabel(" ");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.insets = new Insets(0, 0, 5, 0);
		gbc_label_1.gridx = 1;
		gbc_label_1.gridy = 3;
		add(recipe, gbc_label_1);
		
		parent = cp;
	}

	public void updateGlassInfo(Glass g){
		List<String> stations = g.getProcesses();
		String temp = "";
		if(!g.broken){
			for(int i=0; i < stations.size();i++){
				temp += stations.get(i);
				if((i+1) < stations.size())
					temp += ", ";
			}
		}
		else{
			temp += "GLASS BROKEN";
		}
		
		glassID.setText("Glass ID: " + g.getID());
		recipe.setText(temp);
	}
	/**
	 * Returns the parent panel
	 * @return the parent panel
	 */
	public ControlPanel getGuiParent()
	{
		return parent;
	}
}
