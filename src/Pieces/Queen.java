package Pieces;

import Classes.Square;

import java.util.LinkedList;

public class Queen extends Piece{
    public Queen(int xpos, int ypos, Alliance alliance, LinkedList<Piece> piecesOnBoard) {
        super(xpos, ypos, alliance, piecesOnBoard);
        piecesOnBoard.add(this);
    }

    @Override
    public boolean isLegalMove(int xdestination, int ydestination, Alliance alliance) {
        for(Square s : legalMoves) {
            int xcoord = s.getXcoord();
            int ycoord = s.getYcoord();

            if(xdestination == xcoord && ydestination == ycoord) {
                System.out.println("THIS IS A LEGAL MOVE");
                return true;
            }
        }
        System.out.println("THIS IS AN ILLEGAL MOVE");
        return false;
    }

    public void calculateLegalMoves() {
        legalMoves.clear();
        controlledSquares.clear();
        this.getLegalMoves("northeast");
        this.getLegalMoves("southwest");
        this.getLegalMoves("southeast");
        this.getLegalMoves("northwest");
        this.getLegalMoves("north");
        this.getLegalMoves("east");
        this.getLegalMoves("west");
        this.getLegalMoves("south");

//        for(Square s : legalMoves) {
//            System.out.println(alliance + " QUEEN CAN MOVE HERE:");
//            System.out.println(s.getYcoord() + "," + s.getXcoord());
//        }

    }
}
