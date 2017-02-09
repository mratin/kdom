import {Component, OnInit, Input} from '@angular/core';
import {KdomService} from "../kdom.service";
import {Game} from "../api/model";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.css'],
  providers: [KdomService]
})
export class GameComponent implements OnInit {
  @Input() uuid: string
  turn?: number

  game: Game

  constructor(private kdomService: KdomService, private route: ActivatedRoute) { }

  goToPreviousTurn() {
    if (this.turn > 0) {
      this.turn = this.turn - 1
      this.updateGame()
    }
  }

  goToNextTurn() {
    if (!this.game.gameOver) {
      this.turn = this.turn + 1
      this.updateGame()
    }
  }

  ngOnInit() {
    this.uuid = this.route.snapshot.params['uuid']

    this.updateGame()
  }

  updateGame() {
    this.kdomService.getGame(this.uuid, this.turn).then(game => {
      this.game = game
      this.turn = game.turn
    })
  }
}
