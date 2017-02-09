import {Component, OnInit, Input} from '@angular/core';
import {DraftDomino} from "../api/model";

@Component({
  selector: 'app-draft-domino',
  templateUrl: './draft-domino.component.html',
  styleUrls: ['./draft-domino.component.css']
})
export class DraftDominoComponent implements OnInit {
  @Input() draftDomino: DraftDomino
  constructor() { }

  ngOnInit() {
  }

}
