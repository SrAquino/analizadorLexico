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
        if(tk.getTipo()==Tokens.TK_Delimit && tk.getTexto().compareTo("(")==0){//Abre parenteses no início
            proximoToken();

            if(tk.getTipo()==Tokens.TK_Op_Ari){
                System.out.println("G -> É um operador aritimético: "+tk);
                F();
            } else if (tk.getTipo()==Tokens.TK_WR){
                System.out.println("G -> É uma palavra reservada: "+tk);
                Pr();
            } else {
                throw new SintaticException("Num é função num é operação, era pra ser o que esse "+tk.getType()+" ?");
            }

        } else {
            throw new SintaticException("PA RÊN TE SE abrindo, CADÊ ??? Só to vendo um "+tk.getTexto());
        }

        proximoToken();

        if(tk.getTipo()==Tokens.TK_Delimit){// Fecha parenteses no final
            proximoToken();
        } else {
            throw new SintaticException("O programa termina nesse "+tk.getTexto()+" ?");
        }
    
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
                        if(tk.getTipo()==Tokens.TK_Delimit){//(defun nome (
                            proximoToken();
                                if(tk.getTipo()==Tokens.TK_Id || tk.getTipo()==Tokens.TK_Number ){//(defun nome (1 parametro
                                    while(true){
                                        if(tk.getTipo()==Tokens.TK_Id || tk.getTipo()==Tokens.TK_Number){//(defun nome (mais de um parametro
                                        proximoToken();
                                        } else {////(defun nome (sem parametro
                                            break;
                                        }
                                    }
                                } 
                                
                                if(tk.getTipo()==Tokens.TK_Delimit){////(defun nome ()
                                    proximoToken();
                                } else {
                                    throw new SintaticException("Esse "+tk.getTexto()+" era pra ser o nome do parâmetro?");
                                }                
                    if(tk.getTipo()==Tokens.TK_Delimit){////(defun nome () (
                        proximoToken();
                            F();
                        if(tk.getTipo()==Tokens.TK_Delimit){} else {
                            throw new SintaticException("E tua função acaba onde? nesse "+tk.getTexto()+"?");
                        }
                    }
                }
            

                } else {
                    throw new SintaticException("E o nome da função é oq? Eu advinho ou deixo esse "+tk.getType());
                }
            }/*^Caso seja defun*/ else

            if(tk.getTipo()==Tokens.TK_Id){
                this.currentToken++;
                G();
            } else { 
                throw new SintaticException("");
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

    private void proximoToken(){
        if(l.size() > currentToken+1){
            this.currentToken++;
            tk = this.l.get(currentToken);
            System.out.println(currentToken+" :: "+tk);
        }
    }

}