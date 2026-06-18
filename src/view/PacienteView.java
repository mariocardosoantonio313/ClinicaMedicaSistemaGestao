package view;

import model.Paciente;
import util.DateUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.List;

public class PacienteView extends JPanel {

    private static final Color COR_FUNDO = new Color(240, 248, 255);
    private static final Color COR_TITULO = new Color(25, 118, 210);
    private static final Color COR_BTN_SALVAR = new Color(46, 125, 50);
    private static final Color COR_BTN_ATUALIZAR = new Color(25, 118, 210);
    private static final Color COR_BTN_EXCLUIR = new Color(198, 40, 40);
    private static final Color COR_BTN_LIMPAR = new Color(100, 100, 100);
    private static final Color COR_TABELA_HEADER = new Color(25, 118, 210);

    private final JTextField txtId;
    private final JTextField txtNome;
    private final JTextField txtCpf;
    private final JTextField txtTelefone;
    private final JTextField txtDataNascimento;
    private final JTextField txtEndereco;

    private final JTextField txtPesquisa;
    private final JButton btnPesquisar;

    private final JCheckBox chkAtivo;

    private final JButton btnSalvar;
    private final JButton btnAtualizar;
    private final JButton btnExcluir;
    private final JButton btnLimpar;

    private final JTable tabela;
    private final DefaultTableModel modelo;

