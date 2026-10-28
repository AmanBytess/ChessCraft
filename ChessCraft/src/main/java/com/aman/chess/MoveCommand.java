package com.aman.chess;

public final class MoveCommand {
    private final Board board;
    private final Move move;
    private PositionSnapshot before, after;
    public MoveCommand(Board board, Move move){this.board=board;this.move=move;}
    public void execute(){
        if(before==null)before=new PositionSnapshot(board);
        board.applyUnchecked(move);
        after=new PositionSnapshot(board);
    }
    public void undo(){if(before!=null)before.restore(board);}
    public void redo(){if(after!=null)after.restore(board);}
    public Move move(){return move;}
}
