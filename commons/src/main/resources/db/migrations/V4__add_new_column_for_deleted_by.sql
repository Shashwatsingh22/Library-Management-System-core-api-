-- Add an new column deleted_by to app.users table
alter table app.users add column if not exists deleted_by uuid;

-- Added Types of Genres for books with description
insert into app.genres_types(title, description, image_url, add_date)
values
('Fiction', 'Imaginative or invented stories, usually with some basis in reality.', 'https://fastly.picsum.photos/id/184/4288/2848.jpg?hmac=l0fKWzmWf6ISTPMEm1WjRdxn35sg6U3GwZLn5lvKhTI', now()),
('Non-fiction', 'Real, factual stories or information, typically focused on a specific topic.', 'https://fastly.picsum.photos/id/184/4288/2848.jpg?hmac=l0fKWzmWf6ISTPMEm1WjRdxn35sg6U3GwZLn5lvKhTI', now()),
('Mystery', 'A genre that revolves around solving a crime or mystery.','https://fastly.picsum.photos/id/184/4288/2848.jpg?hmac=l0fKWzmWf6ISTPMEm1WjRdxn35sg6U3GwZLn5lvKhTI', now()),
('Romance', 'Novels or stories centered around love, passion, and relationships.','https://fastly.picsum.photos/id/184/4288/2848.jpg?hmac=l0fKWzmWf6ISTPMEm1WjRdxn35sg6U3GwZLn5lvKhTI', now()),
('Science fiction', 'Stories that involve imagined or futuristic science and technology.','https://fastly.picsum.photos/id/184/4288/2848.jpg?hmac=l0fKWzmWf6ISTPMEm1WjRdxn35sg6U3GwZLn5lvKhTI', now()),
('Fantasy', 'Stories that involve magic, mythical creatures, and imaginative worlds.','https://fastly.picsum.photos/id/184/4288/2848.jpg?hmac=l0fKWzmWf6ISTPMEm1WjRdxn35sg6U3GwZLn5lvKhTI', now()),
('Horror', 'A genre that revolves around fear and terror.','https://fastly.picsum.photos/id/184/4288/2848.jpg?hmac=l0fKWzmWf6ISTPMEm1WjRdxn35sg6U3GwZLn5lvKhTI', now()),
('Historical fiction', 'Novels or stories set in a historical time period.','https://fastly.picsum.photos/id/184/4288/2848.jpg?hmac=l0fKWzmWf6ISTPMEm1WjRdxn35sg6U3GwZLn5lvKhTI', now()),
('Biography', 'A non-fiction genre that tells the story of a person' || 's life.','https://fastly.picsum.photos/id/184/4288/2848.jpg?hmac=l0fKWzmWf6ISTPMEm1WjRdxn35sg6U3GwZLn5lvKhTI', now()),
('Autobiography', 'A subgenre of biography where the author writes about their own life.','https://fastly.picsum.photos/id/184/4288/2848.jpg?hmac=l0fKWzmWf6ISTPMEm1WjRdxn35sg6U3GwZLn5lvKhTI', now()),
('Memoir', 'A subgenre of autobiography that focuses on a specific period or experience in the author' || 's life.','https://fastly.picsum.photos/id/184/4288/2848.jpg?hmac=l0fKWzmWf6ISTPMEm1WjRdxn35sg6U3GwZLn5lvKhTI', now()),
('Thriller', 'A genre that revolves around suspense and danger.','https://fastly.picsum.photos/id/184/4288/2848.jpg?hmac=l0fKWzmWf6ISTPMEm1WjRdxn35sg6U3GwZLn5lvKhTI', now()),
('Young adult', 'Fiction geared towards teenagers or young adults.','https://fastly.picsum.photos/id/184/4288/2848.jpg?hmac=l0fKWzmWf6ISTPMEm1WjRdxn35sg6U3GwZLn5lvKhTI', now()),
('Children' || 's', 'Books written for and targeted towards children.','https://fastly.picsum.photos/id/184/4288/2848.jpg?hmac=l0fKWzmWf6ISTPMEm1WjRdxn35sg6U3GwZLn5lvKhTI', now()),
('Poetry', 'A genre that uses language and rhythm to create emotional responses and express ideas.','https://fastly.picsum.photos/id/184/4288/2848.jpg?hmac=l0fKWzmWf6ISTPMEm1WjRdxn35sg6U3GwZLn5lvKhTI', now());

-- Add Unique constraint on title of book and author name
CREATE UNIQUE INDEX books_title_author_unq ON app.books(title, author) where end_date is null;
