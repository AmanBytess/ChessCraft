package com.aman.chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public final class ChessGame {
    private final Board board=new Board();
    private final List<MoveCommand> history=new ArrayList<>();
    private final Stack<MoveCommand> redoStack=new Stack<>();
    public Board board(){return board;}
    public String play(String text){
        Move requested=Move.parse(text);
        Move actual=null;
        for(Move m:board.legalMoves())if(m.equals(requested)){actual=m;break;}
        if(actual==null)return "Illegal move. Use MOVES to list legal moves.";
        MoveCommand command=new MoveCommand(board,actual);
        command.execute();history.add(command);redoStack.clear();
        String result=board.result();
        return "Played "+actual+(result.isEmpty()?"":"\\n"+result);
    }
    public String undo(){
        if(history.isEmpty())return "Nothing to undo.";
        MoveCommand cmd=history.remove(history.size()-1);cmd.undo();redoStack.push(cmd);
        return "Undid "+cmd.move()+".";
    }
    public String redo(){
        if(redoStack.isEmpty())return "Nothing to redo.";
        MoveCommand cmd=redoStack.pop();cmd.redo();history.add(cmd);
        return "Redid "+cmd.move()+".";
    }
    public List<Move> legalMoves(){return board.legalMoves();}
}
