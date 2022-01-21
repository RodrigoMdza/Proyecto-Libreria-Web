/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.egg.aplicacion;

import ar.edu.egg.aplicacion.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 *
 * @author acer_
 */
public class ServletInitializer  extends SpringBootServletInitializer {
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AplicacionApplication.class);
	}

}

