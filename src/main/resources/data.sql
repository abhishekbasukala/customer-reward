INSERT INTO customer VALUES (1001, 'Peter', 'Parker', 'peterparker@test.com');
INSERT INTO customer VALUES (1002, 'Julie', 'Smith', 'juliesmith@test.com');
INSERT INTO customer VALUES (1003, 'Mark', 'Li', 'markli@test.com');
INSERT INTO customer VALUES (1004, 'Mary', 'Jane', 'maryjane@test.com');

INSERT INTO transaction VALUES (5001, 120, 'JANUARY', 1004);
INSERT INTO transaction VALUES (5002, 150, 'JANUARY', 1004);
INSERT INTO transaction VALUES (5005, 150, 'FEBRUARY', 1004);
INSERT INTO transaction VALUES (5006, 25, 'FEBRUARY', 1004);

INSERT INTO transaction VALUES (5003, 25, 'JANUARY', 1001);
INSERT INTO transaction VALUES (5004, 25, 'JANUARY', 1001);

INSERT INTO transaction VALUES (5007, 75, 'FEBRUARY', 1003);

INSERT INTO transaction VALUES (5008, 35, 'FEBRUARY', 1002);
INSERT INTO transaction VALUES (5009, 125, 'MARCH', 1002);
INSERT INTO transaction VALUES (5010, 65, 'MARCH', 1002);