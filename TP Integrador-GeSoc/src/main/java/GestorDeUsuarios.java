import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;


public class GestorDeUsuarios {



    private ArrayList<Usuario> usuarios = null;

    private static GestorDeUsuarios instance = null;
    private GestorDeUsuarios() {}
    public static GestorDeUsuarios GetInstance()
    {
        if (instance == null)
            instance = new GestorDeUsuarios();
        return instance;
    }





    public void consolaUsuario(){
        this.menuUsuario();
        int opcion = Main.pedirPorPantallaInt();

        while(opcion!=3)
        {
            if(opcion==1)
            {
                this.registrarUsuario();
            }
            else if(opcion==2)
            {
                Usuario unUsuario = this.elegirUsuario();
                this.ingresarNuevaPassword(unUsuario);
            }

            this.menuUsuario();

            opcion = Main.pedirPorPantallaInt();
        }
    }

    private void menuUsuario() //Menu
    {
        System.out.println("Ingrese una opcion:");
        System.out.println(" ");
        System.out.println("1- REGISTRAR USUARIO");
        System.out.println("2- CAMBIAR CONTRASEÑA");
        System.out.println("3- SALIR");
        System.out.println(" ");
    }

    private void registrarUsuario(){
        System.out.println("Ingrese un Usuario :");
        String usuario = Main.pedirPorPantallaString();
        while(usuarioYaExiste(usuario)) {
            System.out.println("Ya existe un usuario con ese nombre, ingrese uno nuevo: ");
            usuario = Main.pedirPorPantallaString();
        }

        String password = setRandomPassword();
        System.out.println("Su contraseña sugerida es: " + password + ", desea cambiarla? si, no");
        String opcion = Main.pedirPorPantallaString();

        if(opcion.equals("si")){
            System.out.println("Ingrese una Contraseña:");
            password = Main.pedirPorPantallaString();
            password = verificarPassword(password);
        }

        seguridadClave(password);

        System.out.println("Usuario: " + usuario);
        System.out.println("Contraseña: " + password);

        usuarios.add(new Usuario(usuario,password));
    }

    private boolean laListaDeUsuariosEstaVacia(){
        return usuarios.isEmpty();
    }

    private boolean usuarioYaExiste(String nombreDeUsuario){
        System.out.println("1");
        if(!this.laListaDeUsuariosEstaVacia()) {
            System.out.println("2");
            for (int i = 0; i < usuarios.size(); i++) {
                if (usuarios.get(i).getUsuario().equals(nombreDeUsuario)) {
                    return true;
                }
            }
        }
        System.out.println("3");
        return false;
    }

    private Usuario elegirUsuario(){
        this.mostrarUsuarios(); //Lo pongo para probar, no tiene sentido mostrarle los usuarios al mismo usuario.
        System.out.println("Ingrese su Usuario");
        String nombreDeUsuario = Main.pedirPorPantallaString();
        if(this.buscarUsuario(nombreDeUsuario) == null){
            return null;
        }
        else{
            return this.buscarUsuario(nombreDeUsuario);
        }

    }

    private Usuario buscarUsuario(String nombreDeUsuario){
        int i=0;
        boolean encontrado = false;
        if(!this.laListaDeUsuariosEstaVacia()) {
            while (i < usuarios.size() && !encontrado) {
                if (usuarios.get(i).getUsuario().equals(nombreDeUsuario)) {
                    return usuarios.get(i);
                } else {
                    i++;
                }
            }
        }
            System.out.println("Usuario No Encontrado");


        return null;
    }

