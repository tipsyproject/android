package tipsy.commun.commerce;


/**
 * Created by valoo on 04/01/14.
 */
public class Item{
    private Article article;
    private int quantite;

    public Item(Article a, int q){
        article = a;
        quantite = q;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public int getPrixTotal(){
        return article.getPrix() * quantite;
    }

    @Override
    public boolean equals(Object o){
        return this.article.equals(((Item) o).getArticle());
    }

    @Override
    public int hashCode(){
        return article.getID().hashCode();
    }
}
