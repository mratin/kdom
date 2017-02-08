import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule }   from '@angular/router';

import { AppComponent } from './app.component';
import {GamesComponent} from "./games/games.component";
import { GameComponent } from './game/game.component';


@NgModule({
  declarations: [
    AppComponent,
    GamesComponent,
    GameComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,

    RouterModule.forRoot([
      {
        path: 'games/:uuid',
        component: GameComponent
      },
      {
        path: '',
        component: GamesComponent
      }

    ])
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
