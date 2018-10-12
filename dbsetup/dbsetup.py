import json
import mysql.connector

mydb = mysql.connector.connect(
    host="localhost",
    user="root",
    password="",
    database="pvmbingo"
)
cursor = mydb.cursor()
cursor.execute("DROP TABLE IF EXISTS drops")
cursor.execute(
    """CREATE TABLE drops(
        id INT PRIMARY KEY AUTO_INCREMENT,
        item_name VARCHAR(255),
        item_file VARCHAR(255),
        boss_name VARCHAR(255),
        boss_file VARCHAR(255),
        drop_rate INT)""")

with open('drops.json') as file_data:
    json_data = json.load(file_data)

    for json_item in json_data:
        boss_name, boss_file = json_item['boss'], json_item['boss_file']
        item_name, item_file = json_item['name'], json_item['item_file']
        drop_rate = json_item['drop_rate']
        print "Inserting " + boss_name + " " + boss_file + " " + item_name + " " + item_file + " " + str(drop_rate)

        cursor.execute("""
            INSERT INTO drops (item_name, item_file, boss_name, boss_file, drop_rate) VALUES (%s, %s, %s, %s, %s)
            """, (item_name, item_file, boss_name, boss_file, drop_rate))

mydb.commit()
mydb.close()