package controller;

import dao.UsuarioDAOImpl;
import model.Usuario;
import view.LoginView;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import view.MainView;

/**
 * Controlador da tela de login.
 */
public class LoginController {

    private final LoginView view;
    private final UsuarioDAOImpl usuarioDAO;

    public LoginController(LoginView view) {
        this.view = view;
        this.usuarioDAO = new UsuarioDAOImpl();

        this.view.addLoginListener(e -> autenticar());
        
        // Garantir que a janela fecha completamente ao clicar no X
        this.view.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
    }

    public void showView() {
        // Garantir que a janela aparece no centro do ecrã
        SwingUtilities.invokeLater(() -> {
            view.setLocationRelativeTo(null);
            view.setVisible(true);
            view.toFront();  // Traz a janela para a frente
            view.requestFocus();  // Dá foco à janela
            System.out.println("DEBUG: Tela de login deveria estar visível agora!");
        });
    }

    private void autenticar() {
        try {
            String login = view.getLogin();
            String senha = view.getSenha();

            // validação básica
            if (login.isEmpty() || senha.isEmpty()) {
                mostrarErro("Por favor, preencha usuário e senha.");
                return;
            }

            System.out.println("Autenticando usuário: " + login);
            Usuario usuario = usuarioDAO.buscarPorLoginESenha(login, senha);

            if (usuario == null) {
                System.err.println("Falha na autenticação: usuário ou senha incorretos.");
                mostrarErro("Usuário ou senha incorretos.");
                return;
            }

            System.out.println("Login efetuado com sucesso: " + usuario.getLogin() + " (" + usuario.getPerfil() + ")");
            abrirTelaPrincipal(usuario);

        } catch (Exception ex) {
            System.err.println("Erro ao processar o login: " + ex.getMessage());
            mostrarErro("Erro ao processar o login: " + ex.getMessage());
        }
    }

    private void mostrarErro(String mensagem) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                    view,
                    mensagem,
                    "Erro de Autenticação",
                    JOptionPane.ERROR_MESSAGE
            );
        });
    }

    private void abrirTelaPrincipal(Usuario usuario) {
    try {
        System.out.println("DEBUG: Iniciando abrirTelaPrincipal para usuario: " + usuario.getLogin());
        
        view.setVisible(false);
        view.dispose();
        System.out.println("DEBUG: LoginView ocultada e descartada");
        
        SwingUtilities.invokeLater(() -> {
            try {
                // ✅ Abre directamente a MainView com todas as abas
                MainView mainView = new MainView();
                MainController controller = new MainController(mainView, usuario);
                controller.inicializar();
                mainView.setLocationRelativeTo(null);
                mainView.setVisible(true);
                System.out.println("DEBUG: MainView visível - sucesso!");
            } catch (Exception ex) {
                System.err.println("DEBUG: Erro ao criar MainView: " + ex.getMessage());
                mostrarErro("Erro ao abrir o sistema: " + ex.getMessage());
                showView();
            }
        });
        
    } catch (Exception ex) {
        System.err.println("DEBUG: Erro ao abrir MainView: " + ex.getMessage());
        mostrarErro("Erro ao abrir o sistema: " + ex.getMessage());
        showView();
    }
}
    }
