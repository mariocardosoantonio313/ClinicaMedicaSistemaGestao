package view;
import model.Consulta;
import model.Medico;
import model.Paciente;
import util.DateUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.List;

public class ConsultaView extends JPanel {
   private static final Color COR_FUNDO = new Color(240, 248, 255);
    private static final Color COR_TITULO = new Color(25, 118, 210);
    private static final Color COR_BTN_SALVAR = new Color(46, 125, 50);
    private static final Color COR_BTN_ATUALIZAR = new Color(25, 118, 210);
    private static final Color COR_BTN_EXCLUIR = new Color(198, 40, 40);
    private static final Color COR_BTN_LIMPAR = new Color(100, 100, 100);
    private static final Color COR_TABELA_HEADER = new Color(25, 118, 210);


    private final JTextField txtId;
    private final JComboBox<Paciente> comboPaciente;
    private final JComboBox<Medico> comboMedico;
    private final JTextField txtDataHora, txtMotivo, txtStatus;
    private final JButton btnSalvar, btnAtualizar, btnExcluir, btnLimpar;
    private final JTable tabela;
    private final DefaultTableModel modelo;

    public ConsultaView() {
        setLayout(new BorderLayout(8, 8));
        setBackground(COR_FUNDO);

        txtId = new JTextField(5); txtId.setVisible(false);
        comboPaciente = new JComboBox<>();
        comboMedico = new JComboBox<>();
        txtDataHora = new JTextField(16);
        txtMotivo = new JTextField(20);
        txtStatus = new JTextField(15);

         btnSalvar = criarBotao("Salvar", COR_BTN_SALVAR);
         btnAtualizar = criarBotao("Atualizar", COR_BTN_ATUALIZAR);
         btnExcluir = criarBotao("Excluir", COR_BTN_EXCLUIR);
         btnLimpar = criarBotao("Limpar", COR_BTN_LIMPAR);
        JLabel titulo = new JLabel("📅 Gestão de Consultas");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        titulo.setForeground(COR_TITULO);
        titulo.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(COR_FUNDO);
        panelForm.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COR_TITULO, 1), "Dados da Consulta",
            0, 0, new Font("SansSerif", Font.BOLD, 12), COR_TITULO));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        adicionarCampo(panelForm, gbc, "Paciente:", comboPaciente, 0);
        adicionarCampo(panelForm, gbc, "Médico:", comboMedico, 1);
        adicionarCampo(panelForm, gbc, "Data e Hora (yyyy-MM-dd HH:mm:ss):", txtDataHora, 2);
        adicionarCampo(panelForm, gbc, "Motivo:", txtMotivo, 3);
        adicionarCampo(panelForm, gbc, "Status:", txtStatus, 4);

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panelBotoes.setBackground(COR_FUNDO);
        panelBotoes.add(btnSalvar);
        panelBotoes.add(btnAtualizar);
        panelBotoes.add(btnExcluir);
        panelBotoes.add(btnLimpar);

        modelo = new DefaultTableModel(
            new String[]{"ID", "Paciente", "Médico", "DataHora", "Motivo", "Status"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setRowHeight(24);
        tabela.setGridColor(new Color(200, 200, 200));
        tabela.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
    @Override
    public java.awt.Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
        lbl.setBackground(COR_TABELA_HEADER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setOpaque(true);
        lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, Color.WHITE));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        return lbl;
    }
});
        tabela.setSelectionBackground(new Color(255, 220, 180));
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() >= 0)
                preencherFormularioPorLinha(tabela.getSelectedRow());
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COR_TITULO, 1), "Lista de Consultas",
            0, 0, new Font("SansSerif", Font.BOLD, 12), COR_TITULO));

        JPanel panelTopo = new JPanel(new BorderLayout());
        panelTopo.setBackground(COR_FUNDO);
        panelTopo.add(titulo, BorderLayout.NORTH);
        panelTopo.add(panelForm, BorderLayout.CENTER);
        panelTopo.add(panelBotoes, BorderLayout.SOUTH);

        add(panelTopo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

   private JButton criarBotao(String texto, Color cor) {
    JButton btn = new JButton(texto);
    btn.setBackground(cor);
    btn.setForeground(Color.WHITE);
    btn.setFocusPainted(false);
    btn.setOpaque(true);
    btn.setBorderPainted(false);
    btn.setFont(new Font("Arial", Font.BOLD, 12));
    btn.setPreferredSize(new Dimension(110, 32));
    return btn;
}
    private void adicionarCampo(JPanel panel, GridBagConstraints gbc, String label, JComponent campo, int linha) {
        gbc.gridx = 0; gbc.gridy = linha;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        panel.add(lbl, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(campo, gbc);
    }

    private void preencherFormularioPorLinha(int row) {
        Object id = modelo.getValueAt(row, 0);
        Object pac = modelo.getValueAt(row, 1);
        Object med = modelo.getValueAt(row, 2);
        Object dh = modelo.getValueAt(row, 3);
        Object mot = modelo.getValueAt(row, 4);
        Object sta = modelo.getValueAt(row, 5);
        txtId.setText(id != null ? id.toString() : "");
        if (pac != null) selecionarComboPaciente(pac.toString());
        if (med != null) selecionarComboMedico(med.toString());
        txtDataHora.setText(dh != null ? dh.toString() : "");
        txtMotivo.setText(mot != null ? mot.toString() : "");
        txtStatus.setText(sta != null ? sta.toString() : "");
    }

    public void preencherTabela(List<Consulta> consultas) {
        modelo.setRowCount(0);
        for (Consulta c : consultas)
            modelo.addRow(new Object[]{c.getId(), c.getPaciente().getNome(),
                c.getMedico().getNome(), DateUtil.formatDateTime(c.getDataHora()),
                c.getMotivo(), c.getStatus()});
    }

    public void preencherComboPacientes(List<Paciente> pacientes) {
        DefaultComboBoxModel<Paciente> m = new DefaultComboBoxModel<>();
        for (Paciente p : pacientes) m.addElement(p);
        comboPaciente.setModel(m);
    }

    public void preencherComboMedicos(List<Medico> medicos) {
        DefaultComboBoxModel<Medico> m = new DefaultComboBoxModel<>();
        for (Medico med : medicos) m.addElement(med);
        comboMedico.setModel(m);
    }

    public Consulta getConsultaFromForm() throws ParseException {
        Consulta c = new Consulta();
        if (!txtId.getText().trim().isEmpty())
            c.setId(Integer.parseInt(txtId.getText().trim()));
        c.setPaciente((Paciente) comboPaciente.getSelectedItem());
        c.setMedico((Medico) comboMedico.getSelectedItem());
        c.setDataHora(DateUtil.parseDateTime(txtDataHora.getText().trim()));
        c.setMotivo(txtMotivo.getText().trim());
        c.setStatus(txtStatus.getText().trim());
        return c;
    }

    public int getConsultaId() { return Integer.parseInt(txtId.getText().trim()); }

    private void selecionarComboPaciente(String nome) {
        for (int i = 0; i < comboPaciente.getItemCount(); i++)
            if (comboPaciente.getItemAt(i).getNome().equals(nome)) {
                comboPaciente.setSelectedIndex(i); return; }
    }

    private void selecionarComboMedico(String nome) {
        for (int i = 0; i < comboMedico.getItemCount(); i++)
            if (comboMedico.getItemAt(i).getNome().equals(nome)) {
                comboMedico.setSelectedIndex(i); return; }
    }

    public void limparFormulario() {
        txtId.setText(""); comboPaciente.setSelectedIndex(-1);
        comboMedico.setSelectedIndex(-1); txtDataHora.setText("");
        txtMotivo.setText(""); txtStatus.setText(""); tabela.clearSelection();
    }

    public void addSalvarListener(ActionListener l) { btnSalvar.addActionListener(l); }
    public void addAtualizarListener(ActionListener l) { btnAtualizar.addActionListener(l); }
    public void addExcluirListener(ActionListener l) { btnExcluir.addActionListener(l); }
    public void addLimparListener(ActionListener l) { btnLimpar.addActionListener(l); }

    public void mostrarMensagem(String m) {
        JOptionPane.showMessageDialog(this, m, "Informação", JOptionPane.INFORMATION_MESSAGE); }
    public void mostrarErro(String m) {
        JOptionPane.showMessageDialog(this, m, "Erro", JOptionPane.ERROR_MESSAGE); }

    public void setEditable(boolean e) {
        btnSalvar.setEnabled(e); btnAtualizar.setEnabled(e);
        btnExcluir.setEnabled(e); btnLimpar.setEnabled(e);
        comboPaciente.setEnabled(e); comboMedico.setEnabled(e);
        txtDataHora.setEditable(e); txtMotivo.setEditable(e); txtStatus.setEditable(e);
    }
}