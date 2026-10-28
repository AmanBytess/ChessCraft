package com.aman.chess;
public final class Rook extends Piece { private static final int[][] D={{-1,0},{1,0},{0,-1},{0,1}}; public Rook(boolean w){super(w);} public char symbol(){return white?'R':'r';} protected int[][] directions(){return D;} protected boolean slides(){return true;} }
