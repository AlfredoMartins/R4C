<?php

header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");


// Pega a conexão com o banco de dados
include_once '../../config/database.php';

//Instancia o objecto da classe usuarios
include_once '../../objects/mensagem.php';

$con = Database::getConnection();

if($_SERVER['REQUEST_METHOD']=='POST') {

    //Get mensagem object
    $mensagem = new Mensagem($con);

    $mensagem->setIdEmissor($_POST['id_emissor']);
    $mensagem->setIdReceptor($_POST['id_receptor']);
    $mensagem->setTexto($_POST['texto']);

    //If sucess has sucess
    if ($mensagem->inserir_mensagem()) {

        //Send sucess status code
        http_response_code(201);

        // Create array
        $data = array(
            "status" => true,
            "message" => "Mensagem enviada com sucesso.",
        );
    } else {

        //Send fail status code
        http_response_code(503);

        // Create array
        $data = array(
            "status" => false,
            "message" => "Erro ao enviar mensagem.",
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