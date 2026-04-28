/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package beans;

import EJBs.CartEJBLocal;
import EJBs.ClientEJB;
import static EJBs.ClientEJB.Register.FIELDS_EMPTY;
import static EJBs.ClientEJB.Register.REGISTER_EMAIL_IN_USE;
import static EJBs.ClientEJB.Register.REGISTER_SUCCESS;
import EJBs.ClientEJBLocal;
import EJBs.TiendaEJBLocal;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Client;
import modelo.LineaProducto;
import modelo.Product;

/**
 *
 * @author edria
 */
@Named(value = "tiendaBean")
@SessionScoped
public class TiendaBean implements Serializable {

    //Inyecto los tres EJBs
    @EJB
    private CartEJBLocal cartEJB;
    @EJB
    private ClientEJBLocal clientEJB;
    @EJB
    private TiendaEJBLocal tiendaEJB;

    //Declaro todas las variables
    private String email;
    private String password;
    private String cpassword;
    private boolean logeado;
    private String rol;
    private String mensajeError;
    private Collection<Product> products;
    private Integer cantidad;
    private Product product;
    private Product selectedProduct;
    private Product selectedItem;
    private Collection<Client> clients;
    private Client client;
    private Boolean clearCart;
    private Boolean removeCart;
    private Boolean productDlg;
    private Collection<Integer> convo;
    private String stock;
    private String selectedCategory;
    private String selectedImage;
    private List<String> availableSizes;
    private String selectedSize;
    private boolean loggedIn;
    private boolean mostrarDlg;
    private Product prToRemove;
    private BigDecimal priceNoDiscount;
    private Boolean compra;

    //Constructor vacío
    public TiendaBean() {
    }

