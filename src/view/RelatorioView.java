package view;
import model.Consulta;
import model.Paciente;
import util.DateUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class RelatorioView extends JPanel {
     private static final Color COR_FUNDO = new Color(240, 248, 255);
    private static final Color COR_TITULO = new Color(25, 118, 210);
     private static final Color COR_TABELA_HEADER = new Color(25, 118, 210);

    private final JTable tabelaPacientes, tabelaConsultas;
    private final DefaultTableModel modeloPacientes, modeloConsultas;
    private final JButton btnGerarRelatorio;

    public RelatorioView() {
        setLayout(new BorderLayout(8, 8));
        setBackground(COR_FUNDO);

        modeloPacientes = new DefaultTableModel(
            new String[]{"ID", "Nome", "CPF", "Ativo"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        modeloConsultas = new DefaultTableModel(
            new String[]{"ID", "Médico", "Paciente", "DataHora", "Motivo", "Status"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tabelaPacientes = criarTabela(modeloPacientes);
        tabelaConsultas = criarTabela(modeloConsultas);

        btnGerarRelatorio = new JButton("📊 Gerar Relatórios");
        btnGerarRelatorio.setBackground(COR_TITULO);
        btnGerarRelatorio.setForeground(Color.WHITE);
        btnGerarRelatorio.setFocusPainted(false);
        btnGerarRelatorio.setOpaque(true);
        btnGerarRelatorio.setBorderPainted(false);
        btnGerarRelatorio.setFont(new Font("Arial", Font.BOLD, 13));
        btnGerarRelatorio.setPreferredSize(new Dimension(200, 36));

        JLabel titulo = new JLabel("📊 Relatórios do Sistema");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        titulo.setForeground(COR_TITULO);
        titulo.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));

        JPanel panelTop = new JPanel(new BorderLayout(4, 4));
        panelTop.setBackground(COR_FUNDO);
        panelTop.add(titulo, BorderLayout.NORTH);
        panelTop.add(btnGerarRelatorio, BorderLayout.CENTER);

        JScrollPane scrollPacientes = new JScrollPane(tabelaPacientes);
        scrollPacientes.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COR_TITULO, 1), "👤 Pacientes Ativos",
            0, 0, new Font("SansSerif", Font.BOLD, 12), COR_TITULO));

        JScrollPane scrollConsultas = new JScrollPane(tabelaConsultas);
        scrollConsultas.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COR_TITULO, 1), "📅 Consultas",
            0, 0, new Font("SansSerif", Font.BOLD, 12), COR_TITULO));

        JPanel panelCenter = new JPanel(new GridLayout(2, 1, 8, 8));
        panelCenter.setBackground(COR_FUNDO);
        panelCenter.add(scrollPacientes);
        panelCenter.add(scrollConsultas);

        add(panelTop, BorderLayout.NORTH);
        add(panelCenter, BorderLayout.CENTER);
    }

    private JTable criarTabela(DefaultTableModel modelo) {
        JTable t = new JTable(modelo);
        t.setRowHeight(24);
        t.setGridColor(new Color(200, 200, 200));
t.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
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
        t.setSelectionBackground(new Color(255, 220, 180));
        return t;
    }

    public void exibirPacientesAtivos(List<Paciente> pacientes) {
        modeloPacientes.setRowCount(0);
        for (Paciente p : pacientes)
            modeloPacientes.addRow(new Object[]{p.getId(), p.getNome(), p.getCpf(), p.isAtivo()});
    }

    public void exibirConsultasPorMedico(List<Consulta> consultas) {
        modeloConsultas.setRowCount(0);
        for (Consulta c : consultas)
            modeloConsultas.addRow(new Object[]{c.getId(), c.getMedico().getNome(),
                c.getPaciente().getNome(), DateUtil.formatDateTime(c.getDataHora()),
                c.getMotivo(), c.getStatus()});
    }

    public void addGerarRelatorioListener(ActionListener l) { btnGerarRelatorio.addActionListener(l); }

    public void mostrarErro(String m) {
        JOptionPane.showMessageDialog(this, m, "Erro", JOptionPane.ERROR_MESSAGE); }
}