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

    $usuario->setLatitude($_POST['latitude']);
    $usuario->setLongitude($_POST["longitude"]);
    $usuario->setId($_POST["id"]);

    // Create array
    $data = array();

    //Verify if login has sucess
    if ($usuario->atualizarLocalizacao()) {

        //Send fail status code
        http_response_code(201);

        $data = array(
            "status" => true,
            "message" => "Localização atualizada com sucesso!"
        );

    } else {

        http_response_code(503);

        $data = array(
            "status" => false,
            "message" => "Localização não atualizada!",
        );
    }

    $con->close();

    //Make it json format
    print_r(utf8_encode(json_encode($data)));
}