/*Problemas do Banco de Dados*/
/*Carros e Motoristas*/

/*create database r4c_db

use  r4c_db;

*/


/*Tabela Províncias*/
create table tb_provincias(
id int primary key auto_increment,
nome varchar(30) unique,
data_cadastro date 
) default char set utf8;

insert into tb_provincias values (default, 'Luanda', curdate()), (default, 'Malanje', curdate()), (default, 'Kwanza-Sul', curdate());

/*Tabela Tipo de Usuario*/
create table tb_categoria_usuario(
id int primary key auto_increment,
nome varchar(15) unique
) default char set utf8;

insert into tb_categoria_usuario values (default, 'Motorista'), (default, 'Passgeiro');

/*Tabela Usuário*/
create table tb_usuarios(
id int primary key auto_increment,
nome varchar(30),
sobrenome varchar(10),
email varchar(30),
senha varchar(30),
id_categoria int,
foreign key(id_categoria) references tb_categoria_usuario(id),
foto_url text,
id_provincia int,
foreign key(id_provincia) references tb_provincias(id),
telefone varchar(12),
latitude decimal(10,8),
longitude decimal(11,8),
tempo_ultimo_acesso datetime,
estado boolean default 0,
data_cadastro date
) default char set utf8;

/*Criando Trigger para tabela localizacao*/

/*Tabela Localização*/
create table tb_locais(
id int primary key auto_increment,
nome varchar(40),
latitude decimal(10,8),
longitude decimal(11,8),
descricao varchar(200),
data_insercao date
);

/*Inserção de alguns locais*/
insert into tb_locais(nome, latitude, longitude) values
('IMIL Vila Alice', -8.82432600, 13.24666200),
('ENDE', -8.81676500, 13.24872200),
('IMEL', -8.82974800, 13.24573000);


/*Tabela Nível Conforto*/
create table tb_avaliacao(
id int primary key auto_increment,
nome varchar(10),
data_cadastro date 
) default char set utf8;

/*Inserção de alguns níveis de avaliação*/
insert into tb_avaliacao(nome) values
('Sem avaliação'),
('Péssimo'),
('Mal'),
('Normal'),
('Bom'),
('Exelente');

/*Tabela Estado Viagem*/
create table tb_estado_viagem(
id int primary key auto_increment,
nome varchar(30),
data_insercao date
) default char set utf8;

/*Inserção de alguns níveis de avaliação*/
insert into tb_estado_viagem(nome) values
('Completa'),
('Cancelada'),
('Interrompida'),
('Incompleta');


/*Tabela Viagem*/
create table tb_viagem(
id int primary key auto_increment,
id_motorista int,
foreign key(id_motorista) references tb_usuarios(id),
id_passageiro int,
foreign key(id_passageiro) references tb_usuarios(id),
id_local_origem int,
foreign key(id_local_origem) references tb_locais(id),
id_local_destino int,
foreign key(id_local_destino) references tb_locais(id),
descricao varchar(100),
preco decimal,
id_avaliacao int,
foreign key(id_avaliacao) references tb_avaliacao(id),
id_estado int,
foreign key(id_estado) references tb_estado_viagem(id),
tempo_inicio datetime,
tempo_termino datetime
) default char set utf8;

insert into tb_viagem(id_motorista, id_passageiro, id_local_origem, id_local_destino, descricao,
preco, id_avaliacao, id_estado, tempo_inicio, tempo_termino) values

(2, 3, 1, 2, 'Yeah.', 1000, 3, 1, current_time(), current_time());

/*Tabela Mensagem*/
create table tb_mensagem(
id int primary key auto_increment,
id_emissor int,
foreign key(id_emissor) references tb_usuarios(id),
id_receptor int,
foreign key(id_receptor) references tb_usuarios(id),
texto text,
emissor_visualizado boolean default 0,
receptor_visualizado boolean default 0,
data_mensagem datetime
) default char set utf8;


/*Tabela Marca*/
create table tb_marca(
id int primary key auto_increment,
nome varchar(30) unique,
foto_url text
) default char set utf8;

/*Tabela Modelo*/
create table tb_modelo(
id int primary key auto_increment,
nome varchar(30) unique,
id_marca int,
foreign key(id_marca) references tb_marca(id)
) default char set utf8;

/*Tabela Nível Conforto*/
create table tb_conforto(
id int primary key auto_increment,
nivel_conforto varchar(10),
data_cadastro date 
) default char set utf8;

/*Tabela Carro*/
create table tb_carro(
id int primary key auto_increment,
id_modelo int,
foreign key (id_modelo) references tb_modelo(id),
matricula varchar(40) unique,
id_motorista int,
foreign key(id_motorista) references tb_motoristas(id),
id_nivel_conforto int,
foreign key(id_nivel_conforto) references tb_conforto(id),
data_cadastro date
) default char set utf8;

/*Tabela Motorista Carro*/
create table tb_motorista_carro(
id_motorista int,
foreign key(id_motorista) references tb_motoristas(id),
id_carro int,
foreign key(id_carro) references tb_carro(id),
primary key (id_motorista, id_carro)
) default char set utf8;

/*Tabela Preferência de Música*/
create table tb_preferencia_musica(
id int primary key auto_increment,
estilo_musica varchar(30),
descricao text
) default char set utf8;

insert into tb_preferencia_musica values (default, 'Slow', 'Música sauve, alivia a alma'), (default, 'House', 'Energética');

/*Tabela Destino*/
create table tb_destino(
id int primary key auto_increment,
id_passageiro int,
foreign key(id_passageiro) references tb_passageiro(id),
nome varchar(30),
descricao text,
id_localizacao int,
foreign key(id_localizacao) references tb_localizacao(id)
) default char set utf8;


/

/*Consulta que lista todos os usuários activos*/
select id, nome, latitude, longitude, minute(current_timestamp())-minute(tempo_ultimo_acesso) as minutos from tb_usuarios having minutos<3;