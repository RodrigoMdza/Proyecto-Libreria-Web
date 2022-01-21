package ar.edu.egg.aplicacion.controladores;

import ar.edu.egg.aplicacion.entidades.Libro;
import ar.edu.egg.aplicacion.entidades.Prestamo;
import ar.edu.egg.aplicacion.entidades.Usuario;
import ar.edu.egg.aplicacion.errores.ErrorServicio;
import ar.edu.egg.aplicacion.servicios.LibroServicio;
import ar.edu.egg.aplicacion.servicios.PrestamoServicio;
import ar.edu.egg.aplicacion.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/prestamo")
public class PrestamoController {

    @Autowired
    private PrestamoServicio prestamoServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private LibroServicio libroServicio;

    @GetMapping("/registro")
    public String nuevo_prestamo(ModelMap modelo) {
        try {
            //Traemos e inyectamos dos listados con los Libros y Clientes activos para settearlos en el nuevo prestamo
            List<Usuario> usuarios = usuarioServicio.todosLosUsuarios();
            List<Libro> libros = libroServicio.listarTodos();
            modelo.addAttribute("usuarios", usuarios);
            modelo.addAttribute("libros", libros);

        } catch (ErrorServicio e) {
            modelo.put("error", "Hubo un problema: " + e.getMessage());
        }
        return "cargar-prestamo";
    }

    @PostMapping("/registro")
    public String guardar_prestamo(ModelMap modelo, @RequestParam(required = false) String idLibro, @RequestParam(required = false) String idUsuario) {

        try {
            //Intetnamos persistir el nuevo Objeto usando un método de la Clase Service
            prestamoServicio.crearPrestamo(idLibro, idUsuario);

            modelo.put("exito", "¡Prestamo efectuado!");
            return "cargar-prestamo";
        } catch (ErrorServicio e) {
            modelo.put("error", "¡Algo salió mal! " + e.getMessage());
        }
        return "cargar-prestamo";
    }

    @GetMapping("/lista")
    public String lista(ModelMap modelo) throws ErrorServicio {
        List<Prestamo> prestamos = prestamoServicio.listarTodos();
        modelo.addAttribute("prestamos", prestamos);
        return "listar-prestamos";
    }

    @GetMapping("/alta/{id}")
    public String darAlta(@PathVariable String id, ModelMap modelo) {
        try {
            prestamoServicio.darAlta(id);
            return "redirect:/prestamo/lista";
        } catch (ErrorServicio e) {
            modelo.put("error", "Algo salió mal: " + e.getMessage());
            return "listar-prestamos";
        }
    }

    @GetMapping("/baja/{id}")
    public String darBaja(@PathVariable String id, ModelMap modelo) {
        try {
            prestamoServicio.darBaja(id);
            return "redirect:/prestamo/lista";
        } catch (ErrorServicio e) {
            modelo.put("error", "Algo salió mal: " + e.getMessage());
            return "listar-prestamos";
        }
    }
    
    @GetMapping("/delete")
    public String eliminarPrestamo(@RequestParam(required = true) String id) {
           prestamoServicio.deleteById(id);
            return "redirect:/prestamo/lista";
     }
}
