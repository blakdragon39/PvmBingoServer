import json
import mysql.connector
from dbcreds import DB_USERNAME, DB_PASSWORD

mydb = mysql.connector.connect(
    host="localhost",
    user=DB_USERNAME,
    password=DB_PASSWORD,
    database="pvmbingo"
)
cursor = mydb.cursor()
cursor.execute("DROP TABLE IF EXISTS drops")
cursor.execute(
    """CREATE TABLE drops(
        id INT PRIMARY KEY AUTO_INCREMENT,
        item_name VARCHAR(255) NOT NULL,
        item_file VARCHAR(255) NOT NULL,
        boss_name VARCHAR(255) NOT NULL,
        boss_file VARCHAR(255) NOT NULL,
        drop_rate INT NOT NULL)""")

cursor.execute("DROP TABLE IF EXISTS card_items")
cursor.execute(
    """CREATE TABLE card_items(
        id INT PRIMARY KEY AUTO_INCREMENT,
        drop_id INT NOT NULL,
        proof VARCHAR(255))""")

cursor.execute("DROP TABLE IF EXISTS cards")
cursor.execute(
    """CREATE TABLE cards(
        id INT PRIMARY KEY AUTO_INCREMENT,
        user_name VARCHAR(255) NOT NULL,
        card_item_1 INT NOT NULL,
        card_item_2 INT NOT NULL,
        card_item_3 INT NOT NULL,
        card_item_4 INT NOT NULL,
        card_item_5 INT NOT NULL,
        card_item_6 INT NOT NULL,
        card_item_7 INT NOT NULL,
        card_item_8 INT NOT NULL,
        card_item_9 INT NOT NULL,
        card_item_10 INT NOT NULL,
        card_item_11 INT NOT NULL,
        card_item_12 INT NOT NULL,
        card_item_13 INT NOT NULL,
        card_item_14 INT NOT NULL,
        card_item_15 INT NOT NULL,
        card_item_16 INT NOT NULL,
        card_item_17 INT NOT NULL,
        card_item_18 INT NOT NULL,
        card_item_19 INT NOT NULL,
        card_item_20 INT NOT NULL,
        card_item_21 INT NOT NULL,
        card_item_22 INT NOT NULL,
        card_item_23 INT NOT NULL,
        card_item_24 INT NOT NULL,
        card_item_25 INT NOT NULL)""")

with open('drops.json') as file_data:
    json_data = json.load(file_data)

    for json_item in json_data:
        boss_name, boss_file = json_item['boss'], json_item['boss_file']
        item_name, item_file = json_item['name'], json_item['item_file']
        drop_rate = json_item['drop_rate']
        print("Inserting " + boss_name + " " + boss_file + " " + item_name + " " + item_file + " " + str(drop_rate))

        cursor.execute("""
            INSERT INTO drops (item_name, item_file, boss_name, boss_file, drop_rate) VALUES (%s, %s, %s, %s, %s)
            """, (item_name, item_file, boss_name, boss_file, drop_rate))

mydb.commit()
mydb.close()