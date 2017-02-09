import { Component, OnInit } from '@angular/core';
import {KdomService} from "../kdom.service";
import {NewGame} from "../api/model";

@Component({
  selector: 'app-new-games',
  templateUrl: './new-games.component.html',
  styleUrls: ['./new-games.component.css'],
  providers: [KdomService]
})
export class NewGamesComponent implements OnInit {

  constructor(private kdomService: KdomService) { }

  newGames: NewGame[]

  ngOnInit() {
    this.kdomService.getNewGames().then(newGames => this.newGames = newGames)
  }

  formattedPlayers(newGame: NewGame) {
    return newGame.joinedPlayers.map(p => p.name).join(', ');
  }
}
