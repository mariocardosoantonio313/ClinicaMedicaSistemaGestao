package controller;

import dao.ConsultaDAOImpl;
import dao.MedicoDAOImpl;
import dao.PacienteDAOImpl;
import model.Consulta;
import model.Medico;
import model.Paciente;
import view.ConsultaView;
import view.MedicoView;
import view.PacienteView;

import java.util.List;

/**
 * Controlador responsável pela gestão das consultas.
 * Realiza operações CRUD e carregamento dos dados da interface.
 */
public class ConsultaController {

    private final ConsultaView view;
    private final ConsultaDAOImpl dao;

    public ConsultaController(
            ConsultaView view,
            PacienteView pacienteView,
            MedicoView medicoView) {

        this.view = view;
        this.dao = new ConsultaDAOImpl();

        inicializarEventos();
    }

    private void inicializarEventos() {

        view.addSalvarListener(e -> salvar());
        view.addAtualizarListener(e -> atualizar());
        view.addExcluirListener(e -> excluir());
        view.addLimparListener(e -> limpar());
    }

    /**
     * Carrega os dados da tela.
     */
    public void carregarLista() {

        try {

            prepararCombos();

            List<Consulta> consultas = dao.listarTodos();

            view.preencherTabela(consultas);

        } catch (Exception ex) {

            view.mostrarErro(
                    "Erro ao carregar consultas:\n" + ex.getMessage()
            );
        }
    }

    /**
     * Carrega pacientes e médicos nos combos.
     */
    private void prepararCombos() {

        try {

            List<Paciente> pacientes =
                    new PacienteDAOImpl().listarTodos();

            List<Medico> medicos =
                    new MedicoDAOImpl().listarTodos();

            view.preencherComboPacientes(pacientes);
            view.preencherComboMedicos(medicos);

        } catch (Exception ex) {

            view.mostrarErro(
                    "Erro ao carregar pacientes e médicos:\n"
                            + ex.getMessage()
            );
        }
    }

    /**
     * Salva uma nova consulta.
     */
    private void salvar() {

        try {

            Consulta consulta = view.getConsultaFromForm();

            if (consulta == null) {
                throw new Exception("Dados da consulta inválidos.");
            }

            dao.salvar(consulta);

            carregarLista();
            limpar();

            view.mostrarMensagem(
                    "Consulta agendada com sucesso."
            );

        } catch (Exception ex) {

            view.mostrarErro(
                    "Erro ao agendar consulta:\n"
                            + ex.getMessage()
            );
        }
    }

    /**
     * Atualiza uma consulta existente.
     */
    private void atualizar() {

        try {

            Consulta consulta = view.getConsultaFromForm();

            if (consulta == null) {
                throw new Exception("Dados da consulta inválidos.");
            }

            if (consulta.getId() <= 0) {
                throw new Exception(
                        "Selecione uma consulta válida."
                );
            }

            dao.atualizar(consulta);

            carregarLista();
            limpar();

            view.mostrarMensagem(
                    "Consulta atualizada com sucesso."
            );

        } catch (Exception ex) {

            view.mostrarErro(
                    "Erro ao atualizar consulta:\n"
                            + ex.getMessage()
            );
        }
    }

    /**
     * Exclui uma consulta.
     */
    private void excluir() {

        try {

            int id = view.getConsultaId();

            if (id <= 0) {
                throw new Exception(
                        "Selecione uma consulta para excluir."
                );
            }

            int resposta = javax.swing.JOptionPane.showConfirmDialog(
                    view,
                    "Deseja realmente excluir esta consulta?",
                    "Confirmação",
                    javax.swing.JOptionPane.YES_NO_OPTION
            );

            if (resposta != javax.swing.JOptionPane.YES_OPTION) {
                return;
            }

            dao.excluir(id);

            carregarLista();
            limpar();

            view.mostrarMensagem(
                    "Consulta excluída com sucesso."
            );

        } catch (Exception ex) {

            view.mostrarErro(
                    "Erro ao excluir consulta:\n"
                            + ex.getMessage()
            );
        }
    }

    /**
     * Limpa os campos do formulário.
     */
    private void limpar() {

        view.limparFormulario();
    }

    /**
     * Recarrega todos os dados da tela.
     */
    public void atualizarTela() {

        carregarLista();
    }
}