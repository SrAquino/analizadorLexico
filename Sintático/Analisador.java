package Sintático;

import Exceptions.SintaticException;
import Lexico.*;

import java.util.LinkedList;

public class Analisador{
    private LinkedList<Tokens> l;
    private Tokens tk;
    private int currentToken;
    
    public Analisador(LinkedList<Tokens> l){
        this.l = new LinkedList<Tokens>(l);
        this.currentToken = 0;
        tk = this.l.get(currentToken);
    }

    public void G(){

        while(l.size() > currentToken+1) {
        
            Delimitador("(");//Abre parenteses no início

            if(tk.getTipo()==Tokens.TK_Op_Ari){
                //System.out.println("G -> É um operador aritimético: "+tk);
                F();
            } else if (tk.getTipo()==Tokens.TK_WR){
                //System.out.println("G -> É uma palavra reservada: "+tk);
                Pr();
            } else {
                throw new SintaticException("Num é função num é operação, era pra ser o que esse "+tk.getType()+" ?");
            }

        Delimitador(")");
    
        }
    }

    private void F(){

        if(tk.getTipo()==Tokens.TK_Op_Ari){
            OPa();
            N();
            F();
        } else {
            N();
        }
   
    }

    private void Pr(){

        if(tk.getTipo()==Tokens.TK_WR){

            if (tk.getTexto().compareTo("defun") == 0){      
                proximoToken();
                if(tk.getTipo()==Tokens.TK_Id){//(defun nome
                    proximoToken();
                    Delimitador("(");
                    if (tk.getTipo()==Tokens.TK_Delimit && tk.getTexto().compareTo(")")==0){
                        proximoToken();
                    } else {
                        N();//(defun nome (1 parametro
                        do {
                            if(!(tk.getTipo()==Tokens.TK_Delimit)){//(defun nome (mais de um parametro
                                N();
                            } else {
                                break;
                            }
                        } while(true);
                    } Delimitador(")");//(defun nome (parametros)
                    
                }
                Delimitador("(");
                F();
                Delimitador(")");
       
            }/*^Caso seja defun*/ else {
                if (tk.getTexto().compareTo("if") == 0){
                    proximoToken();
                    Delimitador("(");//(if (
                    OPr();//(if (condição
                    N();
                    N();
                    Delimitador(")");

                    Delimitador("(");
                    F();//Caso verdadeiro 
                    Delimitador(")");

                    Delimitador("(");
                    F();//Caso falso  
                    Delimitador(")");                 
        
                } /*Caso seja if ^*/else {
                    proximoToken();
                
                    if(tk.getTipo()==Tokens.TK_Id){
                        proximoToken();
                        G();
                    } else { 
                        throw new SintaticException("Eu acho que uma função não pode ser chamada assim");
                    }

                }
            }

            
        }//^Caso seja uma palavra reservada
    }

    public void N(){

        if(tk.getTipo() != Tokens.TK_Id && tk.getTipo() != Tokens.TK_Number){
            throw new SintaticException("Véi, achei que ia vim um Identificador ou Número e brotou um "+tk.getType()+" do nada");
        }

        proximoToken();
    }

    public void OPa(){

        if(tk.getTipo() != Tokens.TK_Op_Ari){
            throw new SintaticException("Mano, esse "+tk.getType()+" não devia ser um operador não?");
        }

        proximoToken();
    }

    public void OPr(){

        if(tk.getTipo() != Tokens.TK_Op_Rel){
            throw new SintaticException("Mano, esse "+tk.getType()+" não devia ser um operador não?");
        }

        proximoToken();
    }

    public void Delimitador(String tipo/*Aberto ou Fechado*/){
        
        if(tk.getTipo() != Tokens.TK_Delimit || tk.getTexto().compareTo(tipo)==0){
            throw new SintaticException("Faltando o parenteses de novo...");
        }

        proximoToken();

    }
    
    private void proximoToken(){
        if(l.size() > currentToken+1){
            this.currentToken++;
            tk = this.l.get(currentToken);
            //System.out.println(currentToken+" :: "+tk);
        }
    }

}