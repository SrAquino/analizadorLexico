package Sintático;

import Exceptions.SintaticException;
import Lexico.*;

import java.util.LinkedList;

public class Analisador{
    private LinkedList<Tokens> l;
    private Tokens tk;
    private int currentToken;
    
    public Analisador(LinkedList<Tokens> l){
        //this.l = new LinkdList<Tokens>(l);
        this.currentToken = 0;
    }

    public void F(){
        N();
        Fl();
    }

    public void N(){
        tk = this.l.get(currentToken);
        
        if(tk == null){
            System.out.println("Arquivo vazio");
        }

        if(tk.getTipo() != Tokens.TK_Id && tk.getTipo() != Tokens.TK_Number){
            throw new SintaticException("Esperava um Identificador ou Número e encontrei um "+tk.getType());
        }
        this.currentToken++;
    }

    public void OP(){
        if(tk.getTipo() != 2){
            throw new SintaticException("Esperava um operador");
        }
    }

    public void Fl(){
        tk = this.l.get(currentToken);

        if(tk != null){
            OP();
            N();
            Fl();
        }
        this.currentToken++;
    }

}