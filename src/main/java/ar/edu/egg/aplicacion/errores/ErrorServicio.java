package ar.edu.egg.aplicacion.errores;

public class ErrorServicio extends Exception {

    public ErrorServicio(String msn) {
        super(msn);
    }
}