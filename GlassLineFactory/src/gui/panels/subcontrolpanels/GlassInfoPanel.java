
package gui.panels.subcontrolpanels;

import gui.panels.ControlPanel;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JScrollPane;

/**
 * The GlassInfoPanel class displays information on glass in production
 */
@SuppressWarnings("serial")
public class GlassInfoPanel extends JPanel
{
	/** The ControlPanel this is linked to */
	private ControlPanel parent;

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
		
		JLabel lblGlassId = new JLabel("Glass ID:");
		GridBagConstraints gbc_lblGlassId = new GridBagConstraints();
		gbc_lblGlassId.insets = new Insets(0, 0, 5, 0);
		gbc_lblGlassId.gridx = 1;
		gbc_lblGlassId.gridy = 1;
		add(lblGlassId, gbc_lblGlassId);
		
		JLabel label = new JLabel(" ");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 0);
		gbc_label.gridx = 1;
		gbc_label.gridy = 2;
		add(label, gbc_label);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.anchor = GridBagConstraints.WEST;
		gbc_scrollPane.fill = GridBagConstraints.VERTICAL;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 4;
		add(scrollPane, gbc_scrollPane);
		parent = cp;
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