    //Init general: Aqui reinicio todo a su estado por defecto
    @PostConstruct
    public void init() {
        product = new Product();
        selectedProduct = new Product();
        selectedItem = new Product();
        cantidad = 1;
        convo = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            convo.add(i);
        }
        stock = "";
        selectedCategory = "";
        clients = new ArrayList<>();
        products = new ArrayList<>();
        client = new Client();
        clearCart = false;
        productDlg = false;
        selectedImage = "";
        availableSizes = new ArrayList<>();
        selectedSize = "";
        initProducts();
        initClients();
    }

    //Init especifico de productos: Para cargar todos los productos desde la base.
    //si no tiene crea nueva lista (caso improbable)
    private void initProducts() {
        try {
            products = tiendaEJB.getProducts();
        } catch (Exception e) {
            products = new ArrayList<>();
            System.err.println("Error cargando productos: " + e.getMessage());
        }
    }

    //Lo mismo aqui, su init especifico para recoger todos los clientes
    private void initClients() {
        try {
            clients = clientEJB.getClients();
        } catch (Exception e) {
            clients = new ArrayList<>();
            System.err.println("Error cargando clientes: " + e.getMessage());
        }
    }

    //Método de login
    public String login() {
        mensajeError = null;
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            //Si los campos estan vacíos: Error propio
            mensajeError = "Email y contraseña son obligatorios";
            return null;
        }

        //Llamamos al clientEJB y tratamos de loggear (lo hacemos un objeto Login
        //porque tiene un ENUM con posibles resultados)
        ClientEJB.Login resultado = clientEJB.login(email, password);

        //Si funciona: Logeado = true y vaciamos el mensaje de error
        if (resultado == ClientEJB.Login.LOGIN_SUCCESS) {
            this.logeado = true;
            this.mensajeError = null;
            //Pillamos el cliente con su email y si existe lo asignamos
            Optional<Client> clienteOpt = clientEJB.getByMail(email);
            if (clienteOpt.isPresent()) {
                this.client = clienteOpt.get();
            }
            //Si es admin, lo que he hecho ha sido redirigirle al inicio de sesión
            //de los administradores, tiene que volver a loggearse pero así puede
            //acceder desde cualquier lado
            if ("admin".equalsIgnoreCase(email)) {
                this.logeado = false;
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/Manager-WEB/login.xhtml");
                } catch (IOException ex) {
                    System.getLogger(TiendaBean.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
                return null;
            } else {
                //Y si no es admin, su rol es evidentemente user, redirigimos al index
                this.rol = "USER";
                return "/index.xhtml?faces-redirect=true";
            }
        } else {
            //Si el cliente no existe, log a false y error de credenciales
            this.logeado = false;
            this.mensajeError = "Credenciales incorrectas";
            return null;
        }
    }

    //Al salir, log a false, email y contraseña a null, rol a null, carrito vacío,
    //creamos nuevo cliente y redirigimos al index
    public String logout() {
        this.logeado = false;
        this.email = null;
        this.password = null;
        this.rol = null;
        this.emptyCart();
        this.client = new Client();
        return "/index.xhtml";
    }

    //Método registrarse, este tiene chicha
    public String register() {
        //Si la contraseña NO es igual al campo de confirmar contraseña, error especifico
        if (!cpassword.equals(client.getPassword())) {
            mensajeError = "Las contraseñas deben coincidir";
            return null;
        }
        //Este switch con lambdas lo hizo alex el año pasado en mi ordenador,
        //entiendo como funciona pero no lo he tocado
        return switch (clientEJB.register(client.getName(), client.getEmail(), client.getPassword())) {
            case null -> {
                mensajeError = "Error en registro: resultado nulo";
                yield null;
            }
            case REGISTER_SUCCESS -> {
                client = new Client();
                mensajeError = "Registro exitoso";
                yield "login";
            }
            case REGISTER_EMAIL_IN_USE -> {
                mensajeError = "Email ya en uso";
                yield null;
            }
            case FIELDS_EMPTY -> {
                mensajeError = "Campos obligatorios vacíos";
                yield null;
            }
            default -> {
                mensajeError = "Estado de registro desconocido";
                yield null;
            }
        };
    }

    //Método de recoger los productos normal y corriente
    public Collection<LineaProducto> getProductos() {
        try {
            return cartEJB.getProducts();
        } catch (Exception e) {
            System.err.println("Error obteniendo productos del carrito: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    //Este método te recoge con un stream solo 12, porque al tirar la página
    //cargaba todos, y me tardaba en cargar 30 segundos, así que lo limite a 12
    //para que queden justo 3 lineas de 4 en plan "nuevos ingresos"
    public Collection<Product> getProductosLimitados() {
        try {
            Collection<Product> productosConDescuento = getProducts();
            return productosConDescuento.stream().limit(12).toList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    //Este método es para que muestre cuanto hay de cada producto con etiquetas
    //en lugar de numeros. Recoge disponibilidad (que devuelve un 1 (Disponible),
    //0 (Últimas unidades) y -1 (Agotado)) y lo convierte en sus respectivas etiquetas.
    public String availability(Product pcheck) {
        try {
            int disponibilidad = cartEJB.getStockAvailability(pcheck);
            switch (disponibilidad) {
                case 1 ->
                    stock = "Disponible";
                case 0 ->
                    stock = "Últimas unidades";
                case -1 ->
                    stock = "No disponible";
                default ->
                    stock = "Estado desconocido";
            }
        } catch (Exception e) {
            stock = "Error verificando disponibilidad";
            System.err.println("Error en availability: " + e.getMessage());
        }
        return stock;
    }

    //El método de añadir al carrito es simple, si la cantidad que pide el client
    //no es nula y no es menor o igual a 0, le ponemos esa cantidad
    public void addCart(Product addProduct) {
        int qty = (cantidad == null || cantidad <= 0) ? 1 : cantidad;
        //Y si la cantidad que pide es mayor que el stock disponible, ponemos
        //el stock maximo en su pedido (no he logrado que le dialogo funcione)
        if (cantidad > addProduct.getStock()) {
            mostrarDlg = true;
        } else {
            LineaProducto lp = new LineaProducto(addProduct, cantidad, addProduct.getPrice());
            cartEJB.addCart(lp);
        }
        //Reseteamos cantidad
        cantidad = 1;
    }

    //RemoveCart elimina un producto concreto del carrito
    public void removeCart(Product removeProduct) {
        try {
            //Si no es null borramos ese, si es null el que nos mande
            Product productoAEliminar = (prToRemove != null) ? prToRemove : removeProduct;
            //Y aqui borramos la linea y reseteamos
            cartEJB.deleteLine(productoAEliminar, 1);
            removeCart = false;
            prToRemove = null;
        } catch (Exception e) {
            System.err.println("Error eliminando del carrito: " + e.getMessage());
        }
    }

    //Vacía el carrito, sin más misterio
    public void emptyCart() {
        try {
            cartEJB.emptyCart();
            clearCart = false;
        } catch (SQLException ex) {
            System.err.println("Error vaciando carrito: " + ex.getMessage());
        } catch (Exception e) {
            System.err.println("Error general vaciando carrito: " + e.getMessage());
        }
    }

    //Calcula el precio total de los productos del carrito
    public BigDecimal showTotalCart() {
        try {
            //Aqui recogemos al cliente para mirar su descuento y aplicarlo al
            //precio total
            if (isLogeado() && email != null && !email.isEmpty()) {
                Optional<Client> clienteOpt = clientEJB.getByMail(email);
                if (clienteOpt.isPresent()) {
                    Client cliente = clienteOpt.get();
                    //(True es el boolean de si esta loggeado)
                    return cartEJB.totalCalc(true, cliente.getDiscount());
                }
            }
            //No esta loggeado y tiene descuento 0
            return cartEJB.totalCalc(false, 0);
        } catch (Exception e) {
            System.err.println("Error calculando total: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    //Este método es solo para mostrar el descuento de cada cliente, sin mucho
    //misterio
    public Integer showDiscountCart() {
        try {
            if (isLogeado() && email != null && !email.isEmpty()) {
                Optional<Client> clienteOpt = clientEJB.getByMail(email);
                if (clienteOpt.isPresent()) {
                    Client cliente = clienteOpt.get();
                    return cliente.getDiscount();
                }
            }
            return 0;
        } catch (Exception e) {
            System.err.println("Error obteniendo descuento: " + e.getMessage());
            return 0;
        }
    }

    //Método de comprar
    public int buy(String email) {
        try {
            //Metemos un retardo ligero al pulsar por pura estética
            TimeUnit.SECONDS.sleep((long) 1.5);
            int resultado = 0;
            //Si el cliente esta loggeado compramos con su id
            if (isLogeado() && email != null && !email.isEmpty()) {
                Optional<Client> clienteOpt = clientEJB.getByMail(email);
                if (clienteOpt.isPresent()) {
                    resultado = cartEJB.buy(clienteOpt.get().getId());
                }
            } else {
                //Si no compramos con -1 (defecto)
                resultado = cartEJB.buy(Long.valueOf(-1));
            }
            //Si resultado devuelve 1 (ha funcionado) vaciamos el carrito
            if (resultado == 1) {
                emptyCart();
            }
            openPurchaseDoneDlg();
            return resultado;
        } catch (InterruptedException ex) {
            Logger.getLogger(TiendaBean.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        } catch (Exception e) {
            System.err.println("Error en compra: " + e.getMessage());
            return 0;
        }
    }

    //Abrimos y cerramos dialogos
    public void openClearCartDlg() {
        clearCart = true;
    }

    public void closeClearCartDlg() {
        clearCart = false;
    }

    public void openPurchaseDoneDlg() {
        compra = true;
    }

    public void closePurchaseDoneDlg() {
        compra = false;
    }

    public void openRemoveCartDlg(Product producto) {
        this.prToRemove = producto;
        this.removeCart = true;
    }

    public void closeRemoveCartDlg() {
        removeCart = false;
    }

    //Getter perdido, no se por qué
    public Boolean getRemoveCart() {
        return removeCart;
    }

    public void setRemoveCart(Boolean removeCart) {
        this.removeCart = removeCart;
    }

    //Método para abrir un producto
    public String openProductDlg(Product productoSeleccionado) {
        selectedItem = productoSeleccionado;
        //Si la url del producto no es null, sacamos sus imagenes
        if (selectedItem.getUrl() != null) {
            List<String> images = getProductImages(selectedItem.getUrl());
            if (!images.isEmpty()) {
                selectedImage = images.get(0);
            }
        }
        //Si la talla del producto no es null, sacamos las tallas
        if (selectedItem.getSize() != null) {
            availableSizes = getProductSizes(selectedItem.getSize());
            if (!availableSizes.isEmpty()) {
                selectedSize = availableSizes.get(0);
            }
        }
        //redirigimos al xhtml de producto con redirect, ya hemos asignado el producto
        //que tiene que salir
        return "/producto.xhtml?faces-redirect=true";
    }

    //Métodos para moversea
    public String closeProductDlg() {
        return "index";
    }

    public String goHome() {
        //Méto initproducts en cada uno porque cada vez que te mueves al index
        //la lista de productos se reinicia para quitar los filtros que hayas puesto
        initProducts();
        return "index";
    }

    public String goCart() {
        initProducts();
        return "cart";
    }

    public String goLogin() {
        initProducts();
        mensajeError = "";
        return "login";
    }

    public String goRegister() {
        initProducts();
        mensajeError = "";
        return "register?faces-redirect=true";
    }

    //Método para ir a las listas
    public String goLists(String category) {
        this.selectedCategory = category;
        //Si la categoría es una de estas tres (Alojadas en el campo descripción) va a ellas
        if (category.equals("Urban") || category.equals("Minimal") || category.equals("Street")) {
            products = tiendaEJB.getProductsByDescription(category);
        }
        //Si es una de estas tres (Alojada en el campo categoría)
        if (category.equals("Gorras") || category.equals("Camisetas") || category.equals("Pantalones") || category.equals("Sudaderas")) {
            products = tiendaEJB.getProductsByCategory(category);
        }
        return "lists";
    }

    //Splitter para sacar las imagenes (las tengo en formato f1.jpg,f2.jpg,f3.jpg)
    public List<String> getProductImages(String url) {
        List<String> images = new ArrayList<>();
        if (url != null && !url.trim().isEmpty()) {
            String[] imageArray = url.split(",");
            for (String img : imageArray) {
                images.add(img.trim());
            }
        }
        return images;
    }

    //Splitter para sacar las tallas, las tengo exactamente igual
    public List<String> getProductSizes(String talla) {
        List<String> sizes = new ArrayList<>();
        if (talla != null && !talla.trim().isEmpty()) {
            String[] sizeArray = talla.split(",");
            for (String size : sizeArray) {
                sizes.add(size.trim());
            }
        }
        return sizes;
    }

    public String goLogout() {
        return logout();
    }

    //Getter setters
    public Collection<Product> getProducts() {
        if (isLogeado()) {
            ArrayList<Product> listaDescontada = new ArrayList<>();
            client = clientEJB.getByMail(email).get();
            for (Product p : products) {
                listaDescontada.add(cartEJB.precioDescontado(isLogeado(), client.getDiscount(), p));
            }
            return listaDescontada;
        }
        return products;
    }

    public void setProducts(Collection<Product> products) {
        this.products = products;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Collection<Client> getClients() {
        return clients;
    }

    public void setClients(Collection<Client> clients) {
        this.clients = clients;
    }

    public Collection<Integer> getConvo() {
        return convo;
    }

    public void setConvo(Collection<Integer> convo) {
        this.convo = convo;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public Boolean getClearCart() {
        return clearCart;
    }

    public void setClearCart(Boolean clearCart) {
        this.clearCart = clearCart;
    }

    public Boolean getProductDlg() {
        return productDlg;
    }

    public void setProductDlg(Boolean productDlg) {
        this.productDlg = productDlg;
    }

    public Product getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Product selectedItem) {
        this.selectedItem = selectedItem;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return "";
    }

    public boolean isLoggedin() {
        return logeado;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public String getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(String selectedImage) {
        this.selectedImage = selectedImage;
    }

    public List<String> getAvailableSizes() {
        return availableSizes;
    }

    public void setAvailableSizes(List<String> availableSizes) {
        this.availableSizes = availableSizes;
    }

    public String getSelectedSize() {
        return selectedSize;
    }

    public void setSelectedSize(String selectedSize) {
        this.selectedSize = selectedSize;
    }

    public String getCpassword() {
        return cpassword;
    }

    public void setCpassword(String cpassword) {
        this.cpassword = cpassword;
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

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isMostrarDlg() {
        return mostrarDlg;
    }

    public void setMostrarDlg(boolean mostrarDlg) {
        this.mostrarDlg = mostrarDlg;
    }

    public Product getPrToRemove() {
        return prToRemove;
    }

    public void setPrToRemove(Product prToRemove) {
        this.prToRemove = prToRemove;
    }

    public BigDecimal getPriceNoDiscount(Product selectedProd) {
        if (isLogeado()) {
            try {
                Optional<Client> clienteOpt = clientEJB.getByMail(email);
                if (clienteOpt.isPresent()) {
                    Client cliente = clienteOpt.get();
                    if (cliente.getDiscount() != null && cliente.getDiscount() > 0) {
                        Product nuevo = cartEJB.precioOriginal(cliente.getDiscount(), selectedProd);
                        return nuevo.getPrice();
                    }
                }
            } catch (Exception e) {
                System.err.println("Error en getPriceNoDiscount: " + e.getMessage());
            }
        }
        return selectedProd.getPrice();
    }

    public void setPriceNoDiscount(BigDecimal priceNoDiscount) {
        this.priceNoDiscount = priceNoDiscount;
    }

    public Boolean isCompra() {
        return compra;
    }

    public void setCompra(Boolean compra) {
        this.compra = compra;
    }

    public BigDecimal getPriceOriginal(Product product) {
        if (isLogeado()) {
            try {
                Optional<Client> clienteOpt = clientEJB.getByMail(email);
                if (clienteOpt.isPresent()) {
                    Client cliente = clienteOpt.get();
                    if (cliente.getDiscount() != null && cliente.getDiscount() > 0) {
                        Product productoSinDescuento = cartEJB.precioOriginal(cliente.getDiscount(), product);
                        return productoSinDescuento.getPrice();
                    }
                }
            } catch (Exception e) {
                System.err.println("Error calculando precio original: " + e.getMessage());
            }
        }
        return product.getPrice();
    }
}
