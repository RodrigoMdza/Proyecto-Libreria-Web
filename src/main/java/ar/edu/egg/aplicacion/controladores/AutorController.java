/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.egg.aplicacion.controladores;

import ar.edu.egg.aplicacion.entidades.Autor;
import ar.edu.egg.aplicacion.servicios.AutorServicio;
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
@RequestMapping("/autor")
public class AutorController {

    @Autowired
    private AutorServicio autorServicio;

    @GetMapping("/cargarautor")
    public String formulario(Model modelo, @RequestParam(required = false) String id) {
        if (id != null) {
            Optional<Autor> optional = autorServicio.findById(id);
            if (optional.isPresent()) {
                modelo.addAttribute("autor", optional.get());
            } else {
                return "redirect:/autor/lista";
            }
        } else {
            modelo.addAttribute("autor", new Autor());
        }
        return "cargar-autor";
    }

    @PostMapping("/cargarautor")
    public String lista(ModelMap modelo, @RequestParam String nombre) {

        List<Autor> autores = autorServicio.buscarPorNombre(nombre);

        modelo.addAttribute("autores", autores);

        modelo.put("exito", "Registro exitoso");
        return "cargar-autor";
    }

    @PostMapping("/registro")
    public String guardarAutor(ModelMap modelo, @ModelAttribute Autor autor) {

        try {

            autorServicio.modificar(autor);

            modelo.put("exitoGuardar", "Registro exitoso");
            return "cargar-autor";
        } catch (Exception e) {
            modelo.put("errorGuardar", "Falto algun dato");
            return "cargar-autor";
        }
    }

    @GetMapping("/lista")
    public String lista(ModelMap modelo) {

        List<Autor> autores = autorServicio.listarTodos();

        modelo.addAttribute("autores", autores);

        return "listar-autores";
    }

    @GetMapping("/baja/{id}")
    public String baja(ModelMap modelo, @PathVariable String id) {

        try {
            autorServicio.baja(id);
            return "redirect:/autor/lista";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/alta/{id}")
    public String alta(ModelMap modelo, @PathVariable String id) {

        try {
            autorServicio.alta(id);
            return "redirect:/autor/lista";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/delete")
    public String eliminarPersona(@RequestParam(required = true) String id) {
        autorServicio.deleteById(id);
        return "redirect:/autor/lista";
    }

    @GetMapping("/buscarautor")
    public String listas(Model modelo, @RequestParam(required = false) String id) {
        modelo.addAttribute("autor", new Autor());
        return "buscar-autor";
    }

    @PostMapping("/buscarautor")
    public String listas(ModelMap modelo, @RequestParam String nombre) {
        List<Autor> autores = autorServicio.buscarPorNombre(nombre);

        modelo.addAttribute("autores", autores);

        modelo.put("exito", "Registro exitoso");
        return "buscar-autor";
    }
}
