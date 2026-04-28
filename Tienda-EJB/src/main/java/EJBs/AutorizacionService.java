package EJBs;

import jakarta.ejb.Stateless;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import modelo.AutorizacionDTO;
import modelo.UsuarioDTO;

@Stateless
public class AutorizacionService {
    
    private static final String LINK = "http://localhost:8080/Tienda-WEB/api/auth/login";
    
    public AutorizacionDTO login(String email, String contrasena) {
        System.out.println("=== AutorizacionService ===");
        System.out.println("Llamando a: " + LINK);
        System.out.println("Usuario: " + email);
        
        Client cliente = ClientBuilder.newClient();
        Response respuesta = null;
        
        try {
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setEmail(email);
            usuarioDTO.setContrasena(contrasena);
            
            respuesta = cliente.target(LINK)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(usuarioDTO, MediaType.APPLICATION_JSON));
            
            System.out.println("Status respuesta: " + respuesta.getStatus());
            
            if (respuesta.getStatus() == 200) {
                AutorizacionDTO auth = respuesta.readEntity(AutorizacionDTO.class);
                System.out.println("Login OK - Rol: " + auth.getRol());
                return auth;
            } else {
                System.out.println("Login FAILED - Status: " + respuesta.getStatus());
                return new AutorizacionDTO(false, email, null);
            }
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            return new AutorizacionDTO(false, email, null);
        } finally {
            if (respuesta != null) respuesta.close();
            cliente.close();
        }
    }
}