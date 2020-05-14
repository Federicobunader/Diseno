package dominio;//package Negocio;


import dominio.TipoDeSector.Agropecuaria;
import dominio.TipoDeSector.TipoDeSector;
import dominio.TiposDeEmpresas.TipoDeEmpresa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) {

        Empresa unaEmpresa = new Empresa('A',100,50, Agropecuaria.GetInstance());
        unaEmpresa.contratarPersonal(100);
        unaEmpresa.despedirPersonal(500);

        Sistema sistema = Sistema.GetInstance();
        sistema.arrancar();
    }



    public static String pedirPorPantallaString() {
        String res = "";
        try {
            BufferedReader entrada =
                    new BufferedReader(new InputStreamReader(System.in));
            res = entrada.readLine();
        }
        catch (IOException e) {}

        return res;
    }

    public static int pedirPorPantallaInt() {
        String dato = pedirPorPantallaString();

        int res = Integer.parseInt(dato);
        return res;
    }
}

