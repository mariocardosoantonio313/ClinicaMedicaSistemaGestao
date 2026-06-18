package dao;

import model.Medico;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação JDBC do DAO de médicos com suporte a modo demo/offline.
 */
public class MedicoDAOImpl implements MedicoDAO {

    private static final String SQL_INSERT =
            "INSERT INTO medico (nome, cpf, telefone, especialidade, crm) VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE =
            "UPDATE medico SET nome = ?, cpf = ?, telefone = ?, especialidade = ?, crm = ? WHERE id = ?";

    private static final String SQL_DELETE =
            "DELETE FROM medico WHERE id = ?";

    private static final String SQL_SELECT_BY_ID =
            "SELECT id, nome, cpf, telefone, especialidade, crm FROM medico WHERE id = ?";

    private static final String SQL_SELECT_ALL =
            "SELECT id, nome, cpf, telefone, especialidade, crm FROM medico";

    private static final String SQL_PESQUISAR =
            "SELECT id, nome, cpf, telefone, especialidade, crm " +
            "FROM medico " +
            "WHERE nome LIKE ? OR especialidade LIKE ?";

    @Override
    public void salvar(Medico medico) throws Exception {

        if (!DBConnection.isOnline()) {
            throw new Exception("Operação indisponível em modo demo.");
        }

        medico.validar();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {

            ps.setString(1, medico.getNome());
            ps.setString(2, medico.getCpf());
            ps.setString(3, medico.getTelefone());
            ps.setString(4, medico.getEspecialidade());
            ps.setString(5, medico.getCrm());

            ps.executeUpdate();
        }
    }

    @Override
    public void atualizar(Medico medico) throws Exception {

        if (!DBConnection.isOnline()) {
            throw new Exception("Operação indisponível em modo demo.");
        }

        medico.validar();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, medico.getNome());
            ps.setString(2, medico.getCpf());
            ps.setString(3, medico.getTelefone());
            ps.setString(4, medico.getEspecialidade());
            ps.setString(5, medico.getCrm());
            ps.setInt(6, medico.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public void excluir(int id) throws Exception {

        if (!DBConnection.isOnline()) {
            throw new Exception("Operação indisponível em modo demo.");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Medico buscarPorId(int id) throws Exception {

        if (!DBConnection.isOnline()) {
            return buscarPorIdDemo(id);
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapMedico(rs) : null;
            }
        }
    }

    @Override
    public List<Medico> listarTodos() throws Exception {

        if (!DBConnection.isOnline()) {
            return listarTodosDemo();
        }

        List<Medico> medicos = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                medicos.add(mapMedico(rs));
            }
        }

        return medicos;
    }

    /**
     * Pesquisa por nome ou especialidade.
     */
    public List<Medico> pesquisar(String texto) throws Exception {

        if (!DBConnection.isOnline()) {

            String filtro = texto.toLowerCase();

            return listarTodosDemo()
                    .stream()
                    .filter(m ->
                            m.getNome().toLowerCase().contains(filtro)
                            || m.getEspecialidade().toLowerCase().contains(filtro))
                    .toList();
        }

        List<Medico> lista = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_PESQUISAR)) {

            String like = "%" + texto + "%";

            ps.setString(1, like);
            ps.setString(2, like);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(mapMedico(rs));
                }
            }
        }

        return lista;
    }

    private Medico mapMedico(ResultSet rs) throws Exception {

        Medico medico = new Medico();

        medico.setId(rs.getInt("id"));
        medico.setNome(rs.getString("nome"));
        medico.setCpf(rs.getString("cpf"));
        medico.setTelefone(rs.getString("telefone"));
        medico.setEspecialidade(rs.getString("especialidade"));
        medico.setCrm(rs.getString("crm"));

        return medico;
    }

    // ===== MÉTODOS DEMO/OFFLINE =====

    private Medico buscarPorIdDemo(int id) {
        return listarTodosDemo()
                .stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private List<Medico> listarTodosDemo() {

        List<Medico> medicos = new ArrayList<>();

        medicos.add(new Medico(
                1,
                "Dr. Carlos Lima",
                "111.222.333-44",
                "11-98765-0001",
                "Cardiologia",
                "CRM123456"));

        medicos.add(new Medico(
                2,
                "Dra. Ana Santos",
                "222.333.444-55",
                "11-98765-0002",
                "Ortopedia",
                "CRM654321"));

        medicos.add(new Medico(
                3,
                "Dr. Rafael Costa",
                "333.444.555-66",
                "11-98765-0003",
                "Neurocirurgia",
                "CRM789456"));

        return medicos;
    }
}