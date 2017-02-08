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

  game: Game

  constructor(private kdomService: KdomService, private route: ActivatedRoute) { }

  ngOnInit() {
    this.uuid = this.route.snapshot.params['uuid']

    this.kdomService.getGame(this.uuid).then(game => this.game = game)
  }

}
