-- Insert sample data into book table
INSERT INTO book (title, category, publication_date, copies_owned) VALUES
('Book Title 1', 'NEW', DATE '2021-01-15', 5),
('Book Title 2', 'CLASSIC', DATE '2022-02-20', 3),
('Book Title 3', 'STANDARD', DATE '2020-03-10', 7);

-- Insert sample data into author table
INSERT INTO author (first_name, last_name) VALUES
('John', 'Doe'),
('Jane', 'Smith'),
('James', 'Johnson');

-- Insert sample data into book_author table
INSERT INTO book_author (book_id, author_id) VALUES
(1, 1),
(1, 2),
(2, 2),
(3, 3);

-- Insert sample data into member_status table
INSERT INTO member_status (status_value) VALUES
('ACTIVE'),
('INACTIVE');
--
-- Insert sample data into member table
INSERT INTO member (first_name, last_name, joined_date, active_status_id) VALUES
('Michael', 'Smith', '2020-05-25', 1),
('Emily', 'Brown', '2019-10-12', 1),
('David', 'Lee', '2021-02-08', 1),
('Sarah', 'Johnson', '2018-07-03', 2);

---- Insert sample data into loan table
----INSERT INTO loan (id, book_id, member_id, loan_date, returned_date) VALUES
----(1, 1, 1, '2021-06-10', '2021-07-05'),
----(2, 3, 2, '2021-08-25', '2021-09-15'),
----(3, 4, 3, '2021-09-15', NULL);
--
---- Insert sample data into fine table
----INSERT INTO fine (id, book_id, loan_id, fine_date, fine_amount) VALUES
----(1, 1, 1, '2021-07-10', 3),
----(2, 3, 2, '2021-09-25', 7);

