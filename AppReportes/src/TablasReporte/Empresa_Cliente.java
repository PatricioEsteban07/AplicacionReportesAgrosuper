/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TablasReporte;

import Model.Cliente;
import Model.Empresa;
import java.util.ArrayList;

/**
 *
 * @author Patricio
 */
public class Empresa_Cliente
{
    public Empresa empresa;
    public int idEmpresa;
    public Cliente cliente;
    public int idCliente;

    public Empresa_Cliente(int idEmpresa, int idCliente)
    {
        this.idEmpresa = idEmpresa;
        this.idCliente = idCliente;
        this.empresa = null;
        this.cliente = null;
    }
    
    public Empresa_Cliente(ArrayList<Integer> ids)
    {
        this.idEmpresa = ids.get(0);
        this.idCliente = ids.get(1);
        this.empresa = null;
        this.cliente = null;
    }

    public void searchObjects(ArrayList<Cliente> clientes, ArrayList<Empresa> empresas)
    {
        for (Cliente clienteAux : clientes)
        {
            if(clienteAux.id==this.idCliente)
            {
                this.cliente=clienteAux;
            }
        }
        for (Empresa empresaAux : empresas)
        {
            if(empresaAux.id==this.idEmpresa)
            {
                this.empresa=empresaAux;
            }
        }
    }
    
    
}
