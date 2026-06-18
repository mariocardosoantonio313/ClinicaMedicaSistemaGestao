package dao;

import model.Paciente;
import model.Prontuario;
import util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação JDBC do DAO de prontuários com suporte a modo demo/offline.
 */
public class ProntuarioDAOImpl implements ProntuarioDAO {
    private static final String SQL_INSERT = "INSERT INTO prontuario (paciente_id, descricao, data_atualizacao) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE prontuario SET paciente_id = ?, descricao = ?, data_atualizacao = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM prontuario WHERE id = ?";
    private static final String SQL_SELECT_BY_ID = "SELECT id, paciente_id, descricao, data_atualizacao FROM prontuario WHERE id = ?";
    private static final String SQL_SELECT_BY_PACIENTE = "SELECT id, paciente_id, descricao, data_atualizacao FROM prontuario WHERE paciente_id = ?";
    private static final String SQL_SELECT_ALL = "SELECT id, paciente_id, descricao, data_atualizacao FROM prontuario";

    @Override
    public void salvar(Prontuario prontuario) throws Exception {
        if (!DBConnection.isOnline()) {
            throw new Exception("Operação indisponível em modo demo.");
        }
        prontuario.validar();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setInt(1, prontuario.getPaciente().getId());
            ps.setString(2, prontuario.getDescricao());
            ps.setDate(3, new Date(prontuario.getDataAtualizacao().getTime()));
            ps.executeUpdate();
        }
    }

    @Override
    public void atualizar(Prontuario prontuario) throws Exception {
        if (!DBConnection.isOnline()) {
            throw new Exception("Operação indisponível em modo demo.");
        }
        prontuario.validar();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setInt(1, prontuario.getPaciente().getId());
            ps.setString(2, prontuario.getDescricao());
            ps.setDate(3, new Date(prontuario.getDataAtualizacao().getTime()));
            ps.setInt(4, prontuario.getId());
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
    public Prontuario buscarPorId(int id) throws Exception {
        if (!DBConnection.isOnline()) {
            return buscarPorIdDemo(id);
        }
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapProntuario(rs) : null;
            }
        }
    }

    @Override
    public Prontuario buscarPorPaciente(int pacienteId) throws Exception {
        if (!DBConnection.isOnline()) {
            return buscarPorPacienteDemo(pacienteId);
        }
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_PACIENTE)) {
            ps.setInt(1, pacienteId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapProntuario(rs) : null;
            }
        }
    }

    @Override
    public List<Prontuario> listarTodos() throws Exception {
        if (!DBConnection.isOnline()) {
            return listarTodosDemo();
        }
        List<Prontuario> prontuarios = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                prontuarios.add(mapProntuario(rs));
            }
        }
        return prontuarios;
    }

    private Prontuario mapProntuario(ResultSet rs) throws Exception {
        Prontuario prontuario = new Prontuario();
        prontuario.setId(rs.getInt("id"));
        prontuario.setPaciente(buscarPaciente(rs.getInt("paciente_id")));
        prontuario.setDescricao(rs.getString("descricao"));
        prontuario.setDataAtualizacao(rs.getDate("data_atualizacao"));
        return prontuario;
    }

    private Paciente buscarPaciente(int id) throws Exception {
        return new PacienteDAOImpl().buscarPorId(id);
    }
    
    // ===== MÉTODOS DEMO/OFFLINE =====
    
    private Prontuario buscarPorIdDemo(int id) {
        return listarTodosDemo().stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }
    
    private Prontuario buscarPorPacienteDemo(int pacienteId) {
        return listarTodosDemo().stream().filter(p -> p.getPaciente().getId() == pacienteId).findFirst().orElse(null);
    }
    
    private List<Prontuario> listarTodosDemo() {
        List<Prontuario> prontuarios = new ArrayList<>();
        try {
            Paciente p1 = new PacienteDAOImpl().buscarPorId(1);
            if (p1 != null) {
                Prontuario pron1 = new Prontuario();
                pron1.setId(1);
                pron1.setPaciente(p1);
                pron1.setDescricao("Alergías: Pennicilina");
                pron1.setDataAtualizacao(new java.util.Date());
                prontuarios.add(pron1);
            }
        } catch (Exception ignored) {}
        return prontuarios;
    }
}
