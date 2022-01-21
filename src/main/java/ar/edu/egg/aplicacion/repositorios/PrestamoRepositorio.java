
package ar.edu.egg.aplicacion.repositorios;

import ar.edu.egg.aplicacion.entidades.Prestamo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepositorio extends JpaRepository<Prestamo,String>{
    
    /**
     * Query personalizada para traer una Lista de Préstamos según el atributo 'dni' del Objeto Cliente que tenga una relación con
     * dichos préstamos
     * @param dni Parámetro de búsqueda. Atributo del Objeto Cliente vinculado a los préstamos a buscar
     * @return Listado de préstamos vinculados al dni de ese Cliente
     */
    //@Query("SELECT p FROM Prestamo p WHERE p.cliente.dni = :dni")
    //public List<Prestamo> buscarPorDniCliente(@Param("dni") Long dni);
}
