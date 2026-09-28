-- Run this once in MySQL Workbench / MySQL command line BEFORE starting the application
CREATE DATABASE IF NOT EXISTS parkspot;
USE parkspot;

-- Tables are created automatically by Hibernate (spring.jpa.hibernate.ddl-auto=update).
-- The 10 slots (P01-P10), 5 sample flats (101-105) and the login user (admin / admin123)
-- are inserted automatically by DataInitializer.java, only if they do not exist yet.

-- Useful queries for the viva / demo:
-- SELECT * FROM parking_slot;
-- SELECT * FROM flat;
-- SELECT * FROM visitor_vehicle;
-- SELECT * FROM parking_record;
-- SELECT * FROM staff_user;
