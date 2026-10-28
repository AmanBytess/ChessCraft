package com.aman.chess;
public final class Bishop extends Piece { private static final int[][] D={{-1,-1},{-1,1},{1,-1},{1,1}}; public Bishop(boolean w){super(w);} public char symbol(){return white?'B':'b';} protected int[][] directions(){return D;} protected boolean slides(){return true;} }
