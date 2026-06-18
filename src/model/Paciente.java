package model;

import java.util.Date;

/**
 * Entidade de Paciente com atributos específicos e validação de cadastro.
 */
public class Paciente extends Pessoa implements Validavel {

    private Date dataNascimento;
    private boolean ativo;
    private String endereco;

    public Paciente() {
        this.ativo = true;
    }

    public Paciente(int id, String nome, String cpf, String telefone,
                    Date dataNascimento, String endereco, boolean ativo) {
        super(id, nome, cpf, telefone);
        this.dataNascimento = dataNascimento;
        this.endereco = endereco;
        this.ativo = ativo;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    @Override
    public void validar() {

        if (getNome() == null || getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do paciente é obrigatório.");
        }

        if (getCpf() == null || getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("CPF do paciente é obrigatório.");
        }

        if (dataNascimento == null) {
            throw new IllegalArgumentException("Data de nascimento é obrigatória.");
        }
    }
}