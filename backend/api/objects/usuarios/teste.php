<?php


 $HOST = "localhost";
 $PORT = 3306;
 $USER = "root";
 $PASSWORD = "";
 $DATABASE_NAME = "r4c_db";


$connection->exec("set names utf8");

echo json_encode($connection->errorInfo());
