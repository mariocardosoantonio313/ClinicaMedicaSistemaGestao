package model;

import java.util.Date;

/**
 * Entidade de Consulta Médica.
 * Responsável por relacionar Paciente, Médico, Data/Hora e informações da consulta.
 */
public class Consulta implements Validavel {

    private int id;
    private Paciente paciente;
    private Medico medico;
    private Date dataHora;
    private String motivo;
    private String status;

    public Consulta() {
        this.status = "Agendada";
    }

    public Consulta(int id, Paciente paciente, Medico medico,
                     Date dataHora, String motivo, String status) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.dataHora = dataHora;
        this.motivo = motivo;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("ID da consulta inválido.");
        }
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo != null ? motivo.trim() : "";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status != null ? status.trim() : "";
    }

    /**
     * Validação de regras de negócio.
     */
    @Override
public void validar() {

    if (paciente == null) {
        throw new IllegalArgumentException("Selecione um paciente.");
    }

    if (medico == null) {
        throw new IllegalArgumentException("Selecione um médico.");
    }

    if (dataHora == null) {
        throw new IllegalArgumentException("Informe a data e hora da consulta.");
    }

    if (motivo == null || motivo.trim().isEmpty()) {
        throw new IllegalArgumentException("Informe o motivo da consulta.");
    }

    if (status == null || status.trim().isEmpty()) {
        throw new IllegalArgumentException("Informe o status da consulta.");
    }
}
    @Override
    public String toString() {
        return "Consulta{"
                + "id=" + id
                + ", paciente=" + (paciente != null ? paciente.getNome() : "")
                + ", medico=" + (medico != null ? medico.getNome() : "")
                + ", dataHora=" + dataHora
                + ", status='" + status + '\''
                + '}';
    }
}