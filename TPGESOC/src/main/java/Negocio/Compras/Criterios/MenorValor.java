package Negocio.Compras.Criterios;

import Negocio.Compras.Presupuesto;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MenorValor extends Criterio {


    public MenorValor() {
    }

    public Presupuesto elegirPresupuesto(List<Presupuesto> presupuestos) {
        double presupuestoMasBarato = this.menorValor(presupuestos);

        for (int i = 0; i < presupuestos.size(); i++) {
            if (presupuestos.get(i).valorTotal() == presupuestoMasBarato) {
                return presupuestos.get(i);
            }
        }
        return null;
    }

    private double menorValor(List<Presupuesto> presupuestos) {
        return this.valoresTotales(presupuestos).stream().min(Comparator.naturalOrder()).get();
    }

    private List<Double> valoresTotales(List<Presupuesto> presupuestos){
        return presupuestos.stream().map(presupuesto -> presupuesto.valorTotal()).collect(Collectors.toList());
    }
}
