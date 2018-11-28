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
        drop_rate INT NOT NULL,
        slayer_level INT)""")

cursor.execute("DROP TABLE IF EXISTS card_items")
cursor.execute(
    """CREATE TABLE card_items(
        id INT PRIMARY KEY AUTO_INCREMENT,
        drop_id INT NOT NULL,
        user_name VARCHAR(255) NOT NULL,
        proof VARCHAR(255))""")

with open('drops.json') as file_data:
    json_data = json.load(file_data)

    for json_item in json_data:
        boss_name, boss_file = json_item['boss'], json_item['boss_file']
        item_name, item_file = json_item['name'], json_item['item_file']
        drop_rate = json_item['drop_rate']
        slayer_level = json_item['slayer_level']
        print("Inserting " + boss_name + " " + boss_file + " " + item_name + " " + item_file + " " + str(drop_rate) + " " + str(slayer_level))

        cursor.execute("""
            INSERT INTO drops (item_name, item_file, boss_name, boss_file, drop_rate, slayer_level) VALUES (%s, %s, %s, %s, %s, %s)
            """, (item_name, item_file, boss_name, boss_file, drop_rate, slayer_level))

mydb.commit()
mydb.close()