package view;

import util.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {

    private final JTextField txtLogin;
    private final JPasswordField txtSenha;
    private final JButton btnLogin;
    private final JCheckBox chkRemember;
    private final JLabel lblStatus;

    // Cores
    private static final Color COR_FUNDO = new Color(52, 73, 94);
    private static final Color COR_PAINEL = new Color(255, 255, 255);
    private static final Color COR_TITULO = new Color(44, 62, 80);
    private static final Color COR_BOTAO = new Color(41, 128, 185);
    private static final Color COR_LABEL = new Color(44, 62, 80);

    public LoginView() {
        super("Login - Clinica Medica");

        txtLogin = new JTextField(20);
        txtSenha = new JPasswordField(20);
        chkRemember = new JCheckBox("Lembrar-me");
        lblStatus = new JLabel();

        // Botao
        btnLogin = new JButton("Entrar");
        btnLogin.setBackground(COR_BOTAO);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setOpaque(true);
        btnLogin.setBorderPainted(false);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setPreferredSize(new Dimension(200, 40));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Estilo dos campos
        txtLogin.setFont(new Font("Arial", Font.PLAIN, 13));
        txtLogin.setPreferredSize(new Dimension(220, 32));
        txtLogin.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        txtSenha.setFont(new Font("Arial", Font.PLAIN, 13));
        txtSenha.setPreferredSize(new Dimension(220, 32));
        txtSenha.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        // Painel branco central
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(COR_PAINEL);
        painel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titulo do sistema
        JLabel lblNome = new JLabel("CLÍNICA SÃO JOSÉ ");
        lblNome.setFont(new Font("Arial", Font.BOLD, 22));
        lblNome.setForeground(COR_TITULO);
        lblNome.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        painel.add(lblNome, gbc);

        // Subtitulo
        JLabel lblSub = new JLabel("Sistema de Gestão");
        lblSub.setFont(new Font("Arial", Font.PLAIN, 13));
        lblSub.setForeground(new Color(127, 140, 141));
        lblSub.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        painel.add(lblSub, gbc);

        // Separador
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(189, 195, 199));
        gbc.gridy = 2;
        gbc.insets = new Insets(4, 0, 16, 0);
        painel.add(sep, gbc);

        // Label Usuario
        gbc.gridwidth = 1;
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(8, 8, 8, 8);
        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setFont(new Font("Arial", Font.BOLD, 13));
        lblUser.setForeground(COR_LABEL);
        painel.add(lblUser, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        painel.add(txtLogin, gbc);

        // Label Senha
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Arial", Font.BOLD, 13));
        lblSenha.setForeground(COR_LABEL);
        painel.add(lblSenha, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        painel.add(txtSenha, gbc);

        // Lembrar-me
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        chkRemember.setBackground(COR_PAINEL);
        chkRemember.setFont(new Font("Arial", Font.PLAIN, 12));
        painel.add(chkRemember, gbc);

        // Botao Entrar
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(16, 8, 8, 8);
        painel.add(btnLogin, gbc);

        // Status
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 11));
        lblStatus.setForeground(Color.WHITE);
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatus.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        lblStatus.setText("");

        // Fundo azul escuro
        JPanel fundoEscuro = new JPanel(new GridBagLayout());
        fundoEscuro.setBackground(COR_FUNDO);
        fundoEscuro.add(painel);

        // Status em baixo
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(COR_FUNDO);
        statusPanel.setVisible(false);
        statusPanel.add(lblStatus, BorderLayout.CENTER);
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(COR_FUNDO);
        root.add(fundoEscuro, BorderLayout.CENTER);
        root.add(statusPanel, BorderLayout.SOUTH);

        setContentPane(root);
        setSize(480, 380);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(btnLogin);
    }

    public String getLogin() {
        return txtLogin.getText() != null ? txtLogin.getText().trim() : "";
    }

    public String getSenha() {
        return txtSenha.getPassword() != null ? new String(txtSenha.getPassword()) : "";
    }

    public void addLoginListener(ActionListener listener) {
        if (listener != null) btnLogin.addActionListener(listener);
    }

    public void setStatus(String status) {
        if (lblStatus != null) lblStatus.setText(status);
    }

    public void limparCampos() {
        txtLogin.setText("");
        txtSenha.setText("");
    }
}