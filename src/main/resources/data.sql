-- Insert sample data into reservation_status table
INSERT INTO reservation_status (id, status_value) VALUES
(1, 'Reserved'),
(2, 'Cancelled'),
(3, 'Checked Out'),
(4, 'Returned');

-- Insert sample data into book table
INSERT INTO book (id, title, category, publication_date, copies_owned) VALUES
(1, 'Book Title 1', 'NEW', '2021-01-15', 5),
(2, 'Book Title 2', 'CLASSIC', '2019-08-20', 3),
(3, 'Book Title 3', 'STANDARD', '2020-03-10', 7),
(4, 'Book Title 4', 'NEW', '2018-11-05', 2);

-- Insert sample data into author table
INSERT INTO author (id, first_name, last_name) VALUES
(1, 'John', 'Doe'),
(2, 'Jane', 'Smith'),
(3, 'James', 'Johnson');

-- Insert sample data into book_author table
INSERT INTO book_author (book_id, author_id) VALUES
(1, 1),
(1, 2),
(2, 2),
(3, 3),
(4, 1),
(4, 3);

-- Insert sample data into member_status table
INSERT INTO member_status (id, status_value) VALUES
(1, 'Active'),
(2, 'Inactive');

-- Insert sample data into member table
INSERT INTO member (id, first_name, last_name, joined_date, active_status_id) VALUES
(1, 'Michael', 'Smith', '2020-05-25', 1),
(2, 'Emily', 'Brown', '2019-10-12', 1),
(3, 'David', 'Lee', '2021-02-08', 1),
(4, 'Sarah', 'Johnson', '2018-07-03', 2);

-- Insert sample data into reservation table
INSERT INTO reservation (id, book_id, member_id, reservation_date, reservation_status_id) VALUES
(1, 1, 1, '2021-06-05', 1),
(2, 2, 2, '2021-08-15', 1),
(3, 3, 3, '2021-07-20', 1),
(4, 4, 4, '2021-09-10', 1);

-- Insert sample data into fine_payment table
INSERT INTO fine_payment (id, member_id, payment_date, payment_amount) VALUES
(1, 1, '2021-06-20', 10),
(2, 3, '2021-08-30', 5),
(3, 4, '2021-09-25', 8);

-- Insert sample data into loan table
--INSERT INTO loan (id, book_id, member_id, loan_date, returned_date) VALUES
--(1, 1, 1, '2021-06-10', '2021-07-05'),
--(2, 3, 2, '2021-08-25', '2021-09-15'),
--(3, 4, 3, '2021-09-15', NULL);

-- Insert sample data into fine table
--INSERT INTO fine (id, book_id, loan_id, fine_date, fine_amount) VALUES
--(1, 1, 1, '2021-07-10', 3),
--(2, 3, 2, '2021-09-25', 7);
