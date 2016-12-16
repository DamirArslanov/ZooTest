CREATE TABLE `animal` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `animalClass` varchar(50) NOT NULL,
  `age` INT(11) NOT NULL,
  CONSTRAINT pk_animal PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE `keeper` (
  `keeperID` INT(11) NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `surname` VARCHAR(50) NOT NULL,
  CONSTRAINT pk_keeper PRIMARY KEY (keeperID)
)ENGINE=InnoDB;

CREATE TABLE `keeper_animals` (
  `animalID` INT (11) NOT NULL,
  `keeperID` INT (11) NOT NULL,
  FOREIGN KEY (animalID) REFERENCES animal(id),
  FOREIGN KEY (keeperID) REFERENCES keeper(keeperID),
  UNIQUE (animalID, keeperID)
)ENGINE=InnoDB;

CREATE TABLE `cage` (
  `cageID` INT(11) NOT NULL,
  `number` VARCHAR(50) NOT NULL,
  CONSTRAINT pk_cage PRIMARY KEY (cageID)
) ENGINE=InnoDB;

CREATE TABLE `cage_animals` (
  `animalID` INT(11) NOT NULL,
  `cageID` INT(11) NOT NULL,
  FOREIGN KEY (animalID) REFERENCES animal (id),
  FOREIGN KEY (cageID) REFERENCES cage(cageID),
  UNIQUE (animalID, cageID)
) ENGINE=InnoDB;

INSERT INTO cage VALUES (1, 1);
INSERT INTO cage VALUES (2, 13);

INSERT INTO animal VALUES (1, 'LionKing', 'CATS', 3);
INSERT INTO animal VALUES (2, 'Tiger', 'CATS', 2);
INSERT INTO animal VALUES (3, 'Milka', 'CATS', 2);
INSERT INTO animal VALUES (4, 'Kadjit', 'CATS', 6);


INSERT INTO cage_animals VALUES (1, 2);
INSERT INTO cage_animals VALUES (2, 1);
INSERT INTO cage_animals VALUES (3, 2);
INSERT INTO cage_animals VALUES (4, 1);

INSERT INTO keeper VALUES (1, 'Василий', 'Новоселцев');
INSERT INTO keeper VALUES (2, 'Александр', 'Садовник');

INSERT INTO keeper_animals VALUES (1, 1);
INSERT INTO keeper_animals VALUES (2, 2);
INSERT INTO keeper_animals VALUES (3, 1);
INSERT INTO keeper_animals VALUES (4, 2);

SELECT animal.name, animal.age, keeper.name, keeper.surname
FROM animal INNER JOIN keeper_animals
ON animal.id = keeper_animals.animalID
LEFT JOIN keeper ON keeper_animals.keeperID = keeper.keeperID;


SELECT COUNT(*) FROM keeper;

SELECT COUNT(*) FROM animal;

SELECT * FROM animal;

SELECT animal.name FROM animal WHERE id = 56;

SELECT animal.id, animal.name, animal.animalClass, animal.age, keeper.name, keeper.surname
FROM animal INNER JOIN keeper_animals
ON animal.id = keeper_animals.animalID
LEFT JOIN keeper ON keeper_animals.keeperID = keeper.keeperID;

SELECT animal.id, animal.name, animal.animalClass, keeper_animals.animalID, keeper_animals.keeperID
FROM animal INNER JOIN keeper_animals
ON animal.id = keeper_animals.animalID AND animal.id = 5;



SELECT
  animal.id, animal.name, animal.animalClass, animal.age,
  keeper.keeperID, keeper.name, keeper.surname,
  cage.cageID, cage.number
FROM animal
  LEFT JOIN keeper_animals ON animal.id = keeper_animals.animalID
  LEFT JOIN keeper ON keeper_animals.keeperID = keeper.keeperID
  LEFT JOIN cage_animals ON animal.id = cage_animals.animalID
  LEFT JOIN cage ON cage_animals.cageID = cage.cageID
  WHERE animal.id = 88;

SELECT animal.id, animal.name, animal.animalClass, animal.age
FROM animal
WHERE animal.id = 55;

SELECT keeper.keeperID, keeper.name, keeper.surname
FROM keeper JOIN keeper_animals ON keeper.keeperID = keeper_animals.keeperID
WHERE keeper_animals.animalID = 3;

SELECT cage.cageID, cage.number
FROM cage JOIN cage_animals ON cage.cageID = cage_animals.cageID
WHERE cage_animals.animalID = 3;

UPDATE keeper_animals SET keeperID = ? WHERE animalID = ?;
UPDATE cage_animals SET cageID = ? WHERE animalID = ?;

INSERT INTO keeper_animals (animalID, keeperID) VALUES (?, ?);
INSERT INTO cage_animals (animalID, cageID) VALUES (?, ?);



SELECT animal.id, animal.name, animal.animalClass, animal.age FROM animal WHERE animal.id = 3;
