# ChessCraft — Object-Oriented Two-Player Chess Engine

A Java console chess engine recreated from the project description in my resume. It is a new portfolio implementation, not recovery of the original source files.

## Features
- Two-player local chess played with coordinate moves such as `e2e4`
- Object-oriented piece hierarchy with an abstract `Piece` class and polymorphic move generation
- Legal-move filtering that prevents leaving the king in check
- Check, checkmate and stalemate detection
- Castling, en passant and pawn promotion
- Reversible move commands with undo and redo
- Board and game-state responsibilities separated into classes

## Requirements
Java 17 or later. No external dependencies.

## Run
```bash
javac -d out src/main/java/com/aman/chess/*.java
java -cp out com.aman.chess.ChessCraftApp
```

## Commands
- Enter a move like `e2e4`
- Promotion: enter a move like `a7a8q` (q, r, b, or n)
- `undo` — undo the last move
- `redo` — redo the last undone move
- `moves` — list legal moves
- `help` — show commands
- `quit` — exit

## Design overview
- `Piece` and its subclasses encapsulate movement patterns.
- `Board` owns the board position and validates legal moves.
- `GameState` tracks side to move, castling rights, en-passant target and move counters.
- `MoveCommand` stores a move and supports execute/undo.
- `ChessGame` manages turn progression and undo/redo history.

## Limitations
This is a console-based educational engine. It does not include a graphical interface, online multiplayer, clocks, PGN import/export, or a computer opponent.

## Portfolio note
This repository is a recreated implementation based on the project summary in my resume because the original files were unavailable. Please run it, review it, and understand the implementation before publishing it. Do not represent it as the exact original source if it is not.
