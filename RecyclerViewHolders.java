package ar.com.nbcargo.nbcargo_choferes;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RecyclerViewHolders extends RecyclerView.ViewHolder {

    public View hora;
    public TextView hora_txt;
    public CardView carril1_card;
    public View patente1;
    public TextView disponible1;
    public ImageView i_patente1;
    public TextView t_patente11;
    public TextView chofer1;
    public CardView carril2_card;
    public View patente2;
    public TextView disponible2;
    public ImageView i_patente2;
    public TextView t_patente12;
    public TextView chofer2;

    public RecyclerViewHolders(View itemView) {
        super(itemView);

        hora = itemView.findViewById(R.id.comb_item_hora);
        carril1_card = itemView.findViewById(R.id.comb_item_card1);
        patente1 = itemView.findViewById(R.id.comb_item_patente1);
        disponible1 = itemView.findViewById(R.id.comb_item_disponible1);
        i_patente1 = itemView.findViewById(R.id.comb_item_imagenPatente1);
        t_patente11 = itemView.findViewById(R.id.comb_item_textoPatente1);
        chofer1 = itemView.findViewById(R.id.comb_item_chofer1);
        carril2_card = itemView.findViewById(R.id.comb_item_card2);
        patente2 = itemView.findViewById(R.id.comb_item_patente2);
        disponible2 = itemView.findViewById(R.id.comb_item_disponible2);
        i_patente2 = itemView.findViewById(R.id.comb_item_imagenPatente2);
        t_patente12 = itemView.findViewById(R.id.comb_item_textoPatente2);
        chofer2 = itemView.findViewById(R.id.comb_item_chofer2);
        hora_txt = itemView.findViewById(R.id.txt_card_hora);
    }

}
