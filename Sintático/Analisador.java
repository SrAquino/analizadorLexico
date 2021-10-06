package Sintático;

import Exceptions.SintaticException;
import Lexico.*;

public class Analisador{
    private Leitor l;
    private Tokens tk;
    
    public Analisador(Leitor l){
        this.l = l;
    }

    public void F(){
        N();
        Fl();
    }

    public void N(){
        tk = l.proxToken();
        
        if(tk == null){
            System.out.println("Arquivo vazio");
        }

        if(tk.getTipo() != Tokens.TK_Id && tk.getTipo() != Tokens.TK_Number){
            throw new SintaticException("Esperava um Identificador ou Número e encontrei um "+tk.getType());
        }
    }

    public void OP(){
        if(tk.getTipo() != 2){
            throw new SintaticException("Esperava um operador");
        }
    }

    public void Fl(){
        tk = l.proxToken();

        if(tk != null){
            OP();
            N();
            Fl();
        }
    }

}