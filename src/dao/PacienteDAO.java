package dao;

import model.Paciente;

import java.util.List;

/**
 * DAO de Paciente com consulta adicional para pacientes ativos.
 */
public interface PacienteDAO extends GenericDAO<Paciente> {
    List<Paciente> listarAtivos() throws Exception;
}
