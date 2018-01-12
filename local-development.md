# Local development tips

## Running the server using Docker

Simply run the command
```bash
docker-compose up
```
and the server will start on localhost on port `80`.


## Running the server from IntelliJ

Set up a run-configuration Application with `se.apogo.kdom.api.Main` as the main class. Adding an argument 
such as `-Djetty.port=4711` as VM argument sets the port to `4711` instead of the default `8080`. 

The server needs to have the UI available for serving in a directory `public/ui/` in the 
working directory to be able to serve the UI part. 

### Building the UI

The first step is to ensure that you have `npm` installed (`brew install npm` on a Mac), as well as 
the `ng` tool (`npm install -g  angular-cli`).

Go to the `kdom-ui` directory and run `npm install`. After all the pre-requisites are installed, 
run `ng build --prod --base-href /ui/` to build the actual files used for the UI. Copy the files produced to the 
static files directory (`cp -Rf dist/* ../public/ui/`).  


## Starting a game and running moves

When running a local server for development, it can be useful to be able to start a new game and execute some moves. 
Instead of starting full clients, it can easily be done using the following bash commands
```bash
SERVER=localhost:8000
GAME=$(curl -s -X POST http://$SERVER/new-games/?playerCount=2 | grep uuid | cut -d':' -f 2 | tr -d ' ",')
PLAYER1=$(curl -s -X POST http://$SERVER/new-games/$GAME/join/player1 | grep uuid | cut -d':' -f 2 | tr -d ' "')
PLAYER2=$(curl -s -X POST http://$SERVER/new-games/$GAME/join/player2 | grep uuid | cut -d':' -f 2 | tr -d ' "')
echo Game id $GAME 
echo Player1 id $PLAYER1 
echo Player2 id $PLAYER2
for i in `seq 1 10`
do 
    for id in $PLAYER1 $PLAYER2 
    do 
        curl -s -X POST http://$SERVER/games/$GAME/players/$id/moves/0 > /dev/null
    done
done
curl http://$SERVER/games/$GAME/available-moves
```
