CREATE TABLE reservation_status (
  id INT PRIMARY KEY,
  status_value VARCHAR(50)
);

CREATE TABLE book (
  id INT PRIMARY KEY,
  title VARCHAR(500),
  category VARCHAR(500),
  publication_date DATE,
  copies_owned INT
);

CREATE TABLE author (
  id INT PRIMARY KEY,
  first_name VARCHAR(300),
  last_name VARCHAR(300)
);

CREATE TABLE book_author (
  book_id INT,
  author_id INT,
  PRIMARY KEY (book_id, author_id),
  FOREIGN KEY (book_id) REFERENCES book(id),
  FOREIGN KEY (author_id) REFERENCES author(id)
);

CREATE TABLE member_status (
  id INT PRIMARY KEY,
  status_value VARCHAR(50)
);

CREATE TABLE member (
  id INT PRIMARY KEY,
  first_name VARCHAR(300),
  last_name VARCHAR(300),
  joined_date DATE,
  active_status_id INT,
  FOREIGN KEY (active_status_id) REFERENCES member_status(id)
);

CREATE TABLE reservation (
  id INT PRIMARY KEY,
  book_id INT,
  member_id INT,
  reservation_date DATE,
  reservation_status_id INT,
  FOREIGN KEY (book_id) REFERENCES book(id),
  FOREIGN KEY (member_id) REFERENCES member(id),
  FOREIGN KEY (reservation_status_id) REFERENCES reservation_status(id)
);

CREATE TABLE fine_payment (
  id INT PRIMARY KEY,
  member_id INT,
  payment_date DATE,
  payment_amount INT,
  FOREIGN KEY (member_id) REFERENCES member(id)
);

CREATE TABLE loan (
  id INT PRIMARY KEY AUTO_INCREMENT,
  book_id INT,
  member_id INT,
  loan_date DATE,
  due_date DATE,
  returned_date DATE,
  FOREIGN KEY (book_id) REFERENCES book(id),
  FOREIGN KEY (member_id) REFERENCES member(id)
);

CREATE TABLE fine (
  id INT PRIMARY KEY,
  book_id INT,
  loan_id INT,
  fine_date DATE,
  fine_amount INT,
  FOREIGN KEY (book_id) REFERENCES book(id),
  FOREIGN KEY (loan_id) REFERENCES loan(id)
);

CREATE INDEX idx_loan_book ON loan (book_id);
CREATE INDEX idx_loan_member ON loan (member_id);
