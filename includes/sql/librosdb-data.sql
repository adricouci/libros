source librodb-schema.sql;
insert into usuarios values('admin', MD5('admin'), 'admin');
insert into user_roles values ('admin', 'Administrador');

insert into usuarios values('test', MD5('test'), 'Test');
insert into user_roles values ('test', 'registered');

insert into usuarios values('donramon', MD5('donramon'), 'DonRamon');
insert into user_roles values ('donramon', 'registered');

insert into autores values('Autor0');
insert into autores values('Autor1');
insert into autores values('Autor2');
insert into autores values('Autor3');
insert into autores values('Autor4');
insert into autores values('Autor5');
insert into autores values('Autor6');
insert into autores values('Autor7');
insert into autores values('Autor8');
insert into autores values('Autor9');

insert into libros (titulo, autor, lengua ,edicion,fechaedicion,fechaimpresion, editorial) values ('titulo0', 'autor0', 'lengua1', "10", '10-12-19' ,'10-12-01','editorial1');
insert into libros (titulo, autor, lengua ,edicion,fechaedicion,fechaimpresion, editorial) values ('titulo1', 'autor1', 'lengua1', "13", '10-12-19' ,'10-12-19','editorial2');
insert into libros (titulo, autor, lengua ,edicion,fechaedicion,fechaimpresion, editorial) values ('titulo2', 'autor2', 'lengua2', "5", '10-12-19' ,'10-12-01','editorial3');
insert into libros (titulo, autor, lengua ,edicion,fechaedicion,fechaimpresion, editorial) values ('titulo3', 'autor3', 'lengua2', "1", '10-12-19' ,'10-12-01','editorial5');
insert into libros (titulo, autor, lengua ,edicion,fechaedicion,fechaimpresion, editorial) values ('titulo4', 'autor4', 'lengua1', "1",'10-12-19' ,'10-12-01', 'editorial1');
insert into libros (titulo, autor, lengua ,edicion,fechaedicion,fechaimpresion, editorial) values ('titulo5', 'autor1', 'lengua2', "7", '10-12-19' ,'10-12-19','editorial0');
insert into libros (titulo, autor, lengua ,edicion,fechaedicion,fechaimpresion, editorial) values ('titulo6', 'autor3', 'lengua1', "19", '10-12-19' ,'10-12-01','editorial4');
insert into libros (titulo, autor, lengua ,edicion,fechaedicion,fechaimpresion, editorial) values ('titulo7', 'autor5', 'lengua1', "1", '10-12-00' ,'10-12-01','editorial1');
insert into libros (titulo, autor, lengua ,edicion,fechaedicion,fechaimpresion, editorial) values ('titulo8', 'autor6', 'lengua1', "8",'10-12-19' ,'10-12-01', 'editorial7');
insert into libros (titulo, autor, lengua ,edicion,fechaedicion,fechaimpresion, editorial) values ('titulo9', 'autor3', 'lengua3', "1", '10-12-00' ,'10-12-01','editorial1');


insert into resena (idlibro, creador, datos, fecha) values ("1", 'test', 'un libro muy bonico','10-12-19');
insert into resena (idlibro, creador, datos, fecha) values ("2", 'test', 'un libro muy bonico','10-12-19');
insert into resena (idlibro, creador, datos, fecha) values ("3", 'test', 'un libro muy bonico','10-12-19');
insert into resena (idlibro, creador, datos, fecha) values ("4", 'test', 'un libro muy bonico','10-12-19');
insert into resena (idlibro, creador, datos, fecha) values ("5", 'test', 'un libro muy bonico','10-12-19');
