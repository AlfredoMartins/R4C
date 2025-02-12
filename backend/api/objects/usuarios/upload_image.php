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

    $usuario->setId($_POST['id']);

    $id = $usuario->getId();
    $usuario->setFotoUrl("upload_images/$id.jpg");

    $image = $_POST['image'];

    // Create array
    $data = array();

    //Verify if login has sucess
    if ($usuario->upload_image()) {


        file_put_contents($usuario->getFotoUrl(), base64_decode($image));

        //Send fail status code
        http_response_code(201);

        $data = array(
            "status" => true,
            "message" => "Imagem alterada com sucesso!"
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