package ar.edu.egg.aplicacion.servicios;

import ar.edu.egg.aplicacion.entidades.Libro;
import ar.edu.egg.aplicacion.entidades.Prestamo;
import ar.edu.egg.aplicacion.entidades.Usuario;
import ar.edu.egg.aplicacion.errores.ErrorServicio;
import ar.edu.egg.aplicacion.repositorios.LibroRepositorio;
import ar.edu.egg.aplicacion.repositorios.PrestamoRepositorio;
import ar.edu.egg.aplicacion.repositorios.UsuarioRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrestamoServicio {

    @Autowired
    private PrestamoRepositorio prestamoRepositorio;
    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private LibroServicio libroServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;

    @Transactional
    public void crearPrestamo(String idLibro, String idUsuario) throws ErrorServicio {
        Libro libro = libroServicio.buscarPorId(idLibro);
        Usuario usuario = usuarioServicio.buscarPorId(idUsuario);
        Prestamo prestamo = new Prestamo();
        prestamo.setFechaPrestamo(new Date());
        prestamo.setFechaDevolucion(null);
        prestamo.setAlta(true);
        prestamo = verificarSettear(prestamo, libro, usuario);
        prestamoRepositorio.save(prestamo);
    }

    @Transactional
    private Prestamo verificarSettear(Prestamo prestamo, Libro libro, Usuario usuario) throws ErrorServicio {

        //Verificamos primero que el Libro y el Cliente tengan valores válidos y no estén vacíos
        if (libro == null) {
            throw new ErrorServicio("No se ha encontrado el libro especificado.");
        }
        if (usuario == null) {
            throw new ErrorServicio("No se ha encontrado el cliente especificado.");
        }

        //Verificamos que existan ejemplares disponibles para realizar el prestamo
        if (libro.getEjemplaresRestantes() > 0) {
            libro.setEjemplaresPrestados(libro.getEjemplaresPrestados() + 1);
            libro.setEjemplaresRestantes(libro.getEjemplaresRestantes() - 1);
            prestamo.setLibro(libro);
            libroRepositorio.save(libro);
        } else {
            throw new ErrorServicio("No quedan más ejemplares disponibles de este libro.");
        }
        //Seteamos en el Prestamo el Cliente anexado y persistimos el nuevo atributo del mismo
        prestamo.setUsuario(usuario);
        usuarioRepositorio.save(usuario);

        //Devolvemos el prestamo ya seteado
        return prestamo;
    }

    @Transactional(readOnly = true)
    public List<Prestamo> listarTodos() throws ErrorServicio {
        try {
            return prestamoRepositorio.findAll();
        } catch (Exception e) {
            throw new ErrorServicio("Hubo un problema para traer todos los prestamos.");
        }
    }

    @Transactional
    public void darAlta(String id) throws ErrorServicio {
        Optional<Prestamo> respuesta = prestamoRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Prestamo prestamo = reactivarPrestamo(respuesta.get());
            prestamoRepositorio.save(prestamo);
        } else {
            throw new ErrorServicio("No se ha encontrado el prestamo solicitado.");
        }
    }

    @Transactional
    private Prestamo reactivarPrestamo(Prestamo prestamo) throws ErrorServicio {
        Libro libro = prestamo.getLibro();
        Usuario usuario = prestamo.getUsuario();
        if (libro.getEjemplaresRestantes() > 0) {
            libro.setEjemplaresPrestados(libro.getEjemplaresPrestados() + 1);
            libro.setEjemplaresRestantes(libro.getEjemplaresRestantes() - 1);
        } else {
            throw new ErrorServicio("No quedan más ejemplares disponibles de este libro actualmente.");
        }
        libroRepositorio.save(libro);
        usuarioRepositorio.save(usuario);
        prestamo.setFechaDevolucion(null);
        prestamo.setAlta(true);
        return prestamo;
    }

    @Transactional
    public void darBaja(String id) throws ErrorServicio {
        Optional<Prestamo> respuesta = prestamoRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Prestamo prestamo = devolverPrestamo(respuesta.get());
            prestamoRepositorio.save(prestamo);
        } else {
            throw new ErrorServicio("No se ha encontrado el prestamo solicitado.");
        }
    }

    @Transactional
    private Prestamo devolverPrestamo(Prestamo prestamo) {
        prestamo.setFechaDevolucion(new Date());
        prestamo.setAlta(false);
        Libro libro = prestamo.getLibro();
        Usuario usuario = prestamo.getUsuario();
        libro.setEjemplaresPrestados(libro.getEjemplaresPrestados() - 1);
        libro.setEjemplaresRestantes(libro.getEjemplaresRestantes() + 1);
        libroRepositorio.save(libro);
        usuarioRepositorio.save(usuario);
        return prestamo;
    }

    @Transactional
    public void deleteById(String id) {
        Optional<Prestamo> optional = prestamoRepositorio.findById(id);
        if (optional.isPresent()) {
            prestamoRepositorio.delete(optional.get());
        }
    }

}
