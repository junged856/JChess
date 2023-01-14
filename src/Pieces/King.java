package Pieces;

import ChessUtils.ChessUtils;
import Classes.Square;

import java.util.LinkedList;

public class King extends Piece{
    public boolean isChecked = false;
    public boolean canCastleLeft = false;
    public boolean canCastleRight = false;
    public boolean isFirstMove = true;
    public Piece attackingPiece;
     Piece leftRook;
     Piece rightRook;
    public King(int xpos, int ypos, Alliance alliance, LinkedList<Piece> piecesOnBoard) {
        super(xpos, ypos, alliance, piecesOnBoard);
        piecesOnBoard.add(this);

        // get left and right rooks
        for(Piece p : piecesOnBoard) {
            if(p.getClass() == Rook.class && p.getXpos() == 0 && p.getAlliance() == this.alliance)
                leftRook = (Rook) p;
            else if(p.getClass() == Rook.class && p.getXpos() == 7 && p.getAlliance() == this.alliance)
                rightRook = (Rook) p;
        }

        System.out.println("POSITION OF right " + alliance + " ROOK IS" + rightRook.getYpos() + "," + rightRook.getXpos());
    }

    @Override
    public boolean isLegalMove(int xdestination, int ydestination, Alliance alliance) {
        calculateLegalMoves();

        for(Square s : legalMoves) {
            int xcoord = s.getXcoord();
            int ycoord = s.getYcoord();
            if(xdestination == xcoord && ydestination == ycoord) {
                System.out.println("this is legal");
                return true;
            }
        }
        System.out.println("this is illegal");
        return false;
    }

    public Piece getAttackingPiece() {
        return this.attackingPiece;
    }

    public void getCandidateMoves() {
        legalMoves.add(new Square(xpos + 1, ypos));
        legalMoves.add(new Square(xpos + 1, ypos - 1));
        legalMoves.add(new Square(xpos + 1, ypos + 1));

        legalMoves.add(new Square(xpos - 1, ypos));
        legalMoves.add(new Square(xpos - 1, ypos - 1));
        legalMoves.add(new Square(xpos - 1, ypos + 1));

        legalMoves.add(new Square(xpos, ypos + 1));
        legalMoves.add(new Square(xpos, ypos - 1));

        getCastlingMoves();
        // these should only be legal moves if castling is an option
        if(canCastleLeft)
            legalMoves.add(new Square(xpos - 2, ypos));
        if(canCastleRight)
            legalMoves.add(new Square(xpos + 2, ypos));

        legalMoves.removeIf(Square::isOutOfBounds);
        controlledSquares.addAll(legalMoves);
    }

    public void calculateLegalMoves() {
        legalMoves.clear();
        controlledSquares.clear();
        getCandidateMoves(); // all theoretical king moves are considered legal moves

        for(Piece p : piecesOnBoard) {
            if (p.getAlliance() != alliance) {
                // cant move into check
                LinkedList<Square> controlledSquares = p.getControlledSquares();
                for (Square controlledSquare : controlledSquares) {
                    legalMoves.removeIf(s -> s.equals(controlledSquare));
                }
            } else {
                // cant move where alliance pieces already are
                Square candidate = new Square(p.getXpos(), p.getYpos());
                legalMoves.removeIf(s -> s.equals(candidate));
            }
        }
    }

    public void getCastlingMoves() {
        if(this.isFirstMove &&
                // squares between the king and rook must be empty
                ChessUtils.getPiece(this.xpos - 1, ypos, piecesOnBoard) == null
                && ChessUtils.getPiece(this.xpos - 2, ypos, piecesOnBoard) == null
                && ChessUtils.getPiece(this.xpos - 3, ypos, piecesOnBoard) == null
                // squares between the king and rook must be uncontrolled
                // rook must be untouched
                && leftRook.isFirstMove)
        {
            canCastleLeft = true;
            System.out.println(this.alliance + " CAN CASTLE LEFT");
        }

        if(this.isFirstMove
                && rightRook.isFirstMove
                && ChessUtils.getPiece(this.xpos + 1, ypos, piecesOnBoard) == null
                && ChessUtils.getPiece(this.xpos + 2, ypos, piecesOnBoard) == null)
        {
            canCastleRight = true;
            System.out.println(this.alliance + " CAN CASTLE RIGHT");
        }
    }

    // assumes that this king is checked
    public boolean canNegateCheck() {
        for(Piece p : piecesOnBoard) {
            if(p.canBlock(attackingPiece) && p.alliance != attackingPiece.alliance || p.isLegalMove(attackingPiece.xpos, attackingPiece.ypos, p.alliance))
                return true;
        }
        return false;
    }
    public void setChecked(Piece attackingPiece) {
        this.attackingPiece = attackingPiece;
        System.out.println(this.alliance + " KING GOT CHECKED BY " + attackingPiece.alliance + " " + attackingPiece);
        isChecked = true;
    }
    public boolean isChecked() {
        return isChecked;
    }
    public boolean isCheckmate() {
        this.calculateLegalMoves();
        if(!this.canNegateCheck() && legalMoves.size() == 0)
            return true;
        return false;
    }
    @Override
    public void move(int xpos, int ypos, int SQUARE_WIDTH) {
        // get left and right rooks
        for(Piece p : piecesOnBoard) {
            if(p.getClass() == Rook.class && p.getXpos() == 0 && p.getAlliance() == this.alliance && p.isFirstMove)
                leftRook = (Rook) p;
            else if(p.getClass() == Rook.class && p.getXpos() == 7 && p.getAlliance() == this.alliance && p.isFirstMove)
                rightRook = (Rook) p;
        }
        // if king is moved two squares to the right, then put the rook to the left of the king (castle right)
        if(xpos == this.xpos + 2) {
            System.out.println("CASTLED RIGHT");
            rightRook.setXpos(this.xpos + 1); // didnt use the move function because I dont want to switch the board until the king is moved
            rightRook.setYpos(this.ypos);
            canCastleRight = false;
        }
        // if king is moved two squares left, rook goes to the right of the king
        if(xpos == this.xpos - 2) {
            System.out.println("CASTLED LEFT");
            leftRook.setXpos(this.xpos - 1);
            leftRook.setYpos(this.ypos);
            canCastleLeft = false;
        }
        super.move(xpos, ypos, SQUARE_WIDTH);
        System.out.println("KING WAS MOVED");
        isFirstMove = false;
        isChecked = false; // if the king moves to a particular square, we know it cant be in check since king cannot move into check
    }
}