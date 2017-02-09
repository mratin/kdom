export interface Player {
  name: string
}

export interface Position {
  row: number
  col: number
}

export interface Tile {
  terrain: string
  crowns: number
}

export interface PlacedTile {
  position: Position
  tile: Tile
}

export interface Score {
  total: number
  areaScores: number[]
  centerBonus: number
  completeBonus: number
}

export interface Kingdom {
  player: Player
  placedTiles: PlacedTile[]
  score: Score
}

export interface Domino {
  number: number
  tile1: Tile
  tile2: Tile
}

export interface DraftDomino {
  player: Player
  domino: Domino
}

export interface Draft {
  dominoes: DraftDomino[]
}

export interface Game {
  uuid: string
  created: string
  updated: string
  kingdoms: Kingdom[]
  currentDraft: Draft
  previousDraft: Draft
  gameOver: boolean
  turn: number
  round: number
  players: Player[]
  playerOnTurn: Player
}

export interface NewGame {
  uuid: string
  created: string
  updated: string
  numberOfPlayers: number
  joinedPlayers: Player[]
}
