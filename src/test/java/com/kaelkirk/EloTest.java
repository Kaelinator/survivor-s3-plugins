package com.kaelkirk;

import com.kaelkirk.machines.duels.EloCalculator;

import org.junit.Assert;

import org.junit.Test;

public class EloTest {

  @Test
  public void shouldAnswerWithTrue() {
    double x = 400;
    double k = 32;
    Assert.assertArrayEquals(new int[] { 16, -16 }, EloCalculator.calculateElo(1400, 1400, 1, 0, x, k));
    Assert.assertArrayEquals(new int[] { 2, -2 }, EloCalculator.calculateElo(1400, 1000, 1, 0, x, k));

    x = 20;
    k = 10;
    Assert.assertArrayEquals(new int[] { 5, -5 }, EloCalculator.calculateElo(25, 25, 1, 0, x, k));
    Assert.assertArrayEquals(new int[] { 3, -3 }, EloCalculator.calculateElo(30, 25, 1, 0, x, k));
    Assert.assertArrayEquals(new int[] { -1, 1 }, EloCalculator.calculateElo(30, 25, 0.5, 0.5, x, k));
    Assert.assertArrayEquals(new int[] { -6, 6 }, EloCalculator.calculateElo(30, 25, 0, 1, x, k));
  }
}
