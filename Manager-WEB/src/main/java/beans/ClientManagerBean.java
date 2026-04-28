package beans;

import ejbs.ClientEJB;
import ejbs.ClientEJBLocal;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import modelo.Client;
import modelo.Product;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

@Named
@SessionScoped
public class ClientManagerBean implements Serializable {

    //Inyecto EJBs
    @EJB
    private ClientEJBLocal clientEJB;

    //Variables
    private Collection<Client> clients;
    private Client selectedClient;
    private Client client;
    private Collection<Product> products;
    private Product selectedProduct;
    private Product product;
    private String email;
    private String password;
    private boolean logeado;
    private String rol;
    private String mensajeError;
    private String selectedImageName;
    private List<String> categories;
    private List<String> colors;
    private List<String> sizes;

    //Inicializo todo nuevo
    @PostConstruct
    public void init() {
        client = new Client();
        selectedClient = new Client();
        product = new Product();
        initializeProduct(product);
        selectedProduct = new Product();
        initializeProduct(selectedProduct);
        initializeLists();
        refreshClients();
        refreshProducts();
    }

    //Creo las listas que se usan para dropdowns (Categorías y colores)
    private void initializeLists() {
        categories = Arrays.asList(
                "Gorras", "Camisetas", "Pantalones", "Sudaderas"
        );

        colors = Arrays.asList(
                "Blanco"
        );

    }

    //Inicializo un producto vacío
    private void initializeProduct(Product p) {
        p.setStock(0);
        p.setRating(0);
        p.setPrice(BigDecimal.ZERO);
        p.setUrl("");
        p.setCategory("");
        p.setColor("");
        p.setSize("");
        p.setDescription("");
        p.setName("");
    }

    //Estos metodos sobran, los hice enteros planteando las medias estrellas
    //sacando un 10 de la base de datos y dividiendolo entre 2, y luego me di
    //cuenta de que PrimeFaces no soporta medias estrellas. No lo toco porque 
    //funciona (y voy mal de tiempo), pero las estrellas no salen correctamente
    public Double getDisplayRating(Product p) {
        if (p == null || p.getRating() == null) {
            return 0.0;
        }
        return p.getRating() / 2.0;
    }

    public Double getFormRating() {
        if (product.getRating() == null) {
            return 0.0;
        }
        return product.getRating() / 2.0;
    }

    public void setFormRating(Double formRating) {
        if (formRating != null) {
            product.setRating((int) (formRating * 2));
        } else {
            product.setRating(0);
        }
    }

    public Double getSelectedFormRating() {
        if (selectedProduct.getRating() == null) {
            return 0.0;
        }
        return selectedProduct.getRating() / 2.0;
    }

    public void setSelectedFormRating(Double formRating) {
        if (formRating != null) {
            selectedProduct.setRating((int) (formRating * 2));
        } else {
            selectedProduct.setRating(0);
        }
    }

