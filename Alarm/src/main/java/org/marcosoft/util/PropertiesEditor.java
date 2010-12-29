package org.marcosoft.util;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class PropertiesEditor extends javax.swing.JDialog {
	/** Serial. */
	private static final long serialVersionUID = 8884030517324029091L;
	private JScrollPane jScrollPane1;
	private JButton btnCancel;
	private JButton btnOk;
	private JPanel jPanel1;
	private JTable tblProperties;
	private PropertyValidator validator;
	private ApplicationProperties properties;

	public PropertiesEditor(ApplicationProperties properties, PropertyValidator validator, String... propertiesToEdit) {
		super();
		initGUI();
		this.properties = properties;
		this.validator = validator;
		
		TableModel tblPropertiesModel = 
			new DefaultTableModel(new String[propertiesToEdit.length][2],
							      new String[] { "Opção", "Valor" });
		tblProperties.setModel(tblPropertiesModel);				
		
		for (int row=0; row<propertiesToEdit.length; row++) {
			String propertyName = propertiesToEdit[row];
			tblProperties.setValueAt(propertyName, row, 0);
			tblProperties.setValueAt(properties.getProperty(propertyName), row, 1);
		}
		
		tblProperties.getColumnModel().getColumn(0).setMaxWidth(250);
		tblProperties.getColumnModel().getColumn(0).setPreferredWidth(200);

		TableColumn col = tblProperties.getColumnModel().getColumn(1);					
		col.setCellEditor(new CellEditor());					
		
		setAlwaysOnTop(true);
		setModal(true);
		setVisible(true);
	}
	
	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			thisLayout.rowWeights = new double[] {0.1, 0.0};
			thisLayout.rowHeights = new int[] {7, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			getContentPane().setLayout(thisLayout);
			this.setTitle("Opções");
			{
				jScrollPane1 = new JScrollPane();
				getContentPane().add(jScrollPane1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					TableModel tblPropertiesModel = 
						new DefaultTableModel(
								new String[][] { { "", "" }, { "", "" }, { "", "" } },
								new String[] { "Opção", "Valor" });
					tblProperties = new JTable() {						
						private static final long serialVersionUID = 1L;

						public boolean isCellEditable(int row, int column) {
							return column == 1;
						};
					};
					jScrollPane1.setViewportView(tblProperties);
					tblProperties.setModel(tblPropertiesModel);					
				}
			}
			{
				jPanel1 = new JPanel();
				FlowLayout jPanel1Layout = new FlowLayout();
				jPanel1Layout.setAlignment(FlowLayout.RIGHT);
				jPanel1.setLayout(jPanel1Layout);
				getContentPane().add(jPanel1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					btnCancel = new JButton();
					jPanel1.add(btnCancel);
					btnCancel.setPreferredSize(new java.awt.Dimension(110, 22));
					btnCancel.setText("Cancelar");
					btnCancel.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnCancelActionPerformed(evt);
						}
					});
				}
				{
					btnOk = new JButton();
					jPanel1.add(btnOk);
					btnOk.setText("ok");
					btnOk.setPreferredSize(new java.awt.Dimension(110, 22));
					btnOk.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnOkActionPerformed(evt);
						}
					});
				}
			}
			pack();
			setSize(700, 200);
			SwingUtil.center(this);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}
	
	private void btnCancelActionPerformed(ActionEvent evt) {
		dispose();
	}
	
	private void btnOkActionPerformed(ActionEvent evt) {
		for (int row=0; row<tblProperties.getRowCount(); row++) {
			properties.setProperty((String)tblProperties.getValueAt(row, 0), 
					(String)tblProperties.getValueAt(row, 1));
		}
		dispose();
	}

	public class CellEditor extends AbstractCellEditor implements TableCellEditor {
		/** Serial. */
		private static final long serialVersionUID = -679122425784443569L;
		final JTextField component;
		String valorAnterior;
		
		public CellEditor() {
			component = new JTextField();
		}	
		
		@Override
		public boolean stopCellEditing() {
			try {
				String value = component.getText();
				if (!value.equals(valorAnterior)) {
					String property = (String) tblProperties.getValueAt(tblProperties.getSelectedRow(), 0);
					validator.validate(property, value);
				}
			} catch (ValidatorException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
				return false;
			}
			super.stopCellEditing();
			return true;
		}
		
		@Override
		public Object getCellEditorValue() {
			return component.getText(); 
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			component.setText((String)value);
			valorAnterior = (String) value;
			return component;
		} 
		
	}


}


