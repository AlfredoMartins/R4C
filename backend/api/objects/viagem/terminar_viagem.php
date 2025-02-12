<?php

header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");


// Pega a conexão com o banco de dados
include_once '../../config/database.php';

//Instancia o objecto da classe usuarios
include_once '../../objects/viagem.php';

$con = Database::getConnection();

if($_SERVER['REQUEST_METHOD']=='POST') {

    //Get mensagem object
    $viagem = new Viagem($con);

    $viagem->setId($_POST['id']);
    $viagem->setPreco($_POST['preco']);

    //If sucess has sucess
    if ($viagem->terminar_viagem()) {

        //Send sucess status code
        http_response_code(201);

        // Create array
        $data = array(
            "status" => true,
            "message" => "Viagem completa.",
        );
    } else {

        //Send fail status code
        http_response_code(503);

        // Create array
        $data = array(
            "status" => false,
            "message" => "Erro ao actualizar viagem.",
        );
    }

    //Make it json format
    print_r(utf8_encode(json_encode($data)));

    $con->close();
} else{

    http_response_code(503);

    $data = array(
        "status" => false,
        "message" => "Erro!",
    );

    print_r(utf8_encode(json_encode($data)));
}