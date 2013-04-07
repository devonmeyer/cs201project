
package gui.panels.subcontrolpanels;

import gui.panels.ControlPanel;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;

/**
 * The GlassSelectPanel class contains buttons allowing the user to select what
 * type of glass to produce.
 */
@SuppressWarnings("serial")
public class GlassSelectPanel extends JPanel
{
	/** The ControlPanel this is linked to */
	private ControlPanel parent;

	/**
	 * Creates a new GlassSelect and links it to the control panel
	 * @param cp
	 *        the ControlPanel linked to it
	 */
	public GlassSelectPanel(ControlPanel cp)
	{
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JCheckBox chckbxCutter = new JCheckBox("     Cutter");
		GridBagConstraints gbc_chckbxCutter = new GridBagConstraints();
		gbc_chckbxCutter.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxCutter.gridx = 1;
		gbc_chckbxCutter.gridy = 0;
		add(chckbxCutter, gbc_chckbxCutter);
		
		JCheckBox chckbxGrinder = new JCheckBox("Grinder");
		GridBagConstraints gbc_chckbxGrinder = new GridBagConstraints();
		gbc_chckbxGrinder.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxGrinder.gridx = 3;
		gbc_chckbxGrinder.gridy = 0;
		add(chckbxGrinder, gbc_chckbxGrinder);
		
		JCheckBox chckbxBreakout = new JCheckBox("   BreakOut");
		GridBagConstraints gbc_chckbxBreakout = new GridBagConstraints();
		gbc_chckbxBreakout.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxBreakout.gridx = 1;
		gbc_chckbxBreakout.gridy = 1;
		add(chckbxBreakout, gbc_chckbxBreakout);
		
		JCheckBox chckbxWasher = new JCheckBox(" Washer");
		GridBagConstraints gbc_chckbxWasher = new GridBagConstraints();
		gbc_chckbxWasher.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxWasher.gridx = 3;
		gbc_chckbxWasher.gridy = 1;
		add(chckbxWasher, gbc_chckbxWasher);
		
		JCheckBox chckbxManualBo = new JCheckBox("  Manual BO");
		GridBagConstraints gbc_chckbxManualBo = new GridBagConstraints();
		gbc_chckbxManualBo.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxManualBo.gridx = 1;
		gbc_chckbxManualBo.gridy = 2;
		add(chckbxManualBo, gbc_chckbxManualBo);
		
		JCheckBox chckbxUv = new JCheckBox("     UV");
		GridBagConstraints gbc_chckbxUv = new GridBagConstraints();
		gbc_chckbxUv.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxUv.gridx = 3;
		gbc_chckbxUv.gridy = 2;
		add(chckbxUv, gbc_chckbxUv);
		
		JCheckBox chckbxCrossseamer = new JCheckBox("CrossSeamer");
		GridBagConstraints gbc_chckbxCrossseamer = new GridBagConstraints();
		gbc_chckbxCrossseamer.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxCrossseamer.gridx = 1;
		gbc_chckbxCrossseamer.gridy = 3;
		add(chckbxCrossseamer, gbc_chckbxCrossseamer);
		
		JCheckBox chckbxPaint = new JCheckBox("  Paint");
		GridBagConstraints gbc_chckbxPaint = new GridBagConstraints();
		gbc_chckbxPaint.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxPaint.gridx = 3;
		gbc_chckbxPaint.gridy = 3;
		add(chckbxPaint, gbc_chckbxPaint);
		
		JCheckBox chckbxDrill = new JCheckBox("      Drill");
		GridBagConstraints gbc_chckbxDrill = new GridBagConstraints();
		gbc_chckbxDrill.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxDrill.gridx = 1;
		gbc_chckbxDrill.gridy = 4;
		add(chckbxDrill, gbc_chckbxDrill);
		
		JCheckBox chckbxBake = new JCheckBox("   Bake");
		GridBagConstraints gbc_chckbxBake = new GridBagConstraints();
		gbc_chckbxBake.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxBake.gridx = 3;
		gbc_chckbxBake.gridy = 4;
		add(chckbxBake, gbc_chckbxBake);
		
		JButton btnCreateGlass = new JButton("Create Glass");
		GridBagConstraints gbc_btnCreateGlass = new GridBagConstraints();
		gbc_btnCreateGlass.gridx = 3;
		gbc_btnCreateGlass.gridy = 5;
		add(btnCreateGlass, gbc_btnCreateGlass);
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
