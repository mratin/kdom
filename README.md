# kdom

## Creating a Game
         curl -X POST http://kdom.mratin.se/new-games/[?playerCount=<n>]
         
         EXAMPLE: curl -X POST http://kdom.mratin.se/new-games/?playerCount=2
         { "uuid":"e1c84105-b3e7-4717-b808-64be88938e7e", ... }

```uuid``` is the unique identifier to the new game that has been created. This id is used in the future to refer to this game.
If playerCount isn't specified, the number of players will default to 4.

## Joining a Game
         curl -X POST http://kdom.mratin.se/new-games/<game-id>/join/<player-name>
         
         EXAMPLE: curl -X POST http://kdom.mratin.se/new-games/e1c84105-b3e7-4717-b808-64be88938e7e/join/StigHelmer?callbackUrl=http://my.ai.listener
         {  "name":"StigHelmer",  "uuid":"12ea309a-8e3b-4b75-87d0-15d1670baaab", ... }

```uuid``` is the unique identifier of the player. This id is secret to the player and is used in the future when executing moves for this player.

## Getting Game State
Once the game has enough players joined (normally 4), it is no longer considered a "new-game". Instead it is found under /games/

         curl http://kdom.mratin.se/games/<game-id>
         
         EXAMPLE: curl http://kdom.mratin.se/games/e1c84105-b3e7-4717-b808-64be88938e7e
         { "uuid":"e1c84105-b3e7-4717-b808-64be88938e7e", ... "currentPlayer":{ "name":"StigHelmer" }}

This response contains everything there is to know about the state of the game, including who's turn it is to make a move and the current score.

## Getting Available Moves

In order to make a move, we ask the API to list all available moves:
         
         curl http://kdom.mratin.se/games/<game-id>/available-moves
         
         EXAMPLE: curl http://kdom.mratin.se/games/e1c84105-b3e7-4717-b808-64be88938e7e/available-moves
         { "moves": [{ "number":0, ... }]}

This response lists all available moves for the current player. Each move has a number which can be used in a future call to refer to the move to be executed.

## Making a Move

         curl -X POST http://kdom.mratin.se/games/<game-id>/players/<player-id>/moves/<move-number>
         
         EXAMPLE: curl -X POST http://kdom.mratin.se/games/e1c84105-b3e7-4717-b808-64be88938e7e/players/12ea309a-8e3b-4b75-87d0-15d1670baaab/moves/2
         { <updated game-state> }


# kdom Java Client

Here is an implementation for a client in Java:
https://github.com/tdebroc/kingdomino-ia-client
You can see one example of Game here:
https://github.com/tdebroc/kingdomino-ia-client/blob/master/src/test/java/Main.java


