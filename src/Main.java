import util.DBConnection;
import view.LoginView;
import controller.LoginController;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Classe principal que inicializa a aplicação de gestão de clínica médica.
 * Usa Swing para exibir a tela de login e componentes MVC para separar
 * responsabilidades entre model, view e controller.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("     SISTEMA DE GESTÃO - CLÍNICA MÉDICA");
        System.out.println("=".repeat(60) + "\n");
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.out.println("✓ Tema visual do sistema aplicado\n");
        } catch (Exception ex) {
            System.err.println("⚠ Aviso: Não foi possível aplicar tema nativo: " + ex.getMessage() + "\n");
        }

        SwingUtilities.invokeLater(() -> {
            System.out.println("Iniciando banco de dados...");
            DBConnection.initialize();
            System.out.println("Configuração: " + DBConnection.getConfigInfo() + "\n");
            
            System.out.println("Carregando interface gráfica...");
            LoginView loginView = new LoginView();
            LoginController controller = new LoginController(loginView);
            controller.showView();
            
            System.out.println("✓ Aplicação iniciada com sucesso!\n");
            System.out.println("Credenciais de teste (Modo Demo):");
            System.out.println("  • Usuário: admin       | Senha: admin");
            System.out.println("  • Usuário: medico      | Senha: medico123");
            System.out.println("  • Usuário: recepcionista | Senha: rec123\n");
            System.out.println("=".repeat(60) + "\n");
        });
    }
}
