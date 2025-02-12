<?php


header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: GET");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// Get database connection
include_once '../../config/database.php';

// Instantiate user object
include_once '../../objects/usuario.php';

//Get Connection object
$con = Database::getConnection();

if($_SERVER['REQUEST_METHOD'] == 'GET') {

    //Get usuario object
    $usuario = new Usuario($con);

    $stmt = $usuario->listarUsuariosActivos();
    $stmt->store_result();

    $count = $stmt->num_rows();

    if ($count > 0) {
        $usuarios_array = array();

        $stmt->bind_result($id, $nome, $latitude, $longitude, $minutos);

        while ($row = $stmt->fetch()) {

            $u = array(
                "id" => $id,
                "nome" => $nome,
                "latitude" => $latitude,
                "longitude" => $longitude,
                "minutos" => $minutos
            );

            array_push($usuarios_array, $u);
        }

        //Send sucess status code
        http_response_code(201);

        //Make it json format
        print_r(utf8_encode(json_encode($usuarios_array)));

    } else
        http_response_code(503);

    $con->close();
}