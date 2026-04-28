/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author edria
 */
public class LineaProducto {

    private Product p;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal total;

    public LineaProducto(Product p, Integer quantity, BigDecimal price) {
        this.p = p;
        this.quantity = quantity;
        this.price = price;
        recalcularTotal();
        System.err.println("[LP CONSTRUCTOR] " + this);
    }

    private void recalcularTotal() {
        if (price != null && quantity != null) {
            this.total = price.multiply(BigDecimal.valueOf(quantity));
        } else {
            this.total = BigDecimal.ZERO;
        }
        System.err.println("[LP RECALC] qty=" + quantity + " price=" + price + " total=" + total);

    }

    public Product getP() {
        return p;
    }

    public void setP(Product p) {
        this.p = p;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        System.err.println("[LP SET QTY] nueva cantidad=" + quantity);
        recalcularTotal();
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
        recalcularTotal();
    }

    public BigDecimal getTotal() {
        return total;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.p);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LineaProducto other = (LineaProducto) obj;
        return Objects.equals(this.p, other.p);
    }

    @Override
    public String toString() {
        return "LineaProducto{" + "p=" + p + ", quantity=" + quantity + ", price=" + price + ", total=" + total + '}';
    }

}
