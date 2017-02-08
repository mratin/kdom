import { Component, OnInit, Input } from '@angular/core';
import {Tile} from "../api/model";

@Component({
  selector: 'app-tile',
  templateUrl: './tile.component.html',
  styleUrls: ['./tile.component.css']
})
export class TileComponent implements OnInit {
  @Input() tile: Tile

  constructor() { }

  ngOnInit() {
  }

  tileColor(): string {
    if (!this.tile) return "#eee"

    switch(this.tile.terrain) {
       case "castle": return "#333"
       case "field": return "#F7DC6F"
       case "pasture": return "#A9DFBF"
       case "water": return "#5499C7"
       case "forest": return "#27AE60"
       case "mine": return "#515A5A"
       case "clay": return "#F0B27A"
    }
  }
}