    private void mostrarUsuarios(){
        System.out.println("Usuarios Existentes :");
        System.out.println(" -----------------------------");
        if(!this.laListaDeUsuariosEstaVacia()) {
            for (int i = 0; i < usuarios.size(); i++) {
                System.out.println(" -----------------------------");
                System.out.println("Nombre de Usuario: " + usuarios.get(i).getUsuario());
                System.out.println(" -----------------------------");
            }
        }
    }
    private String verificarPassword(String unaPassword) {
        unaPassword = this.chequearEspaciosSeguidos(unaPassword);
        while(this.laPasswordTieneMalTamanio(unaPassword) || this.laPasswordEsMala(unaPassword)){
            System.out.println("Por favor ingrese otra: ");
            unaPassword = Main.pedirPorPantallaString();
            unaPassword = this.chequearEspaciosSeguidos(unaPassword);
        }
        return unaPassword;
    }

    private boolean laPasswordTieneMalTamanio(String unaPassword){
        boolean malTamanio = false;
        if(unaPassword.length() < 8 || unaPassword.length() > 64){
            System.out.println("Su contraseña no tiene entre 8 y 64 caracteres.");
            malTamanio = true;
        }
        return malTamanio;
    }

    private String chequearEspaciosSeguidos(String unaPassword){
        int i;
        int contador = 0;
        String passwordFinal = "";
        for (i = 0; i < unaPassword.length(); i++){
            if(!(unaPassword.charAt(i) == ' ' && unaPassword.charAt((i+1)) == ' ')){
                passwordFinal = passwordFinal +unaPassword.charAt(i);
            }
            else{
                contador += 1;
            }
        }
        if(contador>0) {
            System.out.println("Se encontraron espacios consecutivos y se los reemplazo por uno solo");
        }
        return passwordFinal;
    }



    private void seguridadClave(String clave){
        int seguridad = 0;
        if (clave.length()!=0){
            if (tienenNumeros(clave) && tieneLetras(clave)){
                seguridad += 30;
            }
            if (tieneMinusculas(clave) && tieneMayusculas(clave)){
                seguridad += 30;
            }
            if (clave.length() >= 4 && clave.length() <= 5){
                seguridad += 10;
            }else{
                if (clave.length() >= 6 && clave.length() <= 8){
                    seguridad += 30;
                }else{
                    if (clave.length() > 8){
                        seguridad += 40;
                    }
                }
            }
        }

        System.out.println("La seguridad de la contraseña es de: %"+seguridad);
    }

    private boolean tienenNumeros(String texto){
        String numeros ="0123456789";
        for(int i=0; i<texto.length(); i++){
            if (numeros.indexOf(texto.charAt(i),0)!=-1){
                return true;
            }
        }
        return false;
    }

    private boolean tieneLetras(String texto){
        String letras="abcdefghyjklmnñopqrstuvwxyz";
        texto = texto.toLowerCase();
        for(int i=0; i<texto.length(); i++){
            if (letras.indexOf(texto.charAt(i),0)!=-1){
                return true;
            }
        }
        return false;
    }

