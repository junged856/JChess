package Pieces;

import Classes.Square;

import java.util.LinkedList;

public class Bishop extends Piece{
    public Bishop(int xpos, int ypos, Alliance alliance, LinkedList<Piece> piecesOnBoard) {
        super(xpos, ypos, alliance, piecesOnBoard);
        piecesOnBoard.add(this);
    }
    @Override
    public boolean isLegalMove(int xdestination, int ydestination, Alliance alliance) {
        calculateLegalMoves();

        for(Square s : legalMoves) {
            int xcoord = s.getXcoord();
            int ycoord = s.getYcoord();

            if(xdestination == xcoord && ydestination == ycoord) {
                System.out.println("Legal move");
                return true;
            }
        }
        System.out.println("illegal move");
        return false;
    }
    public void calculateLegalMoves() {
        legalMoves.clear();
        controlledSquares.clear();
        this.getLegalMoves("northeast");
        this.getLegalMoves("southwest");
        this.getLegalMoves("southeast");
        this.getLegalMoves("northwest");
    }
}