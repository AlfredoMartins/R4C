<?php

class Solicitacao
{
    //Conexão do banco de dados e nome da tabela
    private $connection;
    private $table_name = "tb_solicitacoes";

    //Propriedades do objecto
    private $id;
    private $id_motorista;
    private $nome_motorista;
    private $id_passageiro;
    private $nome_passageiro;
    private $minutos;
    private $id_local_origem;
    private $id_local_destino;

    //Construtor que recebe como parâmetro a conexão
    function __construct($connection)
    {
        $this->connection = $connection;
    }

    /*Permite inserir nova solicitação*/
    function inserir_solicitacao()
    {
        //Insert query
        $query = "INSERT INTO " . $this->table_name . " (id_motorista, id_passageiro, id_local_origem, id_local_destino, tempo_solicitacao)
        values ( ?, ?, ?, ?, current_time)";


        //Prepare query
        if ($stmt = $this->connection->prepare($query)) {

            //Sanitize
            $this->id_passageiro = htmlspecialchars(strip_tags($this->id_passageiro));
            $this->id_motorista = htmlspecialchars(strip_tags($this->id_motorista));
            $this->id_local_origem = htmlspecialchars(strip_tags($this->id_local_origem));
            $this->id_local_destino = htmlspecialchars(strip_tags($this->id_local_destino));

            //Bind parameters
            $stmt->bind_param("iiii", $this->id_motorista, $this->id_passageiro, $this->id_local_origem, $this->id_local_destino);

            //Execute query
            if ($stmt->execute())
                return true;
        }

        return false;
    }


    //Permite listar todos os usuarios activos
    function verificarSolicitacao()
    {
        //Query to list record
        $query = " select id,
                          id_motorista, (select nome from tb_usuarios where id = tb_solicitacoes.id_motorista) as motorista,
                          id_passageiro, (select nome from tb_usuarios where id=tb_solicitacoes.id_passageiro) as passageiro,
                          minute(CURRENT_TIME ())-minute(tempo_solicitacao) as minutos, lat_origem, lon_origem, lat_destino, lon_destino,
                          estado_solicitacao, descricao
                          from " . $this->table_name . " where id_motorista=?

                          ";

        //Prepare query
        $stmt = $this->connection->prepare($query);

        //Sanitize
        $this->id_motorista = htmlspecialchars(strip_tags($this->id_motorista));

        $stmt->bind_param("i", $this->id_motorista);

        //Execute query
        $stmt->execute();

        return $stmt;
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
    public function getMinutos()
    {
        return $this->minutos;
    }

    /**
     * @param mixed $minutos
     */
    public function setMinutos($minutos)
    {
        $this->minutos = $minutos;
    }

    /**
     * @return mixed
     */
    public function getNomeMotorista()
    {
        return $this->nome_motorista;
    }

    /**
     * @param mixed $nome_motorista
     */
    public function setNomeMotorista($nome_motorista)
    {
        $this->nome_motorista = $nome_motorista;
    }

    /**
     * @return mixed
     */
    public function getNomePassageiro()
    {
        return $this->nome_passageiro;
    }

    /**
     * @param mixed $nome_passageiro
     */
    public function setNomePassageiro($nome_passageiro)
    {
        $this->nome_passageiro = $nome_passageiro;
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