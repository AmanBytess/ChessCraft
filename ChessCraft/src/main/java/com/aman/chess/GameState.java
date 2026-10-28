package com.aman.chess;

public final class GameState {
    boolean whiteToMove=true;
    boolean whiteKingSide=true, whiteQueenSide=true, blackKingSide=true, blackQueenSide=true;
    int epRow=-1, epCol=-1, halfmoveClock=0, fullmoveNumber=1;
    char captured='.';
    char moved='.';
    char rookBefore='.';
    int rookFromRow=-1, rookFromCol=-1, rookToRow=-1, rookToCol=-1;
    char previousPromotion=' ';
    GameState copy() {
        GameState s=new GameState();
        s.whiteToMove=whiteToMove; s.whiteKingSide=whiteKingSide; s.whiteQueenSide=whiteQueenSide;
        s.blackKingSide=blackKingSide; s.blackQueenSide=blackQueenSide;
        s.epRow=epRow; s.epCol=epCol; s.halfmoveClock=halfmoveClock; s.fullmoveNumber=fullmoveNumber;
        return s;
    }
    public boolean isWhiteToMove(){return whiteToMove;}
}
