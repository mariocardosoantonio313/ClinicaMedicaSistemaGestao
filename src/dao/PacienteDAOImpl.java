package dao;

import model.Paciente;
import util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação JDBC do DAO de pacientes com suporte a modo demo/offline.
 */
public class PacienteDAOImpl implements PacienteDAO {

    private static final String SQL_INSERT =
            "INSERT INTO paciente (nome, cpf, telefone, data_nascimento, endereco, ativo) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE =
            "UPDATE paciente SET nome=?, cpf=?, telefone=?, data_nascimento=?, endereco=?, ativo=? WHERE id=?";

    private static final String SQL_DELETE =
            "DELETE FROM paciente WHERE id=?";

    private static final String SQL_SELECT_BY_ID =
            "SELECT id, nome, cpf, telefone, data_nascimento, endereco, ativo FROM paciente WHERE id=?";

    private static final String SQL_SELECT_ALL =
            "SELECT id, nome, cpf, telefone, data_nascimento, endereco, ativo FROM paciente";

    private static final String SQL_SELECT_ACTIVE =
            "SELECT id, nome, cpf, telefone, data_nascimento, endereco, ativo FROM paciente WHERE ativo=TRUE";

    private static final String SQL_PESQUISAR =
            "SELECT id, nome, cpf, telefone, data_nascimento, endereco, ativo " +
            "FROM paciente WHERE nome LIKE ? OR cpf LIKE ?";

    @Override
    public void salvar(Paciente paciente) throws Exception {

        if (!DBConnection.isOnline()) {
            throw new Exception("Operação indisponível em modo demo.");
        }

        paciente.validar();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {

            ps.setString(1, paciente.getNome());
            ps.setString(2, paciente.getCpf());
            ps.setString(3, paciente.getTelefone());
            ps.setDate(4, new Date(paciente.getDataNascimento().getTime()));
            ps.setString(5, paciente.getEndereco());
            ps.setBoolean(6, paciente.isAtivo());

            ps.executeUpdate();
        }
    }

    @Override
    public void atualizar(Paciente paciente) throws Exception {

        if (!DBConnection.isOnline()) {
            throw new Exception("Operação indisponível em modo demo.");
        }

        paciente.validar();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, paciente.getNome());
            ps.setString(2, paciente.getCpf());
            ps.setString(3, paciente.getTelefone());
            ps.setDate(4, new Date(paciente.getDataNascimento().getTime()));
            ps.setString(5, paciente.getEndereco());
            ps.setBoolean(6, paciente.isAtivo());
            ps.setInt(7, paciente.getId());

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
    public Paciente buscarPorId(int id) throws Exception {

        if (!DBConnection.isOnline()) {
            return listarTodosDemo()
                    .stream()
                    .filter(p -> p.getId() == id)
                    .findFirst()
                    .orElse(null);
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapPaciente(rs) : null;
            }
        }
    }

    @Override
    public List<Paciente> listarTodos() throws Exception {

        List<Paciente> pacientes = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                pacientes.add(mapPaciente(rs));
            }
        }

        return pacientes;
    }

    @Override
    public List<Paciente> listarAtivos() throws Exception {

        if (!DBConnection.isOnline()) {
            return listarTodosDemo()
                    .stream()
                    .filter(Paciente::isAtivo)
                    .toList();
        }

        List<Paciente> pacientes = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ACTIVE);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                pacientes.add(mapPaciente(rs));
            }
        }

        return pacientes;
    }

    /**
     * Pesquisa por nome ou CPF
     */
    public List<Paciente> pesquisar(String texto) throws Exception {

        if (!DBConnection.isOnline()) {

            String filtro = texto.toLowerCase();

            return listarTodosDemo()
                    .stream()
                    .filter(p ->
                            p.getNome().toLowerCase().contains(filtro)
                            || p.getCpf().contains(filtro))
                    .toList();
        }

        List<Paciente> lista = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_PESQUISAR)) {

            String like = "%" + texto + "%";

            ps.setString(1, like);
            ps.setString(2, like);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(mapPaciente(rs));
                }
            }
        }

        return lista;
    }

    private Paciente mapPaciente(ResultSet rs) throws Exception {

        Paciente paciente = new Paciente();

        paciente.setId(rs.getInt("id"));
        paciente.setNome(rs.getString("nome"));
        paciente.setCpf(rs.getString("cpf"));
        paciente.setTelefone(rs.getString("telefone"));
        paciente.setDataNascimento(rs.getDate("data_nascimento"));
        paciente.setEndereco(rs.getString("endereco"));
        paciente.setAtivo(rs.getBoolean("ativo"));

        return paciente;
    }

    // ===== MÉTODOS DEMO/OFFLINE =====

    private List<Paciente> listarTodosDemo() {

        List<Paciente> pacientes = new ArrayList<>();

        Paciente p1 = new Paciente();
        p1.setId(1);
        p1.setNome("João Silva");
        p1.setCpf("123.456.789-00");
        p1.setTelefone("11-99999-0001");
        p1.setEndereco("Rua A");
        p1.setDataNascimento(new java.util.Date());
        p1.setAtivo(true);

        Paciente p2 = new Paciente();
        p2.setId(2);
        p2.setNome("Maria Santos");
        p2.setCpf("234.567.890-00");
        p2.setTelefone("11-99999-0002");
        p2.setEndereco("Rua B");
        p2.setDataNascimento(new java.util.Date());
        p2.setAtivo(true);

        Paciente p3 = new Paciente();
        p3.setId(3);
        p3.setNome("Pedro Costa");
        p3.setCpf("345.678.901-00");
        p3.setTelefone("11-99999-0003");
        p3.setEndereco("Rua C");
        p3.setDataNascimento(new java.util.Date());
        p3.setAtivo(false);

        pacientes.add(p1);
        pacientes.add(p2);
        pacientes.add(p3);

        return pacientes;
    }
}