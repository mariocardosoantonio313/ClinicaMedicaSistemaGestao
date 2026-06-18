
package view;
import model.Medico;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class MedicoView extends JPanel {
    
    private static final Color COR_FUNDO = new Color(240, 248, 255);
    private static final Color COR_TITULO = new Color(25, 118, 210);
    private static final Color COR_BTN_SALVAR = new Color(46, 125, 50);
    private static final Color COR_BTN_ATUALIZAR = new Color(25, 118, 210);
    private static final Color COR_BTN_EXCLUIR = new Color(198, 40, 40);
    private static final Color COR_BTN_LIMPAR = new Color(100, 100, 100);
  private static final Color COR_TABELA_HEADER = new Color(25, 118, 210);


    private final JTextField txtId, txtNome, txtCpf, txtTelefone, txtEspecialidade, txtCrm;
    private final JButton btnSalvar, btnAtualizar, btnExcluir, btnLimpar;
    private final JTable tabela;
    private final DefaultTableModel modelo;
    

    public MedicoView() {
        setLayout(new BorderLayout(8, 8));
        setBackground(COR_FUNDO);

        txtId = new JTextField(5); txtId.setVisible(false);
        txtNome = new JTextField(20);
        txtCpf = new JTextField(15);
        txtTelefone = new JTextField(15);
        txtEspecialidade = new JTextField(15);
        txtCrm = new JTextField(15);

        btnSalvar = criarBotao("Salvar", COR_BTN_SALVAR);
        btnAtualizar = criarBotao("Atualizar", COR_BTN_ATUALIZAR);
        btnExcluir = criarBotao("Excluir", COR_BTN_EXCLUIR);
        btnLimpar = criarBotao("Limpar", COR_BTN_LIMPAR);

        JLabel titulo = new JLabel("🩺 Gestão de Médicos");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        titulo.setForeground(COR_TITULO);
        titulo.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));

 
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(COR_FUNDO);
        panelForm.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COR_TITULO, 1), "Dados do Médico",
            0, 0, new Font("SansSerif", Font.BOLD, 12), COR_TITULO));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        adicionarCampo(panelForm, gbc, "Nome:", txtNome, 0);
        adicionarCampo(panelForm, gbc, "CPF:", txtCpf, 1);
        adicionarCampo(panelForm, gbc, "Telefone:", txtTelefone, 2);
        adicionarCampo(panelForm, gbc, "Especialidade:", txtEspecialidade, 3);
        adicionarCampo(panelForm, gbc, "CRM:", txtCrm, 4);

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panelBotoes.setBackground(COR_FUNDO);
        panelBotoes.add(btnSalvar);
        panelBotoes.add(btnAtualizar);
        panelBotoes.add(btnExcluir);
        panelBotoes.add(btnLimpar);

        modelo = new DefaultTableModel(
            new String[]{"ID", "Nome", "CPF", "Telefone", "Especialidade", "CRM"}, 0) {
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
        tabela.setSelectionBackground(new Color(200, 230, 200));
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() >= 0)
                preencherFormularioPorLinha(tabela.getSelectedRow());
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COR_TITULO, 1), "Lista de Médicos",
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
        Object nome = modelo.getValueAt(row, 1);
        Object cpf = modelo.getValueAt(row, 2);
        Object tel = modelo.getValueAt(row, 3);
        Object esp = modelo.getValueAt(row, 4);
        Object crm = modelo.getValueAt(row, 5);
        txtId.setText(id != null ? id.toString() : "");
        txtNome.setText(nome != null ? nome.toString() : "");
        txtCpf.setText(cpf != null ? cpf.toString() : "");
        txtTelefone.setText(tel != null ? tel.toString() : "");
        txtEspecialidade.setText(esp != null ? esp.toString() : "");
        txtCrm.setText(crm != null ? crm.toString() : "");
    }

    public void preencherTabela(List<Medico> medicos) {
        modelo.setRowCount(0);
        for (Medico m : medicos)
            modelo.addRow(new Object[]{m.getId(), m.getNome(), m.getCpf(),
                m.getTelefone(), m.getEspecialidade(), m.getCrm()});
    }

    public Medico getMedicoFromForm() {
        Medico m = new Medico();
        if (!txtId.getText().trim().isEmpty())
            m.setId(Integer.parseInt(txtId.getText().trim()));
        m.setNome(txtNome.getText().trim());
        m.setCpf(txtCpf.getText().trim());
        m.setTelefone(txtTelefone.getText().trim());
        m.setEspecialidade(txtEspecialidade.getText().trim());
        m.setCrm(txtCrm.getText().trim());
        return m;
    }

    public int getMedicoId() { return Integer.parseInt(txtId.getText().trim()); }

    public void limparFormulario() {
        txtId.setText(""); txtNome.setText(""); txtCpf.setText("");
        txtTelefone.setText(""); txtEspecialidade.setText("");
        txtCrm.setText(""); tabela.clearSelection();
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
    }
}
