package com.aman.chess;

final class PositionSnapshot {
    final char[][] board;
    final GameState state;
    PositionSnapshot(Board b) {
        board=new char[8][8];
        for(int r=0;r<8;r++)board[r]=b.cells()[r].clone();
        state=b.state();
    }
    void restore(Board b) {
        for(int r=0;r<8;r++)System.arraycopy(board[r],0,b.cells()[r],0,8);
        b.restoreState(state);
    }
}
