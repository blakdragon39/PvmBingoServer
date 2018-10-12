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
		pass