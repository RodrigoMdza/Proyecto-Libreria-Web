/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.egg.aplicacion.controladores;

import ar.edu.egg.aplicacion.entidades.Editorial;
import ar.edu.egg.aplicacion.servicios.EditorialServicio;
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
@RequestMapping("/editorial")
public class EditorialController {

    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/cargareditorial")
    public String formulario(Model modelo, @RequestParam(required = false) String id) {
        if (id != null) {
            Optional<Editorial> optional = editorialServicio.findById(id);
            if (optional.isPresent()) {
                modelo.addAttribute("editorial", optional.get());
            } else {
                return "redirect:/editorial/lista";
            }
        } else {
            modelo.addAttribute("editorial", new Editorial());
        }
        return "cargar-editorial";
    }

    @PostMapping("/cargareditorial")
    public String lista(ModelMap modelo, @RequestParam String nombre) {

        List<Editorial> editoriales = editorialServicio.buscarPorNombre(nombre);

        modelo.addAttribute("editoriales", editoriales);

        modelo.put("exito", "Registro exitoso");
        return "cargar-editorial";
    }

    @PostMapping("/registro")
    public String guardarAutor(ModelMap modelo, @ModelAttribute Editorial editorial) {

        try {

            editorialServicio.modificar(editorial);

            modelo.put("exitoGuardar", "Registro exitoso");
            return "cargar-editorial";
        } catch (Exception e) {
            modelo.put("errorGuardar", "Falto algun dato");
            return "cargar-editorial";
        }
    }

    @GetMapping("/lista")
    public String lista(ModelMap modelo) {

        List<Editorial> editoriales = editorialServicio.listarTodos();

        modelo.addAttribute("editoriales", editoriales);

        return "listar-editoriales";
    }

    @GetMapping("/baja/{id}")
    public String baja(ModelMap modelo, @PathVariable String id) {

        try {
            editorialServicio.baja(id);
            return "redirect:/editorial/lista";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/alta/{id}")
    public String alta(ModelMap modelo, @PathVariable String id) {

        try {
            editorialServicio.alta(id);
            return "redirect:/editorial/lista";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/delete")
    public String eliminarPersona(@RequestParam(required = true) String id) {
        editorialServicio.deleteById(id);
        return "redirect:/editorial/lista";
    }

    @GetMapping("/buscareditorial")
    public String listas(Model modelo, @RequestParam(required = false) String id) {
        modelo.addAttribute("editorial", new Editorial());
        return "buscar-editorial";
    }

    @PostMapping("/buscareditorial")
    public String listas(ModelMap modelo, @RequestParam String nombre) {
        List<Editorial> editoriales = editorialServicio.buscarPorNombre(nombre);

        modelo.addAttribute("editoriales", editoriales);

        modelo.put("exito", "Registro exitoso");
        return "buscar-editorial";
    }
}
