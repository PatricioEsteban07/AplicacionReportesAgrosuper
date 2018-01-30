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
    
    public String cliente_id;
    public String cliente_nombre;
    public String cliente_apellido;
    public String cliente_edad;
    public String cliente_sexo;
    public String cliente_descripcion;
    public String empresa_id;
    public String empresa_nombre;
    public String empresa_direccion;
    public String empresa_descripcion;

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
            if(clienteAux.equals(this.idCliente))
            {
                this.cliente=clienteAux;
            }
        }
        for (Empresa empresaAux : empresas)
        {
            if(empresaAux.id.equals(this.idEmpresa))
            {
                this.empresa=empresaAux;
            }
        }
        completaClase();
    }
    
    public void completaClase()
    {
        this.cliente_id=(this.idCliente+"");
        this.cliente_nombre=(this.cliente.nombre+"");
        this.cliente_apellido=(this.cliente.apellido+"");
        this.cliente_edad=(this.cliente.edad+"");
        this.cliente_sexo=(this.cliente.sexo+"");
        this.cliente_descripcion=(this.cliente.descripcion+"");
        
        this.empresa_id=(this.idEmpresa+"");
        this.empresa_nombre=(this.empresa.nombre+"");
        this.empresa_direccion=(this.empresa.direccion+"");
        this.empresa_descripcion=(this.empresa.descripcion+"");
    }
    
    
}
