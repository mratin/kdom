import { Injectable } from '@angular/core';
import {Game} from "./api/model";
import {NewGame} from "./api/model";
import {Http} from "@angular/http";
import 'rxjs/add/operator/toPromise';
import {environment} from "../environments/environment";

@Injectable()
export class KdomService {
  private apiBaseUrl = environment.apiBaseUrl

  constructor(private http: Http) { }

  getGame(uuid: string, turn?: number): Promise<Game> {
    let turnParam: string = turn == null? "" : "?turn=" + turn
    return this.http.get(this.apiBaseUrl + "games/" + uuid + turnParam)
      .toPromise()
      .then(response => response.json() as Game)
      .catch(this.handleError)
  }

  getGames(): Promise<Game[]> {
    return this.http.get(this.apiBaseUrl + "games/")
      .toPromise()
      .then(response => response.json().games as Game[])
      .catch(this.handleError)
  }

  getNewGames(): Promise<NewGame[]> {
    return this.http.get(this.apiBaseUrl + "new-games/")
      .toPromise()
      .then(response => response.json().newGames as NewGame[])
      .catch(this.handleError)
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error)
    return Promise.reject(error.message || error)
  }
}
