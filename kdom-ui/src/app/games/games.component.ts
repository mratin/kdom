import { Component, OnInit } from '@angular/core';
import {KdomService} from "../kdom.service";
import {Game} from "../api/model";

@Component({
  selector: 'app-games',
  templateUrl: './games.component.html',
  styleUrls: ['./games.component.css'],
  providers: [KdomService]
})
export class GamesComponent implements OnInit {

  constructor(private kdomService: KdomService) { }

  games: Game[]

  ngOnInit() {
    this.kdomService.getGames().then(games => this.games = games)
  }

  formattedPlayers(game: Game) {
    return game.players.map(p => p.name).join(', ');
  }

  status(game: Game) {
    if (game.playerOnTurn) {
      return game.playerOnTurn.name + "'s turn"
    } else if (game.gameOver) {
      return "Game Over"
    }
  }
}
