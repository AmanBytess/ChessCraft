package com.aman.chess;
public final class Pawn extends Piece { public Pawn(boolean w){super(w);} public char symbol(){return white?'P':'p';} protected int[][] directions(){return new int[0][0];} protected boolean slides(){return false;} }
