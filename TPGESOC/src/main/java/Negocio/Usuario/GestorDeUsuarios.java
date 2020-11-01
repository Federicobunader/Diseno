package Negocio.Usuario;

import InterfazDeUsuario.InterfazUsuarios;
import Negocio.Compras.GestorDeCriterios;
import Negocio.Compras.GestorDeEgresos;
import Negocio.Compras.GestorDeIngresos;
import Negocio.Compras.Vinculacion.*;
import Negocio.Main;
import repositories.RepositorioDeUsuarios;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class GestorDeUsuarios {

    private ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

    GestorDePasswords gestorDePasswords = GestorDePasswords.GetInstance();
    InterfazUsuarios interfazUsuarios = InterfazUsuarios.GetInstance();
    GestorDeEgresos gestorDeEgresos = GestorDeEgresos.GetInstance();
    GestorDeIngresos gestorDeIngresos = GestorDeIngresos.GetInstance();

    private static GestorDeUsuarios instance = null;

    private GestorDeUsuarios() {
    }

    public static GestorDeUsuarios GetInstance() {
        if (instance == null)
            instance = new GestorDeUsuarios();
        return instance;
    }

    public void consolaUsuario(){
        interfazUsuarios.menuUsuario();
        int opcion = Main.pedirPorPantallaInt();
        GestorDeCriterios gestorDeCriterios = GestorDeCriterios.GetInstance();

        while (opcion != 10) {
            switch (opcion) {
                case 1:
                   // this.registrarUsuario();
                    break;
                case 2:
                  //  this.loguearse();
                    break;
                case 3:
                    Usuario unUsuario = this.elegirUsuario();
                    ;
                case 4:
                    this.mostrarUsuarios();
                    break;
                case 5:
                    gestorDeCriterios.agregarCriterio("");
                    break;
                case 6:
                    gestorDeCriterios.agregarCategoria();
                    break;
                case 7:
                    gestorDeCriterios.mostrarCategorias();
                    break;
                case 8:
                    gestorDeIngresos.agregarIngreso();
                    break;
                case 9:
                    String passwordAdmin = interfazUsuarios.pedirString("Ingrese la Contraseña de administrador");

                    if(passwordAdmin.equals("GESOC")){

                        String criterio = interfazUsuarios.pedirString("Ingrese el criterio que desea utilizar para la vinculación: \n 1- Orden valor primero egreso \n 2- Orden valor primero ingreso \n 3- Por fecha \n 4- Mix");

                        CriterioDeVinculacion criterioElegido = new OrdenValorPrimeroIngreso();
                        switch (criterio){
                            case "1":
                                criterioElegido = new OrdenValorPrimeroEgreso();
                                break;
                            case "2":
                                criterioElegido = new OrdenValorPrimeroIngreso();
                                break;
                            case "3":
                                criterioElegido = new PorFecha();
                                break;
                            case "4":
                                criterioElegido = new Mix();
                                break;
                            default:
                                interfazUsuarios.mostrarError("No existe esa opcion");
                        }

                        if(criterio.equals("4")){
                            interfazUsuarios.mostrarInformacion("Coming soon");
                        }else {
                            criterioElegido.vincular();
                            interfazUsuarios.mostrarInformacion("Vinculación efectuada con éxito");
                        }

                    }else{
                        interfazUsuarios.mostrarError("Error: Contraseña incorrecta.");
                    }
                    break;
                default:
                    interfazUsuarios.mostrarError("La opcion ingresada no es valida.");
            }
            interfazUsuarios.menuUsuario();

            opcion = Main.pedirPorPantallaInt();
        }
    }

    private void mostrarUsuarios(){
        interfazUsuarios.mostrarInformacion("Usuarios Existentes :");
        if (!this.laListaDeUsuariosEstaVacia()) {
            for (int i = 0; i < usuarios.size(); i++) {
                interfazUsuarios.mostrarInformacion(" -----------------------------");
                interfazUsuarios.mostrarInformacion("Nombre de Usuario: " + usuarios.get(i).getUsuario());
                interfazUsuarios.mostrarInformacion("Password de Usuario: " + usuarios.get(i).getPassword());
                interfazUsuarios.mostrarInformacion(" -----------------------------");
            }
        }else{
           interfazUsuarios.mostrarError("No hay usuarios para mostrar.");
        }
    }

    public boolean nombreDeUsuarioVacio(String nombreDeUsuario){
        boolean esVacio = false;
         if(nombreDeUsuario.isEmpty()){
             interfazUsuarios.mostrarError("El nombre de usuario no puede estar vacio");
             esVacio = true;
         }
        return esVacio;
    }

    public boolean laListaDeUsuariosEstaVacia() {
        return usuarios.isEmpty();
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public boolean usuarioYaExiste(String nombreDeUsuario) {
        if (!this.laListaDeUsuariosEstaVacia()) {
            for (int i = 0; i < usuarios.size(); i++) {
                if (usuarios.get(i).elNombreCoincide(nombreDeUsuario)) {
                    interfazUsuarios.mostrarError("El usuario ya existe");
                    return true;
                }
            }
        }
        return false;
    }

    public Usuario buscarUsuario(String nombreDeUsuario) {
        if (!this.laListaDeUsuariosEstaVacia()) {
            for (int i = 0; i < usuarios.size(); i++) {
                if (usuarios.get(i).elNombreCoincide(nombreDeUsuario)) {
                    return usuarios.get(i);
                }
            }
        }
        interfazUsuarios.mostrarError("Usuario No Encontrado");
        return null;
    }

    public void loguearse(String nombreUsuario, String password_hasheada, RepositorioDeUsuarios repoUsuarios) {
        int contador = 0;

        if( ! repoUsuarios.existe(nombreUsuario,password_hasheada) && contador < 10) {

            Usuario usuario = repoUsuarios.buscarUsuario(nombreUsuario,password_hasheada);
            interfazUsuarios.mostrarError("Password invalida.");
            interfazUsuarios.mostrarAdvertencia("Intentos Restantes: " + (10 - contador));
            contador += 1;
            interfazUsuarios.mostrarAdvertencia("Por favor espere " + contador * 10 + " segundos.");
            repoUsuarios.buscar(usuario.getId());
            usuario.setIntentos(contador);
            repoUsuarios.modificar(usuario);

            try {
                TimeUnit.SECONDS.sleep(contador * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (contador == 10) {
            interfazUsuarios.mostrarAdvertencia("Has gastado todos los intentos. Vuelve a intentarlo mas tarde.");
        }
    }

    public void agregarUsuario(String usuario, String passwordHasheada) {
        Usuario usuario1 = new Usuario();
        usuario1.setUsuario(usuario);
        usuario1.setPassword(passwordHasheada);
        usuarios.add(usuario1);
    }

    public Usuario elegirUsuario() {
        String nombreDeUsuario = interfazUsuarios.pedirString("Ingrese su Usuario");
        while (this.buscarUsuario(nombreDeUsuario) == null) {
            nombreDeUsuario = interfazUsuarios.pedirString("Ingrese su Usuario nuevamente");
        }
        return this.buscarUsuario(nombreDeUsuario);
    }

    public void registrarUsuario(String usuario) {
        while (this.nombreDeUsuarioVacio(usuario) || this.usuarioYaExiste(usuario)) {
            usuario = interfazUsuarios.pedirString("Ingrese nuevamente un Usuario:");
        }
        String password = gestorDePasswords.setRandomPassword();
        String opcion = interfazUsuarios.pedirString("Su password sugerida es: " + password + ", desea cambiarla? si, no");

        if (opcion.equals("si")) {
            password = interfazUsuarios.pedirString("Ingrese una Password que cuente con al menos una minuscula, una mayuscula, un numero y/o un simbolo:");
            //password = gestorDePasswords.verificarPassword(password,usuario);
        }

        String passwordHasheada = gestorDePasswords.hashearPassword(password);
        interfazUsuarios.mostrarInformacion("La password hasheada es " + passwordHasheada);

        gestorDePasswords.seguridadClave(password);

       interfazUsuarios.mostrarInformacion("Usuario: " + usuario);
       interfazUsuarios.mostrarInformacion("Password: " + password);

        this.agregarUsuario(usuario, passwordHasheada);
    }
}
