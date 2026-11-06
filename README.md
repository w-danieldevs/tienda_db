base de datos que utiliza la cambie un poco  [tienda_db.sql](https://github.com/user-attachments/files/34485379/tienda_db.sql)
CREATE SCHEMA tienda_db;
USE tienda_db;
-- drop database tienda_db;

CREATE TABLE roles(
  id bigint primary key not null auto_increment,
  name varchar(50) not null
);
 
CREATE TABLE users(
  id bigint primary key not null auto_increment,
  username varchar(50) not null unique,
  email varchar(255) not null unique,
  password varchar(255) not null,
  active boolean not null default true,
  rol_id bigint not null,
  foreign key (rol_id) references roles (id)
);
 
CREATE TABLE categories(
  id bigint primary key not null auto_increment,
  name varchar(100) not null unique
);
 
CREATE TABLE products(
  id bigint primary key not null auto_increment,
  name varchar(150) not null,
  description varchar(500),
  price decimal(12,2) not null,
  stock int not null,
  categoria_id bigint not null,
  seller_id bigint not null,
  foreign key (categoria_id) references categories (id),
  foreign key (seller_id) references users (id)
);
 
CREATE TABLE orders(
  id bigint primary key not null auto_increment,
  client_id bigint not null,
  product_id bigint not null,
  quantity int not null,
  total decimal(12,2) not null,
  status varchar(20) not null default 'PENDIENTE',
  created_at datetime not null default current_timestamp,
  foreign key (client_id) references users (id),
  foreign key (product_id) references products (id)
);
 
INSERT INTO roles (name) VALUES ('ADMIN'), ('CLIENTE'), ('VENDEDOR');

SELECT id, username, email, active, rol_id
FROM users
WHERE username = 'admin'
   OR email = 'admin@tienda.com';
   
   SELECT id, name FROM roles WHERE name = 'admin';
SELECT id, username, rol_id FROM users WHERE username = 'admin';
   

