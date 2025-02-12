<?php


header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// Get database connection
include_once '../../config/database.php';

// Instantiate user object
include_once '../../objects/mensagem.php';

//Get Connection object
$con = Database::getConnection();

if($_SERVER['REQUEST_METHOD'] == 'POST') {

    //Get usuario object
    $mensagem = new Mensagem($con);

    $mensagem->setIdEmissor($_POST['user_id']);

    $stmt = $mensagem->listarMensagens();
    $stmt->store_result();

    $count = $stmt->num_rows();

    if ($count > 0) {

        $mensagem_array = array();

        $stmt->bind_result($id_emissor, $nome, $telefone, $foto_url);

        while ($row = $stmt->fetch()) {

            $stmt_2 = $mensagem->ultimaMensagem($mensagem->getIdEmissor(), $id_emissor);
            $stmt_2->store_result();
            $stmt_2->bind_result($texto, $data);
            $stmt_2->fetch();


            $u = array(
                "user_id" => $id_emissor,
                "nome" => $nome,
                "telefone" => $telefone,
                "texto" => $texto,
                "foto_url" => $foto_url,
                "data" => $data
            );

            array_push($mensagem_array, $u);
        }

        //Send sucess status code
        http_response_code(201);

        //Make it json format
        print_r(utf8_encode(json_encode($mensagem_array)));

    } else
        http_response_code(503);

    $con->close();
}