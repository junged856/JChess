package Classes;

import ChessUtils.ChessUtils;
import Pieces.Piece;

import java.util.LinkedList;

public class Square {
    int xcoord, ycoord;
    public Square(int xcoord, int ycoord) {
        this.xcoord = xcoord;
        this.ycoord = ycoord;
    }
    public int getXcoord() {
        return xcoord;
    }
    public int getYcoord() {
        return ycoord;
    }
    public boolean isOutOfBounds() {
        if(ycoord < 0 || ycoord > 7 || xcoord < 0 || xcoord > 7)
            return true;
        else
            return false;
    }
    // returns whether this square is a legal move for the selected piece
    public boolean isUnoccupied(LinkedList<Piece> piecesOnBoard) {
        Piece pieceOnSquare = ChessUtils.getPiece(this.xcoord, this.ycoord, piecesOnBoard);
        if (pieceOnSquare == null)
            return true;
        else
            return false;
    }
    public boolean equals(Square s) {
        return this.xcoord == s.getXcoord() && this.ycoord == s.getYcoord();
    }
}