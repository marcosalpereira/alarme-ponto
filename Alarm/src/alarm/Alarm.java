package alarm;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.text.MaskFormatter;


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
public class Alarm extends javax.swing.JFrame {
	private static final long serialVersionUID = -1045806326715430034L;

	private static final int COL_HORA = 1;
	
	private JScrollPane jScrollPane1;
	private JTable tblHorarios;
	private JLabel lblMessage;
	private JButton btnOk;
	private JTextField txtUser;

	private TimeChecker timeChecker;
	private Beep beep;

	private ApplicationProperties applicationProperties;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Alarm inst = new Alarm();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});	
	}
	
	public Alarm() {
		initGUI();
		
		applicationProperties = new ApplicationProperties("alarme");
		
		carregarHorariosSalvos();
		
		timeChecker = new TimeChecker();
		timeChecker.setDaemon(true);
		timeChecker.start();
		
		beep = new Beep();
		beep.setDaemon(true);
		beep.start();
	}
	
	private void carregarHorariosSalvos() {
		for (int row = 0; row < tblHorarios.getRowCount(); row++) {
			String property = applicationProperties.getProperty("p." + row, "00:00");
			tblHorarios.setValueAt(property, row, COL_HORA);
		}		
	}
	
	private void salvarHorarios() {
		for (int row = 0; row < tblHorarios.getRowCount(); row++) {
			applicationProperties.setProperty("p." + row, tblHorarios.getValueAt(row, COL_HORA) + "");
		}
	}
	

	/**
	 * Verifica se é hora de alarmar.
	 * @return a hora de alarme ou <code>null</code>
	 */
	private String horaAlarme() {
		String hora = getHoraCorrente();
		for (int row = 0; row < tblHorarios.getRowCount(); row++) {
			if (hora.equals(tblHorarios.getValueAt(row, COL_HORA))) {
				return hora;
			}
		}
		return null;
	}

	/**
	 * Retorna a hora corrente no formato hh:mm.
	 * @return a hora
	 */
	private String getHoraCorrente() {
		Calendar calendar = Calendar.getInstance();
		int h = calendar.get(Calendar.HOUR_OF_DAY);
		int m = calendar.get(Calendar.MINUTE);
		String hora = String.format("%02d:%02d", h, m);
		return hora;
	}	
	
	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			thisLayout.rowWeights = new double[] {0.0, 1.0, 0.0, 0.0};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			getContentPane().setLayout(thisLayout);
			this.setTitle("Alarme Ponto");
			{
				jScrollPane1 = new JScrollPane();
				getContentPane().add(jScrollPane1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					TableModel tblHorariosModel = 
						new DefaultTableModel(
								new Object[][] { 
											{ "1º Período - Entrada", "00:00" }, { "1º Período - Saída", "00:00" }, 
											{ "2º Período - Entrada", "00:00" }, { "2º Período - Saída", "00:00" }, 
											{ "1º Período - Extra - Entrada", "00:00" }, { "1º Período - Extra - Saída", "00:00" }, 
										},
								new String[] { "Período", "Hora" });
					tblHorarios = new JTable() {
						private static final long serialVersionUID = 1L;

						@Override
						public boolean isCellEditable(int row, int column) {
							return (column == COL_HORA);
						}						
					};
					tblHorarios.setModel(tblHorariosModel);
					
					TableColumn col = tblHorarios.getColumnModel().getColumn(COL_HORA);					
					col.setCellEditor(new FormattedEditor());
					
					jScrollPane1.setViewportView(tblHorarios);
					tblHorarios.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							tblHorariosMouseClicked(evt);
						}
					});
					tblHorarios.addPropertyChangeListener(new PropertyChangeListener() {
						public void propertyChange(PropertyChangeEvent evt) {
							tblHorariosPropertyChange(evt);
						}
					});					
					
					tblHorarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					tblHorarios.setToolTipText("SHIFT+CLICK Para setar a hora corrente");
				}
			}
			{
				txtUser = new JTextField();
				getContentPane().add(txtUser, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				txtUser.setText("User");
			}
			{
				btnOk = new JButton();
				getContentPane().add(btnOk, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 2), 80, 0));
				btnOk.setText("ok");
				btnOk.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnOkActionPerformed(evt);
					}
				});
			}
			{
				lblMessage = new JLabel();
				getContentPane().add(lblMessage, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
				lblMessage.setText("jLabel1");
				lblMessage.setFont(new java.awt.Font("Verdana",1,14));
				lblMessage.setOpaque(true);
				lblMessage.setBackground(new java.awt.Color(255,0,0));
				lblMessage.setForeground(new java.awt.Color(255,255,255));
				lblMessage.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				lblMessage.setVisible(false);
			}
			txtUser.setText(System.getProperty("user.name"));
			setAlwaysOnTop(true);			
			this.addWindowListener(new WindowAdapter() {
				public void windowClosed(WindowEvent evt) {
					thisWindowClosed(evt);
				}
			});
			pack();
			setSize(400, 300);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}
	
	private void tblHorariosPropertyChange(PropertyChangeEvent evt) {
		int selectedRow = tblHorarios.getSelectedRow();
		if (selectedRow < 0) {
			return;
		}
		if (getMinutos(selectedRow) == 0) {
			JOptionPane.showMessageDialog(this, "Valor inválido!");
			DefaultCellEditor oldValue = (DefaultCellEditor) evt.getOldValue();
			tblHorarios.setValueAt(oldValue.getCellEditorValue(), selectedRow, COL_HORA);
			return;
		}
		recalcularAlarmes(selectedRow);
	}

	private void recalcularAlarmes(int row) {
		switch (row) {
			case 0:
				recalcularSaidaPrimeiroTurno();
				break;
			case 1:
				recalcularEntradaSegundoTurno();
				break;
			case 2:
				recalcularSaidaSegundoTurno();
				break;
			case 3:
				recalcularEntradaTurnoExtra();
				break;
			case 4:
				recalcularSaidaTurnoExtra();
				break;
		}
	}

	private void recalcularSaidaTurnoExtra() {
		int min = getMinutos(4);
		min += 2 * 60;
		setMinutos(5, min);
	}
	
	private void recalcularEntradaTurnoExtra() {
		int min = getMinutos(3);
		min += 15;
		setMinutos(4, min);
		recalcularAlarmes(4);	
	}

	private void recalcularSaidaPrimeiroTurno() {
		int min = getMinutos(0);
		min += 4 * 60;
		setMinutos(1, min);
		recalcularAlarmes(1);
	}
	
	private void recalcularEntradaSegundoTurno() {
		int min = getMinutos(1);
		min += 60;
		setMinutos(2, min);
		recalcularAlarmes(2);
	}
	
	private void recalcularSaidaSegundoTurno() {
		int p0 = getMinutos(0);
		int p1 = getMinutos(1);
		int primeiroTurno = p1 - p0;
		
		int p2 = getMinutos(2);
		
		int minutosFaltam = 8 * 60 - primeiroTurno;
		int p3 = p2 + minutosFaltam;
		
		setMinutos(3, p3);
		recalcularAlarmes(3);
	}

	/**
	 * Setar os minutos na tabela. 
	 * @param row
	 * @param mins
	 */
	private void setMinutos(int row, int mins) {
		int h = mins / 60;
		int m = mins - (h * 60);
		String hora = String.format("%02d:%02d", h, m);
		tblHorarios.setValueAt(hora, row, COL_HORA);
	}

	/**
	 * Pegar os minutos na linha informada. 
	 * @param row
	 * @return
	 */
	private int getMinutos(int row) {
		String hora = (String) tblHorarios.getValueAt(row, COL_HORA);			
		return parseMinutos(hora);
	}

	/**
	 * Converte hora em minutos.
	 * @param hora hora
	 * @return a hora ou 0 em caso de erro
	 */
	private int parseMinutos(String hora) {
		String[] split = hora.split(":");
		int h = Integer.parseInt(split[0]);
		int m = Integer.parseInt(split[1]);
		if (!(h >= 0 && h <= 23)) {
			throw new NumberFormatException("Hora inválida!");
		}
		if (!(m >= 0 && m <= 59)) {
			throw new NumberFormatException("Minutos inválidos!");
		}
		return h * 60 + m;
	}
	
	private void btnOkActionPerformed(ActionEvent evt) {
		setExtendedState(Frame.ICONIFIED);
		beep.toBeep = false;
		lblMessage.setVisible(false);
	}
	
	private void tblHorariosMouseClicked(MouseEvent evt) {
		int col = tblHorarios.getSelectedColumn();		
		if (evt.isShiftDown() && col == COL_HORA) {
			int row = tblHorarios.getSelectedRow();		
			tblHorarios.setValueAt(getHoraCorrente(), row, COL_HORA);
			recalcularAlarmes(row);
		}
	}

	private void pausa(int milis) {
		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {
			
		}
	}
	
	private void thisWindowClosed(WindowEvent evt) {
		salvarHorarios();		
	}
	
	/**
	 * Continuamente checa se é hora de alarmar.
	 */
	public class TimeChecker extends Thread {
		private Map<String, Boolean> alarmados = new HashMap<String, Boolean>();
		
		@Override
		public void run() {
			for (;;) {
				String hora = horaAlarme();
				if (hora != null && alarmados.get(hora) == null) {
					alarmados.put(hora, true);
					lblMessage.setVisible(true);
					lblMessage.setText(" Ponto: " + hora);
					setExtendedState(Frame.NORMAL);
					beep.toBeep = true;
				}
				pausa(1000);
			}			
		}		
	}
	
	/**
	 * Notificacao sonora do alarme via beep. 
	 */
	public class Beep extends Thread {
		private boolean toBeep;
		private int countBeep;
		
		@Override
		public void run() {
			for(;;) {
				if (toBeep) {
					int beeperTimes = applicationProperties.getProperty("beeper.times", 100);
					String beeperCommand = 
						applicationProperties.getProperty("beeper.command", "/usr/bin/beep -f 4000");
					int beeperPause = applicationProperties.getProperty("beeper.pause", 500);
					
					countBeep++;
					try {
						execute(beeperCommand);
					} catch (Exception e) {
						toBeep = false;
						JOptionPane.showMessageDialog(null, "Can't beep! " + e.getMessage());
					}
					if (countBeep > beeperTimes) {
						toBeep = false;
					}
					pausa(beeperPause);				
				} else {
					countBeep = 0;
					pausa(500);				
				}
			}
		}

		/**
		 * Executa o comando e espera pelo termino do processo.
		 * @param command comando
		 * @throws IOException
		 * @throws InterruptedException
		 */
		private void execute(String command) throws IOException, InterruptedException {
			Process process = Runtime.getRuntime().exec(command);
			process.waitFor();
		}
	}
	
	public class FormattedEditor extends AbstractCellEditor implements TableCellEditor {
		/** Serial. */
		private static final long serialVersionUID = -679122425784443569L;
		final JFormattedTextField component;
		
		public FormattedEditor() throws ParseException {
			MaskFormatter maskFormatter = new MaskFormatter("##:##");
			component = new JFormattedTextField(maskFormatter);
		}
		
		@Override
		public boolean stopCellEditing() {
			try {
				parseMinutos(component.getText());
				super.stopCellEditing();
				return true;
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
				return false;
			}
		}
		
		@Override
		public Object getCellEditorValue() {
			return component.getText(); 
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			component.setText((String)value); 
			return component;
		} 
		
	}
}


