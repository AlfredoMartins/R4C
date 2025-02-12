<?php

class Locais
{
    //Conexão do banco de dados e nome da tabela
    private $connection;
    private $table_name = "tb_locais";

    //Propriedades do objecto
    private $id;
    private $nome;
    private $latitude;
    private $longitude;

    //Construtor que recebe como parâmetro a conexão
    function __construct($connection)
    {
        $this->connection = $connection;
    }

    /*Permite inserir novo local*/
    function inserir_local()
    {
        //Insert query
        $query = "INSERT INTO " . $this->table_name . " (nome, latitude, longitude)
        values ( ?, ?, ?)";

        //Prepare query
        if ($stmt = $this->connection->prepare($query)) {

            //Sanitize
            $this->nome = htmlspecialchars(strip_tags($this->nome));
            $this->latitude = htmlspecialchars(strip_tags($this->latitude));
            $this->longitude = htmlspecialchars(strip_tags($this->longitude));

            //Bind parameters
            $stmt->bind_param("sdd", $this->nome, $this->latitude, $this->longitude);

            //Execute query
            if ($stmt->execute())
                return true;
        }

        return false;
    }


    //Permite listar todos os locais
    function listarLocais()
    {
        //Query to list record
        $query = "select id, nome, latitude, longitude from ".$this->table_name;

        //Prepare query
        $stmt = $this->connection->prepare($query);

        //Execute query
        $stmt->execute();

        return $stmt;
    }

    /**
     * @return mixed
     */
    public function getConnection()
    {
        return $this->connection;
    }

    /**
     * @param mixed $connection
     */
    public function setConnection($connection)
    {
        $this->connection = $connection;
    }

    /**
     * @return mixed
     */
    public function getId()
    {
        return $this->id;
    }

    /**
     * @param mixed $id
     */
    public function setId($id)
    {
        $this->id = $id;
    }

    /**
     * @return mixed
     */
    public function getLatitude()
    {
        return $this->latitude;
    }

    /**
     * @param mixed $latitude
     */
    public function setLatitude($latitude)
    {
        $this->latitude = $latitude;
    }

    /**
     * @return mixed
     */
    public function getLongitude()
    {
        return $this->longitude;
    }

    /**
     * @param mixed $longitude
     */
    public function setLongitude($longitude)
    {
        $this->longitude = $longitude;
    }

    /**
     * @return mixed
     */
    public function getNome()
    {
        return $this->nome;
    }

    /**
     * @param mixed $nome
     */
    public function setNome($nome)
    {
        $this->nome = $nome;
    }

    /**
     * @return string
     */
    public function getTableName()
    {
        return $this->table_name;
    }

    /**
     * @param string $table_name
     */
    public function setTableName($table_name)
    {
        $this->table_name = $table_name;
    }




}