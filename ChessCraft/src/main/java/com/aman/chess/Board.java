package com.aman.chess;

import java.util.ArrayList;
import java.util.List;

public final class Board {
    private char[][] board=new char[8][8];
    private GameState state=new GameState();
    public Board(){reset();}
    public void reset(){
        String[] rows={"rnbqkbnr","pppppppp","........","........","........","........","PPPPPPPP","RNBQKBNR"};
        for(int r=0;r<8;r++) board[r]=rows[r].toCharArray();
        state=new GameState();
    }
    char[][] cells(){return board;}
    int enPassantRow(){return state.epRow;}
    int enPassantCol(){return state.epCol;}
    public GameState state(){return state.copy();}
    public char at(int r,int c){return board[r][c];}
    public static boolean inside(int r,int c){return r>=0&&r<8&&c>=0&&c<8;}
    private Piece piece(char p) {
        if(p=='.') return null;
        boolean w=Character.isUpperCase(p);
        return switch(Character.toLowerCase(p)){
            case 'p'->new Pawn(w); case 'n'->new Knight(w); case 'b'->new Bishop(w);
            case 'r'->new Rook(w); case 'q'->new Queen(w); case 'k'->new King(w);
            default->null;
        };
    }
    public List<Move> legalMoves(){
        List<Move> candidates=new ArrayList<>();
        for(int r=0;r<8;r++)for(int c=0;c<8;c++){
            Piece p=piece(board[r][c]);
            if(p!=null&&p.isWhite()==state.whiteToMove)candidates.addAll(p.pseudoMoves(this,r,c));
        }
        addCastles(candidates);
        List<Move> legal=new ArrayList<>();
        for(Move m:candidates){
            Board copy=copy();
            copy.applyUnchecked(m);
            if(!copy.isInCheck(!state.whiteToMove))legal.add(m);
        }
        return legal;
    }
    private void addCastles(List<Move> out){
        int row=state.whiteToMove?7:0;
        char king=state.whiteToMove?'K':'k', rook=state.whiteToMove?'R':'r';
        if(board[row][4]!=king||isInCheck(state.whiteToMove))return;
        boolean ks=state.whiteToMove?state.whiteKingSide:state.blackKingSide;
        boolean qs=state.whiteToMove?state.whiteQueenSide:state.blackQueenSide;
        if(ks&&board[row][7]==rook&&board[row][5]=='.'&&board[row][6]=='.'
            &&!isSquareAttacked(row,5,!state.whiteToMove)&&!isSquareAttacked(row,6,!state.whiteToMove))
            out.add(new Move(row,4,row,6,' '));
        if(qs&&board[row][0]==rook&&board[row][1]=='.'&&board[row][2]=='.'&&board[row][3]=='.'
            &&!isSquareAttacked(row,3,!state.whiteToMove)&&!isSquareAttacked(row,2,!state.whiteToMove))
            out.add(new Move(row,4,row,2,' '));
    }
    public boolean isInCheck(boolean white){
        char king=white?'K':'k';
        for(int r=0;r<8;r++)for(int c=0;c<8;c++)if(board[r][c]==king)return isSquareAttacked(r,c,!white);
        return true;
    }
    private boolean isSquareAttacked(int r,int c,boolean byWhite){
        for(int sr=0;sr<8;sr++)for(int sc=0;sc<8;sc++){
            char p=board[sr][sc];
            if(p=='.'||Character.isUpperCase(p)!=byWhite)continue;
            int dr=r-sr,dc=c-sc;
            switch(Character.toLowerCase(p)){
                case 'p' -> {int dir=byWhite?-1:1;if(dr==dir&&Math.abs(dc)==1)return true;}
                case 'n' -> {if((Math.abs(dr)==2&&Math.abs(dc)==1)||(Math.abs(dr)==1&&Math.abs(dc)==2))return true;}
                case 'k' -> {if(Math.max(Math.abs(dr),Math.abs(dc))==1)return true;}
                case 'b','r','q' -> {
                    boolean diagonal=Math.abs(dr)==Math.abs(dc)&&dr!=0;
                    boolean straight=(dr==0)^(dc==0);
                    boolean allowed= switch(Character.toLowerCase(p)){
                        case 'b'->diagonal; case 'r'->straight; default->diagonal||straight;
                    };
                    if(!allowed)continue;
                    int stepR=Integer.compare(dr,0),stepC=Integer.compare(dc,0),rr=sr+stepR,cc=sc+stepC;
                    boolean clear=true;
                    while(rr!=r||cc!=c){if(board[rr][cc]!='.'){clear=false;break;}rr+=stepR;cc+=stepC;}
                    if(clear)return true;
                }
            }
        }
        return false;
    }
    Board copy(){
        Board b=new Board();
        for(int r=0;r<8;r++)b.board[r]=board[r].clone();
        b.state=state.copy();
        return b;
    }
    GameState applyUnchecked(Move m){
        GameState before=state.copy();
        char p=board[m.fromRow][m.fromCol], target=board[m.toRow][m.toCol];
        before.moved=p; before.captured=target; before.previousPromotion=m.promotion;
        before.rookFromRow=before.rookFromCol=before.rookToRow=before.rookToCol=-1;
        boolean pawn=Character.toLowerCase(p)=='p';
        boolean ep=pawn&&m.fromCol!=m.toCol&&target=='.';
        if(ep){before.captured=board[m.toRow+(Character.isUpperCase(p)?1:-1)][m.toCol];board[m.toRow+(Character.isUpperCase(p)?1:-1)][m.toCol]='.';}
        if(Character.toLowerCase(p)=='k'&&Math.abs(m.toCol-m.fromCol)==2){
            int row=m.fromRow,rf=m.toCol>m.fromCol?7:0,rt=m.toCol>m.fromCol?5:3;
            before.rookFromRow=row;before.rookFromCol=rf;before.rookToRow=row;before.rookToCol=rt;before.rookBefore=board[row][rf];
            board[row][rt]=board[row][rf];board[row][rf]='.';
        }
        board[m.toRow][m.toCol]=p;
        board[m.fromRow][m.fromCol]='.';
        if(pawn&&(m.toRow==0||m.toRow==7)){
            char promo=m.promotion==' '?'q':m.promotion;
            board[m.toRow][m.toCol]=Character.isUpperCase(p)?Character.toUpperCase(promo):promo;
        }
        updateCastling(p,m.fromRow,m.fromCol,m.toRow,m.toCol,target);
        state.epRow=-1;state.epCol=-1;
        if(pawn&&Math.abs(m.toRow-m.fromRow)==2){state.epRow=(m.toRow+m.fromRow)/2;state.epCol=m.fromCol;}
        state.halfmoveClock=(pawn||target!='.'||ep)?0:state.halfmoveClock+1;
        if(!state.whiteToMove)state.fullmoveNumber++;
        state.whiteToMove=!state.whiteToMove;
        return before;
    }
    private void updateCastling(char p,int fr,int fc,int tr,int tc,char target){
        if(p=='K'){state.whiteKingSide=false;state.whiteQueenSide=false;}
        if(p=='k'){state.blackKingSide=false;state.blackQueenSide=false;}
        if(p=='R'&&fr==7&&fc==0)state.whiteQueenSide=false;
        if(p=='R'&&fr==7&&fc==7)state.whiteKingSide=false;
        if(p=='r'&&fr==0&&fc==0)state.blackQueenSide=false;
        if(p=='r'&&fr==0&&fc==7)state.blackKingSide=false;
        if(target=='R'&&tr==7&&tc==0)state.whiteQueenSide=false;
        if(target=='R'&&tr==7&&tc==7)state.whiteKingSide=false;
        if(target=='r'&&tr==0&&tc==0)state.blackQueenSide=false;
        if(target=='r'&&tr==0&&tc==7)state.blackKingSide=false;
    }
    void restore(Move m,GameState old){
        char moved=old.moved;
        board[m.fromRow][m.fromCol]=moved;
        board[m.toRow][m.toCol]=old.captured;
        boolean ep=Character.toLowerCase(moved)=='p'&&m.fromCol!=m.toCol&&old.captured!='.'&&
            (m.toRow==0||m.toRow==7||m.toRow!=m.fromRow+Integer.signum(m.toRow-m.fromRow));
        // En-passant is recognized from a pawn diagonal move to the old target square.
        if(Character.toLowerCase(moved)=='p'&&m.fromCol!=m.toCol&&old.captured!='.'&&board[m.toRow][m.toCol]=='.'){
            board[m.toRow+(Character.isUpperCase(moved)?1:-1)][m.toCol]=old.captured;
        }
        if(old.rookFromRow>=0){board[old.rookFromRow][old.rookFromCol]=old.rookBefore;board[old.rookToRow][old.rookToCol]='.';}
        state.whiteToMove=!state.whiteToMove;
        // Restore the full state fields using the pre-move snapshot held by MoveCommand.
    }
    void restoreState(GameState s){state=s.copy();}
    public void print(){
        System.out.println("  a b c d e f g h");
        for(int r=0;r<8;r++){System.out.print((8-r)+" ");for(int c=0;c<8;c++)System.out.print(board[r][c]+" ");System.out.println(8-r);}
        System.out.println("  a b c d e f g h");
    }
    public String result(){
        List<Move> moves=legalMoves();
        if(!moves.isEmpty())return isInCheck(state.whiteToMove)?"Check.":"";
        if(isInCheck(state.whiteToMove))return "Checkmate. "+(state.whiteToMove?"Black":"White")+" wins.";
        return "Stalemate. Draw.";
    }
}
