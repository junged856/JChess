package Pieces;

import java.util.LinkedList;
import Classes.Square;

public class Rook extends Piece{
    LinkedList<Piece> piecesOnBoard;
    public Rook(int xpos, int ypos, Alliance alliance, LinkedList<Piece> piecesOnBoard) {
        super(xpos, ypos, alliance, piecesOnBoard);
        this.piecesOnBoard = piecesOnBoard;
        piecesOnBoard.add(this);
        isFirstMove = true;
    }
    @Override
    public boolean isLegalMove(int xpos, int ypos, Alliance alliance) {
        calculateLegalMoves();

        for(Square s : legalMoves) {
            int xcoord = s.getXcoord();
            int ycoord = s.getYcoord();

            if(xpos == xcoord && ypos == ycoord)
                return true;
        }
        return false;
    }
    public void calculateLegalMoves() {
        legalMoves.clear();
        controlledSquares.clear();
        getEnemyKing();
        this.getLegalMoves("north");
        this.getLegalMoves("east");
        this.getLegalMoves("west");
        this.getLegalMoves("south");
    }
}