<?php

class Usuario
{

    //Conexão do banco de dados e nome da tabela
    private $connection;
    private $table_name = "tb_usuarios";

    //Propriedades do objecto

    private $id;
    private $nome;
    private $sobrenome;
    private $email;
    private $senha;
    private $id_categoria;
    private $id_provincia;
    private $data_cadastro;
    private $telefone;
    private $latitude;
    private $longitude;
    private $tempo_ultimo_acesso;
    private $estado;
    private $foto_url;

    //Construtor que recebe como parâmetro a conexão
    function __construct($connection)
    {
        $this->connection = $connection;
    }

    //Cadastra usuário
    function signup()
    {
        if ($this->isAlreadyExist())
            return false;

        //Insert query
        $query = "INSERT INTO " . $this->table_name . " (nome, sobrenome, email, senha, telefone, id_categoria)
        VALUES (? , ?, ?, ?, ?, ?)";

        //Prepare query
        if ($stmt = $this->connection->prepare($query)) {

            //Sanitize
            $this->nome = htmlspecialchars(strip_tags($this->nome));
            $this->sobrenome = htmlspecialchars(strip_tags($this->sobrenome));
            $this->email = htmlspecialchars(strip_tags($this->email));
            $this->senha = htmlspecialchars(strip_tags($this->senha));
            $this->telefone = htmlspecialchars(strip_tags($this->telefone));
            $this->id_categoria = htmlspecialchars(strip_tags($this->id_categoria));

            //Bind parameters
            $stmt->bind_param("sssssi", $this->nome, $this->sobrenome, $this->email, $this->senha, $this->telefone, $this->id_categoria);

            //Execute query
            if ($stmt->execute())
                return true;
        }

        return false;
    }

    //Loga usuário
    function login()
    {
        //Query to insert record
        $query = "SELECT id, nome, sobrenome, email, senha, telefone, id_categoria, id_provincia FROM " . $this->table_name . " WHERE  email=? AND senha=?";

        //Prepare query
        if ($stmt = $this->connection->prepare($query)) {

            //Sanitize
            $this->email = htmlspecialchars(strip_tags($this->email));
            $this->senha = htmlspecialchars(strip_tags($this->senha));

            //Bind parameters
            $stmt->bind_param("ss", $this->email, $this->senha);

            //Execute query
            $stmt->execute();

            $stmt->store_result();

            if ($stmt->affected_rows > 0) {

                $stmt->bind_result($this->id, $this->nome, $this->sobrenome, $this->email, $this->senha, $this->telefone, $this->id_categoria, $this->id_provincia);
                $stmt->fetch();

                return true;
            }
        }

        return false;
    }

    //Verifica se o usuário já se encontra cadastrado
    function isAlreadyExist()
    {
        $query = "SELECT * FROM " . $this->table_name . " WHERE email='" . $this->email . "'";

        //Prepare query
        if ($stmt = $this->connection->prepare($query)) {

            //Execute query
            $stmt->execute();
            $stmt->store_result();

            if ($stmt->affected_rows > 0)
                return true;
        }

        return false;
    }


    //Cadastra usuário
    function active_acount()
    {
        //Query to insert record
        $query = "SELECT id FROM " . $this->table_name . " WHERE telefone=? OR email=?";

        //Prepare query
        if ($stmt = $this->connection->prepare($query)) {

            //Sanitize
            $this->telefone = htmlspecialchars(strip_tags($this->telefone));
            $this->email = htmlspecialchars(strip_tags($this->email));

            //Bind parameters
            $stmt->bind_param("ss", $this->telefone, $this->email);

            //Execute query
            $stmt->execute();

            $stmt->store_result();

            if ($stmt->affected_rows > 0) {

                $stmt->bind_result($this->id);
                $stmt->fetch();

                $query = "UPDATE " . $this->table_name . " SET estado=0 WHERE id=?";

                //Prepare query
                if ($stmt = $this->connection->prepare($query)) {

                    //Bind parameters
                    $stmt->bind_param("i", $this->id);

                    //Execute query
                    if ($stmt->execute())
                        return true;
                }
            }
        }

        return false;
    }

    function atualizarLocalizacao()
    {
        $query = "UPDATE " . $this->table_name . " SET latitude=?, longitude=?, tempo_ultimo_acesso=current_time WHERE id=?";

        //Prepare query
        if ($stmt = $this->connection->prepare($query)) {

            //Sanitize
            $this->id = htmlspecialchars(strip_tags($this->id));
            $this->latitude = htmlspecialchars(strip_tags($this->latitude));
            $this->longitude = htmlspecialchars(strip_tags($this->longitude));

            //Bind parameters
            $stmt->bind_param("ssi", $this->latitude, $this->longitude, $this->id);

            //Execute query
            if ($stmt->execute())
                return true;
        }

        return false;
    }


