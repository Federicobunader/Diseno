package controllers;

import Negocio.Entidad.Empresa.Empresa;
import Negocio.Usuario.DireccionPostal;
import Negocio.Usuario.Usuario;
import repositories.Repositorio;
import repositories.factories.FactoryRepositorio;
import spark.Request;
import spark.Response;

public class EmpresaController {

    private Repositorio<Empresa> repo;

    public EmpresaController() {
        this.repo = FactoryRepositorio.get(Empresa.class);
    }

    private void asignarAtributosA(Empresa unaEmpresa, Request request) {
        if (request.queryParams("RazonSocial") != null) {
            unaEmpresa.setRazonSocial(request.queryParams("RazonSocial"));
        }

        if (request.queryParams("NombreFicticio") != null) {
            unaEmpresa.setNombreFicticio(request.queryParams("NombreFicticio"));
        }

        if (request.queryParams("CUIT") != null) {
            int cuit = Integer.valueOf(request.queryParams("CUIT"));
            unaEmpresa.setCUIT(cuit);
        }

        if (request.queryParams("CodigoDeInscripcion") != null) {
            int codigoIGJ = Integer.valueOf(request.queryParams("CodigoDeInscripcion"));
            unaEmpresa.setCodigoDeInscripcion(codigoIGJ);
        }

        if (request.queryParams("seleccionTipoDeSector") != null) { //SECTOR
            unaEmpresa.setSeleccionTipoDeSector(request.queryParams("seleccionTipoDeSector"));
        }

        if (request.queryParams("cantidadDePersonal") != null) {
            int cantidad = Integer.valueOf(request.queryParams("cantidadDePersonal"));
            unaEmpresa.setCantidadDePersonal(cantidad);
        }

        if (request.queryParams("promedioDeVentasAnuales") != null) {
            double cantidad = new Double (request.queryParams("promedioDeVentasAnuales"));
            unaEmpresa.setPromedioDeVentasAnuales(cantidad);
        }

        if (request.queryParams("Email") != null) {
            unaEmpresa.getUsuario().setMail(request.queryParams("Email"));
        }

        if (request.queryParams("Usuario") != null) {
            unaEmpresa.getUsuario().setUsuario(request.queryParams("Usuario"));
        }

        if (request.queryParams("Password") != null) {
            unaEmpresa.getUsuario().setPassword(request.queryParams("Password"));
        }

        if (request.queryParams("altura") != null) {
            int altura = Integer.valueOf(request.queryParams("altura"));
            unaEmpresa.getUsuario().getDireccionPostal().setAltura(altura);
        }

        if (request.queryParams("calle") != null) {
            unaEmpresa.getUsuario().getDireccionPostal().setCalle(request.queryParams("calle"));
        }

        if (request.queryParams("piso") != null) {
            int piso = Integer.valueOf(request.queryParams("piso"));
            unaEmpresa.getUsuario().getDireccionPostal().setPiso(piso);
        }

        if (request.queryParams("departamento") != null) {
            unaEmpresa.getUsuario().getDireccionPostal().setDepartamento(request.queryParams("departamento"));
        }

        if (request.queryParams("pais") != null) {
            unaEmpresa.getUsuario().getDireccionPostal().setPais(request.queryParams("pais"));
        }
        if (request.queryParams("provincia") != null) {
            unaEmpresa.getUsuario().getDireccionPostal().setProvincia(request.queryParams("provincia"));
        }
        if (request.queryParams("ciudad") != null) {
            unaEmpresa.getUsuario().getDireccionPostal().setCiudad(request.queryParams("ciudad"));
        }

    }

    public Response guardar(Request request, Response response){

        Empresa unaEmpresa = new Empresa();
        Usuario unUsuario = new Usuario();
        DireccionPostal direccionPostal = new DireccionPostal();

        DireccionPostalController direccionPostalController = new DireccionPostalController();
        direccionPostalController.asignarAtributosA(direccionPostal,request);

        Repositorio<DireccionPostal> repoDireccion = FactoryRepositorio.get(DireccionPostal.class);
        repoDireccion.agregar(direccionPostal);

        UsuarioController usuarioController = new UsuarioController();
        usuarioController.asignarAtributosA(unUsuario,request);

        Repositorio<Usuario> repoUsuario = FactoryRepositorio.get(Usuario.class);
        repoUsuario.agregar(unUsuario);

        unUsuario.setDireccionPostal(direccionPostal);
        unaEmpresa.setUsuario(unUsuario);

        asignarAtributosA(unaEmpresa,request);
        System.out.println("RAZON SOCIAL : "+ unaEmpresa.getRazonSocial());
        this.repo.agregar(unaEmpresa);


        response.redirect("/menu_logueado");
        return response;
    }
}
