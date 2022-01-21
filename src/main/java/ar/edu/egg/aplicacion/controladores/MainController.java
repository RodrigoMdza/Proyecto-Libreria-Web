package ar.edu.egg.aplicacion.controladores;

import javax.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class MainController {
    

    @GetMapping("/")
    public String index(ModelMap modelo) {
        return "index.html";
    }
    
    @GetMapping("/login")
	public String login(HttpSession session, Authentication usuario, ModelMap modelo, @RequestParam(required = false) String error) {
		try {
			if (usuario.getName() != null) {
				return "redirect:/";
			} else {
				
				if (error != null && !error.isEmpty()) {
					modelo.addAttribute("error", "La dirección de mail o la contraseña que ingresó son incorrectas.");
				}
				return "login";
			}
			
		} catch (Exception e) {
			if (error != null && !error.isEmpty()) {
				modelo.addAttribute("error", "La dirección de mail o la contraseña que ingresó son incorrectas.");
			}
			return "login";
		}
	}
	
	@GetMapping("/loginsuccess")
	public String loginresolver() {
				
		return "redirect:/";
	}
        
        
}