    public PacienteView() {

        setLayout(new BorderLayout(8, 8));
        setBackground(COR_FUNDO);

        txtId = new JTextField(5);
        txtId.setVisible(false);

        txtNome = new JTextField(20);
        txtCpf = new JTextField(15);
        txtTelefone = new JTextField(15);
        txtDataNascimento = new JTextField(10);
        txtEndereco = new JTextField(25);

        txtPesquisa = new JTextField(18);
        btnPesquisar = criarBotao("Pesquisar", new Color(70, 130, 180));

        chkAtivo = new JCheckBox("Ativo", true);
        chkAtivo.setBackground(COR_FUNDO);

        btnSalvar = criarBotao("Salvar", COR_BTN_SALVAR);
        btnAtualizar = criarBotao("Atualizar", COR_BTN_ATUALIZAR);
        btnExcluir = criarBotao("Excluir", COR_BTN_EXCLUIR);
        btnLimpar = criarBotao("Limpar", COR_BTN_LIMPAR);

        JLabel titulo = new JLabel("👤 Gestão de Pacientes");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        titulo.setForeground(COR_TITULO);
        titulo.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));

        JPanel panelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        panelPesquisa.setBackground(COR_FUNDO);

        JLabel lblPesquisa = new JLabel("Pesquisar por Nome ou CPF:");
        lblPesquisa.setFont(new Font("SansSerif", Font.BOLD, 12));

        panelPesquisa.add(lblPesquisa);
        panelPesquisa.add(txtPesquisa);
        panelPesquisa.add(btnPesquisar);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(COR_FUNDO);

        panelForm.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COR_TITULO, 1),
                "Dados do Paciente",
                0,
                0,
                new Font("SansSerif", Font.BOLD, 12),
                COR_TITULO));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);

        adicionarCampo(panelForm, gbc, "Nome:", txtNome, 0);
        adicionarCampo(panelForm, gbc, "CPF:", txtCpf, 1);
        adicionarCampo(panelForm, gbc, "Telefone:", txtTelefone, 2);
        adicionarCampo(panelForm, gbc, "Data Nasc (yyyy-MM-dd):", txtDataNascimento, 3);
        adicionarCampo(panelForm, gbc, "Endereço:", txtEndereco, 4);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        panelForm.add(chkAtivo, gbc);

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panelBotoes.setBackground(COR_FUNDO);

        panelBotoes.add(btnSalvar);
        panelBotoes.add(btnAtualizar);
        panelBotoes.add(btnExcluir);
        panelBotoes.add(btnLimpar);

        modelo = new DefaultTableModel(
                new String[]{
                        "ID",
                        "Nome",
                        "CPF",
                        "Telefone",
                        "Nascimento",
                        "Endereço",
                        "Ativo"
                }, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modelo);

        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setRowHeight(24);

        tabela.getTableHeader().setDefaultRenderer(
                new javax.swing.table.DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table,
                            Object value,
                            boolean isSelected,
                            boolean hasFocus,
                            int row,
                            int column) {

                        JLabel lbl =
                                (JLabel) super.getTableCellRendererComponent(
                                        table,
                                        value,
                                        isSelected,
                                        hasFocus,
                                        row,
                                        column);

                        lbl.setBackground(COR_TABELA_HEADER);
                        lbl.setForeground(Color.WHITE);
                        lbl.setFont(new Font("Arial", Font.BOLD, 12));
                        lbl.setOpaque(true);
                        lbl.setHorizontalAlignment(SwingConstants.CENTER);

                        return lbl;
                    }
                });

        tabela.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()
                    && tabela.getSelectedRow() >= 0) {

                preencherFormularioPorLinha(
                        tabela.getSelectedRow());
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);

        JPanel panelTopo = new JPanel(new BorderLayout());
        panelTopo.setBackground(COR_FUNDO);

        panelTopo.add(titulo, BorderLayout.NORTH);
        panelTopo.add(panelPesquisa, BorderLayout.CENTER);
        panelTopo.add(panelForm, BorderLayout.SOUTH);

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBackground(COR_FUNDO);

        panelCentro.add(panelBotoes, BorderLayout.NORTH);
        panelCentro.add(scroll, BorderLayout.CENTER);

        add(panelTopo, BorderLayout.NORTH);
        add(panelCentro, BorderLayout.CENTER);
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

    private void adicionarCampo(
            JPanel panel,
            GridBagConstraints gbc,
            String label,
            JComponent campo,
            int linha) {

        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.anchor = GridBagConstraints.EAST;

        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;

        panel.add(campo, gbc);
    }

    private void preencherFormularioPorLinha(int row) {

        txtId.setText(String.valueOf(modelo.getValueAt(row, 0)));
        txtNome.setText(String.valueOf(modelo.getValueAt(row, 1)));
        txtCpf.setText(String.valueOf(modelo.getValueAt(row, 2)));
        txtTelefone.setText(String.valueOf(modelo.getValueAt(row, 3)));
        txtDataNascimento.setText(String.valueOf(modelo.getValueAt(row, 4)));
        txtEndereco.setText(String.valueOf(modelo.getValueAt(row, 5)));

        chkAtivo.setSelected(
                Boolean.parseBoolean(
                        String.valueOf(modelo.getValueAt(row, 6))));
    }

    public void preencherTabela(List<Paciente> pacientes) {

        modelo.setRowCount(0);

        for (Paciente p : pacientes) {

            modelo.addRow(new Object[]{
                    p.getId(),
                    p.getNome(),
                    p.getCpf(),
                    p.getTelefone(),
                    DateUtil.formatDate(p.getDataNascimento()),
                    p.getEndereco(),
                    p.isAtivo()
            });
        }
    }

    public Paciente getPacienteFromForm() throws ParseException {

        Paciente p = new Paciente();

        if (!txtId.getText().trim().isEmpty()) {
            p.setId(Integer.parseInt(txtId.getText().trim()));
        }

        p.setNome(txtNome.getText().trim());
        p.setCpf(txtCpf.getText().trim());
        p.setTelefone(txtTelefone.getText().trim());
        p.setDataNascimento(
                DateUtil.parseDate(
                        txtDataNascimento.getText().trim()));

        p.setEndereco(txtEndereco.getText().trim());

        p.setAtivo(chkAtivo.isSelected());

        return p;
    }

    public String getTextoPesquisa() {
        return txtPesquisa.getText().trim();
    }

    public int getPacienteId() {
        return Integer.parseInt(txtId.getText().trim());
    }

    public void limparFormulario() {

        txtId.setText("");
        txtNome.setText("");
        txtCpf.setText("");
        txtTelefone.setText("");
        txtDataNascimento.setText("");
        txtEndereco.setText("");

        chkAtivo.setSelected(true);

        tabela.clearSelection();
    }

    public void addSalvarListener(ActionListener l) {
        btnSalvar.addActionListener(l);
    }

    public void addAtualizarListener(ActionListener l) {
        btnAtualizar.addActionListener(l);
    }

    public void addExcluirListener(ActionListener l) {
        btnExcluir.addActionListener(l);
    }

    public void addLimparListener(ActionListener l) {
        btnLimpar.addActionListener(l);
    }

    public void addPesquisarListener(ActionListener l) {
        btnPesquisar.addActionListener(l);
    }

    public void mostrarMensagem(String m) {
        JOptionPane.showMessageDialog(
                this,
                m,
                "Informação",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarErro(String m) {
        JOptionPane.showMessageDialog(
                this,
                m,
                "Erro",
                JOptionPane.ERROR_MESSAGE);
    }

    public void setEditable(boolean e) {
        btnSalvar.setEnabled(e);
        btnAtualizar.setEnabled(e);
        btnExcluir.setEnabled(e);
        btnLimpar.setEnabled(e);
    }
}