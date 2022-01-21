package ar.edu.egg.aplicacion.servicios;

import ar.edu.egg.aplicacion.entidades.Usuario;
import ar.edu.egg.aplicacion.enums.Rol;
import ar.edu.egg.aplicacion.errores.ErrorServicio;
import ar.edu.egg.aplicacion.repositorios.UsuarioRepositorio;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public void registrar(String nombre, String apellido, String mail, String clave, String clave2, String dni, String rol) throws ErrorServicio {
        validar(nombre, apellido, mail, clave, clave2, dni);
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setMail(mail);
        usuario.setDni(dni);
        String encriptada = new BCryptPasswordEncoder().encode(clave);
        usuario.setClave(encriptada);
        usuario.setRol(Rol.valueOf(rol));
        usuario.setAlta(true);
        usuarioRepositorio.save(usuario);
    }

    @Transactional
    public void modificar(String id, String nombre, String apellido, String mail, String clave, String clave2, String dni) throws ErrorServicio {
        validar(nombre, apellido, mail, clave, clave2, dni);
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setMail(mail);
            usuario.setDni(dni);
            String encriptada = new BCryptPasswordEncoder().encode(clave);
            usuario.setClave(encriptada);
            
            usuario.setAlta(true);
            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontró el usuario solicitado");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Usuario alta(String id) {

        Usuario entidad = usuarioRepositorio.getOne(id);

        entidad.setAlta(true);
        return usuarioRepositorio.save(entidad);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Usuario baja(String id) {

        Usuario entidad = usuarioRepositorio.getOne(id);

        entidad.setAlta(false);
        return usuarioRepositorio.save(entidad);
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorMail(mail);
        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
            permisos.add(p);

            //Esto me permite guardar el OBJETO USUARIO LOG, para luego ser utilizado
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);

            User user = new User(usuario.getMail(), usuario.getClave(), permisos);
            return user;

        } else {
            return null;
        }
    }

    @Transactional(readOnly = true) 
    public List<Usuario> todosLosUsuarios() throws ErrorServicio {
        return usuarioRepositorio.findAll();
    }
    
     @Transactional(readOnly=true)
    public Usuario buscarPorId(String id) throws ErrorServicio {

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();
            return usuario;
        } else {

            throw new ErrorServicio("No se encontró el usuario solicitado");
        }
}
    
        @Transactional
    public void deleteById(String id) {
        Optional<Usuario> optional = usuarioRepositorio.findById(id);
        if (optional.isPresent()) {
            usuarioRepositorio.delete(optional.get());
        }
    }

    public void validar(String nombre, String apellido, String mail, String clave, String clave2, String dni) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre del usuario no puede ser nulo");
        }

        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("El apellido del usuario no puede ser nulo");
        }

        if (mail == null || mail.isEmpty()) {
            throw new ErrorServicio("El mail no puede ser nulo");
        }

        if (clave == null || clave.isEmpty() || clave.length() <= 6) {
            throw new ErrorServicio("La clave del usuario no puede ser nula y tiene que tener mas de seis digitos");
        }
        if (!clave.equals(clave2)) {
            throw new ErrorServicio("Las claves deben ser iguales");
        }
        if (dni == null || dni.isEmpty()) {
            throw new ErrorServicio("El mail no puede ser nulo");
        }
    }
}
