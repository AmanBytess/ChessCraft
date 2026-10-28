package com.aman.chess;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
    protected final boolean white;
    protected Piece(boolean white) { this.white=white; }
    public boolean isWhite() { return white; }
    public abstract char symbol();
    protected abstract int[][] directions();
    protected abstract boolean slides();
    public List<Move> pseudoMoves(Board b, int r, int c) {
        List<Move> out=new ArrayList<>();
        char[][] cells=b.cells();
        if (Character.toLowerCase(symbol())=='p') {
            int dir=white?-1:1, start=white?6:1, promotionRow=white?0:7;
            int nr=r+dir;
            if (Board.inside(nr,c)&&cells[nr][c]=='.') {
                addPawnMove(out,r,c,nr,c,promotionRow);
                int nr2=r+2*dir;
                if(r==start&&cells[nr2][c]=='.') out.add(new Move(r,c,nr2,c,' '));
            }
            for(int dc:new int[]{-1,1}) {
                int nc=c+dc;
                if(!Board.inside(nr,nc)) continue;
                if(cells[nr][nc]!='.'&&Character.isUpperCase(cells[nr][nc])!=white)
                    addPawnMove(out,r,c,nr,nc,promotionRow);
                else if(b.enPassantRow()==nr&&b.enPassantCol()==nc)
                    out.add(new Move(r,c,nr,nc,' '));
            }
            return out;
        }
        for(int[] d:directions()) {
            int nr=r+d[0],nc=c+d[1];
            while(Board.inside(nr,nc)) {
                char target=cells[nr][nc];
                if(target=='.') out.add(new Move(r,c,nr,nc,' '));
                else {
                    if(Character.isUpperCase(target)!=white) out.add(new Move(r,c,nr,nc,' '));
                    break;
                }
                if(!slides()) break;
                nr+=d[0]; nc+=d[1];
            }
        }
        return out;
    }
    private void addPawnMove(List<Move> out,int r,int c,int nr,int nc,int promotionRow) {
        if(nr==promotionRow) for(char p:new char[]{'q','r','b','n'}) out.add(new Move(r,c,nr,nc,p));
        else out.add(new Move(r,c,nr,nc,' '));
    }
}
