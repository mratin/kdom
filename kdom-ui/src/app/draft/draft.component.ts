import {Component, OnInit, Input} from '@angular/core';
import {Draft} from "../api/model";

@Component({
  selector: 'app-draft',
  templateUrl: './draft.component.html',
  styleUrls: ['./draft.component.css']
})
export class DraftComponent implements OnInit {
  @Input() draft: Draft

  constructor() { }

  ngOnInit() {
  }

}
