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

if($_SERVER['REQUEST_METHOD']=='POST') {

    //Get usuario object
    $usuario = new Usuario($con);

    if(isset($_POST['telefone']))
        $usuario->setTelefone($_POST['telefone']);

    if(isset($_POST['email']))
        $usuario->setEmail($_POST["email"]);

    // Set user property values
    $usuario->setSenha($_POST['senha']);

    // Create array
    $data = array();

    //Verify if login has sucess
    if ($usuario->alterPassoword()) {

        //Send fail status code
        http_response_code(201);

        $data = array(
            "status" => true,
            "message" => "Senha alterada com sucesso!"
        );

    } else {

        http_response_code(503);

        $data = array(
            "status" => false,
            "message" => "Senha não alterada!",
        );
    }

    $con->close();

    //Make it json format
    print_r(utf8_encode(json_encode($data)));
}