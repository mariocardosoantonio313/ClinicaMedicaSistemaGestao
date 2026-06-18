
package controller;

import dao.PacienteDAOImpl;
import model.Paciente;
import view.PacienteView;

import java.util.List;

/**
 * Controlador de Paciente para CRUD e listagem.
 */
public class PacienteController {

    private final PacienteView view;
    private final PacienteDAOImpl dao;

    public PacienteController(PacienteView view) {
        this.view = view;
        this.dao = new PacienteDAOImpl();

        this.view.addSalvarListener(e -> salvar());
        this.view.addAtualizarListener(e -> atualizar());
        this.view.addExcluirListener(e -> excluir());
        this.view.addLimparListener(e -> limpar());

        // NOVO
        this.view.addPesquisarListener(e -> pesquisar());
    }

    public void carregarLista() {
        try {
            List<Paciente> pacientes = dao.listarTodos();
            view.preencherTabela(pacientes);
        } catch (Exception ex) {
            view.mostrarErro("Erro ao carregar pacientes: " + ex.getMessage());
        }
    }

    private void salvar() {
        try {
            Paciente paciente = view.getPacienteFromForm();

            dao.salvar(paciente);

            carregarLista();
            limpar();

            view.mostrarMensagem("Paciente salvo com sucesso.");

        } catch (Exception ex) {
            view.mostrarErro("Erro ao salvar paciente: " + ex.getMessage());
        }
    }

    private void atualizar() {
        try {
            Paciente paciente = view.getPacienteFromForm();

            dao.atualizar(paciente);

            carregarLista();
            limpar();

            view.mostrarMensagem("Paciente atualizado com sucesso.");

        } catch (Exception ex) {
            view.mostrarErro("Erro ao atualizar paciente: " + ex.getMessage());
        }
    }

    private void excluir() {
        try {

            int id = view.getPacienteId();

            dao.excluir(id);

            carregarLista();
            limpar();

            view.mostrarMensagem("Paciente excluído com sucesso.");

        } catch (Exception ex) {
            view.mostrarErro("Erro ao excluir paciente: " + ex.getMessage());
        }
    }

    /**
     * Pesquisa por nome ou CPF.
     */
    private void pesquisar() {

        try {

            String texto = view.getTextoPesquisa();

            if (texto == null || texto.trim().isEmpty()) {
                carregarLista();
                return;
            }

            List<Paciente> resultado = dao.pesquisar(texto);

            view.preencherTabela(resultado);

            if (resultado.isEmpty()) {
                view.mostrarMensagem(
                        "Nenhum paciente encontrado para: " + texto);
            }

        } catch (Exception ex) {
            view.mostrarErro(
                    "Erro na pesquisa: " + ex.getMessage());
        }
    }

    private void limpar() {
        view.limparFormulario();
    }
}