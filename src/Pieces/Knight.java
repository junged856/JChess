package Pieces;

import ChessUtils.ChessUtils;
import Classes.Square;
import java.util.LinkedList;

public class Knight extends Piece{
    Square[] candidateMoves = new Square[8];
    public Knight(int xpos, int ypos, Alliance alliance, LinkedList<Piece> piecesOnBoard) {
        super(xpos, ypos, alliance, piecesOnBoard);
        piecesOnBoard.add(this);
    }
    @Override
    public boolean isLegalMove(int xdestination, int ydestination, Alliance alliance) {
        getCandidateMoves();
        calculateLegalMoves();

        for(Square s : legalMoves) {
            int xcoord = s.getXcoord();
            int ycoord = s.getYcoord();
            if(xdestination == xcoord && ydestination == ycoord)
                return true;
        }
        return false;
    }
    public void calculateLegalMoves() {
        controlledSquares.clear();
        legalMoves.clear();
        getCandidateMoves();

        for(Square candidate : candidateMoves) {
            if(candidate.isUnoccupied(piecesOnBoard)) {
                legalMoves.add(candidate);
                controlledSquares.add(candidate);
            }
            else {
                Piece p = ChessUtils.getPiece(candidate, piecesOnBoard);
                if(p.getAlliance() != this.alliance)  {
                    if(p.getClass() == King.class)
                        ((King) p).setChecked(this);
                    else
                        legalMoves.add(candidate);
                }
                else if(p.getAlliance() == this.alliance)
                    controlledSquares.add(candidate);
            }
        }
    }
    public void getCandidateMoves() {
        candidateMoves[0] = new Square(xpos + 1, ypos + 2);
        candidateMoves[1] = new Square(xpos - 1, ypos + 2);
        candidateMoves[2] = new Square(xpos + 2, ypos + 1);
        candidateMoves[3] = new Square(xpos - 2, ypos - 1);

        candidateMoves[4] = new Square(xpos + 1, ypos - 2);
        candidateMoves[5] = new Square(xpos - 1, ypos - 2);
        candidateMoves[6] = new Square(xpos + 2, ypos - 1);
        candidateMoves[7] = new Square(xpos - 2, ypos + 1);
    }
}