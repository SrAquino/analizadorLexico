package Lexico;

import java.nio.charset.*;
import java.nio.file.*;

import Exceptions.LexicalException;
//import java.util.LinkedList;


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
            //System.out.println("Estado: "+estado);
            //System.out.println("Posição: "+position);
            switch (estado) {
                case 'i':
                //System.out.println("Char atual: "+charAtual);    
                    if(iscaracter(charAtual)){
                        if(isreservada()){

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
                    } else if(isdelimiter(charAtual)){
                        estado = '9';
                        term += charAtual;

                    } else if (ispecial(charAtual)){
                        estado = 'i';
                    } else {
                        throw new LexicalException("JAPONÊS EU NÃO SEI! Como que lê: "+charAtual);
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
                    System.out.println("2: "+charAtual);
                    t = new Tokens();
                    t.setTipo(Tokens.TK_Id);
                    t.setTexto(term);
                    term = "";
                return t;

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
                    //back();
                    t = new Tokens();
                    t.setTipo(Tokens.TK_Op_Ari);
                    t.setTexto(term);
                    term = "";
                return t;

                case '6':
                    //back();
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

                case '8':
                    t = new Tokens();
                    t.setTipo(Tokens.TK_WR);
                    t.setTexto(term);
                    term = "";
                return t;

                case '9':
                    back();
                    t = new Tokens();
                    t.setTipo(Tokens.TK_Delimit);
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
        return ((c >= 'a' && c <= 'z')|| c == 'T');
    }

    private boolean isop(char c) {
        return (c == '+' || c == '*' || c == '-' || c == '/' || c == '>' || c == '<' || c == '='); 
    }

    private boolean isdelimiter(char c){
        return (c == '(' || c == ')');
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

    private boolean isreservada(){
        //atom, car, case, cdr, cond, define, defun, if, lambda, let, list, nil, T
        //eq, geq, leq, neq

        if (charAtual == 'a'){
            
            if(proxreserved('t')){
                if(proxreserved('o')){
                    if(proxreserved('m')){
                        estado = '8';//atom
                        term += "atom";
                        return true;

                    } else {back(); return false;}
                } else {back(); return false;}
            } else {back(); return false;}

        } else if (charAtual == 'c'){
            
            if (proxreserved('a')){
                if (proxreserved('r')){
                    estado = '8';//car
                    term += "car";
                    return true;

                } else if (charAtual == 's'){
                    if (proxreserved('e')){
                        estado = '8';//case
                        term += "case";
                        return true;

                    } else {back(); return false;}
                } else {back(); return false;}

            } else if (charAtual == 'd'){
                if(proxreserved('r')){
                    estado = '8';//cdr
                    term += "cdr";
                    return true;

                } else {back(); return false;}

            } else if (charAtual == 'o'){               
                if(proxreserved('n')){
                    if(proxreserved('d')){
                        estado = '8';//cond
                        term += "cond";
                        return true;

                    } else {back(); return false;}
                }else {back(); return false;}
            } else {back(); return false;}

        } else if (charAtual == 'd'){
            
            if(proxreserved('e')){ 
                if(proxreserved('f')){
                    if(proxreserved('i')){   
                        if(proxreserved('n')){       
                            if(proxreserved('e')){
                                estado = '8';//define
                                term += "define";
                                return true;

                            } else {back(); return false;}
                        } else {back(); return false;}

                    } else if (charAtual == 'u'){
                        if (proxreserved('n')){
                            estado = '8';//defun
                            term += "defun";
                            return true;

                        } else {back(); return false;}
                    } else {back(); return false;}
                } else {back(); return false;}
            } else {back(); return false;}

        } else if (charAtual == 'e'){
            
            if(proxreserved('q')){
                estado = '6';//eq
                term += "eq";
                return true;

            } else {back(); return false;}

        } else if (charAtual == 'g'){
            
            if(proxreserved('e')){
                if(proxreserved('q')){
                    estado = '6';//geq
                    term += "geq";
                    return true;

                } else {back(); return false;}
            } else {back(); return false;}

        } else if (charAtual == 'i'){
            
            if(proxreserved('f')){
                estado = '8';//if
                term += "if";
                return true;

            } else {back(); return false;}

        } else if (charAtual == 'l'){
            
            if(proxreserved('a')){
                if(proxreserved('m')){
                    if(proxreserved('b')){
                        if(proxreserved('d')){
                            if(proxreserved('a')){
                                estado = '8';//lambda
                                term += "lambda";
                                return true;

                            } else {back(); return false;}
                        } else {back(); return false;}
                    } else {back(); return false;}
                } else {back(); return false;}

            } else if (charAtual == 'e'){
                if(proxreserved('t')){
                    estado = '8';//let
                    term += "let";
                    return true;

                } else if (charAtual == 'q'){
                    estado = '6';//leq
                    term += "leq";
                    return true;

                } else {back(); return false;}

            } else if (charAtual == 'i'){
                if(proxreserved('s')){
                    if(proxreserved('t')){
                        estado = '8';//list
                        term += "list";
                        return true;

                    } else {back(); return false;}
                } else {back(); return false;}
            } else {back(); return false;}


        } else if (charAtual == 'n'){
            
            if(proxreserved('e')){
                if(proxreserved('q')){
                    estado = '6';//neq
                    term += "neq";
                    return true;

                } else {back(); return false;}

            } else if (charAtual == 'i'){
                
                if(proxreserved('l')){
                    estado = '8';//nil
                    term += "nil";
                    return true;

                } else {back(); return false;}
            } else {back(); return false;}

        } else if (charAtual == 'T'){
            
            estado = '8';//T
            term += 'T';
            return true;
        }

        return false;
    }

    private boolean proxreserved(char c){
        charAtual = prox();
        term += charAtual;
        return charAtual == c;
    }

}