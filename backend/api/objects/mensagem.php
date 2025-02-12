<?php

class Mensagem
{
    //Conexão do banco de dados e nome da tabela
    private $connection;
    private $table_name = "tb_mensagem";

    //Propriedades do objecto
    private $id;
    private $id_emissor;
    private $id_receptor;
    private $texto;
    private $visualizado;
    private $data_mensagem;

    //Construtor que recebe como parâmetro a conexão
    function __construct($connection)
    {
        $this->connection = $connection;
    }

    /*Permite inserir nova solicitação*/
    function inserir_mensagem()
    {
        //Insert query
        $query = "INSERT INTO " . $this->table_name . " (id_emissor, id_receptor, texto, data_mensagem)
        values ( ?, ?, ?, current_time)";


        //Prepare query
        if ($stmt = $this->connection->prepare($query)) {

            //Sanitize
            $this->id_emissor = htmlspecialchars(strip_tags($this->id_emissor));
            $this->id_receptor = htmlspecialchars(strip_tags($this->id_receptor));
            $this->texto = htmlspecialchars(strip_tags($this->texto));

            //Bind parameters
            $stmt->bind_param("iis", $this->id_emissor, $this->id_receptor, $this->texto);

            //Execute query
            if ($stmt->execute())
                return true;
        }

        return false;
    }


    //Permite listar todos os usuarios activos
    function listarMensagens()
    {
        //Query to list record
        $query = "select distinct m.id_emissor, u.nome, u.telefone, u.foto_url from " . $this->table_name . " m inner join tb_usuarios u on m.id_emissor=u.id where (m.id_emissor=? or m.id_receptor=?) and m.id_emissor!=?
                    union
                select distinct m.id_receptor, u.nome, u.telefone, u.foto_url from tb_mensagem m inner join tb_usuarios u on m.id_receptor=u.id where (m.id_emissor=? or m.id_receptor=?) and m.id_receptor!=?";



        //Prepare query
        $stmt = $this->connection->prepare($query);

        //Sanitize
        $this->id_emissor = htmlspecialchars(strip_tags($this->id_emissor));

        $stmt->bind_param("iiiiii", $this->id_emissor, $this->id_emissor, $this->id_emissor, $this->id_emissor, $this->id_emissor, $this->id_emissor);

        //Execute query
        $stmt->execute();

        return $stmt;
    }

    
    //Permite listar todos os usuarios activos
    function ultimaMensagem($id_emissor, $id_receptor)
    {
        //Query to list record
        $query ="select texto, m.data_mensagem from tb_mensagem m where (m.id_emissor = ? and m.id_receptor=?) or (m.id_receptor = ? and m.id_emissor=?) order by id desc limit 1";

        //Prepare query
        $stmt = $this->connection->prepare($query);

        //Sanitize
        $this->id_emissor = htmlspecialchars(strip_tags($this->id_emissor));

        $stmt->bind_param("iiii", $id_emissor, $id_receptor, $id_emissor, $id_receptor);

        //Execute query
        $stmt->execute();

        return $stmt;
    }

    //Permite listar todos os usuarios activos
    function verificaMensagem()
    {
        //Query to list record
        $query = " select * from " . $this->table_name . " where id_emissor=? and id_receptor=? or id_emissor=? and id_receptor=?";

        //Prepare query
        $stmt = $this->connection->prepare($query);

        //Sanitize
        $this->id_emissor = htmlspecialchars(strip_tags($this->id_emissor));
        $this->id_receptor = htmlspecialchars(strip_tags($this->id_receptor));

        $stmt->bind_param("iiii", $this->id_emissor, $this->id_receptor, $this->id_receptor, $this->id_emissor);

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
    public function getDataMensagem()
    {
        return $this->data_mensagem;
    }

    /**
     * @param mixed $data_mensagem
     */
    public function setDataMensagem($data_mensagem)
    {
        $this->data_mensagem = $data_mensagem;
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
    public function getIdEmissor()
    {
        return $this->id_emissor;
    }

    /**
     * @param mixed $id_emissor
     */
    public function setIdEmissor($id_emissor)
    {
        $this->id_emissor = $id_emissor;
    }

    /**
     * @return mixed
     */
    public function getIdReceptor()
    {
        return $this->id_receptor;
    }

    /**
     * @param mixed $id_receptor
     */
    public function setIdReceptor($id_receptor)
    {
        $this->id_receptor = $id_receptor;
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

    /**
     * @return mixed
     */
    public function getTexto()
    {
        return $this->texto;
    }

    /**
     * @param mixed $texto
     */
    public function setTexto($texto)
    {
        $this->texto = $texto;
    }

    /**
     * @return mixed
     */
    public function getVisualizado()
    {
        return $this->visualizado;
    }

    /**
     * @param mixed $visualizado
     */
    public function setVisualizado($visualizado)
    {
        $this->visualizado = $visualizado;
    }



    

}