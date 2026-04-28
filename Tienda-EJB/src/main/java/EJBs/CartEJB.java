/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatefulEjbClass.java to edit this template
 */
package EJBs;

import dao.DaoInterfazClient;
import dao.DaoInterfazProduct;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateful;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.LineaProducto;
import modelo.Product;

/**
 *
 * @author edria
 */
@Stateful
public class CartEJB implements CartEJBLocal {

    //Inyecto EJBs
    @EJB
    private DaoInterfazProduct productDao;
    @EJB
    private DaoInterfazClient clientDao;

    private Collection<LineaProducto> cart;

    //Inicializamos el carrito
    @PostConstruct
    public void initCart() {
        cart = new ArrayList<>();
    }

    //Override de las interfaces vacío
    @Override
    public void addLine(LineaProducto lp) {
    }

    //Método para borrar una linea del carrito, si el producto existe y tiene id la borra
    @Override
    public void deleteLine(Product p, Integer cantidad) {
        if (p == null || p.getId() == null) {
            return;
        }

        cart.removeIf(lp
                -> lp.getP() != null
                && lp.getP().getId() != null
                && lp.getP().getId().equals(p.getId())
        );
    }

    //Método para añadir al carrito
    @Override
    public int addCart(LineaProducto lp) {
        //Para determinar si ya hay un producto en el carrito de ese tipo y sumarlo
        boolean copia = false;
        int retorno = 0;
        //Por cada producto en el carrito lo añade a la suma, si es más de 10 lo pone a 10
        for (LineaProducto lp1 : cart) {
            if (lp1.getP().equals(lp.getP())) {
                int suma = lp1.getQuantity() + lp.getQuantity();
                if (suma > 10) {
                    retorno = -1;
                }
                lp1.setQuantity(suma);
                copia = true;
                break;
            }
        }

        //Si copia es false añadimos linea
        if (!copia) {
            cart.add(lp);
        }
        return retorno;
    }

    //Vaciar el carrito
    @Override
    public void emptyCart() {
        cart.clear();
    }

    @Override
    public Collection<LineaProducto> getProducts() {
        return cart;
    }

    public void setCart(Collection<LineaProducto> cart) {
        this.cart = cart;
    }

    //Este es el método que se encarga de sacar la disponibilidad
    @Override
    public int getStockAvailability(Product product) {
        int i = 0;
        int retorno = -1;
        try {
            i = productDao.getStock(product);
        } catch (Exception ex) {
            Logger.getLogger(TiendaEJB.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Depende del numero de stock que devuelva la base, manda un numero u otro
        if (i >= 10) {
            retorno = 1; //Disponible
        }
        if (i < 10 && i > 1) {
            retorno = 0; //Ultimas unidades
        }
        if (i <= 0) {
            retorno = -1; //Agotado
        }
        return retorno;
    }

    //Este metodo calcula cuando tiene el carrito
    @Override
    public BigDecimal totalCalc(boolean logged, Integer discount) {
        BigDecimal retorno = BigDecimal.ZERO;
        for (LineaProducto lp : cart) {
            retorno = retorno.add(lp.getTotal());
        }
        if (logged) {
            //Francamente yo tampoco se como funciona esto,
            BigDecimal factorDescuento = BigDecimal.ONE.subtract(BigDecimal.valueOf(discount).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
            retorno = retorno.multiply(factorDescuento).setScale(2, RoundingMode.HALF_UP);
        }
        return retorno;
    }

    @Override
    public Product precioDescontado(boolean logged, Integer discount, Product p) {
        if (logged && discount != null && discount > 0) {
            Product productoConDescuento = new Product();
            productoConDescuento.setId(p.getId());
            productoConDescuento.setName(p.getName());
            productoConDescuento.setDescription(p.getDescription());
            productoConDescuento.setCategory(p.getCategory());
            productoConDescuento.setRating(p.getRating());
            productoConDescuento.setUrl(p.getUrl());
            productoConDescuento.setColor(p.getColor());
            productoConDescuento.setPrice(p.getPrice());
            productoConDescuento.setSize(p.getSize());
            productoConDescuento.setStock(p.getStock());

            BigDecimal factorDescuento = BigDecimal.ONE.subtract(
                    BigDecimal.valueOf(discount).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
            );
            BigDecimal precioDescontado = p.getPrice().multiply(factorDescuento)
                    .setScale(2, RoundingMode.HALF_UP);
            productoConDescuento.setPrice(precioDescontado);

            return productoConDescuento;
        }
        return p;
    }

    @Override
    public Product precioOriginal(Integer discount, Product p) {
        if (discount != null && discount > 0) {
            Product productoSinDescuento = new Product();
            productoSinDescuento.setId(p.getId());
            productoSinDescuento.setName(p.getName());
            productoSinDescuento.setDescription(p.getDescription());
            productoSinDescuento.setCategory(p.getCategory());
            productoSinDescuento.setRating(p.getRating());
            productoSinDescuento.setUrl(p.getUrl());
            productoSinDescuento.setColor(p.getColor());
            productoSinDescuento.setSize(p.getSize());
            productoSinDescuento.setStock(p.getStock());
            BigDecimal factorDescuento = BigDecimal.ONE.subtract(
                    BigDecimal.valueOf(discount).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
            );
            if (factorDescuento.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal precioOriginal = p.getPrice()
                        .divide(factorDescuento, 2, RoundingMode.HALF_UP);
                productoSinDescuento.setPrice(precioOriginal);
            } else {
                productoSinDescuento.setPrice(p.getPrice());
            }

            return productoSinDescuento;
        }
        return p;
    }

    //Método de comprar
    @Override
    public int buy(Long idClient) {
        int retorno = 0;

        //Por cada linea producot, saca el stock le resta la cantidad y compra
        for (LineaProducto lp : cart) {
            try {
                if (productDao.getStock(lp.getP()) - lp.getQuantity() >= 0) {
                    productDao.buy(lp.getP(), lp.getQuantity());
                    retorno = 1;
                } else {
                    return -1;
                }
            } catch (Exception ex) {
                Logger.getLogger(CartEJB.class.getName()).log(Level.SEVERE, null, ex);
                return 0;
            }
        }
        //Update purchase es para subirle +1 al numero de compras del cliente, si esta
        //loggeado le sube 1
        if (idClient != null && idClient > 0) {
            try {
                clientDao.updatePurchase(idClient);

                int compras = clientDao.getPurchases(idClient);
                int discountUpd = 0;

                //Si las compras son 5, 15% de descuento, y así con todas las demás metas
                if (compras == 5) {
                    discountUpd = 15;
                } else if (compras == 10) {
                    discountUpd = 25;
                } else if (compras > 10) {
                    discountUpd = 5;
                }
                clientDao.updateDiscount(idClient, discountUpd);
            } catch (Exception ex) {
                Logger.getLogger(CartEJB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return retorno;
    }
}
