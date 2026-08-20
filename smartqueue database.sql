create database smart_queue_db;
show databases;
use smart_queue_db;

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

CREATE TABLE consultations (id BIGINT PRIMARY KEY AUTO_INCREMENT, appointment_id BIGINT NOT NULL UNIQUE,
doctor_id BIGINT NOT NULL, notes TEXT, diagnosis VARCHAR(500), start_time DATETIME, end_time DATETIME,
status VARCHAR(30) NOT NULL DEFAULT 'IN_PROGRESS',
CONSTRAINT fk_consultation_appointment FOREIGN KEY (appointment_id) REFERENCES appointments(id),
CONSTRAINT fk_consultation_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id));

CREATE TABLE feedback (id BIGINT PRIMARY KEY AUTO_INCREMENT, appointment_id BIGINT NOT NULL UNIQUE,
user_id BIGINT NOT NULL, rating INT NOT NULL, comment VARCHAR(500), created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT chk_feedback_rating CHECK (rating BETWEEN 1 AND 5),
CONSTRAINT fk_feedback_appointment FOREIGN KEY (appointment_id) REFERENCES appointments(id),
CONSTRAINT fk_feedback_user FOREIGN KEY (user_id) REFERENCES users(id));

CREATE TABLE notifications (id BIGINT PRIMARY KEY AUTO_INCREMENT, user_id BIGINT NOT NULL, title VARCHAR(150) NOT NULL,
message VARCHAR(500) NOT NULL, type VARCHAR(50), is_read BOOLEAN NOT NULL DEFAULT FALSE,
created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT fk_notification_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE);

show tables;
DESCRIBE users;

select * from users;