import Exceptions.LexicalException;
import Lexico.*;

public class Main{
    public static void main(String[] args) {
        try{

        Leitor l = new Leitor("Lexico/txtEntrada.txt");
        Tokens TK = null;

        do{
            TK = l.proxToken();
            if(TK != null){
                System.out.println(TK);
            }
        }while(TK != null);

    } catch (LexicalException e){
        System.out.println("ERRO LEXICO: "+e.getMessage());
    } catch (Exception e){
        System.out.println("ERRO !");
    }
}
}