package view;
import controller.LoginController;
import javax.swing.*;
import java.awt.*;
/**
 * Janela principal com abas para cada módulo do sistema.
 */
public class MainView extends JFrame {
    private final JLabel lblUsuario;
    private final JTabbedPane abaPrincipal;
    private final PacienteView pacienteView;
    private final MedicoView medicoView;
    private final ConsultaView consultaView;
    private final ProntuarioView prontuarioView;
    private final RelatorioView relatorioView;
    private final JButton btnSair;

    public MainView() {
        super();
        lblUsuario = new JLabel();
        abaPrincipal = new JTabbedPane();
        pacienteView = new PacienteView();
        medicoView = new MedicoView();
        consultaView = new ConsultaView();
        prontuarioView = new ProntuarioView();
        relatorioView = new RelatorioView();
        btnSair = new JButton("Sair");
        btnSair.setBackground(new Color(180, 50, 50));
        btnSair.setForeground(Color.WHITE);
        btnSair.setFocusPainted(false);
        btnSair.setOpaque(true);
        btnSair.setBorderPainted(false);
        btnSair.setFont(new Font("Arial", Font.BOLD, 12));
        btnSair.setPreferredSize(new Dimension(90, 30));
        btnSair.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja sair?",
                "Confirmar Saída",
                JOptionPane.YES_NO_OPTION
            );
           if (confirm == JOptionPane.YES_OPTION) {

    // Fecha a janela principal
    dispose();

    // Reabre a tela de login
    SwingUtilities.invokeLater(() -> {
       LoginView login = new LoginView();

LoginController loginController = new LoginController(login);
loginController.showView();
    });
}        });
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(8, 8));

        // Painel do topo com utilizador e botão sair
        JPanel panelTopo = new JPanel(new BorderLayout());
        panelTopo.setBackground(new Color(245, 245, 245));
        panelTopo.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
        BorderFactory.createEmptyBorder(6, 10, 6, 10)
));
        panelTopo.add(lblUsuario, BorderLayout.WEST);
        panelTopo.add(btnSair, BorderLayout.EAST);

        add(panelTopo, BorderLayout.NORTH);
        abaPrincipal.addTab("Pacientes", pacienteView);
        abaPrincipal.addTab("Médicos", medicoView);
        abaPrincipal.addTab("Consultas", consultaView);
        abaPrincipal.addTab("Prontuários", prontuarioView);
        abaPrincipal.addTab("Relatórios", relatorioView);
        add(abaPrincipal, BorderLayout.CENTER);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void setUsuarioLogado(String usuario, String perfil) {
        lblUsuario.setText("  Bem-vindo, " + usuario + "   |   Perfil: " + perfil + "   |   " + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date()));
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 12));
        lblUsuario.setForeground(new Color(60, 60, 60));
    }

    public PacienteView getPacienteView() {
        return pacienteView;
    }

    public MedicoView getMedicoView() {
        return medicoView;
    }

    public ConsultaView getConsultaView() {
        return consultaView;
    }

    public ProntuarioView getProntuarioView() {
        return prontuarioView;
    }

    public RelatorioView getRelatorioView() {
        return relatorioView;
    }

    public void setPermissaoAcesso(boolean pacientes, boolean medicos, boolean consultas, boolean prontuarios) {
        pacienteView.setEditable(pacientes);
        medicoView.setEditable(medicos);
        consultaView.setEditable(consultas);
        prontuarioView.setEditable(prontuarios);
        if (!pacientes || !medicos || !consultas || !prontuarios) {
            abaPrincipal.setEnabledAt(0, pacientes);
            abaPrincipal.setEnabledAt(1, medicos);
            abaPrincipal.setEnabledAt(2, consultas);
            abaPrincipal.setEnabledAt(3, prontuarios);
        }
    }

    public JTabbedPane getTabbedPane() {
        return abaPrincipal;
    }
}