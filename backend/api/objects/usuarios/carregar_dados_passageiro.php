<?php


header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// Get database connection
include_once '../../config/database.php';

// Instantiate user object
include_once '../../objects/usuario.php';

//Get Connection object
$con = Database::getConnection();

if($_SERVER['REQUEST_METHOD'] == 'POST') {

    //Get usuario object
    $usuario = new Usuario($con);
    $usuario->setId($_POST["id_passageiro"]);

    $stmt = $usuario->carregarDadosPassageiro();
    $stmt->store_result();

    $count = $stmt->num_rows();

    if ($count > 0) {

        $stmt->bind_result($id, $telefone);

        $row = $stmt->fetch();

        $usuarios_array = array(
                "id" => $id,
                "telefone" => $telefone
            );

        //Send sucess status code
        http_response_code(201);

        //Make it json format
        print_r(utf8_encode(json_encode($usuarios_array)));

    } else
        http_response_code(503);

    $con->close();
}