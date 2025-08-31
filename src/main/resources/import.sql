INSERT INTO owner (id, name, email) VALUES (1, 'Ana Pop', 'ana.pop@example.com');
INSERT INTO owner (id, name, email) VALUES (2, 'Bogdan Ionescu', 'bogdan.ionescu@example.com');

INSERT INTO car (id, vin, make, model, year_of_manufacture, owner_id) VALUES (1, 'VIN12345', 'Dacia', 'Logan', 2018, 1);
INSERT INTO car (id, vin, make, model, year_of_manufacture, owner_id) VALUES (2, 'VIN67890', 'VW', 'Golf', 2021, 2);

INSERT INTO insurancepolicy ( car_id, provider, start_date, end_date) VALUES ( 1, 'Allianz', DATE '2024-01-01', DATE '2024-12-31');
INSERT INTO insurancepolicy ( car_id, provider, start_date, end_date) VALUES ( 1, 'Groupama', DATE '2025-01-01', DATE '2026-01-01' );
INSERT INTO insurancepolicy ( car_id, provider, start_date, end_date) VALUES ( 2, 'Allianz', DATE '2025-03-01', DATE '2025-09-30');

INSERT INTO insuranceclaim ( car_id, claim_date, description, amount) VALUES ( 1, DATE '2024-02-15', 'Front bumper collision', 1200.00);
INSERT INTO insuranceclaim ( car_id, claim_date, description, amount) VALUES ( 1, DATE '2025-07-20', 'Windshield replacement', 470.00);
INSERT INTO insuranceclaim ( car_id, claim_date, description, amount) VALUES ( 1, DATE '2024-06-20', 'Rear windshield replacement', 450.00);
