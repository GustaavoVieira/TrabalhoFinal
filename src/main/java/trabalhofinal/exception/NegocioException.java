// src/main/java/trabalhofinal/exception/NegocioException.java
package trabalhofinal.exception;


public abstract class NegocioException extends RuntimeException {
    protected NegocioException(String message) {
        super(message);
    }
}
