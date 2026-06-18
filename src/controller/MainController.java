package controller;

import model.Perfil;
import model.Usuario;
import view.MainView;

/**
 * Controlador principal que orquestra módulos e aplica a política de perfis.
 */
public class MainController {
    private final MainView view;
    private final Usuario usuario;

    public MainController(MainView view, Usuario usuario) {
        this.view = view;
        this.usuario = usuario;
    }

    public void inicializar() {
        view.setTitle("Sistema de Gestão de Clínica Médica");
        view.setUsuarioLogado(usuario.getLogin(), usuario.getPerfil().toString());
        carregarModulos();
        aplicarPermissoes();
    }

    private void carregarModulos() {
        new PacienteController(view.getPacienteView()).carregarLista();
        new MedicoController(view.getMedicoView()).carregarLista();
        new ConsultaController(view.getConsultaView(), view.getPacienteView(), view.getMedicoView()).carregarLista();
        new ProntuarioController(view.getProntuarioView(), view.getPacienteView()).carregarLista();
        new RelatorioController(view.getRelatorioView()).carregarRelatorios();
    }

    private void aplicarPermissoes() {
        if (usuario.getPerfil() == Perfil.RECEPCIONISTA) {
            view.setPermissaoAcesso(false, false, false, true);
        } else if (usuario.getPerfil() == Perfil.MEDICO) {
            view.setPermissaoAcesso(false, false, true, false);
        } else {  
            view.setPermissaoAcesso(true, true, true, true);
        }
    }
}
