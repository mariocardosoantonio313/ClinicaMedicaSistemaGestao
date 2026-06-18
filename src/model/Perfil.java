package model;

/**
 * Enumeração que representa os perfis de acesso da aplicação.
 */
public enum Perfil {
    ADMINISTRADOR,
    MEDICO,
    RECEPCIONISTA;

    @Override
    public String toString() {
        return switch (this) {
            case ADMINISTRADOR -> "Administrador";
            case MEDICO -> "Médico";
            case RECEPCIONISTA -> "Recepcionista";
            default -> super.toString();
        };
    }
}
