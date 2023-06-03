-- Add an new column update_date to app.books table
alter table app.books add column if not exists update_date timestamp with time zone;
