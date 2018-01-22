  
  INSERT INTO cliente(nombre,apellido,edad,sexo,descripcion) VALUES ("Patricio", "Castro", 25, "M", "hola mundo1");
  INSERT INTO cliente(nombre,apellido,edad,sexo,descripcion) VALUES ("Javiera", "Sandoval", 30, "F", "hola mundo2");
  INSERT INTO cliente(nombre,apellido,edad,sexo,descripcion) VALUES ("Camila", "Rojas", 40, "F", "hola mundo3");
  INSERT INTO cliente(nombre,apellido,edad,sexo,descripcion) VALUES ("Yanira", "Guevara", 17, "F", "hola mundo4");
  INSERT INTO cliente(nombre,apellido,edad,sexo,descripcion) VALUES ("Eduardo", "López", 32, "M", "hola 5");

  INSERT INTO empresa(nombre,direccion) VALUES ("Agrosuper","4 oriente A");
  INSERT INTO empresa(nombre,direccion) VALUES ("Agrosuper","Dir generica");
  INSERT INTO empresa(nombre,direccion,descripcion) VALUES ("Agrosuper","13 sur #346","descripcion piola");
  INSERT INTO empresa(nombre,direccion) VALUES ("Agrosuper","Av Montt");
  INSERT INTO empresa(nombre,direccion) VALUES ("Agrosuper","Calle Balmaceda");
  
  INSERT INTO cliente_empresa(empresa_id,cliente_id) VALUES (1,1);
  INSERT INTO cliente_empresa(empresa_id,cliente_id) VALUES (1,2);
  INSERT INTO cliente_empresa(empresa_id,cliente_id) VALUES (2,1);
  INSERT INTO cliente_empresa(empresa_id,cliente_id) VALUES (2,3);
  INSERT INTO cliente_empresa(empresa_id,cliente_id) VALUES (2,4);
  INSERT INTO cliente_empresa(empresa_id,cliente_id) VALUES (3,5);
  INSERT INTO cliente_empresa(empresa_id,cliente_id) VALUES (4,2);
  