package modelo;

public class AutorizacionDTO {
    
    
    private boolean logeado; 
    
    private String nombre;
    
    private String rol;
    
    
    public AutorizacionDTO(){}

    public AutorizacionDTO(boolean logeado, String nombre, String rol) {
        this.logeado = logeado;
        this.nombre = nombre;
        this.rol = rol;
    }
    
    

    public boolean isLogeado() {
        return logeado;
    }

    public void setLogeado(boolean logeado) {
        this.logeado = logeado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
    
    
    
    
    
}
