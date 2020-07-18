package com.kaelkirk.machines.duels;

public enum DuelState {
  IDLE,     // Nothing is happening
  WAITING,  // Waiting for participants to enter the duelRegion
  PREGAME,  // Participants are waiting, countdown initiated
  INGAME,   // The players are fighting
  POSTGAME, // Winner is declared and cooldown initiated
}