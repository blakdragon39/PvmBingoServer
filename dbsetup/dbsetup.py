import subprocess
import json
import mysql.connector

mydb = mysql.connector.connect(
    host="localhost",
    user="root",
    password="",
    database="pvmbingo"
)
cursor = mydb.cursor()

subprocess.call("""mysql --user="root" --database="pvmbingo" --password="" < "setupdb.sql\"""", shell=True)

with open('drops.json') as file_data:
    json_data = json.load(file_data)

    for json_item in json_data:
        boss_name, boss_file = json_item['boss'], json_item['boss_file']
        item_name, item_file = json_item['name'], json_item['item_file']
        drop_rate = json_item['drop_rate']
        print "Inserting " + boss_name + " " + boss_file + " " + item_name + " " + item_file + " " + str(drop_rate)

        cursor.execute("INSERT IGNORE INTO bosses (name, file) VALUES (%s, %s)", (boss_name, boss_file))
        cursor.execute("INSERT IGNORE INTO items (name, file) VALUES (%s, %s)", (item_name, item_file))
        cursor.execute("INSERT INTO drops (boss, item, drop_rate) VALUES (%s, %s, %s)", (boss_name, item_name, drop_rate))

mydb.commit()
