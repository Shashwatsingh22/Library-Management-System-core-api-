-- Create an "app" main application schema
create schema if not exists app;

-- Create tables for management of different types (app.type_group and app.type)
-- Master type table
CREATE TABLE IF NOT EXISTS app.type_group
(
    id integer,
    name character varying(50),
    add_date timestamp with time zone,
    update_date timestamp with time zone,
    end_date timestamp with time zone,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS app.type
(
    id integer,
    group_id integer,
    name character varying(100),
    add_date timestamp with time zone,
    update_date timestamp with time zone,
    end_date timestamp with time zone,
    CONSTRAINT type_group_id_fk FOREIGN KEY (group_id)
        REFERENCES app.type_group (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    primary key(id)
);

-- Insert required type_group and type
INSERT INTO app.type_group(id, name, add_date) VALUES (1, 'Role', now());
INSERT INTO app.type(id, group_id, name, add_date) VALUES (1, 1, 'Librarian', now());
INSERT INTO app.type(id, group_id, name, add_date) VALUES (2, 1, 'Member', now());

INSERT INTO app.type_group(id, name, add_date) VALUES (3, 'Status', now());
INSERT INTO app.type(id, group_id, name, add_date) VALUES (6, 3, 'Deleted', now());

-- Create User Table
CREATE TABLE IF NOT EXISTS app.users
(
    id uuid,
    name character varying(50),
    email character varying(320),
    user_name character varying(50),
    password character varying(72),
    profile_img_url character varying,
    role_type_id integer,
    status_id integer,
    designation character varying(40),
    college character varying,
    update_date timestamp with time zone,
    add_date timestamp with time zone,
    end_date timestamp with time zone,
    CONSTRAINT users_role_type_id_fk FOREIGN KEY (role_type_id)
    	REFERENCES app.type (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
     CONSTRAINT users_status_id_fk FOREIGN KEY (status_id)
        REFERENCES app.type (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    PRIMARY KEY (id)
);

-- Add unique constraint on app.users table on required column
CREATE UNIQUE INDEX users_email_unq ON app.users(email) where end_date is null;
CREATE UNIQUE INDEX users_user_name_unq ON app.users(user_name) where end_date is null;

-- Create An table for session management
CREATE TABLE IF NOT EXISTS app.user_sessions
(
    id uuid, -- uuid
    user_id uuid, -- uuid
    ip_address character varying(15),
    os_version character varying(100),
    user_agent character varying,
    device_name character varying(100),
    browser character varying,
    browser_version character varying(100),
    city character varying(100),
    country_iso_code character varying(4),
    expiry_date timestamp with time zone,
    update_date timestamp with time zone,
    add_date timestamp with time zone,
    end_date timestamp with time zone,
    CONSTRAINT user_sessions_user_id_fk FOREIGN KEY (user_id)
      REFERENCES app.users(id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    primary key(id)
);

-- Create table to manage the failed attempts
CREATE TABLE IF NOT EXISTS app.login_failed_attempts
(
    user_name character varying(320),
    ip_address character varying(15),
    user_agent character varying,
    add_date timestamp with time zone,
    end_date timestamp with time zone
);

-- Create Status type for books to type_group and make entry to type [Available, Not Available]
INSERT INTO app.type_group(id, name, add_date) VALUES (2, 'Book Status', now());
INSERT INTO app.type(id, group_id, name, add_date) VALUES (3, 2, 'Available', now());
INSERT INTO app.type(id, group_id, name, add_date) VALUES (4, 2, 'Borrowed', now());
INSERT INTO app.type(id, group_id, name, add_date) VALUES (5, 2, 'Removed', now());

-- Create table to manage the genres types and its description
create table if not exists app.genres_types (
    id serial,
    title character varying(50),
    description character varying,
    image_url character varying,
    update_date timestamp with time zone,
    add_date timestamp with time zone,
    end_date timestamp with time zone,
    primary key(id)
);

-- Add Indexs on title of genres
CREATE INDEX genres_types_title_idx ON app.genres_types USING HASH(title) where end_date is null;

-- Create table which keep records about the books
CREATE TABLE IF NOT EXISTS app.books
(
    id uuid,
    title character varying,
    description character varying,
    author character varying,
    image_url character varying,
    shelf_location character varying,
    genre_id integer,
    status_id integer,
    add_date timestamp with time zone,
    end_date timestamp with time zone,
    CONSTRAINT books_genre_id_fk FOREIGN KEY (genre_id)
          REFERENCES app.genres_types(id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT books_status_id_fk FOREIGN KEY (status_id)
          REFERENCES app.type(id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    primary key(id)
);

-- Add Indexs on title of genres
CREATE INDEX books_title_idx ON app.books USING HASH(title) where end_date is null;

-- Create Table for Book checkouts management
create table if not exists app.book_checkouts (
    id serial,
    book_id uuid,
    member_id uuid,
    librarian_id uuid,
    issue_date timestamp with time zone,
    return_date timestamp with time zone,
    update_date timestamp with time zone,
    end_date timestamp with time zone,
    CONSTRAINT book_checkouts_book_id_fk FOREIGN KEY (book_id)
       REFERENCES app.books(id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT book_checkouts_member_id_fk FOREIGN KEY (member_id)
       REFERENCES app.users(id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT book_checkouts_librarian_id_fk FOREIGN KEY (librarian_id)
       REFERENCES app.users(id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    primary key(id)
);

-- Add Indexs on book_id
CREATE INDEX book_checkouts_book_id_idx ON app.book_checkouts USING HASH(book_id) where end_date is null;