/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Patricio
 */
public class LocalDB
{
    public Set<String> materiales;
    public Set<String> marcas;
    public Set<String> sectores;
    public Set<String> agrupados;
    public Set<String> tiposEnvasados;
    public Set<String> estadosRefrigerados;
    public Set<String> n2s;
    public Set<String> n3s;
    public Set<String> n4s;
    public Set<String> centros;
    public Set<String> oficinas;
    public Set<String> pedidos;
    public Set<String> stocks;
    public Set<String> despachos;
    public Set<String> clientes;

    public LocalDB()
    {
        this.materiales = new HashSet<String>();
        this.marcas = new HashSet<String>();
        this.sectores = new HashSet<String>();
        this.agrupados = new HashSet<String>();
        this.tiposEnvasados = new HashSet<String>();
        this.estadosRefrigerados = new HashSet<String>();
        this.n2s = new HashSet<String>();
        this.n3s = new HashSet<String>();
        this.n4s = new HashSet<String>();
        this.centros = new HashSet<String>();
        this.oficinas = new HashSet<String>();
        this.pedidos = new HashSet<String>();
        this.stocks = new HashSet<String>();
        this.despachos = new HashSet<String>();
        this.clientes = new HashSet<String>();
    }
    
    
}
