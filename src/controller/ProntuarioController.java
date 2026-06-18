package controller;

import dao.PacienteDAOImpl;
import dao.ProntuarioDAOImpl;
import model.Paciente;
import model.Prontuario;
import view.PacienteView;
import view.ProntuarioView;

import java.util.List;

/**
 * Controlador de Prontuário para registros médicos.
 */
public class ProntuarioController {
    private final ProntuarioView view;
    private final ProntuarioDAOImpl dao;

    public ProntuarioController(ProntuarioView view, PacienteView pacienteView) {
        this.view = view;
        this.dao = new ProntuarioDAOImpl();
        this.view.addSalvarListener(e -> salvar());
        this.view.addAtualizarListener(e -> atualizar());
        this.view.addExcluirListener(e -> excluir());
        this.view.addLimparListener(e -> limpar());
        preencherPacientes();
    }

    public void carregarLista() {
        try {
            List<Prontuario> prontuarios = dao.listarTodos();
            view.preencherTabela(prontuarios);
        } catch (Exception ex) {
            view.mostrarErro("Erro ao carregar prontuários: " + ex.getMessage());
        }
    }

    private void preencherPacientes() {
        try {
            List<Paciente> pacientes = new PacienteDAOImpl().listarTodos();
            view.preencherComboPacientes(pacientes);
        } catch (Exception ex) {
            view.mostrarErro("Erro ao carregar pacientes: " + ex.getMessage());
        }
    }

    private void salvar() {
        try {
            Prontuario prontuario = view.getProntuarioFromForm();
            dao.salvar(prontuario);
            carregarLista();
            limpar();
            view.mostrarMensagem("Prontuário salvo com sucesso.");
        } catch (Exception ex) {
            view.mostrarErro("Erro ao salvar prontuário: " + ex.getMessage());
        }
    }

    private void atualizar() {
        try {
            Prontuario prontuario = view.getProntuarioFromForm();
            dao.atualizar(prontuario);
            carregarLista();
            limpar();
            view.mostrarMensagem("Prontuário atualizado com sucesso.");
        } catch (Exception ex) {
            view.mostrarErro("Erro ao atualizar prontuário: " + ex.getMessage());
        }
    }

    private void excluir() {
        try {
            int id = view.getProntuarioId();
            dao.excluir(id);
            carregarLista();
            limpar();
            view.mostrarMensagem("Prontuário excluído com sucesso.");
        } catch (Exception ex) {
            view.mostrarErro("Erro ao excluir prontuário: " + ex.getMessage());
        }
    }

    private void limpar() {
        view.limparFormulario();
    }
}
