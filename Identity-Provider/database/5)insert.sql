INSERT INTO Etat_compte (val) VALUES
                                  ('créé'),
                                  ('désactivé'),
                                  ('bloqué');

INSERT INTO Role (id_role, val) VALUES
                                    (1, 'Utilisateur'),
                                    (2, 'Administrateur');

INSERT INTO Genre (val) VALUES
                            ('Homme'),
                            ('Femme');

INSERT INTO Parametre(nbr_tentative, duree_tentative, duree_session, duree_pin) VALUES (DEFAULT, DEFAULT, DEFAULT, DEFAULT);


CREATE EXTENSION IF NOT EXISTS pgcrypto;
INSERT INTO Utilisateur (email, mdp, id_etat, id_role)
VALUES
  ('mahandry.andrianantenaina16@gmail.com', crypt('1234', gen_salt('bf')), 1, 2),
  ('picocom972@sectorid.com', crypt('mdp1', gen_salt('bf')), 1, 1),
  ('mihailgolovanov482@code-gmail.com', crypt('mdp2', gen_salt('bf')), 1, 1),
  ('volodindeniska@tinyios.com', crypt('mdp3', gen_salt('bf')), 1, 1),
  ('ilyushnikovilya@khongtontai.tech', crypt('mdp4', gen_salt('bf')), 1, 1),
  ('qtgqch@btcmod.com', crypt('mdp5', gen_salt('bf')), 1, 1),
  ('julianachezgan@situsbebas.com', crypt('mdp6', gen_salt('bf')), 1, 1),
  ('elddap@kelangthang.com', crypt('mdp7', gen_salt('bf')), 1, 1),
  ('belkakamila@code-gmail.com', crypt('mdp8', gen_salt('bf')), 1, 1),
  ('mavgree@adsensekorea.com', crypt('mdp9', gen_salt('bf')), 1, 1);

INSERT INTO User_profile (nom, prenom, naissance, image, id_genre, id_user)
VALUES
  -- Profils féminins
  ('Andrianantenaina', 'Mahandry', '1985-06-15','http://res.cloudinary.com/duvfwe8zh/image/upload/v1738824267/cryto/inozuq4klmrf6no1l0ml.jpg', 1, (SELECT id_user FROM Utilisateur WHERE email = 'mahandry.andrianantenaina16@gmail.com')),
  ('Rasoarimanana', 'Fanja', '1995-03-15', 'http://res.cloudinary.com/duvfwe8zh/image/upload/v1738824267/cryto/eifvml1j6f0bcixfn6nu', 2, (SELECT id_user FROM Utilisateur WHERE email = 'picocom972@sectorid.com')),
  ('Rasoanaivo', 'Voahangy', '1994-07-20', 'http://res.cloudinary.com/duvfwe8zh/image/upload/v1738824267/cryto/kgopxelz00tvw6y7y21v', 2, (SELECT id_user FROM Utilisateur WHERE email = 'mihailgolovanov482@code-gmail.com')),
  ('Ramanantsoa', 'Mialy', '1996-01-10', 'http://res.cloudinary.com/duvfwe8zh/image/upload/v1738824267/cryto/bvmrako7tz7hsxtplb7k', 2, (SELECT id_user FROM Utilisateur WHERE email = 'volodindeniska@tinyios.com')),
  ('Randrianarisoa', 'Tiana', '1993-11-05', 'http://res.cloudinary.com/duvfwe8zh/image/upload/v1738824267/cryto/y5guzb7d37rei6az9pdo', 2, (SELECT id_user FROM Utilisateur WHERE email = 'ilyushnikovilya@khongtontai.tech')),
  ('Andrianjafy', 'Lalao', '1997-05-30', 'http://res.cloudinary.com/duvfwe8zh/image/upload/v1738824267/cryto/vg90un5qqfz0xkjut4qb', 2, (SELECT id_user FROM Utilisateur WHERE email = 'qtgqch@btcmod.com')),
  -- Profils masculins
  ('Rakotoarisoa', 'Andry', '1990-09-12', 'http://res.cloudinary.com/duvfwe8zh/image/upload/v1738824267/cryto/rploszrnm3vv8ieasibs', 1, (SELECT id_user FROM Utilisateur WHERE email = 'julianachezgan@situsbebas.com')),
  ('Ratsimamanga', 'Toavina', '1988-02-25', 'http://res.cloudinary.com/duvfwe8zh/image/upload/v1738824267/cryto/fbgvegr49ueorszjkycq', 1, (SELECT id_user FROM Utilisateur WHERE email = 'elddap@kelangthang.com')),
  ('Andrianavalona', 'Hery', '1992-12-07', 'http://res.cloudinary.com/duvfwe8zh/image/upload/v1738824267/cryto/yzbuziqhbzkoikvc4gbi', 1, (SELECT id_user FROM Utilisateur WHERE email = 'belkakamila@code-gmail.com')),
  ('Rasolofoson', 'Fetra', '1991-06-18', 'http://res.cloudinary.com/duvfwe8zh/image/upload/v1738824267/cryto/jmc7qx1zlmva0wnxd9bi', 1, (SELECT id_user FROM Utilisateur WHERE email = 'mavgree@adsensekorea.com'));