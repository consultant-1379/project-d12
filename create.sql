create table engineer (id bigint not null, email varchar(255), name varchar(255), primary key (id)) engine=InnoDB;
create table hibernate_sequence (next_val bigint) engine=InnoDB;
insert into hibernate_sequence values ( 1 );
create table microservice (id bigint not null, category varchar(255), date_created datetime(6), description varchar(255), name varchar(255), engineer_id bigint, parent_id bigint, primary key (id)) engine=InnoDB;
create table microservice_dependencies (microservice_id bigint not null, dependencies_id bigint not null) engine=InnoDB;
create table version (microservice_id bigint not null, version_no varchar(255)) engine=InnoDB;
alter table microservice_dependencies add constraint UK_7ao0ghh3jkaig6y8a78j50ak3 unique (dependencies_id);
alter table microservice add constraint FKdojwgvdvl8fu6fyr85q5mrw8n foreign key (engineer_id) references engineer (id);
alter table microservice add constraint FK6bi88v3hk6ipnijhdqjequ8xc foreign key (parent_id) references microservice (id);
alter table microservice_dependencies add constraint FKsui8rimmq7iif6qvv5otk8215 foreign key (dependencies_id) references microservice (id);
alter table microservice_dependencies add constraint FKor067kdfxog7ak4ghl43fg02o foreign key (microservice_id) references microservice (id);
alter table version add constraint FKdmveep6u612qccfbt1n6nq50w foreign key (microservice_id) references microservice (id);create table engineer (id bigint not null, email varchar(255), name varchar(255), primary key (id));
create table microservice (id bigint generated by default as identity, category varchar(255), date_created timestamp, description varchar(255), name varchar(255), engineer_id bigint, parent_id bigint, primary key (id));
create table microservice_dependencies (microservice_id bigint not null, dependencies_id bigint not null);
create table version (microservice_id bigint not null, version_no varchar(255));
alter table microservice_dependencies drop constraint if exists UK_7ao0ghh3jkaig6y8a78j50ak3;
alter table microservice_dependencies add constraint UK_7ao0ghh3jkaig6y8a78j50ak3 unique (dependencies_id);
create sequence hibernate_sequence start with 1 increment by 1;
alter table microservice add constraint FKdojwgvdvl8fu6fyr85q5mrw8n foreign key (engineer_id) references engineer;
alter table microservice add constraint FK6bi88v3hk6ipnijhdqjequ8xc foreign key (parent_id) references microservice;
alter table microservice_dependencies add constraint FKsui8rimmq7iif6qvv5otk8215 foreign key (dependencies_id) references microservice;
alter table microservice_dependencies add constraint FKor067kdfxog7ak4ghl43fg02o foreign key (microservice_id) references microservice;
alter table version add constraint FKdmveep6u612qccfbt1n6nq50w foreign key (microservice_id) references microservice;
create table engineer (id bigint not null, email varchar(255), name varchar(255), primary key (id));
create table microservice (id bigint generated by default as identity, category varchar(255), date_created timestamp, description varchar(255), name varchar(255), engineer_id bigint, parent_id bigint, primary key (id));
create table microservice_dependencies (microservice_id bigint not null, dependencies_id bigint not null);
create table version (microservice_id bigint not null, version_no varchar(255));
alter table microservice_dependencies drop constraint if exists UK_7ao0ghh3jkaig6y8a78j50ak3;
alter table microservice_dependencies add constraint UK_7ao0ghh3jkaig6y8a78j50ak3 unique (dependencies_id);
create sequence hibernate_sequence start with 1 increment by 1;
alter table microservice add constraint FKdojwgvdvl8fu6fyr85q5mrw8n foreign key (engineer_id) references engineer;
alter table microservice add constraint FK6bi88v3hk6ipnijhdqjequ8xc foreign key (parent_id) references microservice;
alter table microservice_dependencies add constraint FKsui8rimmq7iif6qvv5otk8215 foreign key (dependencies_id) references microservice;
alter table microservice_dependencies add constraint FKor067kdfxog7ak4ghl43fg02o foreign key (microservice_id) references microservice;
alter table version add constraint FKdmveep6u612qccfbt1n6nq50w foreign key (microservice_id) references microservice;
create table engineer (id bigint not null, email varchar(255), name varchar(255), primary key (id));
create table microservice (id bigint generated by default as identity, category varchar(255), date_created timestamp, description varchar(255), name varchar(255), engineer_id bigint, parent_id bigint, primary key (id));
create table microservice_dependencies (microservice_id bigint not null, dependencies_id bigint not null);
create table version (microservice_id bigint not null, version_no varchar(255));
alter table microservice_dependencies drop constraint if exists UK_7ao0ghh3jkaig6y8a78j50ak3;
alter table microservice_dependencies add constraint UK_7ao0ghh3jkaig6y8a78j50ak3 unique (dependencies_id);
create sequence hibernate_sequence start with 1 increment by 1;
alter table microservice add constraint FKdojwgvdvl8fu6fyr85q5mrw8n foreign key (engineer_id) references engineer;
alter table microservice add constraint FK6bi88v3hk6ipnijhdqjequ8xc foreign key (parent_id) references microservice;
alter table microservice_dependencies add constraint FKsui8rimmq7iif6qvv5otk8215 foreign key (dependencies_id) references microservice;
alter table microservice_dependencies add constraint FKor067kdfxog7ak4ghl43fg02o foreign key (microservice_id) references microservice;
alter table version add constraint FKdmveep6u612qccfbt1n6nq50w foreign key (microservice_id) references microservice;
