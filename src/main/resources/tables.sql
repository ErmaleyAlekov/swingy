CREATE TABLE IF NOT EXISTS characters(  
    id int,
    name TEXT,
    class TEXT,
    lvl int NOT NULL,
    exp int NOT NULL,
    attack int NOT NULL,
    defense int NOT NULL,
    hp int NOT NULL,
    equip TEXT,
    inventory TEXT
);

CREATE TABLE IF NOT EXISTS users(
    id int,
    name TEXT,
    password TEXT,
    charsID TEXT
);

CREATE TABLE IF NOT EXISTS items(
    id int,
    name TEXT,
    type TEXT,
    attack int,
    defense int
);

INSERT INTO items(id,name,type,attack,defense) VALUES(1,'Leather boots', 'boot', 0, 3);
INSERT INTO items(id,name,type,attack,defense) VALUES(2,'sword', 'weapon', 5, 0);
INSERT INTO items(id,name,type,attack,defense) VALUES(3,'spear', 'weapon', 5, 0);
INSERT INTO items(id,name,type,attack,defense) VALUES(4,'Hummer', 'weapon', 5, 0);
INSERT INTO items(id,name,type,attack,defense) VALUES(5,'Plate boots', 'boot', 0, 5);
INSERT INTO items(id,name,type,attack,defense) VALUES(6,'Silver boots', 'boot', 0, 7);
INSERT INTO items(id,name,type,attack,defense) VALUES(7,'Plate Armor', 'armor', 0, 15);
INSERT INTO items(id,name,type,attack,defense) VALUES(8,'Leather Armor', 'armor', 0, 10);
INSERT INTO items(id,name,type,attack,defense) VALUES(9,'Silver Armor', 'armor', 0, 20);
INSERT INTO items(id,name,type,attack,defense) VALUES(10,'Leather helmet', 'helmet', 0, 5);
INSERT INTO items(id,name,type,attack,defense) VALUES(11,'Leather gloves', 'glove', 0, 5);
INSERT INTO items(id,name,type,attack,defense) VALUES(12,'Plate gloves', 'glove', 0, 7);
INSERT INTO items(id,name,type,attack,defense) VALUES(13,'Silver gloves', 'glove', 0, 7);
INSERT INTO items(id,name,type,attack,defense) VALUES(14,'Plate helmet', 'helmet', 0, 8);
INSERT INTO items(id,name,type,attack,defense) VALUES(15,'Silver helmet', 'helmet', 0, 8);
INSERT INTO items(id,name,type,attack,defense) VALUES(16,'Silver sword', 'weapon', 10, 0);
INSERT INTO items(id,name,type,attack,defense) VALUES(17,'Silver Spear', 'weapon', 10, 0);
INSERT INTO items(id,name,type,attack,defense) VALUES(18,'Silver Hummer', 'weapon', 10, 0);
INSERT INTO items(id,name,type,attack,defense) VALUES(19,'Nunchaku', 'weapon', 15, 0);
INSERT INTO items(id,name,type,attack,defense) VALUES(20,'Bow', 'weapon', 15, 0);
INSERT INTO items(id,name,type,attack,defense) VALUES(21,'Long Bow', 'weapon', 20, 0);
INSERT INTO items(id,name,type,attack,defense) VALUES(22,'Two Handed Sword', 'weapon', 20, 0);
INSERT INTO items(id,name,type,attack,defense) VALUES(23,'Two Handed Silver Sword', 'weapon', 21, 0);
INSERT INTO items(id,name,type,attack,defense) VALUES(24,'Crossbow', 'weapon', 20, 0);
INSERT INTO items(id,name,type,attack,defense) VALUES(25,'Staff', 'weapon', 5, 0);
INSERT INTO items(id,name,type,attack,defense) VALUES(26,'Two handed Staff', 'weapon', 10, 0);
INSERT INTO items(id,name,type,attack,defense) VALUES(27,'Silver Staff', 'weapon', 15, 0);
INSERT INTO items(id,name,type,attack,defense) VALUES(28,'Silver Two handed Staff', 'weapon', 20, 0);
INSERT INTO items(id,name,type,attack,defense) VALUES(29,'Knife', 'weapon', 3, 0);
INSERT INTO items(id,name,type,attack,defense) VALUES(30,'Silver Knife', 'weapon', 10, 0);