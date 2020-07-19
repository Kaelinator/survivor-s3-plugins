package com.kaelkirk.machines.duels;

public class EloCalculator {
  
  /**
   * Returns an array where 
   * - [0] = the player a's change in rating
   * - [1] = the player b's change in rating
   * 
   * Rating params: 
   * @param R_a - player a's rating
   * @param R_b - player b's rating
   * 
   * Score params: (0 = lost, 0.5 = draw, 1 = won)
   * @param S_a - player a's ending score
   * @param S_b - plbyer b's ending score
   * 
   * Variable params:
   * @param x - divisor of the exponent
   * @param k - maximum rating change
   */
  public static int[] calculateElo(int R_a, int R_b, double S_a, double S_b, double x, double k) {

    double Q_a = Math.pow(10, R_a / x);
    double Q_b = Math.pow(10, R_b / x);

    double d = Q_a + Q_b;
    double P_a = Q_a / d;
    double P_b = Q_b / d;

    return new int[] {
      (int) (k * (S_a - P_a)),
      (int) (k * (S_b - P_b))
    };
  }
}