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

    // Set user property values
    if(isset($_POST['telefone']))
        $usuario->setTelefone($_POST['telefone']);

    if(isset($_POST['email']))
        $usuario->setEmail($_POST["email"]);


    // Create array
    $data = array();

    //Verify if login has sucess
    if ($usuario->active_acount()) {

        //Send sucess status code
        http_response_code(201);

        // Create array
        $data = array(
            "sucess" => true,
            "status" => true,
            "message" => "Conta activada!"
        );

    } else {

        //Send fail status code
        http_response_code(503);

        // Create array
        $data = array(
            "sucess" => false,
            "status" => false,
            "message" => "Conta não activada!",
        );
    }

    $con->close();

    //Make it json format
    print_r(utf8_encode(json_encode($data)));
}