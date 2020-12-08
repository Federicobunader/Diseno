package Negocio.Compras;

import BaseDeDatos.EntidadPersistente;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="egreso")
public class Egreso  extends EntidadPersistente {

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "compra_id", referencedColumnName = "id")
    private Compra compra;

    @Column(columnDefinition = "DATE")
    private Date fechaDeOperacion;

    @Column
    private double valorTotal;



    @Transient
    GestorDeEgresos gestorDeEgresos = GestorDeEgresos.GetInstance();

    public Egreso(Compra unaCompra, Date unaFechaDeOperacion, double unValorTotal) {
        this.compra = unaCompra;
        this.fechaDeOperacion = unaFechaDeOperacion;
        this.valorTotal = unValorTotal;
    }

    public Egreso() {
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public void setFechaDeOperacion(Date fechaDeOperacion) {
        this.fechaDeOperacion = fechaDeOperacion;
    }

    public void setGestorDeEgresos(GestorDeEgresos gestorDeEgresos) {
        this.gestorDeEgresos = gestorDeEgresos;
    }

    public Compra getCompra() {
        return compra;
    }

    public Date getFechaDeOperacion() {
        return fechaDeOperacion;
    }

    public void agregarCompraYPresupuesto(){
        gestorDeEgresos.registrarCompra(compra);
    }

    public boolean estaEnElPeriodoAceptable(){
        return fechaDeOperacion.after(gestorDeEgresos.getFechaAcceptableDesde()) && fechaDeOperacion.before(gestorDeEgresos.getFechaAcceptableHasta());
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
}
