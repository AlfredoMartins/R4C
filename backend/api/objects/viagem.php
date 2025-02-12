<?php

class Viagem
{
    //Conexão do banco de dados e nome da tabela
    private $connection;
    private $table_name = "tb_viagem";

    //Propriedades do objecto
    private $id;
    private $id_motorista;
    private $id_passageiro;
    private $id_local_origem;
    private $id_local_destino;
    private $descricao;
    private $preco;
    private $id_avaliacao;
    private $id_estado;
    private $tempo_inicio;
    private $tempo_termino;

    //Construtor que recebe como parâmetro a conexão
    function __construct($connection)
    {
        $this->connection = $connection;
    }

    /*Permite inserir novo local*/
    function inserir_viagem()
    {
        //Insert query
        $query = "INSERT INTO " . $this->table_name . " (id_motorista, id_passageiro, id_local_origem, id_local_destino, descricao, id_estado, tempo_inicio)
        values (?, ?, ?, ?, ?, 2, current_time())";

        //Prepare query
        if ($stmt = $this->connection->prepare($query)) {

            //Sanitize
            $this->id_motorista = htmlspecialchars(strip_tags($this->id_motorista));
            $this->id_passageiro = htmlspecialchars(strip_tags($this->id_passageiro));
            $this->id_local_origem = htmlspecialchars(strip_tags($this->id_local_origem));
            $this->id_local_destino = htmlspecialchars(strip_tags($this->id_local_destino));
            $this->descricao = htmlspecialchars(strip_tags($this->descricao));
            $this->id_avaliacao = htmlspecialchars(strip_tags($this->id_avaliacao));

            //Bind parameters
            $stmt->bind_param("iiiis",
                $this->id_motorista,
                $this->id_passageiro,
                $this->id_local_origem,
                $this->id_local_destino,
                $this->descricao);

            //Execute query
            if ($stmt->execute())
            {
                $this->setId($stmt->insert_id);

                return true;
            }
        }

        return false;
    }

    /*Permite inserir novo local*/
    function terminar_viagem()
    {
        //Insert query
        $query = "UPDATE " . $this->table_name . " SET id_estado = 1, preco = ?, tempo_termino = current_time() where id = ?";

        //Prepare query
        if ($stmt = $this->connection->prepare($query)) {

            //Sanitize
            $this->id_estado = htmlspecialchars(strip_tags($this->id_estado));
            $this->id = htmlspecialchars(strip_tags($this->id));

            //Bind parameters
            $stmt->bind_param("di",
                $this->preco,
                $this->id);

            //Execute query
            if ($stmt->execute())
            {
                return true;
            }
        }

        return false;
    }


    /*Permite inserir novo local*/
    function avaliar_viagem()
    {
        //Insert query
        $query = "UPDATE " . $this->table_name . " SET id_avaliacao = ? where id = ?";

        //Prepare query
        if ($stmt = $this->connection->prepare($query)) {

            //Sanitize
            $this->id_avaliacao = htmlspecialchars(strip_tags($this->id_avaliacao));
            $this->id = htmlspecialchars(strip_tags($this->id));

            //Bind parameters
            $stmt->bind_param("ii",
                $this->preco,
                $this->id);

            //Execute query
            if ($stmt->execute())
            {
                return true;
            }
        }

        return false;
    }



    /*Permite cancelar viagem*/
    function cancelar_viagem()
    {
        //Insert query
        $query = "UPDATE " . $this->table_name . " SET tb_estado_viagem = 2 where id = ?";

        //Prepare query
        if ($stmt = $this->connection->prepare($query)) {

            //Sanitize
            $this->id = htmlspecialchars(strip_tags($this->id));

            //Bind parameters
            $stmt->bind_param("i",
                $this->id);

            //Execute query
            if ($stmt->execute())
            {
                return true;
            }
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
    public function getDescricao()
    {
        return $this->descricao;
    }

    /**
     * @param mixed $descricao
     */
    public function setDescricao($descricao)
    {
        $this->descricao = $descricao;
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
    public function getIdAvaliacao()
    {
        return $this->id_avaliacao;
    }

    /**
     * @param mixed $id_avaliacao
     */
    public function setIdAvaliacao($id_avaliacao)
    {
        $this->id_avaliacao = $id_avaliacao;
    }

    /**
     * @return mixed
     */
    public function getIdEstado()
    {
        return $this->id_estado;
    }

    /**
     * @param mixed $id_estado
     */
    public function setIdEstado($id_estado)
    {
        $this->id_estado = $id_estado;
    }

    /**
     * @return mixed
     */
    public function getIdLocalDestino()
    {
        return $this->id_local_destino;
    }

    /**
     * @param mixed $id_local_destino
     */
    public function setIdLocalDestino($id_local_destino)
    {
        $this->id_local_destino = $id_local_destino;
    }

    /**
     * @return mixed
     */
    public function getIdLocalOrigem()
    {
        return $this->id_local_origem;
    }

    /**
     * @param mixed $id_local_origem
     */
    public function setIdLocalOrigem($id_local_origem)
    {
        $this->id_local_origem = $id_local_origem;
    }

    /**
     * @return mixed
     */
    public function getIdMotorista()
    {
        return $this->id_motorista;
    }

    /**
     * @param mixed $id_motorista
     */
    public function setIdMotorista($id_motorista)
    {
        $this->id_motorista = $id_motorista;
    }

    /**
     * @return mixed
     */
    public function getIdPassageiro()
    {
        return $this->id_passageiro;
    }

    /**
     * @param mixed $id_passageiro
     */
    public function setIdPassageiro($id_passageiro)
    {
        $this->id_passageiro = $id_passageiro;
    }

    /**
     * @return mixed
     */
    public function getPreco()
    {
        return $this->preco;
    }

    /**
     * @param mixed $preco
     */
    public function setPreco($preco)
    {
        $this->preco = $preco;
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
    public function getTempoInicio()
    {
        return $this->tempo_inicio;
    }

    /**
     * @param mixed $tempo_inicio
     */
    public function setTempoInicio($tempo_inicio)
    {
        $this->tempo_inicio = $tempo_inicio;
    }

    /**
     * @return mixed
     */
    public function getTempoTermino()
    {
        return $this->tempo_termino;
    }

    /**
     * @param mixed $tempo_termino
     */
    public function setTempoTermino($tempo_termino)
    {
        $this->tempo_termino = $tempo_termino;
    }


}