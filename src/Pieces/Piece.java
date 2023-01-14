package Pieces;

import ChessUtils.ChessUtils;
import Classes.Square;
import ChessUtils.Board;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Piece {
    public boolean isFirstMove;
    int xpos, ypos, x, y;
    String directionOfCheck;
    Alliance alliance;
    King allianceKing = null;
    King enemyKing = null;
    Board chessBoard = new Board();
    Timer timer = new Timer();
    LinkedList<Piece> piecesOnBoard;
    public LinkedList<Square> legalMoves = new LinkedList<>();
    LinkedList<Square> controlledSquares = new LinkedList<>();

    public Piece(int xpos, int ypos, Alliance alliance, LinkedList<Piece> piecesOnBoard) {
        this.xpos = xpos;
        this.ypos = ypos;
        x = xpos*80 + 50;
        y = ypos*80 + 50;
        this.alliance = alliance;
        this.piecesOnBoard = piecesOnBoard;
    }

    public Alliance getAlliance() {
        return alliance;
    }

    public int getXpos() {
        return xpos;
    }

    public int getYpos() {
        return ypos;
    }
    public void setXpos(int xpos) { this.xpos = xpos;}
    public void setYpos(int ypos) { this.ypos = ypos;}

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    // FIXIT LOOK AT OUTPUT
    public void move(int xpos, int ypos, int SQUARE_WIDTH) {
        System.out.println("MOVING TO " + xpos + "," + ypos);
        Piece pieceTaken = null; // used for when a piece is pinned and tries to take an enemy piece
        int prevXpos = this.xpos;
        int prevYpos = this.ypos;

        this.xpos = xpos;
        this.ypos = ypos;

        // check if theres a piece on the destination square
        for(Piece piece : piecesOnBoard) {
            if(piece.getYpos() == this.ypos && piece.getXpos() == this.xpos && piece != this) {
                pieceTaken = piece; // save the piece taken just in case we need to put it back
                piece.kill();
                break;
            }
        }

        getAllianceKing();

        allianceKing.isChecked = false; // assume king is not checked

        for(Piece p : piecesOnBoard)
            p.calculateLegalMoves();

        // if the alliance king is checked after you move, move back (means the piece is pinned and cannot move)
        if(allianceKing.isChecked()) {
            System.out.println("MOVED INTO A CHECK GOING BACK!");
            this.xpos = prevXpos;
            this.ypos = prevYpos;
            // COULD CAUSE ERRORS**************************
            if(pieceTaken != null)
                piecesOnBoard.add(pieceTaken);
            ChessUtils.changeTurns(); // after move is called, turn is changed, but turn needs to stay the same
        } else {
            this.isFirstMove = false;
            chessBoard.switchBoard(piecesOnBoard);
        }

        this.y = this.ypos*SQUARE_WIDTH + 50;
        this.x = this.xpos*SQUARE_WIDTH + 50;

        allianceKing.isChecked = false; // to prevent allianceKing.isChecked from being true all the time

        for(Piece p : piecesOnBoard)
            p.calculateLegalMoves(); // calculate moves after moving
    }

    public void kill() {
        piecesOnBoard.remove(this);
    }

    public void getAllianceKing() {
        for(Piece p : piecesOnBoard) {
            if (p.getClass() == King.class && p.getAlliance() == this.alliance) {
                allianceKing = (King) p;
            }
        }
    }

    public void getEnemyKing() {
        for(Piece p : piecesOnBoard) {
            if (p.getClass() == King.class && p.getAlliance() != this.alliance) {
                enemyKing = (King) p;
            }
        }
    }
    // gets legal moves of a piece in one of 8 directions
    public LinkedList<Square> getLegalMoves(String direction) {
        int xoffset, yoffset, xcoord, ycoord;
        Square currentSquare;
        LinkedList<Square> legalMovesInDirection = new LinkedList<>();

        xcoord = this.xpos;
        ycoord = this.ypos;
        yoffset = 0;
        xoffset = 0;

        if(direction.equals("north")) {
            yoffset = -1;
        } else if (direction.equals("east")) {
            xoffset = -1;
        } else if (direction.equals("west")) {
            xoffset = 1;
        } else if (direction.equals("south")) {
            yoffset = 1;
        } else if (direction.equals("northeast")) {
            yoffset = -1;
            xoffset = 1;
        } else if (direction.equals("southeast")) {
            yoffset = 1;
            xoffset = 1;
        } else if (direction.equals("northwest")) {
            yoffset = -1;
            xoffset = -1;
        } else if (direction.equals("southwest")) {
            yoffset = 1;
            xoffset = -1;
        }

        boolean keepGettingLegalMoves = true;

        currentSquare = new Square(xcoord + xoffset, ycoord + yoffset);

        while(!currentSquare.isOutOfBounds()) {
            if(!currentSquare.isUnoccupied(piecesOnBoard)) {
                Piece pieceOnSquare = ChessUtils.getPiece(currentSquare.getXcoord(), currentSquare.getYcoord(), piecesOnBoard);
                if(pieceOnSquare.getAlliance() != this.alliance && pieceOnSquare.getClass() == King.class) {
                    // king is in check
                    ((King) pieceOnSquare).setChecked(this);
                    this.directionOfCheck = direction;
                    keepGettingLegalMoves = false;
                } else if (pieceOnSquare.getAlliance() != this.alliance) {
                    // enemy pieces can be captured
                    legalMoves.add(currentSquare);
                    break;
                } else {
                    // friendly piece is protected by this piece
                    controlledSquares.add(currentSquare);
                    break;
                }
            }

            // bottom code only runs in the case of checks to determine what squares after the king are controlled
            if(keepGettingLegalMoves) {
                legalMoves.add(currentSquare);
                legalMovesInDirection.add(currentSquare);
            }
            controlledSquares.add(currentSquare);

            xcoord += xoffset;
            ycoord += yoffset;
            currentSquare = new Square(xcoord, ycoord);
        }

        return legalMovesInDirection;
    }
    public boolean isCorrectColor() {
        if(ChessUtils.isWhiteToMove() && this.alliance == Alliance.WHITE)
            return true;
        else if(!ChessUtils.isWhiteToMove() && this.alliance == Alliance.BLACK)
            return true;
        else
            return false;
    }
    public LinkedList<Square> getControlledSquares() {
        return controlledSquares;
    }
    public String getDirectionOfCheck() {
        return directionOfCheck;
    }
    public abstract boolean isLegalMove(int xpos, int ypos, Alliance alliance);
    // checks if a piece handles check by moving to xpos, ypos
    // ASSUMES THAT THE PIECE IS THE SAME COLOR AS KING IN CHECK
    public boolean handlesCheck(int xpos, int ypos) {
        getAllianceKing();
        
        Piece attackingPiece = allianceKing.getAttackingPiece();
        boolean isLegal = this.isLegalMove(xpos, ypos, alliance);

        if(isLegal && (this.blocks(attackingPiece, xpos, ypos) || ChessUtils.getPiece(xpos, ypos, piecesOnBoard) == attackingPiece)) {
            System.out.println("THIS HANDLES CHECK");
            return true;
        }
        else {
            System.out.println("THIS DOESNT HANDLE CHECK");
            return false;
        }
    }

    // checks if moving to xpos, ypos blocks the attacking piece
    public boolean blocks(Piece attackingPiece, int xpos, int ypos) {
        // get legal moves in direction of check
        LinkedList<Square> legalMoves = attackingPiece.getLegalMoves(attackingPiece.getDirectionOfCheck());

        // moving to any of these squares blocks check
        for(Square s : legalMoves) {
            if(s.getXcoord() == xpos && s.getYcoord() == ypos)
                return true;
        }
        return false;
    }
    public abstract void calculateLegalMoves();  // this method also determines which squares are controlled by that piece

    // see if this piece can block a check from an attacking piece
    public boolean canBlock(Piece attackingPiece) {
        if(attackingPiece.getClass() == Pawn.class || attackingPiece.getClass() == Knight.class)
            return false;

        // for long range pieces: (bishops, queens, rooks)
        LinkedList<Square> legalMoves = attackingPiece.getLegalMoves(attackingPiece.getDirectionOfCheck());

        // if this piece can move to one of the attackingPiece's legal squares, this means the attackingPiece can be blocked
        for (Square s : legalMoves) {
            if (this.isLegalMove(s.getXcoord(), s.getYcoord(), this.alliance)) {
                System.out.println(this.alliance + " " + this + " can block check");
                return true;
            }
        }
        return false;
    }
}