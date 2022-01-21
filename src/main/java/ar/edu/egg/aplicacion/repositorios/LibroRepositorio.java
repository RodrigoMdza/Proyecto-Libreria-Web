package ar.edu.egg.aplicacion.repositorios;

import ar.edu.egg.aplicacion.entidades.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, String> {

    
    public List<Libro> findByTituloContainingOrderByTitulo(String titulo);

    public List<Libro> findAllByOrderByTituloAsc();

}
