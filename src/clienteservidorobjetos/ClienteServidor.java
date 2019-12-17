/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienteservidorobjetos;

/**
 *
 * @author link
 */
public class ClienteServidor {
    public static void main(String[] args) throws InterruptedException{
        
        // Voy a simular dos main con hilos para tardar menos y ejecutar solo un fichero
        Compartido comp = new Compartido();
        // Servidor
        ServidorInformador s = new ServidorInformador(comp);
        s.start();
        
        Thread.sleep(1000);
        
        // Clientes
        ClienteDron[] clientes = new ClienteDron[5];
        for(int i = 0; i<clientes.length; i++){
            clientes[i] = new ClienteDron();
            clientes[i].start();
            
        }
        
        while(!comp.getSalir()){
            Thread.sleep(1000);
        }
        System.exit(0);
    }
    
}
