drop user 'libros'@'localhost';
create user 'libros'@'localhost' identified by 'libros';
grant all privileges on librosdb.* to 'libros'@'localhost';
grant all privileges on realmdb.* to 'libros'@'localhost';
flush privileges;