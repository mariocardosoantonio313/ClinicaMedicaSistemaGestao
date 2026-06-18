package dao;

import model.Consulta;
import model.Medico;
import model.Paciente;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementação JDBC do DAO de Consultas.
 * Compatível com modo Online e Demo.
 */
public class ConsultaDAOImpl implements ConsultaDAO {

    private static final String SQL_INSERT =
            "INSERT INTO consulta (paciente_id, medico_id, data_hora, motivo, status) VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE =
            "UPDATE consulta SET paciente_id = ?, medico_id = ?, data_hora = ?, motivo = ?, status = ? WHERE id = ?";

    private static final String SQL_DELETE =
            "DELETE FROM consulta WHERE id = ?";

    private static final String SQL_SELECT_BY_ID =
            "SELECT id, paciente_id, medico_id, data_hora, motivo, status FROM consulta WHERE id = ?";

    private static final String SQL_SELECT_ALL =
            "SELECT id, paciente_id, medico_id, data_hora, motivo, status FROM consulta ORDER BY data_hora DESC";

    private static final String SQL_SELECT_BY_MEDICO =
            "SELECT id, paciente_id, medico_id, data_hora, motivo, status FROM consulta WHERE medico_id = ? ORDER BY data_hora DESC";

    @Override
    public void salvar(Consulta consulta) throws Exception {

        validarConsulta(consulta);

        if (!DBConnection.isOnline()) {
            throw new Exception("Operação indisponível em modo demo.");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {

            ps.setInt(1, consulta.getPaciente().getId());
            ps.setInt(2, consulta.getMedico().getId());
            ps.setTimestamp(3, new Timestamp(consulta.getDataHora().getTime()));
            ps.setString(4, consulta.getMotivo());
            ps.setString(5, consulta.getStatus());

            ps.executeUpdate();
        }
    }

    @Override
    public void atualizar(Consulta consulta) throws Exception {

        validarConsulta(consulta);

        if (consulta.getId() <= 0) {
            throw new Exception("ID da consulta inválido.");
        }

        if (!DBConnection.isOnline()) {
            throw new Exception("Operação indisponível em modo demo.");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setInt(1, consulta.getPaciente().getId());
            ps.setInt(2, consulta.getMedico().getId());
            ps.setTimestamp(3, new Timestamp(consulta.getDataHora().getTime()));
            ps.setString(4, consulta.getMotivo());
            ps.setString(5, consulta.getStatus());
            ps.setInt(6, consulta.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public void excluir(int id) throws Exception {

        if (id <= 0) {
            throw new Exception("ID inválido.");
        }

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
    public Consulta buscarPorId(int id) throws Exception {

        if (id <= 0) {
            return null;
        }

        if (!DBConnection.isOnline()) {
            return buscarPorIdDemo(id);
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapConsulta(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Consulta> listarTodos() throws Exception {

        if (!DBConnection.isOnline()) {
            return listarTodosDemo();
        }

        List<Consulta> consultas = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                consultas.add(mapConsulta(rs));
            }
        }

        return consultas;
    }

    @Override
    public List<Consulta> listarPorMedico(int medicoId) throws Exception {

        if (medicoId <= 0) {
            return new ArrayList<>();
        }

        if (!DBConnection.isOnline()) {
            return listarPorMedicoDemo(medicoId);
        }

        List<Consulta> consultas = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_MEDICO)) {

            ps.setInt(1, medicoId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    consultas.add(mapConsulta(rs));
                }
            }
        }

        return consultas;
    }

    private Consulta mapConsulta(ResultSet rs) throws Exception {

        Consulta consulta = new Consulta();

        consulta.setId(rs.getInt("id"));
        consulta.setPaciente(buscarPaciente(rs.getInt("paciente_id")));
        consulta.setMedico(buscarMedico(rs.getInt("medico_id")));
        consulta.setDataHora(rs.getTimestamp("data_hora"));
        consulta.setMotivo(rs.getString("motivo"));
        consulta.setStatus(rs.getString("status"));

        return consulta;
    }

    private Paciente buscarPaciente(int id) throws Exception {
        return new PacienteDAOImpl().buscarPorId(id);
    }

    private Medico buscarMedico(int id) throws Exception {
        return new MedicoDAOImpl().buscarPorId(id);
    }

    private void validarConsulta(Consulta consulta) throws Exception {

        if (consulta == null) {
            throw new Exception("Consulta não informada.");
        }

        consulta.validar();

        if (consulta.getPaciente().getId() <= 0) {
            throw new Exception("Paciente inválido.");
        }

        if (consulta.getMedico().getId() <= 0) {
            throw new Exception("Médico inválido.");
        }

        if (consulta.getDataHora() == null) {
            throw new Exception("Data da consulta obrigatória.");
        }

        if (consulta.getMotivo() == null || consulta.getMotivo().trim().isEmpty()) {
            throw new Exception("Motivo da consulta é obrigatório.");
        }

        if (consulta.getStatus() == null || consulta.getStatus().trim().isEmpty()) {
            consulta.setStatus("Agendada");
        }
    }

    // ==========================
    // MODO DEMO / OFFLINE
    // ==========================

    private Consulta buscarPorIdDemo(int id) {
        return listarTodosDemo()
                .stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private List<Consulta> listarTodosDemo() {

        List<Consulta> consultas = new ArrayList<>();

        try {

            Paciente paciente = new PacienteDAOImpl().buscarPorId(1);
            Medico medico = new MedicoDAOImpl().buscarPorId(1);

            if (paciente != null && medico != null) {

                consultas.add(
                        new Consulta(
                                1,
                                paciente,
                                medico,
                                new Date(),
                                "Consulta de rotina",
                                "Realizada"
                        )
                );

                consultas.add(
                        new Consulta(
                                2,
                                paciente,
                                medico,
                                new Date(),
                                "Retorno médico",
                                "Agendada"
                        )
                );
            }

        } catch (Exception ignored) {
        }

        return consultas;
    }

    private List<Consulta> listarPorMedicoDemo(int medicoId) {

        return listarTodosDemo()
                .stream()
                .filter(c ->
                        c.getMedico() != null
                                && c.getMedico().getId() == medicoId)
                .toList();
    }
}
