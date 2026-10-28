package com.aman.chess;
public final class King extends Piece { private static final int[][] D={{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}}; public King(boolean w){super(w);} public char symbol(){return white?'K':'k';} protected int[][] directions(){return D;} protected boolean slides(){return false;} }
