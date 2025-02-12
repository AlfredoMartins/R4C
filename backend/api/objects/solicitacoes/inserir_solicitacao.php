<?php

header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");


// Pega a conexão com o banco de dados
include_once '../../config/database.php';

//Instancia o objecto da classe usuarios
include_once '../../objects/solicitacao.php';

$con = Database::getConnection();

if($_SERVER['REQUEST_METHOD']=='POST') {

    //Get usuario object
    $solicitacao = new Solicitacao($con);

    $solicitacao->setIdMotorista($_POST['id_motorista']);
    $solicitacao->setIdPassageiro($_POST['id_passageiro']);
    $solicitacao->setIdLocalOrigem($_POST['id_local_origem']);
    $solicitacao->setIdLocalDestino($_POST['id_local_destino']);

    //If sucess has sucess
    if ($solicitacao->inserir_solicitacao()) {

        //Send sucess status code
        http_response_code(201);

        // Create array
        $data = array(
            "status" => true,
            "message" => "Solicitação enviada com sucesso.",
        );
    } else {

        //Send fail status code
        http_response_code(503);

        // Create array
        $data = array(
            "status" => false,
            "message" => "Erro ao enviar solicitação.",
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