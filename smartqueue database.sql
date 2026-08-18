create database smart_queue_db;
show databases;

create table users(id bigint primary key auto_increment, name varchar(100) not null, email varchar(50) not null unique,
password varchar(255) not null, phone varchar(10), role varchar(20) not null default 'user',
created_at datetime default current_timestamp);

create table doctors(id bigint primary key auto_increment, name varchar (100) not null,email varchar(150)
not null unique, phone varchar(15),  specialization VARCHAR(100) NOT NULL, consultation_fee DECIMAL(10,2),
status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE', created_at DATETIME DEFAULT CURRENT_TIMESTAMP);

create table doctor_availability(id bigint primary key auto_increment, doctor_id bigint not null,
available_date date not null, start_time time not null, end_time time not null, status varchar(20) not null default 'Available',
constraint fk_availability_doctor foreign key(doctor_id) references doctors(id) on delete cascade);

CREATE TABLE appointments (id BIGINT PRIMARY KEY AUTO_INCREMENT, user_id BIGINT NOT NULL,doctor_id BIGINT NOT NULL,
appointment_date DATE NOT NULL, appointment_time TIME NOT NULL, reason VARCHAR(500), status VARCHAR(30) NOT NULL DEFAULT 'BOOKED',
created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT fk_appointment_user FOREIGN KEY (user_id) REFERENCES users(id),
CONSTRAINT fk_appointment_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id));

CREATE TABLE queue_tokens (id BIGINT PRIMARY KEY AUTO_INCREMENT, appointment_id BIGINT NOT NULL UNIQUE, token_number VARCHAR(20) NOT NULL,
priority VARCHAR(30) NOT NULL DEFAULT 'NORMAL', status VARCHAR(30) NOT NULL DEFAULT 'WAITING',
check_in_time DATETIME, called_at DATETIME, completed_at DATETIME,
CONSTRAINT fk_queue_appointment FOREIGN KEY (appointment_id) REFERENCES appointments(id));

