DROP TABLE IF EXISTS players;

CREATE TABLE players (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(55) NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  updated_date_time TIMESTAMP,
  promo_applied INT NULL DEFAULT 0,
  promo_count INT NULL DEFAULT 0,
  balance double DEFAULT '0.00'
);

DROP TABLE IF EXISTS transactions;

CREATE TABLE transactions (
  id INT AUTO_INCREMENT PRIMARY KEY,
  player_id INT NOT NULL,
  amount DOUBLE NOT NULL,
  transaction VARCHAR(55) NOT NULL,
  created_date_time TIMESTAMP NOT NULL
);

INSERT INTO players (username, first_name, last_name, balance) VALUES
  ('eldzune@gmail.com' ,'Blessing', 'Nkhwashu', '0.00'),
  ('nkateko@gmail.com', 'Nkateko', 'Treasure', '0.00'),
  ('ref@gmail.com', 'Refilwe', 'laElephant', '0.00');