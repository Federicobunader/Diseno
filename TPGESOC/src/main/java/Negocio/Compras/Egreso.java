
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

    @ManyToOne
    @JoinColumn(name = "ingreso_id", referencedColumnName = "id")
    private Ingreso ingresoAVincular;

    @Column
    private boolean estaVinculado;

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

    public boolean isEstaVinculado() {
        return estaVinculado;
    }

    public void setEstaVinculado(boolean estaVinculado) {
        this.estaVinculado = estaVinculado;
    }

    public void agregarCompraYPresupuesto(){
        gestorDeEgresos.registrarCompra(compra);
    }
    public boolean estaEnElPeriodoAceptable(Date fechaInicial,Date fechaFinal){
        return fechaDeOperacion.after(fechaInicial) && fechaDeOperacion.before(fechaFinal);
    }
    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Ingreso getIngreso() {
        return ingresoAVincular;
    }

    public void setIngreso(Ingreso ingreso) {
        this.ingresoAVincular = ingreso;
    }
}

