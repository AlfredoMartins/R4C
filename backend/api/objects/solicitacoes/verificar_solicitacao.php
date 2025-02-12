<?php


header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// Get database connection
include_once '../../config/database.php';

// Instantiate user object
include_once '../../objects/solicitacao.php';

//Get Connection object
$con = Database::getConnection();

if($_SERVER['REQUEST_METHOD'] == 'POST') {

    //Get usuario object
    $solicitacao = new Solicitacao($con);
    $solicitacao->setIdMotorista($_POST['id_motorista']);

    $stmt = $solicitacao->verificarSolicitacao();
    $stmt->store_result();

    $count = $stmt->num_rows();

    if ($count > 0) {

        $stmt->bind_result($id, $id_motorista, $nome_motorista, $id_passageiro, $nome_passageiro,
            $minutos, $estado_solicitacao, $lat_origem, $lon_origem, $lat_destino, $lon_destino, $descricao);

        $stmt->fetch();

        $usuarios_array = array(
            "id" => $id,
            "id_motorista" => $id_motorista,
            "nome_motorista" => $nome_motorista,
            "id_passageiro" => $id_passageiro,
            "nome_passageiro" => $nome_passageiro,
            "minutos" => $minutos,
            "estado_solicitacao" => $estado_solicitacao,
            "lat_origem" => $lat_origem,
            "lon_origem" => $lon_origem,
            "lat_destino" => $lat_destino,
            "lon_destino" => $lon_destino,
            "descricao" => $descricao
        );

        //Send sucess status code
        http_response_code(201);

        //Make it json format
        print_r(utf8_encode(json_encode($usuarios_array)));

    } else
        http_response_code(503);

    $con->close();
}