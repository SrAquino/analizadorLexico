package Lexico;

public class Tokens {
    public static final int TK_Id = 0;
    public static final int TK_Number = 1;
    public static final int TK_Op_Ari = 2;
    public static final int TK_Op_Rel = 3;
    public static final int TK_Atri = 4;

    private int tipo;
    private String texto;

    public Tokens(int tipo, String texto){
        this.tipo = tipo;
        this.texto = texto;
    }
    public Tokens(){
    }

    public int getTipo(){
        return this.tipo;
    }

    public String getType(){
        switch(this.getTipo()){
            case 0:return "Identificador";

            case 1:return "Number";

            case 2:return "Operador Aritimético";
            
            case 3:return "Operador Relacional";

            case 4:return "Atribuição";

            default: return "ERROR";
        }
    }

    public String getTexto(){
        return this.texto;
    }

    public void setTipo(int tipo){
        this.tipo = tipo;
    }

    public void setTexto(String texto){
        this.texto = texto;
    }

    public String toString(){
        return "Token tipo: " +getType()+", texto: "+texto;
    }
}