    private boolean laPasswordEsMala(String palabra) {
        int lineasTotales = 0;
        boolean esMala = false;
        File archivo = new File("claves.txt");
        try {
            // SI EXISTE EL ARCHIVO
            if(archivo.exists()) {
                // ABRE LECTURA DEL ARCHIVO
                BufferedReader leerArchivo = new BufferedReader(new FileReader(archivo));
                // LINEA LEIDA
                String lineaLeida;
                // MIENTRAS LA LINEA LEIDA NO SEA NULL
                while((lineaLeida = leerArchivo.readLine()) != null) {
                    lineasTotales = lineasTotales + 1;

                    String[] palabras = lineaLeida.split(" "); // si lee espacio en blanco detecta q es nueva palabra
                    for(int i = 0 ; i < palabras.length ; i++) {
                        if(palabras[i].equals(palabra)) {
                            System.out.println("Su contraseña está en el puesto número " + lineasTotales +
                                    " de contraseñas malas.");
                            esMala = true;
                        }
                    }
                }
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return esMala;
    }

    private boolean tieneMinusculas(String texto){
        String letras="abcdefghyjklmnñopqrstuvwxyz";
        for(int i=0; i<texto.length(); i++){
            if (letras.indexOf(texto.charAt(i),0)!=-1){
                return true;
            }
        }
        return false;
    }

    private boolean tieneMayusculas(String texto){
        String letrasMayusculas="ABCDEFGHYJKLMNÑOPQRSTUVWXYZ";
        for(int i=0; i<texto.length(); i++){
            if (letrasMayusculas.indexOf(texto.charAt(i),0)!=-1){
                return true;
            }
        }
        return false;
    }

    private String setRandomPassword(){

        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz" + "°@#$%&/=";
        StringBuilder sb = new StringBuilder(16);

        for (int i = 0; i < 16; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

    public void ingresarNuevaPassword(Usuario usuario) {
        System.out.println("Ingrese una NUEVA Contraseña:");
        String nuevaPassword = Main.pedirPorPantallaString();
        this.verificarPassword(nuevaPassword);

        while(!(this.laNuevaPasswordEstaOK(nuevaPassword))){
            System.out.println("La clave no es aceptada porque es debil, ingrese otra:");
            nuevaPassword = Main.pedirPorPantallaString();
        }

        this.seguridadClave(nuevaPassword);
        usuario.setPassword(nuevaPassword); //Creo que esto esta mal, porque rompe el encapsulamiento

    }

    private boolean laNuevaPasswordEstaOK(String nuevaPassword){
        return  this.noTiene3LetrasSeguidasIguales(nuevaPassword)&&
                this.noTiene3CaracteresConsecutivos(nuevaPassword);
    }

    private boolean noTiene3LetrasSeguidasIguales(String nuevaPassword){
        int i=0;

        while(i < nuevaPassword.length()-1){
            if(!this.las2LetrasSonDistintas(nuevaPassword.charAt(i),nuevaPassword.charAt(i+1))){
                if(!this.las2LetrasSonDistintas(nuevaPassword.charAt((i+1)),nuevaPassword.charAt(i+2))) {
                    System.out.println("La Contraseña no puede tener 3 letras iguales seguidas");
                    return false;
                }
            }
            i++;
        }
        return  true;
    }

    private boolean las2LetrasSonDistintas(char unaLetra,char otraLetra){
        return unaLetra != otraLetra;
    }


    private boolean noTiene3CaracteresConsecutivos(String nuevaPassword){
        int i=0;

        while(i<nuevaPassword.length()-2){

            if(this.esUnCaracterSucesivoParaAdelante(nuevaPassword.codePointAt(i),nuevaPassword.codePointAt(i+1),nuevaPassword.codePointAt(i+2)) ||
                    this.esUnCaracterSucesivoParaAtras(nuevaPassword.codePointAt(i),nuevaPassword.codePointAt(i+1),nuevaPassword.codePointAt(i+2))){
                System.out.println("La Contraseña no puede tener mas de 2 caracteres consecutivos");
                return false;
            }

            i++;
        }
        return true;
    }

    private boolean esUnCaracterSucesivoParaAdelante(int valor, int otroValor, int unValor){

        return valor == otroValor -1 && valor == unValor -2;
    }

    private boolean esUnCaracterSucesivoParaAtras(int valor, int otroValor, int unValor){
        return valor == otroValor + 1 && valor == unValor +2;
    }

    /*private void loguearse(String unaPassword) throws InterruptedException {
        int contador = 0;
        while(!(this.password.equals(unaPassword)) && contador < 10){
            System.out.println("Contraseña invalida. Por favor espere.");
            System.out.println("Intentos Restantes: " + (10-contador));
            contador += 1;
            TimeUnit.SECONDS.sleep(5);
            System.out.println("Ingrese otra contraseña:");
            password = Main.pedirPorPantallaString();
        };
        if(contador == 10){
            System.out.println("Has gastado todos los intentos. Vuelve a intentarlo mas tarde.");
        } else {
            System.out.println("Usuario: " + usuario);
            System.out.println("Contraseña: " + password);
        }
    }
*/

}
