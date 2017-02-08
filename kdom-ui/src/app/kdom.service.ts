import { Injectable } from '@angular/core';
import {Game} from "./api/game";
import {Http} from "@angular/http";
import 'rxjs/add/operator/toPromise';
import {environment} from "../environments/environment";

@Injectable()
export class KdomService {
  private apiBaseUrl = environment.apiBaseUrl

  constructor(private http: Http) { }

  getGame(uuid: string): Promise<Game> {
    return this.http.get(this.apiBaseUrl + "games/" + uuid)
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

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error)
    return Promise.reject(error.message || error)
  }
}
