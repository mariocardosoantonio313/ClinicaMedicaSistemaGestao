package dao;

import java.util.List;

/**
 * Interface genérica para operações CRUD em entidades.
 * @param <T> tipo da entidade.
 */
public interface GenericDAO<T> {
    void salvar(T entidade) throws Exception;
    void atualizar(T entidade) throws Exception;
    void excluir(int id) throws Exception;
    T buscarPorId(int id) throws Exception;
    List<T> listarTodos() throws Exception;
}
