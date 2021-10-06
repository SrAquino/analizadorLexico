import Exceptions.LexicalException;
import Exceptions.SintaticException;
import Lexico.*;
import Sintático.Analisador;

public class Main{
    public static void main(String[] args) {
        try{

        Leitor l = new Leitor("../Lexico/txtEntrada.txt");
        
        Tokens TK = null;

        do{
            TK = l.proxToken();
            if(TK != null){
                System.out.println(TK);
            }
        }while(TK != null);

        l = new Leitor("../Lexico/txtEntrada.txt");
        Analisador a = new Analisador(l);
        a.F();

        System.out.println("Parabéns, Você não escreveu nada errado!");

    } catch (LexicalException e){
        System.out.println("ERRO LEXICO: "+e.getMessage());
    } catch (SintaticException e){
        System.out.println("ERRO SINTÁTICO: "+e.getMessage());
    } catch (Exception e){
        System.out.println("ERRO! nenhuma solução encontrada");
    }
}
}