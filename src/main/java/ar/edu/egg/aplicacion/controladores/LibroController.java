package ar.edu.egg.aplicacion.controladores;

import ar.edu.egg.aplicacion.entidades.Autor;
import ar.edu.egg.aplicacion.entidades.Editorial;
import ar.edu.egg.aplicacion.entidades.Libro;
import ar.edu.egg.aplicacion.errores.ErrorServicio;
import ar.edu.egg.aplicacion.servicios.AutorServicio;
import ar.edu.egg.aplicacion.servicios.EditorialServicio;
import ar.edu.egg.aplicacion.servicios.LibroServicio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/libro")
public class LibroController {

    @Autowired
    private LibroServicio libroServicio;
    @Autowired
    private AutorServicio autorServicio;
    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/registro")
    public String formulario(ModelMap modelo, @RequestParam(required = false) String id) {
        if (id != null) {
            Optional<Libro> optional = libroServicio.findById(id);
            if (optional.isPresent()) {
                modelo.addAttribute("libro", optional.get());
            } else {
                return "redirect:/libro/lista";
            }
        } else {
            modelo.addAttribute("libro", new Libro());
        }
        List<Autor> autores = autorServicio.listarTodos();
        modelo.addAttribute("autores", autores);
        List<Editorial> editoriales = editorialServicio.listarTodos();
        modelo.addAttribute("editoriales", editoriales);

        return "cargar-libro";
    }

    @PostMapping("/registro")
    public String guardar(ModelMap modelo, @ModelAttribute Libro libro) {

        try {

            libroServicio.modificar(libro);

            modelo.put("exito", "Registro exitoso");
            return "cargar-libro";
        } catch (Exception e) {
            modelo.put("error", "Falto algun dato");
            return "cargar-libro";
        }
    }

    @GetMapping("/lista")
    public String lista(ModelMap modelo) throws ErrorServicio {

        List<Libro> libros = libroServicio.listarTodos();

        modelo.addAttribute("libros", libros);

        return "listar-libros";
    }

    @GetMapping("/baja/{id}")
    public String baja(ModelMap modelo, @PathVariable String id) {

        try {
            libroServicio.baja(id);
            return "redirect:/libro/lista";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/alta/{id}")
    public String alta(ModelMap modelo, @PathVariable String id) {

        try {
            libroServicio.alta(id);
            return "redirect:/libro/lista";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/delete")
    public String eliminarPersona(@RequestParam(required = true) String id) {
        libroServicio.deleteById(id);
        return "redirect:/libro/lista";
    }

   @GetMapping("/buscarlibro")
    public String listas(Model modelo, @RequestParam(required = false) String id) {
        modelo.addAttribute("libro", new Libro());
        return "buscar-libro";
    }

   @PostMapping("/buscarlibro")
    public String listas(ModelMap modelo, @RequestParam String titulo) {
        List<Libro> libros = libroServicio.buscarPorTitulo(titulo);
        modelo.addAttribute("libros", libros);

        modelo.put("exito", "Registro exitoso");
        return "buscar-libro";
    }
}
