package org.marcosoft.alarm;
import static org.marcosoft.util.HoraUtils.formatHoras;

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
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.text.MaskFormatter;

import org.marcosoft.alarm.Horarios.Turnos;
import org.marcosoft.util.SwingUtil;
import org.marcosoft.util.SystemUtil;

public class AlarmEditor extends Observable {
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
						stopCellEditing();
					}					
				}
			});
			
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
			component.setSelectionStart(0);
			component.setSelectionEnd(hora.length());
			valorAnterior = parseMinutos(hora);
			return component;
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
		
	}
	
	private static final long serialVersionUID = -1045806326715430034L;
	
	private static final int COL_TURNO = 0;	
	private static final int COL_HORA = 1;	
	private static final int COL_ACUMULADO = 2;
	
	private static final int ROW_PRIMEIRO_TURNO_ENTRADA = 0;	
	private static final int ROW_PRIMEIRO_TURNO_SAIDA = 1;
	
	private static final int ROW_SEGUNDO_TURNO_ENTRADA = 3;	
	private static final int ROW_SEGUNDO_TURNO_SAIDA = 4;
	
	private static final int ROW_PRIMEIRO_TURNO_EXTRA_ENTRADA = 6;	
	private static final int ROW_PRIMEIRO_TURNO_EXTRA_SAIDA = 7;
	
	private static final int ROW_SEGUNDO_TURNO_EXTRA_ENTRADA = 9;	
	private static final int ROW_SEGUNDO_TURNO_EXTRA_SAIDA = 10;
	
	private JScrollPane jScrollPane1;
	private JTable tblHorarios;
	private JLabel lblMessage;
	private JButton btnOpcoes;
	private JPanel jPanel1;
	private JButton btnOk;

	private JLabel txtUser;

	private Horarios horarios;

	private JFrame win;
	
	private JCheckBox chkMute;

	public AlarmEditor(Horarios horarios) {
		initGUI();
		this.horarios = horarios;
		lblMessage.setVisible(false);
		atualizarView(horarios);
	}

	private void atualizarView(Horarios horarios) {
		String[] horariosArray = horarios.asArray();
		int index = 0;
		for (int row = 0; row < tblHorarios.getRowCount(); row++) {
			if (tblHorarios.isCellEditable(row, COL_HORA)) {
				tblHorarios.setValueAt(horariosArray[index++], row, COL_HORA);
			}
		}
		calcularDuracao();
	}

	private void btnOkActionPerformed(ActionEvent evt) {
		win.setExtendedState(Frame.ICONIFIED);
		lblMessage.setVisible(false);
		
		notifyObservers("OK");
	}

	private void btnOpcoesActionPerformed(ActionEvent evt) {
		notifyObservers("OPCOES");
	}

	private void calcularDuracao() {
		int acumulado = horarios.getDuracaoPrimeiroTurno();
		setDuracao(ROW_PRIMEIRO_TURNO_SAIDA, horarios.getDuracaoPrimeiroTurno(), acumulado);
		
		acumulado += horarios.getDuracaoSegundoTurno();
		setDuracao(ROW_SEGUNDO_TURNO_SAIDA, horarios.getDuracaoSegundoTurno(), acumulado);
		
		int extraAcumulado = horarios.getDuracaoPrimeiroTurnoExtra();
		setDuracao(ROW_PRIMEIRO_TURNO_EXTRA_SAIDA, horarios.getDuracaoPrimeiroTurnoExtra(), extraAcumulado);
		
		extraAcumulado += horarios.getDuracaoSegundoTurnoExtra();
		setDuracao(ROW_SEGUNDO_TURNO_EXTRA_SAIDA, horarios.getDuracaoSegundoTurnoExtra(), extraAcumulado);		
	}

	private void chkMuteActionPerformed(ActionEvent evt) {
		notifyObservers(chkMute.isSelected() ? "MUTE_ON" : "MUTE_OFF");
	}

	private void entradaPrimeiroTurnoAlterada() {
		int min = horarios.getEntrada(Turnos.Primeiro);
		min += 4 * 60;
		horarios.setSaida(Turnos.Primeiro, min);
		saidaPrimeiroTurnoAlterada();
	}
	
	private void entradaPrimeiroTurnoExtraAlterada() {
		int min = horarios.getEntrada(Turnos.PrimeiroExtra);
		min += 2 * 60;
		horarios.setSaida(Turnos.PrimeiroExtra, min);
		saidaPrimeiroTurnoExtraAlterada();
	}
	
	private void entradaSegundoTurnoAlterada() {
		int primeiroTurno = horarios.getDuracaoPrimeiroTurno();		
		int p2 = horarios.getEntrada(Turnos.Segundo);
		
		int minutosFaltam = 8 * 60 - primeiroTurno;
		int p3 = p2 + minutosFaltam;
		
		horarios.setSaida(Turnos.Segundo, p3);		
		saidaSegundoTurnoAlterada();
	}
	
	private void entradaSegundoTurnoExtraAlterada() {
		int minExtrasRealizados = horarios.getDuracaoPrimeiroTurnoExtra();
		int minExtrasRestantes = 2 * 60 - minExtrasRealizados;
		
		horarios.setSaida(Turnos.SegundoExtra, minExtrasRestantes + horarios.getEntrada(Turnos.SegundoExtra));
		saidaSegundoTurnoExtraAlterada();
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
	
	private class MyTableCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			
			JLabel renderedLabel = (JLabel) super
					.getTableCellRendererComponent(table, value, isSelected,
							hasFocus, row, column);
			
			switch (column) {
			case COL_TURNO:
				renderedLabel.setHorizontalAlignment(SwingConstants.LEFT);
				break;

			case COL_HORA:
				renderedLabel.setHorizontalAlignment(SwingConstants.CENTER);
				break;
				
			case COL_ACUMULADO:
				renderedLabel.setHorizontalAlignment(SwingConstants.CENTER);
				break;
			}
			if ( ((String) table.getValueAt(row, COL_TURNO)).isEmpty()) {
				renderedLabel.setBackground(new Color(238, 238, 238));
			} else {
				renderedLabel.setBackground(Color.white);
			}
			
			return renderedLabel;
		}
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
				win.getContentPane().add(
						jScrollPane1,
						new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.BOTH,
								new Insets(0, 0, 0, 0), 0, 0));
				{
					TableModel tblHorariosModel = 
						new DefaultTableModel(
								new Object[][] { 
											{ "1º Período - Entrada", "00:00", "" }, { "1º Período - Saída", "00:00", "" }, 
											{ "", "", "" }, 
											{ "2º Período - Entrada", "00:00", "" }, { "2º Período - Saída", "00:00", "" }, 
											{ "", "", "" }, 
											{ "1º Período - Extra - Entrada", "00:00", "" }, { "1º Período - Extra - Saída", "00:00", "" }, 
											{ "", "", "" }, 
											{ "2º Período - Extra - Entrada", "00:00", "" }, { "2º Período - Extra - Saída", "00:00", "" }, 
										},
								new String[] { "Período", "Hora", "Duração (Acumulado)" });
					
					tblHorarios = new JTable() {
						private static final long serialVersionUID = 1L;
						private TableCellRenderer renderer = new MyTableCellRenderer();

						@Override
						public boolean isCellEditable(int row, int column) {
							return column == COL_HORA && !((String) getValueAt(row, column)).isEmpty();							
						}
						
						@Override
						public TableCellRenderer getCellRenderer(int row,
								int column) {
							return renderer;
						}						
					};
					tblHorarios.setModel(tblHorariosModel);
					
					TableColumnModel columnModel = tblHorarios.getColumnModel();
					TableColumn col = columnModel.getColumn(COL_HORA);					
					col.setCellEditor(new FormattedEditor());
					col.setPreferredWidth(40);
					
					col = columnModel.getColumn(COL_TURNO);					
					col.setPreferredWidth(160);
					
					col = columnModel.getColumn(COL_ACUMULADO);					
					col.setPreferredWidth(140);					
					
					jScrollPane1.setViewportView(tblHorarios);
				}
			}
			{
				txtUser = new JLabel();
				win.getContentPane().add(
						txtUser,
						new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.BOTH,
								new Insets(0, 0, 0, 0), 0, 0));
				txtUser.setText("User");
			}
			{
				lblMessage = new JLabel();
				win.getContentPane().add(
						lblMessage,
						new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.BOTH,
								new Insets(2, 2, 2, 2), 0, 0));
				jPanel1 = new JPanel();
				FlowLayout jPanel1Layout = new FlowLayout();
				jPanel1Layout.setAlignment(FlowLayout.RIGHT);
				jPanel1.setLayout(jPanel1Layout);				
				{
					chkMute = new JCheckBox();
					chkMute.setText("Sem som");
					jPanel1.add(chkMute);
					chkMute.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							chkMuteActionPerformed(evt);
						}
					});					
					
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
				win.getContentPane().add(
						jPanel1,
						new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.BOTH,
								new Insets(0, 0, 0, 0), 0, 0));
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
	
	public void mostrarMensagemAlarme(String hora) {
		lblMessage.setVisible(true);
		lblMessage.setText("Alarme: " + hora);
		win.setExtendedState(Frame.NORMAL);
	}

	@Override
	public void notifyObservers(Object arg) {
		setChanged();
		super.notifyObservers(arg);
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

	private void recalcularAlarmes(int linha) {
		switch (linha) {
			case ROW_PRIMEIRO_TURNO_ENTRADA:
				horarios.setPrimeiroTurnoEntrada(getHora(linha));
				entradaPrimeiroTurnoAlterada();
				break;
			case ROW_PRIMEIRO_TURNO_SAIDA:
				horarios.setPrimeiroTurnoSaida(getHora(linha));
				saidaPrimeiroTurnoAlterada();
				break;
			case ROW_SEGUNDO_TURNO_ENTRADA:
				horarios.setSegundoTurnoEntrada(getHora(linha));
				entradaSegundoTurnoAlterada();
				break;
			case ROW_SEGUNDO_TURNO_SAIDA:
				horarios.setSegundoTurnoSaida(getHora(linha));
				saidaSegundoTurnoAlterada();
				break;
			case ROW_PRIMEIRO_TURNO_EXTRA_ENTRADA:
				horarios.setPrimeiroTurnoExtraEntrada(getHora(linha));
				entradaPrimeiroTurnoExtraAlterada();
				break;
			case ROW_PRIMEIRO_TURNO_EXTRA_SAIDA:
				horarios.setPrimeiroTurnoExtraSaida(getHora(linha));
				saidaPrimeiroTurnoExtraAlterada();
				break;
			case ROW_SEGUNDO_TURNO_EXTRA_ENTRADA:
				horarios.setSegundoTurnoExtraEntrada(getHora(linha));
				entradaSegundoTurnoExtraAlterada();
				break;
			case ROW_SEGUNDO_TURNO_EXTRA_SAIDA:
				horarios.setSegundoTurnoExtraSaida(getHora(linha));
				saidaSegundoTurnoExtraAlterada();
				break;
		}
		horarios.notifyObservers();
		atualizarView(horarios);
	}
	
	private void saidaPrimeiroTurnoAlterada() {
		int min = horarios.getSaida(Turnos.Primeiro);
		min += 60;
		horarios.setEntrada(Turnos.Segundo, min);
		entradaSegundoTurnoAlterada();
	}
	
	private void saidaPrimeiroTurnoExtraAlterada() {
		horarios.setEntrada(Turnos.SegundoExtra, horarios.getSaida(Turnos.PrimeiroExtra));
		entradaSegundoTurnoExtraAlterada();
	}
	
	private void saidaSegundoTurnoAlterada() {
		int min = horarios.getSaida(Turnos.Segundo);
		min += 15;
		horarios.setEntrada(Turnos.PrimeiroExtra, min);
		entradaPrimeiroTurnoExtraAlterada();	
	}
	
	private void saidaSegundoTurnoExtraAlterada() {
		
	}
	
	/**
	 * Setar os minutos acumuldados na tabela. 
	 * @param row
	 * @param duracao
	 */
	private void setDuracao(int row, int duracao, int acumulado) {
		final String hora;
		if (acumulado > 0) {
			hora = String.format("%s (%s)", formatHoras(duracao), formatHoras(acumulado));
		} else {
			hora = formatHoras(duracao);
		}
		tblHorarios.setValueAt(hora, row, COL_ACUMULADO);
	}

}


