
<?php
class Database
{
    public static function  getConnection()
    {
        //Dados do Servidor
        $host = "localhost";
        $port = 3306;
        $socket = "";
        $user = "root";
        $password = "";
        $dbname = "r4c_db";

        //Conecta-se ao Servidor
        $con = new mysqli($host, $user, $password, $dbname, $port, $socket)
        or die ('Could not connect to the database server' . mysqli_connect_error());

        //Permite extrair caracteres acentuados
        $con->set_charset("utf8");

        return $con;
    }
}