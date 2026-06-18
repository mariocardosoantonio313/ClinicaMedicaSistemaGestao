package controller;

import dao.MedicoDAOImpl;
import model.Medico;
import view.MedicoView;

import java.util.List;

/**
 * Controlador de Médico para operações CRUD.
 */
public class MedicoController {
    private final MedicoView view;
    private final MedicoDAOImpl dao;

    public MedicoController(MedicoView view) {
        this.view = view;
        this.dao = new MedicoDAOImpl();
        this.view.addSalvarListener(e -> salvar());
        this.view.addAtualizarListener(e -> atualizar());
        this.view.addExcluirListener(e -> excluir());
        this.view.addLimparListener(e -> limpar());
    }

    public void carregarLista() {
        try {
            List<Medico> medicos = dao.listarTodos();
            view.preencherTabela(medicos);
        } catch (Exception ex) {
            view.mostrarErro("Erro ao carregar médicos: " + ex.getMessage());
        }
    }

    private void salvar() {
        try {
            Medico medico = view.getMedicoFromForm();
            dao.salvar(medico);
            carregarLista();
            limpar();
            view.mostrarMensagem("Médico salvo com sucesso.");
        } catch (Exception ex) {
            view.mostrarErro("Erro ao salvar médico: " + ex.getMessage());
        }
    }

    private void atualizar() {
        try {
            Medico medico = view.getMedicoFromForm();
            dao.atualizar(medico);
            carregarLista();
            limpar();
            view.mostrarMensagem("Médico atualizado com sucesso.");
        } catch (Exception ex) {
            view.mostrarErro("Erro ao atualizar médico: " + ex.getMessage());
        }
    }

    private void excluir() {
        try {
            int id = view.getMedicoId();
            dao.excluir(id);
            carregarLista();
            limpar();
            view.mostrarMensagem("Médico excluído com sucesso.");
        } catch (Exception ex) {
            view.mostrarErro("Erro ao excluir médico: " + ex.getMessage());
        }
    }

    private void limpar() {
        view.limparFormulario();
    }
}
