package model;

/**
 * Interface para entidades que podem validar seu estado antes de persistir.
 */
public interface Validavel {
    void validar() throws IllegalArgumentException;
}
