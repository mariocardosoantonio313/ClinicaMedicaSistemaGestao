package dao;

import model.Perfil;
import model.Usuario;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação JDBC do DAO de usuários com suporte a modo demo/offline.
 */
public class UsuarioDAOImpl implements UsuarioDAO {
    private static final String SQL_INSERT = "INSERT INTO usuario (login, senha, perfil) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE usuario SET login = ?, senha = ?, perfil = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM usuario WHERE id = ?";
    private static final String SQL_SELECT_BY_ID = "SELECT id, login, senha, perfil FROM usuario WHERE id = ?";
    private static final String SQL_SELECT_ALL = "SELECT id, login, senha, perfil FROM usuario";
    private static final String SQL_AUTH = "SELECT id, login, senha, perfil FROM usuario WHERE login = ? AND senha = ?";

    @Override
    public void salvar(Usuario usuario) throws Exception {
        if (!DBConnection.isOnline()) {
            throw new Exception("Operação indisponível em modo demo.");
        }
        usuario.validar();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setString(1, usuario.getLogin());
            ps.setString(2, usuario.getSenha());
            ps.setString(3, usuario.getPerfil().name());
            ps.executeUpdate();
        }
    }

    @Override
    public void atualizar(Usuario usuario) throws Exception {
        if (!DBConnection.isOnline()) {
            throw new Exception("Operação indisponível em modo demo.");
        }
        usuario.validar();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, usuario.getLogin());
            ps.setString(2, usuario.getSenha());
            ps.setString(3, usuario.getPerfil().name());
            ps.setInt(4, usuario.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void excluir(int id) throws Exception {
        if (!DBConnection.isOnline()) {
            throw new Exception("Operação indisponível em modo demo.");
        }
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Usuario buscarPorId(int id) throws Exception {
        if (!DBConnection.isOnline()) {
            return buscarPorIdDemo(id);
        }
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapUsuario(rs) : null;
            }
        }
    }

    @Override
    public List<Usuario> listarTodos() throws Exception {
        if (!DBConnection.isOnline()) {
            return listarTodosDemo();
        }
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                usuarios.add(mapUsuario(rs));
            }
        }
        return usuarios;
    }

    @Override
    public Usuario buscarPorLoginESenha(String login, String senha) throws Exception {
        if (!DBConnection.isOnline()) {
            return buscarPorLoginESenhaDemo(login, senha);
        }
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_AUTH)) {
            ps.setString(1, login);
            ps.setString(2, senha);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapUsuario(rs) : null;
            }
        }
    }

    private Usuario mapUsuario(ResultSet rs) throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setLogin(rs.getString("login"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setPerfil(Perfil.valueOf(rs.getString("perfil")));
        return usuario;
    }
    
    // ===== MÉTODOS DEMO/OFFLINE =====
    
    private Usuario buscarPorIdDemo(int id) {
        List<Usuario> usuarios = listarTodosDemo();
        return usuarios.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }
    
    private List<Usuario> listarTodosDemo() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Usuario(1, "admin", "admin", Perfil.ADMINISTRADOR));
        usuarios.add(new Usuario(2, "medico", "medico123", Perfil.MEDICO));
        usuarios.add(new Usuario(3, "recepcionista", "rec123", Perfil.RECEPCIONISTA));
        return usuarios;
    }
    
    private Usuario buscarPorLoginESenhaDemo(String login, String senha) {
        return listarTodosDemo().stream()
            .filter(u -> u.getLogin().equals(login) && u.getSenha().equals(senha))
            .findFirst()
            .orElse(null);
    }
}
