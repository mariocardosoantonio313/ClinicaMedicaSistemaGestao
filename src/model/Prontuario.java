package model;

import java.util.Date;

/**
 * Entidade de Prontuário associada a um paciente com histórico e data.
 */
public class Prontuario implements Validavel {
    private int id;
    private Paciente paciente;
    private String descricao;
    private Date dataAtualizacao;

    public Prontuario() {
    }

    public Prontuario(int id, Paciente paciente, String descricao, Date dataAtualizacao) {
        this.id = id;
        this.paciente = paciente;
        this.descricao = descricao;
        this.dataAtualizacao = dataAtualizacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Date dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    @Override
    public void validar() {
        if (paciente == null) {
            throw new IllegalArgumentException("Paciente é obrigatório no prontuário.");
        }
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição do prontuário é obrigatória.");
        }
        if (dataAtualizacao == null) {
            throw new IllegalArgumentException("Data de atualização do prontuário é obrigatória.");
        }
    }
}
