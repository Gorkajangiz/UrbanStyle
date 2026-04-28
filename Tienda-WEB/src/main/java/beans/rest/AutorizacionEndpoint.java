package beans.rest;

import EJBs.ClientEJBLocal;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import modelo.AutorizacionDTO;
import modelo.UsuarioDTO;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AutorizacionEndpoint {
    
    @EJB
    private ClientEJBLocal clientEJB;
    
    @POST
    @Path("/login")
    public Response login(UsuarioDTO usuarioDTO) {
        
        if (usuarioDTO == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new AutorizacionDTO(false, null, null))
                    .build();
        }
        
        
        EJBs.ClientEJB.Login resultado = clientEJB.login(usuarioDTO.getEmail(), usuarioDTO.getContrasena());
        
        if (resultado == EJBs.ClientEJB.Login.LOGIN_SUCCESS) {
            String rol = "USER";
            if ("admin".equalsIgnoreCase(usuarioDTO.getEmail())) {
                rol = "ADMIN";
            }
            
            return Response.ok(new AutorizacionDTO(true, usuarioDTO.getEmail(), rol)).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new AutorizacionDTO(false, usuarioDTO.getEmail(), null))
                    .build();
        }
    }
}