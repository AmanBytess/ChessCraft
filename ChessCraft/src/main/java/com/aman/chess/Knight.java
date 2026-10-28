package com.aman.chess;
public final class Knight extends Piece { private static final int[][] D={{-2,-1},{-2,1},{-1,-2},{-1,2},{1,-2},{1,2},{2,-1},{2,1}}; public Knight(boolean w){super(w);} public char symbol(){return white?'N':'n';} protected int[][] directions(){return D;} protected boolean slides(){return false;} }
