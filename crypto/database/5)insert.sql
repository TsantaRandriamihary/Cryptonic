INSERT INTO Crypto (nom, symbole, prix, prix_min, prix_max) VALUES
  ('Bitcoin', 'BTC', 455872578.00, 452755662.00, 459570373.00),
  ('Ethereum', 'ETH', 12577085.00, 12355312.00, 12670401.00),
  ('Ripple', 'XRP', 11475.92, 11239.79, 11806.50),
  ('Litecoin', 'LTC', 513063.26, 491008.72, 513771.65),
  ('Cardano', 'ADA', 3293.26, 3207.35, 3362.91),
  ('Polkadot', 'DOT', 22810.16, 21771.19, 22999.06),
  ('BNB', 'BNB', 2957338.00, 2884233.00, 3050186.00),
  ('Solana', 'SOL', 954956.95, 922371.01, 977861.56),
  ('Dogecoin', 'DOGE', 1200.94, 1177.94, 1223.20),
  ('Chainlink', 'LINK', 89209.91, 85762.42, 89351.59);


INSERT INTO Commission (pourcent_achat, pourcent_vente, date_modif) 
VALUES (10, 15, '2024-01-01 00:00:00');