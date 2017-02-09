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
  lastTurn?: number

  game: Game

  constructor(private kdomService: KdomService, private route: ActivatedRoute) { }

  hasPreviousTurn(): boolean {
    return this.turn > 0
  }

  hasNextTurn(): boolean {
    return this.lastTurn != null && this.turn < this.lastTurn
  }

  goToPreviousTurn() {
    if (this.hasPreviousTurn()) {
      this.turn = this.turn - 1
      this.updateGame()
    }
  }

  goToNextTurn() {
    if (this.lastTurn != null && !this.game.gameOver) {
      this.turn = Math.min(this.turn + 1, this.lastTurn)
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
      if (this.lastTurn == null) this.lastTurn = this.game.turn
      this.turn = this.game.turn
    })
  }
}
