package com.codingronin.selenium.webscrapper;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.util.Random;

public class MoveMouse {
  Random r = new Random();

  public static void main(String[] args) throws AWTException {

    Robot bot = new Robot();

    int z = 1;
    int step = 0;
    while (Math.abs(z) == 1) {
      try {
        int start = 6000;
        int delay = rangedRand(start, start * 10);
        int movx = rangedRand(1, 100);
        int movy = rangedRand(1, 100);
        System.out
            .println(String.format("step:%d, mx:%d, my:%d, delay:%d", step, movx, movy, delay));

        Point p = MouseInfo.getPointerInfo().getLocation();

        bot.mouseMove((int) p.getX() + (movx * z), (int) p.getY() + (movy * z));
        z = z * -1;
        step++;
        Thread.sleep(delay);
      } catch (InterruptedException e) {
        z = 0;
        Thread.currentThread().interrupt();
      }
    }

  }

  static Random rnd = new Random();

  static int rangedRand(int min, int max) {
    int diff = max - min;
    return rnd.nextInt(diff) + min;
  }
}
