package com.aman.chess;

import java.util.Scanner;
import java.util.stream.Collectors;

public final class ChessCraftApp {
    private ChessCraftApp(){}
    public static void main(String[] args){
        ChessGame game=new ChessGame();
        System.out.println("=== ChessCraft ===");
        System.out.println("Enter moves as e2e4. Promotion: a7a8q. Type HELP for commands.");
        game.board().print();
        try(Scanner sc=new Scanner(System.in)){
            while(true){
                System.out.print((game.board().state().isWhiteToMove()?"White":"Black")+"> ");
                if(!sc.hasNextLine())break;
                String input=sc.nextLine().trim();
                if(input.isEmpty())continue;
                String cmd=input.toLowerCase();
                switch(cmd){
                    case "quit","exit" -> {System.out.println("Goodbye.");return;}
                    case "help" -> printHelp();
                    case "undo" -> System.out.println(game.undo());
                    case "redo" -> System.out.println(game.redo());
                    case "moves" -> System.out.println(game.legalMoves().stream().map(Move::toString).collect(Collectors.joining(", ")));
                    case "board" -> {}
                    default -> System.out.println(game.play(input));
                }
                game.board().print();
            }
        }
    }
    private static void printHelp(){
        System.out.println("Moves: e2e4 or a7a8q for promotion");
        System.out.println("Commands: moves, undo, redo, board, help, quit");
    }
}
