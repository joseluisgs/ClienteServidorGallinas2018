/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienteservidorobjetos;

import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author link
 */
public class GestionClientes extends Thread {
    private Socket cliente;
    private FileWriter fichero;
    private boolean salir;
    private Compartido comp;
    private int numCliente;
    
    public GestionClientes(Socket socket, Compartido comp){
        this.cliente = socket;
        this.fichero = fichero;
        this.comp = comp;
        this.comp.nuevoCliente();
        this.numCliente = this.comp.getCliente();
    }
    
    @Override
    public void run() {
        ObjectInputStream bufferObjetosEntrada = null;
        ObjectOutputStream bufferObjetosSalida = null;
        
        try {
            System.out.println("Servidor["+numCliente+"]->Conectado con cliente: " + numCliente + " de " + cliente.getInetAddress());
            
            // Envío el ID
            bufferObjetosSalida = new ObjectOutputStream(cliente.getOutputStream());
            bufferObjetosSalida.writeObject(numCliente);
            System.out.println("Servidor["+numCliente+"]->Enviado el ID alcliente");
            
            
            boolean salida = this.comp.getSalir();
            
            while(!salida){
                // Preparo la entrada
                bufferObjetosEntrada = new ObjectInputStream(cliente.getInputStream());
                // Recibimos del cliente, y como nos entra datos es por el input
                Mensaje datoEntrada = (Mensaje) bufferObjetosEntrada.readObject();
                System.out.println("Servidor["+numCliente+"]->Recibido del Cliente: " + datoEntrada.toString());

                //Procesamos la entrada
                this.comp.procesarEntrada(datoEntrada);

                // Le decimos al cliente si debe seguir o no
                salida = this.comp.getSalir();
                bufferObjetosSalida = new ObjectOutputStream(cliente.getOutputStream());
                bufferObjetosSalida.writeObject(salida);
                System.out.println("Servidor["+numCliente+"]->Enviado al Cliente["+numCliente+"] salida: "+salida);

                // Mostramos el número de huevos, por curiosidad
                System.out.println("Servidor->Número de huevos: "+this.comp.getHuevos());
                //this.comp.setSalir(true);
            }
            this.cliente.close();
            this.finalize();
            System.out.println("Servidor["+numCliente+"]->Terminé");
        } catch (IOException ex) {
            Logger.getLogger(GestionClientes.class.getName()).log(Level.SEVERE, null, ex);
        //} catch (ClassNotFoundException ex) {
        //    Logger.getLogger(GestionClientes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Throwable ex) {
            Logger.getLogger(GestionClientes.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
}
