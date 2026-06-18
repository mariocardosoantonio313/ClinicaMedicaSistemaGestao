package model;

/**
 * Entidade de Médico que estende Pessoa e adiciona especialidade CRM.
 */
public class Medico extends Pessoa implements Validavel {
    private String especialidade;
    private String crm;

    public Medico() {
    }

    public Medico(int id, String nome, String cpf, String telefone, String especialidade, String crm) {
        super(id, nome, cpf, telefone);
        this.especialidade = especialidade;
        this.crm = crm;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    @Override
    public void validar() {
        if (getNome() == null || getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do médico é obrigatório.");
        }
        if (crm == null || crm.trim().isEmpty()) {
            throw new IllegalArgumentException("CRM do médico é obrigatório.");
        }
    }
}
