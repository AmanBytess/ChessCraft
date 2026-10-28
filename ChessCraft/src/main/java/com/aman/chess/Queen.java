package com.aman.chess;
public final class Queen extends Piece { private static final int[][] D={{-1,-1},{-1,1},{1,-1},{1,1},{-1,0},{1,0},{0,-1},{0,1}}; public Queen(boolean w){super(w);} public char symbol(){return white?'Q':'q';} protected int[][] directions(){return D;} protected boolean slides(){return true;} }