    //Este metodo lo que hace es que al seleccionar una foto extraiga su nombre + extension
    //para meterlo en la base de datos como su url. La foto tiene que estar ya dentro
    //de la carpeta de imagenes (no es necesario para que funcione el codigo pero si 
    //para que haya una foto en la página)
    public void handleFileUpload(FileUploadEvent event) {
        try {
            UploadedFile file = event.getFile();
            if (file != null && file.getFileName() != null) {
                selectedImageName = file.getFileName();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void clearSelectedImage() {
        selectedImageName = null;
    }

    //Este método es redundante de la tienda, lo unico que aquí prioriza admin
    //antes que user
    public String login() {
        mensajeError = null;
        if (email == null || email.isEmpty()
                || password == null || password.isEmpty()) {
            mensajeError = "Email y contraseña son obligatorios";
            return null;
        }
        ClientEJB.Login resultado = clientEJB.login(email, password);
        if (resultado == ClientEJB.Login.LOGIN_SUCCESS) {
            this.logeado = true;
            this.mensajeError = null;
            this.rol = "ADMIN";
            if ("admin".equalsIgnoreCase(email)) {
                return "adminPanel.xhtml?faces-redirect=true";
            } else {
                mensajeError = "No tienes permisos de administrador";
                this.rol = null;
                this.logeado = false;
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/Tienda-WEB/login.xhtml");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return null;
            }
        } else {
            this.logeado = false;
            this.mensajeError = "Credenciales incorrectas";
            return null;
        }
    }

    //Este método creo que no tiene ni un botón que lo use, esta aqui por herencia
    public String logout() {
        this.logeado = false;
        this.email = null;
        this.password = null;
        this.rol = null;
        this.client = new Client();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/Tienda-WEB/login.xhtml");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //Un getClients con otro nombre
    private void refreshClients() {
        try {
            clients = clientEJB.getClients();
        } catch (Exception e) {
            System.err.println("Error cargando clientes: " + e.getMessage());
            clients = null;
        }
    }

    //El getClients con el nombre correcto
    public Collection<Client> getClients() {
        if (clients == null) {
            refreshClients();
        }
        return clients;
    }

    public Client getSelectedClient() {
        return selectedClient;
    }

    public void setSelectedClient(Client selectedClient) {
        this.selectedClient = selectedClient;
    }

    public Client getClient() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }

    //Método insertar
    public void insertar() {
        try {
            //Comprobamos que todos los campos sean validos y esten llenos
            if (client.getName() == null || client.getName().isEmpty()
                    || client.getEmail() == null || client.getEmail().isEmpty()
                    || client.getPassword() == null || client.getPassword().isEmpty()) {
                //Si no, ponemos este mensaje de error en vez de hacer un dialog que es más facil
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Error", "Los campos son obligatorios");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }

            //Insertamos y refrescamos
            clientEJB.insert(client);
            refreshClients();
            //Reiniciamos el cliente
            client = new Client();
            //Aquí, por si no se nota, aprendí a hacer los mensajitos y me gustaron
            //mucho, por eso los empecé a poner hasta en la sopa.
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Éxito", "Cliente creado correctamente");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception e) {
            System.err.println("Error insertando cliente: " + e.getMessage());
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "No se ha creado el cliente: " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    //Método editar
    public void editar() {
        try {
            //Si el cliente es null o no tiene id da error (imposible, pero por si acaso)
            if (selectedClient == null || selectedClient.getId() == null) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Error", "No hay cliente seleccionado");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }

            //Actualiza y refresca
            clientEJB.update(selectedClient);
            refreshClients();

            //más mensajitos bonitos
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Éxito", "Cliente actualizado correctamente");
            FacesContext.getCurrentInstance().addMessage(null, message);

        } catch (Exception e) {
            System.err.println("Error editando cliente: " + e.getMessage());
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "No se ha actualizado el cliente: " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    //Método de borrar
    public void borrar() {
        try {
            //Lo mismo que en editar
            if (selectedClient == null || selectedClient.getId() == null) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Error", "No hay cliente seleccionado");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }
            clientEJB.deleteId(selectedClient.getId());
            refreshClients();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Éxito", "Cliente eliminado correctamente");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception e) {
            System.err.println("Error eliminando cliente: " + e.getMessage());
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "No se ha eliminado el cliente: " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    //Lo mismo que refresh clients
    private void refreshProducts() {
        try {
            products = clientEJB.getProducts();
        } catch (Exception e) {
            System.err.println("Error cargando productos: " + e.getMessage());
            products = null;
        }
    }

    public Collection<Product> getProducts() {
        if (products == null) {
            refreshProducts();
        }
        return products;
    }

    public Product getSelectedProduct() {
        if (selectedProduct == null) {
            selectedProduct = new Product();
            initializeProduct(selectedProduct);
        }
        return selectedProduct;
    }

    public String goLogout() {
        return logout();
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public Product getProduct() {
        if (product == null) {
            product = new Product();
            initializeProduct(product);
        }
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    //Estos son iguales que los de insertar borrar y editar clientes pero con productos
    //lo unico que tienen distinto son algunas comprobaciones y ya
    public void insertarProducto() {
        try {
            if (product.getName() == null || product.getName().isEmpty()
                    || product.getPrice() == null || product.getStock() == null) {

                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Error", "Los campos Nombre, Precio y Stock son obligatorios");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }
            if (selectedImageName != null && !selectedImageName.isEmpty()) {
                product.setUrl(selectedImageName);
            }
            clientEJB.insertProduct(product);
            selectedImageName = null;
            refreshProducts();
            product = new Product();
            initializeProduct(product);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Éxito", "Producto creado correctamente");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception e) {
            System.err.println("Error insertando producto: " + e.getMessage());
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "No se pudo crear el producto: " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void editarProducto() {
        try {
            if (selectedProduct == null || selectedProduct.getId() == null) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Error", "No hay producto seleccionado");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }

            clientEJB.updateProduct(selectedProduct);

            refreshProducts();

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Éxito", "Producto actualizado correctamente");
            FacesContext.getCurrentInstance().addMessage(null, message);

        } catch (Exception e) {
            System.err.println("Error editando producto: " + e.getMessage());
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "No se pudo actualizar el producto: " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void borrarProducto() {
        try {

            if (selectedProduct == null || selectedProduct.getId() == null) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Error", "No hay producto seleccionado");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }

            clientEJB.deleteProductId(selectedProduct.getId());
            refreshProducts();

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Éxito", "Producto eliminado correctamente");
            FacesContext.getCurrentInstance().addMessage(null, message);

        } catch (Exception e) {
            System.err.println("Error eliminando producto: " + e.getMessage());
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "No se pudo eliminar el producto: " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    //Un método auxiliar para saber el stock
    public String getProductStatus(Product p) {
        if (p == null || p.getStock() == null) {
            return "Desconocido";
        }
        if (p.getStock() > 0) {
            return "Disponible";
        } else {
            return "Agotado";
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLogeado() {
        return logeado;
    }

    public void setLogeado(boolean logeado) {
        this.logeado = logeado;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    public String getSelectedImageName() {
        return selectedImageName;
    }

    public void setSelectedImageName(String selectedImageName) {
        this.selectedImageName = selectedImageName;
    }

    public List<String> getCategories() {
        return categories;
    }

    public List<String> getColors() {
        return colors;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public String getFoto(String url) {
        if (url != null && !url.trim().isEmpty()) {
            String[] partes = url.split(",");
            if (partes.length > 0) {
                return partes[0].trim();
            }
        }
        return "";
    }

    public void prepareEditClient(Client c) {
        this.selectedClient = c;
    }

    public void prepareDeleteClient(Client c) {
        this.selectedClient = c;
    }

    public void prepareEditProduct(Product p) {
        this.selectedProduct = p;
    }

    public void prepareDeleteProduct(Product p) {
        this.selectedProduct = p;
    }

    public String goHome() {
        return "index.xhtml?faces-redirect=true";
    }
}
