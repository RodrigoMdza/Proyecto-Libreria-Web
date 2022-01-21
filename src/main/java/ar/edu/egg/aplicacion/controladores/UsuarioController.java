package ar.edu.egg.aplicacion.controladores;

import ar.edu.egg.aplicacion.entidades.Usuario;
import ar.edu.egg.aplicacion.errores.ErrorServicio;
import ar.edu.egg.aplicacion.servicios.UsuarioServicio;
import java.util.List;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/usuario")
class UsuarioController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/registro")
    public String formulario() {
        return "registro";
    }

    @PostMapping("/registro")
    public String guardar(ModelMap modelo, @RequestParam String nombre, @RequestParam String apellido,
            @RequestParam String mail, @RequestParam String clave, @RequestParam String clave2, @RequestParam String dni, @RequestParam String rol) {

        try {
            usuarioServicio.registrar(nombre, apellido, mail, clave, clave2, dni, rol);
            modelo.put("exito", "registro exitoso");
            return "registro";
        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage());
            return "registro";
        }
    }

    @GetMapping("/editar-perfil")
    public String editarPerfil(HttpSession session, @RequestParam String id, ModelMap model) {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/inicio";
        }

        try {
            Usuario usuario = usuarioServicio.buscarPorId(id);
            model.addAttribute("perfil", usuario);
        } catch (ErrorServicio e) {
            model.addAttribute("error", e.getMessage());
        }
        return "perfil.html";
    }

    @PostMapping("/actualizar-perfil")
    public String registrar(ModelMap modelo, HttpSession session, @RequestParam String id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail, @RequestParam String clave1, @RequestParam String clave2) {

        Usuario usuario = null;
        try {

            Usuario login = (Usuario) session.getAttribute("usuariosession");
            if (login == null || !login.getId().equals(id)) {
                return "redirect:/inicio";
            }

            usuario = usuarioServicio.buscarPorId(id);
            usuarioServicio.modificar(id, nombre, apellido, mail, clave2, clave2, mail);
            session.setAttribute("usuariosession", usuario);

            return "index.html";
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("perfil", usuario);

            return "perfil.html";
        }
    }

    @GetMapping("/lista")
    public String lista(ModelMap modelo) throws ErrorServicio {
        List<Usuario> usuarios = usuarioServicio.todosLosUsuarios();

        modelo.addAttribute("usuarios", usuarios);

        return "listar-usuarios";
    }

    @GetMapping("/baja/{id}")
    public String baja(ModelMap modelo, @PathVariable String id) {
        try {
            usuarioServicio.baja(id);
            return "redirect:/usuario/lista";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/alta/{id}")
    public String alta(ModelMap modelo, @PathVariable String id) {
        try {
            usuarioServicio.alta(id);
            return "redirect:/usuario/lista";
        } catch (Exception e) {
            return "redirect:/";
        }
    }
    
    @GetMapping("/delete")
    public String eliminarPersona(@RequestParam(required = true) String id) {
            usuarioServicio.deleteById(id);
            return "redirect:/usuario/lista";
     }
}