    //Permite listar todos os usuarios activos
    function listarUsuariosActivos()
    {
        //Query to list record
        //$query = "select id, nome, latitude, longitude, minute(current_timestamp())-minute(tempo_ultimo_acesso) as minutos from tb_usuarios having minutos<3";
        //$query = "select id, nome, latitude, longitude, minute(CURRENT_TIME ())-minute(tempo_ultimo_acesso) as minutos from tb_usuarios having minutos>=0 and minutos<=10 ";

        $query = "select id, nome, latitude, longitude, minute(CURRENT_TIME ())-minute(tempo_ultimo_acesso) as minutos from tb_usuarios ";

        //Prepare query
        $stmt = $this->connection->prepare($query);

        //Execute query
        $stmt->execute();

        return $stmt;
    }


    //Permite pegar os dados do passageiro
    function carregarDadosPassageiro()
    {
        $query = "select id, telefone from tb_usuarios where id=?";

        //Prepare query
        $stmt = $this->connection->prepare($query);


        //Sanitize
        $this->id = htmlspecialchars(strip_tags($this->id));

        //Bind parameters
        $stmt->bind_param("i", $this->id);

        //Execute query
        $stmt->execute();

        return $stmt;
    }



    //Permite alterar a senha
    function alterPassoword()
    {
        //Query to insert record
        $query = "SELECT id FROM " . $this->table_name . " WHERE email=? OR telefone=?";

        //Prepare query
        if ($stmt = $this->connection->prepare($query)) {

            //Sanitize
            $this->email = htmlspecialchars(strip_tags($this->email));

            //Bind parameters
            $stmt->bind_param("ss", $this->email, $this->telefone);

            //Execute query
            $stmt->execute();

            $stmt->store_result();

            if ($stmt->affected_rows > 0) {

                $stmt->bind_result($this->id);
                $stmt->fetch();

                $query = "UPDATE " . $this->table_name . " SET senha=? WHERE id=?";

                //Prepare query
                if ($stmt = $this->connection->prepare($query)) {

                    //Sanitize
                    $this->senha = htmlspecialchars(strip_tags($this->senha));

                    //Bind parameters
                    $stmt->bind_param("si", $this->senha, $this->id);

                    //Execute query
                    if ($stmt->execute())
                        return true;
                }
            }
        }

        return false;
    }


    /*Permite inserir nova solicitação*/
    function upload_image()
    {
        //Insert query
        $query = "UPDATE " . $this->table_name . " SET foto_url=? WHERE id=?";

        //Prepare query
        if ($stmt = $this->connection->prepare($query)) {

            //Sanitize
            $this->id = htmlspecialchars(strip_tags($this->id));
            $this->foto_url = htmlspecialchars(strip_tags($this->foto_url));

            //Bind parameters
            $stmt->bind_param("si", $this->foto_url, $this->id);

            //Execute query
            if ($stmt->execute())
                return true;
        }

        return false;
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
    public function getDataCadastro()
    {
        return $this->data_cadastro;
    }

    /**
     * @param mixed $data_cadastro
     */
    public function setDataCadastro($data_cadastro)
    {
        $this->data_cadastro = $data_cadastro;
    }

    /**
     * @return mixed
     */
    public function getEmail()
    {
        return $this->email;
    }

    /**
     * @param mixed $email
     */
    public function setEmail($email)
    {
        $this->email = $email;
    }

    /**
     * @return mixed
     */
    public function getEstado()
    {
        return $this->estado;
    }

    /**
     * @param mixed $estado
     */
    public function setEstado($estado)
    {
        $this->estado = $estado;
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
    public function getIdCategoria()
    {
        return $this->id_categoria;
    }

    /**
     * @param mixed $id_categoria
     */
    public function setIdCategoria($id_categoria)
    {
        $this->id_categoria = $id_categoria;
    }

    /**
     * @return mixed
     */
    public function getIdProvincia()
    {
        return $this->id_provincia;
    }

    /**
     * @param mixed $id_provincia
     */
    public function setIdProvincia($id_provincia)
    {
        $this->id_provincia = $id_provincia;
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
     * @return mixed
     */
    public function getSenha()
    {
        return $this->senha;
    }

    /**
     * @param mixed $senha
     */
    public function setSenha($senha)
    {
        $this->senha = $senha;
    }

    /**
     * @return mixed
     */
    public function getSobrenome()
    {
        return $this->sobrenome;
    }

    /**
     * @param mixed $sobrenome
     */
    public function setSobrenome($sobrenome)
    {
        $this->sobrenome = $sobrenome;
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
    public function getTelefone()
    {
        return $this->telefone;
    }

    /**
     * @param mixed $telefone
     */
    public function setTelefone($telefone)
    {
        $this->telefone = $telefone;
    }

    /**
     * @return mixed
     */
    public function getTempoUltimoAcesso()
    {
        return $this->tempo_ultimo_acesso;
    }

    /**
     * @param mixed $tempo_ultimo_acesso
     */
    public function setTempoUltimoAcesso($tempo_ultimo_acesso)
    {
        $this->tempo_ultimo_acesso = $tempo_ultimo_acesso;
    }

    /**
     * @return mixed
     */
    public function getFotoUrl()
    {
        return $this->foto_url;
    }

    /**
     * @param mixed $foto_url
     */
    public function setFotoUrl($foto_url)
    {
        $this->foto_url = $foto_url;
    }





}