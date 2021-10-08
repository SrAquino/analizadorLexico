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
    }

    public void G(){

        tk = this.l.get(currentToken);

        if(tk.getTipo()==Tokens.TK_Delimit){
            this.currentToken++;
            tk = this.l.get(currentToken);

            if(tk.getTipo()==Tokens.TK_Op_Ari){
                F();
            } else if (tk.getTipo()==Tokens.TK_WR){
                Pr();
            } else {
                throw new SintaticException("Num é função num é operação, é o que então? "+tk.getType()+" ?");
            }

        } else {
            throw new SintaticException("PA RÊN TE SE, CADÊ ??? Só to vendo um "+tk.getType());}
    }

    private void F(){

        tk = this.l.get(currentToken);

        if(tk.getTipo()==Tokens.TK_Op_Ari){
            OPa();
            N();
            F();
        } else {
            N();
        }
   
    }

    private void Pr(){

       proximoToken();

        if(tk.getTipo()==Tokens.TK_WR){

            if (tk.getTexto().compareTo("defun") == 0){      
                proximoToken();
            if(tk.getTipo()==Tokens.TK_Id){
                proximoToken();
                if(tk.getTipo()==Tokens.TK_Delimit){
                    proximoToken();
                if(tk.getTipo()==Tokens.TK_Id){
                    proximoToken();
                if(tk.getTipo()==Tokens.TK_Id){
                    proximoToken();
                if(tk.getTipo()==Tokens.TK_Delimit){
                    proximoToken();
                    if(tk.getTipo()==Tokens.TK_Delimit){
                        F();
                    if(tk.getTipo()==Tokens.TK_Delimit){

                    }
                }}}}}

                } else {
                    throw new SintaticException("E o nome da função é oq? Eu advinho ou deixo esse "+tk.getType());
                }
            } else

            if(tk.getTipo()==Tokens.TK_Id){
                this.currentToken++;
                G();
            } else { 
                throw new SintaticException("");
            }
        }
    }

    public void N(){
  
        tk = this.l.get(currentToken);

        if(tk.getTipo() != Tokens.TK_Id && tk.getTipo() != Tokens.TK_Number){
            throw new SintaticException("Véi, achei que ia vim um Identificador ou Número e brotou um "+tk.getType()+" do nada");
        }
        this.currentToken++;
    }

    public void OPa(){

        tk = this.l.get(currentToken);

        if(tk.getTipo() != Tokens.TK_Op_Ari){
            throw new SintaticException("Mano, esse "+tk.getType()+" não devia ser um operador não?");
        }

        this.currentToken++;
    }

    private void proximoToken(){
        this.currentToken++;
        tk = this.l.get(currentToken);
    }

}