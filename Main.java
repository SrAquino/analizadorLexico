import Exceptions.LexicalException;
import Exceptions.SintaticException;
import Lexico.*;
import Sintático.Analisador;
import java.util.LinkedList;

public class Main{
    public static void main(String[] args) {
        try{

        Leitor l = new Leitor("../txtEntrada.txt");
        LinkedList<Tokens> tokenList = new LinkedList<Tokens>();
        
        Tokens TK = null;

        do{
            TK = l.proxToken();
            if(TK != null){
                tokenList.add(TK);
                //System.out.println(TK);
            }
        }while(TK != null);

        Analisador a = new Analisador(tokenList);
        a.G();

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