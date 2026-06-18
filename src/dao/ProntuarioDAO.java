package dao;

import model.Prontuario;

/**
 * DAO para armazenamento e recuperação de prontuários.
 */
public interface ProntuarioDAO extends GenericDAO<Prontuario> {
    Prontuario buscarPorPaciente(int pacienteId) throws Exception;
}
