package controller;

import dao.ConsultaDAOImpl;
import dao.PacienteDAOImpl;
import model.Consulta;
import model.Paciente;
import view.RelatorioView;

import java.util.List;

/**
 * Controlador de relatórios que reúne consultas por médico e pacientes ativos.
 */
public class RelatorioController {
    private final RelatorioView view;
    private final ConsultaDAOImpl consultaDAO;
    private final PacienteDAOImpl pacienteDAO;

    public RelatorioController(RelatorioView view) {
        this.view = view;
        this.consultaDAO = new ConsultaDAOImpl();
        this.pacienteDAO = new PacienteDAOImpl();
        this.view.addGerarRelatorioListener(e -> gerarRelatorios());
    }

    public void carregarRelatorios() {
        gerarRelatorios();
    }

    private void gerarRelatorios() {
        try {
            List<Paciente> ativos = pacienteDAO.listarAtivos();
            List<Consulta> todas = consultaDAO.listarTodos();
            view.exibirPacientesAtivos(ativos);
            view.exibirConsultasPorMedico(todas);
        } catch (Exception ex) {
            view.mostrarErro("Erro ao gerar relatórios: " + ex.getMessage());
        }
    }
}
