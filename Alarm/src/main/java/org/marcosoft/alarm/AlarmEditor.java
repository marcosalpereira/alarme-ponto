package org.marcosoft.alarm;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Observable;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.text.MaskFormatter;

import org.marcosoft.util.SwingUtil;
import org.marcosoft.util.SystemUtil;

public class AlarmEditor extends Observable {
	private static final long serialVersionUID = -1045806326715430034L;
	
	private static final int COL_HORA = 1;
	
	private JScrollPane jScrollPane1;
	private JTable tblHorarios;
	private JLabel lblMessage;
	private JButton btnOpcoes;
	private JPanel jPanel1;
	private JButton btnOk;
	private JLabel txtUser;

	private Horarios horarios;

	private JFrame win;
	
	public AlarmEditor(Horarios horarios) {
		initGUI();
		this.horarios = horarios;
		lblMessage.setVisible(false);
		atualizarHorariosTabela(horarios);
	}

	private void atualizarHorariosTabela(Horarios horarios) {
		String[] horariosArray = horarios.asArray();
		for (int row = 0; row < horariosArray.length; row++) {
			tblHorarios.setValueAt(horariosArray[row], row, COL_HORA);
		}
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
			win = new JFrame();
			GridBagLayout thisLayout = new GridBagLayout();
			win.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			thisLayout.rowWeights = new double[] {0.0, 1.0, 0.0, 0.0};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			win.getContentPane().setLayout(thisLayout);
			win.setTitle("Alarme Ponto " + SystemUtil.getAppVersion());
			{
				jScrollPane1 = new JScrollPane();
				win.getContentPane().add(jScrollPane1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
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
							return column == COL_HORA;							
						}
					};
					tblHorarios.setModel(tblHorariosModel);
					
					TableColumn col = tblHorarios.getColumnModel().getColumn(COL_HORA);					
					col.setCellEditor(new FormattedEditor());
					
					jScrollPane1.setViewportView(tblHorarios);
				}
			}
			{
				txtUser = new JLabel();
				win.getContentPane().add(txtUser, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				txtUser.setText("User");
			}
			{
				lblMessage = new JLabel();
				win.getContentPane().add(lblMessage, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
				jPanel1 = new JPanel();
				FlowLayout jPanel1Layout = new FlowLayout();
				jPanel1Layout.setAlignment(FlowLayout.RIGHT);
				jPanel1.setLayout(jPanel1Layout);
				{
					btnOk = new JButton();
					btnOpcoes = new JButton();
					btnOpcoes.setText("Opções");
					btnOpcoes.setPreferredSize(new java.awt.Dimension(100, 22));
					btnOpcoes.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnOpcoesActionPerformed(evt);
						}
					});					
					jPanel1.add(btnOpcoes);
					jPanel1.add(btnOk);
					btnOk.setText("Minimizar");
					btnOk.setPreferredSize(new java.awt.Dimension(110, 22));
					btnOk.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnOkActionPerformed(evt);
						}
					});
				}
				win.getContentPane().add(jPanel1, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				lblMessage.setText("jLabel1");
				lblMessage.setFont(new java.awt.Font("Verdana",1,14));
				lblMessage.setOpaque(true);
				lblMessage.setBackground(new java.awt.Color(255,0,0));
				lblMessage.setForeground(new java.awt.Color(255,255,255));
				lblMessage.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			}
			txtUser.setText(System.getProperty("user.name"));
			win.setAlwaysOnTop(true);
			
			win.pack();
			win.setSize(400, 300);
			SwingUtil.center(win);
			win.setVisible(true);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}

	private void recalcularAlarmes(int indice) {
		switch (indice) {
			case 0:
				entradaPrimeiroTurnoAlterada();
				break;
			case 1:
				saidaPrimeiroTurnoAlterada();
				break;
			case 2:
				entradaSegundoTurnoAlterada();
				break;
			case 3:
				saidaSegundoTurnoAlterada();
				break;
			case 4:
				entradaTurnoExtraAlterada();
				break;
			case 5:
				saidaTurnoExtraAlterada();
				break;
		}
		atualizarHorarios();
	}

	private void atualizarHorarios() {
		horarios.setPrimeiroTurnoEntrada(getHora(0));
		horarios.setPrimeiroTurnoSaida(getHora(1));
		
		horarios.setSegundoTurnoEntrada(getHora(2));
		horarios.setSegundoTurnoSaida(getHora(3));
		
		horarios.setTurnoExtraEntrada(getHora(4));
		horarios.setTurnoExtraSaida(getHora(5));
		
		horarios.notifyObservers();
	}

	private void saidaTurnoExtraAlterada() {				
	}
	
	private void entradaTurnoExtraAlterada() {
		int min = getHoraEmMinutos(4);
		min += 2 * 60;
		setMinutos(5, min);
		saidaTurnoExtraAlterada();
	}
	
	private void saidaSegundoTurnoAlterada() {
		int min = getHoraEmMinutos(3);
		min += 15;
		setMinutos(4, min);
		entradaTurnoExtraAlterada();	
	}

	private void entradaPrimeiroTurnoAlterada() {
		int min = getHoraEmMinutos(0);
		min += 4 * 60;
		setMinutos(1, min);
		saidaPrimeiroTurnoAlterada();
	}
	
	private void saidaPrimeiroTurnoAlterada() {
		int min = getHoraEmMinutos(1);
		min += 60;
		setMinutos(2, min);
		entradaSegundoTurnoAlterada();
	}
	
	private void entradaSegundoTurnoAlterada() {
		int p0 = getHoraEmMinutos(0);
		int p1 = getHoraEmMinutos(1);
		int primeiroTurno = p1 - p0;
		
		int p2 = getHoraEmMinutos(2);
		
		int minutosFaltam = 8 * 60 - primeiroTurno;
		int p3 = p2 + minutosFaltam;
		
		setMinutos(3, p3);
		saidaSegundoTurnoAlterada();
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
	 * Pegar a hora na linha informada. 
	 * @param row
	 * @return a hora
	 */
	private String getHora(int row) {
		String hora = (String) tblHorarios.getValueAt(row, COL_HORA);			
		return hora;
	}
	/**
	 * Pegar os minutos na linha informada. 
	 * @param row
	 * @return
	 */
	private int getHoraEmMinutos(int row) {
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
		try {
			int h = Integer.parseInt(split[0]);
			int m = Integer.parseInt(split[1]);
			
			if (!(h >= 0 && h <= 23)) {
				throw new NumberFormatException("Hora inválida!");
			}
			if (!(m >= 0 && m <= 59)) {
				throw new NumberFormatException("Minutos inválidos!");
			}
			return h * 60 + m;
		} catch (NumberFormatException e) {
			throw new NumberFormatException("Valor inválido!");
		}
		
	}
	
	private void btnOkActionPerformed(ActionEvent evt) {
		win.setExtendedState(Frame.ICONIFIED);
		lblMessage.setVisible(false);
		
		notifyObservers("OK");
	}
	
	private void btnOpcoesActionPerformed(ActionEvent evt) {
		notifyObservers("OPCOES");
	}
	
	@Override
	public void notifyObservers(Object arg) {
		setChanged();
		super.notifyObservers(arg);
	}
	
	public void mostrarMensagemAlarme(String hora) {
		lblMessage.setVisible(true);
		lblMessage.setText("Alarme: " + hora);
		win.setExtendedState(Frame.NORMAL);
	}	
	
	public class FormattedEditor extends AbstractCellEditor implements TableCellEditor {
		/** Serial. */
		private static final long serialVersionUID = -679122425784443569L;
		final JFormattedTextField component;
		private int valorAnterior; 
		
		public FormattedEditor() throws ParseException {
			MaskFormatter maskFormatter = new MaskFormatter("##:##");
			component = new JFormattedTextField(maskFormatter);
			component.setDisabledTextColor(Color.GRAY);
			component.setToolTipText("DUPLO CLICK Para setar a hora corrente");
			
			component.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					if (evt.getClickCount() > 1 && component.isEnabled()) {
						component.setText(getHoraCorrente());
					}					
				}
			});
			
		}	
		
		@Override
		public boolean stopCellEditing() {
			int minutos;
			try {
				minutos = parseMinutos(component.getText());
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
				return false;
			}
			super.stopCellEditing();
			if (minutos != valorAnterior) {
				recalcularAlarmes(tblHorarios.getSelectedRow());
			}
			return true;
				
		}
		
		@Override
		public Object getCellEditorValue() {
			return component.getText(); 
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			String hora = (String)value;
			component.setText(hora);
			valorAnterior = parseMinutos(hora);
			return component;
		} 
		
	}
	

}


