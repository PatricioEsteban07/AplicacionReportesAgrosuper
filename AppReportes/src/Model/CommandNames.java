/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Patricio
 */
public final class CommandNames
{
    public final static String queryPrueba="SELECT cliente.id as 'cliente_id',\n" +
                                            "	   cliente.nombre as 'cliente_nombre',\n" +
                                            "	   cliente.apellido as 'cliente_apellido',\n" +
                                            "	   cliente.edad as 'cliente_edad',\n" +
                                            "	   cliente.sexo as 'cliente_sexo',\n" +
                                            "	   cliente.descripcion as 'cliente_descripcion',\n" +
                                            "	   empresa.id as 'empresa_id', \n" +
                                            "	   empresa.nombre as 'empresa_nombre', \n" +
                                            "	   empresa.direccion as 'empresa_direccion', \n" +
                                            "	   empresa.descripcion as 'empresa_descripcion' \n" +
                                            "FROM cliente, empresa, cliente_empresa\n" +
                                            "WHERE cliente.id = cliente_empresa.cliente_id \n" +
                                            "	  AND empresa.id = cliente_empresa.empresa_id;";
}
