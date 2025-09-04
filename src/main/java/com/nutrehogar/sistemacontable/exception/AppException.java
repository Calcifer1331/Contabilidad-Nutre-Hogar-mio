package com.nutrehogar.sistemacontable.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Clase base para todas las excepciones de la aplicación.
 *
 * <p>Esta clase extiende de {@link RuntimeException} y proporciona varios constructores para crear excepciones con diferentes niveles de detalle. Es la clase base de la cual todas las excepciones específicas de la aplicación deben heredar.</p>
 *
 * <p>Ejemplos de uso:</p>
 * <pre>
 * {@code
 * // Lanzar una excepción con un mensaje
 * throw new AppException("Ocurrió un error en la aplicación.");
 *
 * // Lanzar una excepción con un mensaje y una causa
 * throw new AppException("Ocurrió un error en la aplicación.", new IOException("Error de E/S"));
 *
 * // Lanzar una excepción con solo una causa
 * throw new AppException(new IOException("Error de E/S"));
 * }
 * </pre>
 */
@Slf4j
public abstract class AppException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "An error has occurred in the system.";

    /**
     * Constructor por defecto.
     *
     * <p>Crea una nueva instancia de {@code AppException} sin ningún detalle adicional.</p>
     */
    public AppException() {
        super(DEFAULT_MESSAGE);
        logException();
    }

    /**
     * Constructor que acepta un mensaje de error.
     *
     * <p>Crea una nueva instancia de {@code AppException} con el mensaje de error especificado.</p>
     *
     * @param message El mensaje de error que describe la excepción.
     */
    public AppException(String message) {
        super(message);
        logException();
    }

    /**
     * Constructor que acepta un mensaje de error y una causa.
     *
     * <p>Crea una nueva instancia de {@code AppException} con el mensaje de error especificado y la causa subyacente de la excepción.</p>
     *
     * @param message El mensaje de error que describe la excepción.
     * @param cause   La causa subyacente de la excepción (puede ser {@code null}).
     */
    public AppException(String message, Throwable cause) {
        super(message, cause);
        logException();
    }

    /**
     * Constructor que acepta una causa.
     *
     * <p>Crea una nueva instancia de {@code AppException} con la causa subyacente de la excepción.</p>
     *
     * @param cause La causa subyacente de la excepción (puede ser {@code null}).
     */
    public AppException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
        logException();
    }

    /**
     * Registra la excepción en los logs.
     */
    private void logException() {
        if (getCause() != null) {
            log.error("Excepción: {}. Causa: {}", this.getMessage(), getCause().getMessage(), this);
        } else {
            log.error("Excepción: {}", this.getMessage(), this);
        }    }
}