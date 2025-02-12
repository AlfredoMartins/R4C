var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var port = 3000;


app.get('/', function(req, res){
    res.sendFile(__dirname + '/index.html');
});


//AllUserDrivers
var online_drivers = [];
var online_customers = [];

var RIDER_COMPLETED = 'Viagem terminada.';
var VIAGEM_CANCELADA = 'Viagem cancelada.';

io.on('connection', function(socket){

    socket.on('user_connect', function(data, categoria){
		if(categoria==1)
        {
            console.log('User Id (Driver): ' + data + " is connected.\n");
            socket.join(data);
        }
		else {
            console.log('User Id (Customer): ' + data + " is connected.\n");
            socket.join(data);
        }

    });


    socket.on('update_driver_data', function(id, nome, latitude, longitude){

        if(typeof online_drivers[id]==='undefined'){

            var driver_item = {
                nome: nome,
                latitude: latitude,
                longitude: longitude
            };

            socket.join(id);

            online_drivers[id] = driver_item;

        } else {
            online_drivers[id].latitude = latitude;
            online_drivers[id].longitude = longitude;
        }

        console.log('Driver Id = ' + id);
        console.log('\nName = ' + online_drivers[id].nome);
        console.log('\nLatitude = ' + online_drivers[id].latitude);
        console.log('\nLongitude = ' + online_drivers[id].longitude+'\n');

    });

    socket.on('drivers_avalibles', function(){

        socket.emit('drivers_avalibles', online_drivers);

    });


    socket.on('call_driver', function(driver_id, id_passageiro,
                                      nome, telefone, id_origem, id_destino,
                                      origem, destino,
                                      distancia) {

        console.log('Um passageiro chamando.\n');

        socket.to(driver_id).emit('call_driver',{
				id_passageiro,
                nome,
                telefone,
                id_origem,
                id_destino,
                origem,
                destino,
                distancia
            });

        });


    socket.on('driver_answer', function(customer_id, answer, nome, telefone){

        console.log('Motorista respondeu chamada.\n');

        socket.to(customer_id).
				emit('driver_answer',{answer, nome, telefone});

    });


    socket.on('trip_created', function(id_motorista, id_passageiro, id_viagem){

        console.log('Passageiro criou viagem.\n');

        socket.to(id_motorista).
            emit('trip_created',{id_passageiro, id_viagem});

    });


    socket.on('rider_completed', function(id_passageiro){

        console.log('Viagem terminada.\n');

        socket.to(id_passageiro).emit('rider_completed',  {RIDER_COMPLETED});

    });

    socket.on('cancelar_viagem', function(id_motorista){

        console.log('Cancelar viagem.\n');

        socket.to(id_motorista).emit('cancelar_viagem',  {VIAGEM_CANCELADA});

    });



    socket.on('active_drivers', function(){

        var sockets = io.sockets.sockets;

        sockets.forEach(function(sock){
            if(sock.id!=socket.id){
                socket.emit('active_drivers', drivers_array);
            }
        });

    });


    socket.on('user_disconnected', function(data, categoria){
		if(categoria==1)
			console.log('User Id (Driver): ' + data + " has disconnected.\n");
		else
			console.log('User Id (Customer): ' + data + " has disconnected.\n");

    });


    socket.on('disconnect', function(){
		//console.log('User disconnected: ' + socket.id+".");
    });

});

http.listen(port, function(){
    console.log('Listening on *: ' + port);
});