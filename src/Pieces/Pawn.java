package Pieces;

import java.util.LinkedList;
import ChessUtils.ChessUtils;
import Classes.Square;

public class Pawn extends Piece{
    boolean isFirstMove = true;
    int offset; // negative offset for white, positive for black (sign determines forwards direction)
    public Pawn(int xpos, int ypos, Alliance alliance, LinkedList<Piece> piecesOnBoard) {
        super(xpos, ypos, alliance, piecesOnBoard);
        piecesOnBoard.add(this);
        offset = -1;
    }
    public boolean isLegalMove(int xdestination, int ydestination, Alliance alliance) {
        calculateLegalMoves();
        for(Square s : legalMoves) {
            int xcoord = s.getXcoord();
            int ycoord = s.getYcoord();
            if(xdestination == xcoord && ydestination == ycoord)
                return true;
        }
        return false;
    }
    @Override
    public void calculateLegalMoves() {
        legalMoves.clear();
        controlledSquares.clear();
        // check if piece can be taken on these squares
        Piece candidate1 = ChessUtils.getPiece(xpos + 1, ypos + offset, piecesOnBoard);
        Piece candidate2 = ChessUtils.getPiece(xpos - 1, ypos + offset, piecesOnBoard);

        // pawn can capture on diagonal squares that are right in front of it
        if(candidate1 != null && candidate1.getAlliance() != alliance) {
            if(candidate1.getClass() != King.class)
                legalMoves.add(new Square(candidate1.getXpos(), candidate1.getYpos()));
            else
                ((King) candidate1).setChecked(this);
        } else if(candidate1 == null || candidate1.getAlliance() == alliance)
            controlledSquares.add(new Square(xpos + 1, ypos + offset));

        if(candidate2 != null && candidate2.getAlliance() != alliance) {
            if (candidate2.getClass() != King.class)
                legalMoves.add(new Square(candidate2.getXpos(), candidate2.getYpos()));
            else
                ((King) candidate2).setChecked(this);
        } else if(candidate2 == null || candidate2.getAlliance() == alliance)
            controlledSquares.add(new Square(xpos - 1, ypos + offset));

        // pawn can move 1 square forward
        if (ChessUtils.getPiece(xpos, ypos+offset, piecesOnBoard) == null) {
            legalMoves.add(new Square(xpos, ypos+offset));
        }

        // pawn can move 2 squares forward if its the first move
        if (isFirstMove && ChessUtils.getPiece(xpos, ypos+offset, piecesOnBoard) == null && ChessUtils.getPiece(xpos, ypos+2*offset, piecesOnBoard) == null) {
            legalMoves.add(new Square(xpos, ypos+2*offset));
        }
    }

    @Override
    public void move(int xpos, int ypos, int SQUARE_WIDTH) {
        if(!(xpos == this.xpos && ypos == this.ypos))
            isFirstMove = false;
        super.move(xpos, ypos, SQUARE_WIDTH);
    }
}
