create table usuarios (
    id bigint primary key auto_increment,
    nombre varchar(100) not null,
    email varchar(100) unique,
    password varchar(255),
    tipo varchar(20) not null,
    fecha_creacion datetime not null default current_timestamp
);