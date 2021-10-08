package Lexico;

import java.nio.charset.*;
import java.nio.file.*;

import Exceptions.LexicalException;
//import java.util.LinkedList;


public class Leitor{
    private char[] texto;
    private char estado, charAtual;
    private int strLen;
    private int position;
    private String term;

    public Leitor(String filename) {
        try {
            String conteudo_txt;

            position = 0;
            term = "";

            conteudo_txt = new String(Files.readAllBytes(Paths.get(filename)),StandardCharsets.UTF_8);
            this.strLen = conteudo_txt.length();
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

            charAtual = this.prox();
            //System.out.println(String.format("current char: %s | current index: %d | current char[index]: %s", this.charAtual, this.position, this.texto[this.position]));
            //System.out.println("Estado: "+estado);
            //System.out.println("Posição: "+position);
            switch (estado) {
                case 'i':
                     
                    if(isreservada()){
                        
                    } else if(iscaracter(charAtual)){
                        estado = '1';
                        term += charAtual;
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
            
            if(proxreserved('t', 1)){
                if(proxreserved('o', 2)){
                    if(proxreserved('m', 3)){
                        estado = '8';//atom
                        term="";
                        term += "atom";
                        position += 3;
                        return true;

                    } else {; return false;}
                } else {; return false;}
            } else {; return false;}

        } else if (charAtual == 'c'){
            
            if (proxreserved('a', 1)){
                if (proxreserved('r', 2)){
                    estado = '8';//car
                    term="";
                    term += "car";
                    position += 2;
                    return true;

                } else if (proxreserved('s', 2)){
                    if (proxreserved('e', 3)){
                        estado = '8';//case
                        term="";
                        term += "case";
                        position += 3;
                        return true;

                    } else { return false;}
                } else { return false;}

            } else if (proxreserved('d', 1)){
                if(proxreserved('r', 2)){
                    estado = '8';//cdr
                    term="";
                    term += "cdr";
                    position += 2;
                    return true;

                } else { return false;}

            } else if (proxreserved('o', 1)){               
                if(proxreserved('n',2 )){
                    if(proxreserved('d', 3)){
                        estado = '8';//cond
                        term="";
                        term += "cond";
                        position += 3;
                        return true;

                    } else { return false;}
                }else { return false;}
            } else { return false;}

        } else if (charAtual == 'd'){
            if(proxreserved('e', 1)){ 
                if(proxreserved('f', 2 )){
                    if(proxreserved('i', 3)){   
                        if(proxreserved('n', 4)){       
                            if(proxreserved('e', 5)){
                                estado = '8';//define
                                term="";
                                term += "define";
                                position += 5;
                                return true;

                            } else { return false;}
                        } else { return false;}

                    } else if (proxreserved('u', 3)){
                        if (proxreserved('n', 4)){
                            estado = '8';//defun
                            term="";
                            term += "defun";
                            position += 4;
                            return true;

                        } else { return false;}
                    } else { return false;}
                } else { return false;}
            } else { return false;}

        } else if (charAtual == 'e'){
            
            if(proxreserved('q', 1)){
                estado = '6';//eq
                term="";
                term += "eq";
                position += 1;
                return true;

            } else { return false;}

        } else if (charAtual == 'g'){
            
            if(proxreserved('e', 1)){
                if(proxreserved('q', 2)){
                    estado = '6';//geq
                    term="";
                    term += "geq";
                    position += 2;
                    return true;

                } else { return false;}
            } else { return false;}

        } else if (charAtual == 'i'){
            
            if(proxreserved('f', 1)){
                estado = '8';//if
                term="";
                term += "if";
                position += 1;
                return true;

            } else { return false;}

        } else if (charAtual == 'l'){
            
            if(proxreserved('a', 1)){
                if(proxreserved('m', 2)){
                    if(proxreserved('b', 3)){
                        if(proxreserved('d', 4)){
                            if(proxreserved('a', 5)){
                                estado = '8';//lambda
                                term="";
                                term += "lambda";
                                position += 5;
                                return true;

                            } else { return false;}
                        } else { return false;}
                    } else { return false;}
                } else { return false;}

            } else if (proxreserved('e', 1)){
                if(proxreserved('t', 2)){
                    estado = '8';//let
                    term="";
                    term += "let";
                    position += 2;
                    return true;

                } else if (proxreserved('q', 2)){
                    estado = '6';//leq
                    term="";
                    term += "leq";
                    position += 2;
                    return true;

                } else { return false;}

            } else if (proxreserved('i', 1)){
                if(proxreserved('s', 2)){
                    if(proxreserved('t', 3)){
                        estado = '8';//list
                        term="";
                        term += "list";
                        position += 3;
                        return true;

                    } else { return false;}
                } else { return false;}
            } else { return false;}


        } else if (charAtual == 'n'){
            
            if(proxreserved('e', 1)){
                if(proxreserved('q', 2)){
                    estado = '6';//neq
                    term="";
                    term += "neq";
                    position += 2;
                    return true;

                } else { return false;}

            } else if (proxreserved('i', 1)){
                
                if(proxreserved('l', 2)){
                    estado = '8';//nil
                    term="";
                    term += "nil";
                    position += 2;
                    return true;

                } else { return false;}
            } else { return false;}

        } else if (charAtual == 'T'){
            
            estado = '8';//T
            term="";
            term += 'T';
            return true;
        }

        return false;
    }

    private boolean proxreserved(char c, int offset){

        //System.out.println(String.format("char: %s | comparator: %s", c, this.texto[(this.position - 1) + offset]));
        return this.texto[(this.position - 1) + offset] == c;
    }

}