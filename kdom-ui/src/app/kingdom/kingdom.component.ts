import {Component, OnInit, Input} from "@angular/core";
import {Kingdom, Tile} from "../api/model";

@Component({
  selector: 'app-kingdom',
  templateUrl: './kingdom.component.html',
  styleUrls: ['./kingdom.component.css']
})
export class KingdomComponent implements OnInit {
  @Input() kingdom: Kingdom

  rows: number[] = [-4,-3,-2,-1,0,1,2,3,4]
  cols: number[] = this.rows

  constructor() { }

  ngOnInit() {
  }

  getTile(row: number, col: number): Tile {
     for (let placedTile of this.kingdom.placedTiles) {
        if (placedTile.position.row == row && placedTile.position.col == col) {
           return placedTile.tile
        }
     }
  }
}
