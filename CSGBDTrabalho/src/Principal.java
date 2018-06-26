import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class Principal extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private ConsultaJDBCDAO consultaJDBCDAO;
	private JPanel contentPane;
	private JComboBox<String> comboBox;
	private JLabel labelValorDoacao;
	private JLabel labelTempo;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal frame = new Principal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Principal() {
		
		try {
			consultaJDBCDAO = new ConsultaJDBCDAO();
		} catch (SQLException e) {
			e.printStackTrace();
			//JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 260);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnConsultaSemIndice = new JButton("Consulta sem indice");
		btnConsultaSemIndice.addActionListener(new ConsultaSemIndiceListner());
		btnConsultaSemIndice.setBounds(22, 179, 245, 25);
		contentPane.add(btnConsultaSemIndice);
		
		JButton btnConsultaComIndice = new JButton("Consulta com indice");
		btnConsultaComIndice.addActionListener(new ConsultaComIndiceListner());
		btnConsultaComIndice.setBounds(279, 179, 244, 25);
		contentPane.add(btnConsultaComIndice);
		
		JLabel lblSelecionarEstado = new JLabel("Selecionar estado:");
		lblSelecionarEstado.setFont(new Font("Dialog", Font.BOLD, 20));
		lblSelecionarEstado.setBounds(22, 27, 289, 20);
		contentPane.add(lblSelecionarEstado);
		
		comboBox = new JComboBox<>();
		comboBox.setFont(new Font("Dialog", Font.BOLD, 20));
		comboBox.addItem("AC");
		comboBox.addItem("AL");
		comboBox.addItem("AP");
		comboBox.addItem("AM");
		comboBox.addItem("BA");
		comboBox.addItem("CE");
		comboBox.addItem("DF");
		comboBox.addItem("ES");
		comboBox.addItem("GO");
		comboBox.addItem("MA");
		comboBox.addItem("MT");
		comboBox.addItem("MS");
		comboBox.addItem("MG");
		comboBox.addItem("PA");
		comboBox.addItem("PB");
		comboBox.addItem("PR");
		comboBox.addItem("PE");
		comboBox.addItem("PI");
		comboBox.addItem("RJ");
		comboBox.addItem("RN");
		comboBox.addItem("RS");
		comboBox.addItem("RO");
		comboBox.addItem("RR");
		comboBox.addItem("SC");
		comboBox.addItem("SP");
		comboBox.addItem("SE");
		comboBox.addItem("TO");
		
		comboBox.setToolTipText("");
		comboBox.setBounds(341, 27, 149, 24);
		contentPane.add(comboBox);
		
		JLabel lblMaiorDoacaoDo = new JLabel("Maior doacao do estado:");
		lblMaiorDoacaoDo.setFont(new Font("Dialog", Font.BOLD, 20));
		lblMaiorDoacaoDo.setBounds(22, 70, 302, 15);
		contentPane.add(lblMaiorDoacaoDo);
		
		labelValorDoacao = new JLabel("");
		labelValorDoacao.setFont(new Font("Dialog", Font.BOLD, 20));
		labelValorDoacao.setBounds(341, 63, 149, 24);
		contentPane.add(labelValorDoacao);
		
		JLabel lblTempoDaConsulta = new JLabel("Tempo da consulta:");
		lblTempoDaConsulta.setFont(new Font("Dialog", Font.BOLD, 20));
		lblTempoDaConsulta.setBounds(22, 107, 226, 25);
		contentPane.add(lblTempoDaConsulta);
		
		labelTempo = new JLabel("");
		labelTempo.setFont(new Font("Dialog", Font.BOLD, 20));
		labelTempo.setBounds(337, 107, 149, 25);
		contentPane.add(labelTempo);
	}
	
	class ConsultaSemIndiceListner implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				String estado = comboBox.getSelectedItem().toString();
				long tempoInicio = System.currentTimeMillis();
				Integer valorDoado = consultaJDBCDAO.consulta(estado);
				long tempoFim = System.currentTimeMillis();
				labelValorDoacao.setText(String.valueOf(valorDoado));
				labelTempo.setText(String.valueOf(tempoFim - tempoInicio) + " ms");
				JOptionPane.showMessageDialog(null, consultaJDBCDAO.planoExecucao(estado), "Plano de execucao", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
				//JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}
	
	class ConsultaComIndiceListner implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				consultaJDBCDAO.criarIndice();
				String estado = comboBox.getSelectedItem().toString();
				long tempoInicio = System.currentTimeMillis();
				Integer valorDoado = consultaJDBCDAO.consulta(estado);
				long tempoFim = System.currentTimeMillis();
				labelValorDoacao.setText(String.valueOf(valorDoado));
				labelTempo.setText(String.valueOf(tempoFim - tempoInicio) + " ms");
				JOptionPane.showMessageDialog(null, consultaJDBCDAO.planoExecucao(estado), "Plano de execucao", JOptionPane.INFORMATION_MESSAGE);
				consultaJDBCDAO.deletarIndice();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}
}
