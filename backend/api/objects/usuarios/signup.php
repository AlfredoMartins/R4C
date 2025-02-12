<?php

header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");


// Pega a conexão com o banco de dados
include_once '../../config/database.php';

//Instancia o objecto da classe usuarios
include_once '../../objects/usuario.php';

$con = Database::getConnection();

if($_SERVER['REQUEST_METHOD']=='POST') {

    //Get usuario object
    $usuario = new Usuario($con);

    $usuario->setNome($_POST['nome']);
    $usuario->setSobrenome($_POST['sobrenome']);
    $usuario->setEmail($_POST['email']);
    $usuario->setTelefone($_POST['telefone']);
    $usuario->setIdCategoria($_POST['id_categoria']);
    $usuario->setSenha($_POST['senha']);

    //Create array
    $data = array();

    //If sucess has sucess
    if ($usuario->signup()) {

        //Send sucess status code
        http_response_code(201);

        // Create array
        $data = array(
            "status" => true,
            "message" => "Usuário cadastrado com sucesso!",
            "usuarios" => $usuario->getNome() . " " . $usuario->getSobrenome()
        );
    } else {

        //Send fail status code
        http_response_code(503);

        // Create array
        $data = array(
            "status" => false,
            "message" => "Usuário já existe!",
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