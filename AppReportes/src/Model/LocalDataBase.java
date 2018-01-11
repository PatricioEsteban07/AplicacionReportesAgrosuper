/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;

/**
 *
 * @author Patricio
 */
public class LocalDataBase
{
    public ArrayList<Cliente> clientes;
    public ArrayList<Empresa> empresas;

    public LocalDataBase()
    {
        this.clientes = new ArrayList<>();
        this.empresas = new ArrayList<>();
    }
    
}
