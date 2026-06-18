
package dao;

import model.Usuario;

/**
 * DAO específico para autenticação e consulta de usuários.
 */
public interface UsuarioDAO extends GenericDAO<Usuario> {
    Usuario buscarPorLoginESenha(String login, String senha) throws Exception;
}
