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

    // Set user property values
    $usuario->setEmail($_POST['email']);
    $usuario->setSenha($_POST['senha']);

    // Create array
    $usuario_arr = array();

    //Verify if login has sucess
    if ($usuario->login()) {

        //Send sucess status code
        http_response_code(201);

        // Create array
        $usuario_arr = array(
            "id" => $usuario->getId(),
            "nome" => $usuario->getNome(),
            "sobrenome" => $usuario->getSobrenome(),
            "email" => $usuario->getEmail(),
            "senha" => $usuario->getSenha(),
            "id_categoria" => $usuario->getIdCategoria(),
            "id_provincia" => $usuario->getIdProvincia(),
            "telefone" => $usuario->getTelefone()
         );

    } else {

        //Send fail status code
        http_response_code(503);

        // Create array
        $usuario_arr = array(
            "status" => false,
            "message" => "Nome de usuário ou senha inválida!",
            "connection" => $con->error
        );
    }

    $con->close();

    //Make it json format
    print_r(utf8_encode(json_encode($usuario_arr)));
}
