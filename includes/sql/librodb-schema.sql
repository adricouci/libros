drop database if exists librosdb;
create database librosdb;

use librosdb;


create table usuarios (
	username	varchar(40) not null primary key,
	pasword 	char(32) not null,
	name	varchar(40) not null);


create table user_roles (
	username	varchar(40) not null,
	rolename	varchar(40) not null,
	foreign key(username) references usuarios(username) on delete cascade,
	primary key (username, rolename));
	
create table autores (
	nombreautor	varchar(40) not null primary key);

create table libros (
	idlibro int not null auto_increment primary key,
	titulo	varchar(40) not null,
	autor	varchar(40) not null,
	lengua	varchar(10) not null,
	edicion	int not null,
	fechaedicion varchar(10) not null,
	fechaimpresion varchar(10) not null,
	editorial varchar(40) not null,
	lastmodified timestamp default current_timestamp ON UPDATE CURRENT_TIMESTAMP,
	foreign key(autor) 	references autores(nombreautor)on delete cascade);

create table resena (
	idresena int not null auto_increment primary key,
	idlibro int not null,
	creador varchar(40) not null,
	datos varchar(500) not null,
	fecha varchar(20),
	foreign key(creador) 	references usuarios(username) on delete cascade,
	foreign key(idlibro) 	references libros(idlibro));
