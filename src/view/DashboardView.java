package view;

import model.Usuario;
import util.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Tela principal do sistema da clínica médica
 */
public class DashboardView extends JFrame {

    private final JLabel welcomeLabel;
    private final JLabel statusLabel;

    public DashboardView() {
        this(null);
    }

    public DashboardView(Usuario usuario) {
        super("Sistema Clínica Médica");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 520);
        setLocationRelativeTo(null);
        setResizable(true);

        // Menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Arquivo");
        JMenuItem exitItem = new JMenuItem("Sair");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Toolbar simples
        JToolBar toolBar = new JToolBar();
        JButton btnPacientes = new JButton("Pacientes");
        JButton btnMedicos = new JButton("Médicos");
        JButton btnConsultas = new JButton("Consultas");
        toolBar.add(btnPacientes);
        toolBar.add(btnMedicos);
        toolBar.add(btnConsultas);

        // Painel central
        JPanel center = new JPanel(new BorderLayout());
        welcomeLabel = new JLabel("Bem-vindo ao sistema da clínica!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        center.add(welcomeLabel, BorderLayout.CENTER);

        // Status bar
        statusLabel = new JLabel();
        statusLabel.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        // Montagem
        JPanel root = new JPanel(new BorderLayout());
        root.add(toolBar, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);
        root.add(statusLabel, BorderLayout.SOUTH);

        setContentPane(root);

        // Atualiza com dados do usuário e estado do BD
        if (usuario != null) {
            String perfilText = usuario.getPerfil() != null ? usuario.getPerfil().toString() : "";
            welcomeLabel.setText(String.format("Bem-vindo, %s (%s)", usuario.getLogin(), perfilText));
        }

        // Mostrar estado do sistema de forma amigável
        if (DBConnection.isOnline()) {
            statusLabel.setText("Conectado ao banco de dados.");
        } else if (!DBConnection.isDriverAvailable()) {
            statusLabel.setText("Modo demonstração ativo (nenhum driver JDBC detectado). Dados em memória.");
        } else {
            statusLabel.setText("Modo demonstração ativo (sem conexão). Dados em memória.");
        }

        // Ações de toolbar (placeholders)
        ActionListener placeholder = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(DashboardView.this, "Funcionalidade em desenvolvimento.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        btnPacientes.addActionListener(placeholder);
        btnMedicos.addActionListener(placeholder);
        btnConsultas.addActionListener(placeholder);
    }
}