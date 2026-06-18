package dao;

import model.Consulta;

import java.util.List;

/**
 * DAO para operações de consulta médica e relatórios.
 */
public interface ConsultaDAO extends GenericDAO<Consulta> {
    List<Consulta> listarPorMedico(int medicoId) throws Exception;
}
