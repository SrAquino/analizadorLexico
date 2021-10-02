package Lexico;

import java.nio.charset.*;
import java.nio.file.*;
import Exceptions.LexicalException;


public class Leitor{
    private char[] texto;
    private char estado, charAtual;
    private int position;
    private String term;

    public Leitor(String filename) {
        try {
            String conteudo_txt;

            position = 0;
            term = "";

            conteudo_txt = new String(Files.readAllBytes(Paths.get(filename)),StandardCharsets.UTF_8);
                texto = conteudo_txt.toCharArray();
            System.out.println("----------------------");
            System.out.println(conteudo_txt);
            System.out.println("----------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Tokens proxToken() {
        
        Tokens t;

        estado = 'i';
        while(true){
            if(isEOF()){
                System.out.println("EndOfFile");
                return null;
            }

            charAtual = prox();

            switch (estado) {
                case 'i':    
                    if(iscaracter(charAtual)){
                        if(charAtual=='a' || charAtual=='c' ||charAtual=='d' ||charAtual=='e' ||charAtual=='g' ||charAtual=='i' ||charAtual=='l' ||charAtual=='n' ||charAtual=='t'){
                            
                        } else {
                            estado = '1';
                            term += charAtual;
                        }
                    } else if(isdigito(charAtual)){
                        estado = '3';
                        term += charAtual;
                    } else if(isop(charAtual)){
                        if(charAtual=='+' ||charAtual=='*' ||charAtual=='-' ||charAtual=='/'){
                            estado = '5';
                            term += charAtual;
                        } else if(charAtual=='<' || charAtual=='>'){
                            estado = '6';
                            term += charAtual;
                        } else if(charAtual=='='){
                            estado = '7';
                            term += charAtual;
                        }
                    } else if (ispecial(charAtual)){
                        estado = 'i';
                    } else {
                        throw new LexicalException("SIMBOLO NÃO RECONHECIDO");
                    }
                break;

                case '1':
                    if(iscaracter(charAtual) || isdigito(charAtual)){
                        estado = '1';
                        term += charAtual;
                    } else {
                        estado = '2';
                    }
                break;

                case '2':
                    back();
                    t = new Tokens();
                    t.setTipo(Tokens.TK_Id);
                    t.setTexto(term);
                    term = "";
                return t;
            
                default:    
                break;

                case '3':
                    if(isdigito(charAtual)){
                        estado = '3';
                        term += charAtual;
                    } else {
                        estado = '4';
                    }
                break;

                case '4':
                    back();
                    t = new Tokens();
                    t.setTipo(Tokens.TK_Number);
                    t.setTexto(term);
                    term = "";
                return t;
                
                case '5':
                    t = new Tokens();
                    t.setTipo(Tokens.TK_Op_Ari);
                    t.setTexto(term);
                    term = "";
                return t;

                case '6':
                    t = new Tokens();
                    t.setTipo(Tokens.TK_Op_Rel);
                    t.setTexto(term);
                    term = "";
                return t;

                case '7':
                    t = new Tokens();
                    t.setTipo(Tokens.TK_Atri);
                    t.setTexto(term);
                    term = "";
                return t;

            }

        }
        
    }
        
    private boolean isdigito(char c){
        return c >= '0' && c <= '9';
    }

    private boolean iscaracter(char c){
        return (c >= 'a' && c <= 'z');
    }

    private boolean isop(char c) {
        return (c == '+' || c == '*' || c == '-' || c == '/' || c == '>' || c == '<' || c == '='); 
    }
        
    private boolean ispecial(char c) {
        return (c == ' ' || c == '\n' || c == '\t'); 
    }

    private char prox() {
        return texto[position++];   
    }

    private boolean isEOF() {
        return texto.length <= position;      
    }

    private void back() {
        position--;
    }

}