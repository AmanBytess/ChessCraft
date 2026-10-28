package com.aman.chess;

import java.util.Objects;

public final class Move {
    public final int fromRow, fromCol, toRow, toCol;
    public final char promotion;
    public Move(int fromRow, int fromCol, int toRow, int toCol, char promotion) {
        this.fromRow=fromRow; this.fromCol=fromCol; this.toRow=toRow; this.toCol=toCol;
        this.promotion=Character.toLowerCase(promotion);
    }
    public static Move parse(String s) {
        s=s.trim().toLowerCase();
        if (s.length()!=4 && s.length()!=5) throw new IllegalArgumentException("Use coordinate notation like e2e4 or a7a8q.");
        int fc=s.charAt(0)-'a', fr=8-(s.charAt(1)-'0'), tc=s.charAt(2)-'a', tr=8-(s.charAt(3)-'0');
        if (fc<0||fc>7||tc<0||tc>7||fr<0||fr>7||tr<0||tr>7 ||
            !Character.isDigit(s.charAt(1)) || !Character.isDigit(s.charAt(3)) ||
            s.charAt(1)<'1'||s.charAt(1)>'8'||s.charAt(3)<'1'||s.charAt(3)>'8')
            throw new IllegalArgumentException("Invalid square in move.");
        char p=s.length()==5?s.charAt(4):' ';
        if (s.length()==5 && "qrbn".indexOf(p)<0) throw new IllegalArgumentException("Promotion must be q, r, b, or n.");
        return new Move(fr,fc,tr,tc,p);
    }
    public String toUci() {
        String move = "" + (char)('a' + fromCol) + (8 - fromRow)
                + (char)('a' + toCol) + (8 - toRow);
        return promotion == ' ' ? move : move + promotion;
    }
    @Override public boolean equals(Object o) {
        if (!(o instanceof Move m)) return false;
        return fromRow==m.fromRow&&fromCol==m.fromCol&&toRow==m.toRow&&toCol==m.toCol&&promotion==m.promotion;
    }
    @Override public int hashCode() { return Objects.hash(fromRow,fromCol,toRow,toCol,promotion); }
    @Override public String toString() { return toUci(); }
}
