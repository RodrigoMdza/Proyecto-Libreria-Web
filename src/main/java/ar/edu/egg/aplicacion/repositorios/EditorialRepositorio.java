
package ar.edu.egg.aplicacion.repositorios;


import ar.edu.egg.aplicacion.entidades.Editorial;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial,String>  {
    
    public List<Editorial> findByNombreContainingOrderByNombre(String nombre);
    
    public List<Editorial> findAllByOrderByNombreAsc();
}
