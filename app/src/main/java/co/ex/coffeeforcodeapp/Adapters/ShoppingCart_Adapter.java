package co.ex.coffeeforcodeapp.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import co.ex.coffeeforcodeapp.Api.ShoppingCart.DtoShoppingCart;
import co.ex.coffeeforcodeapp.R;

public class ShoppingCart_Adapter extends RecyclerView.Adapter<ShoppingCart_Adapter.MyHolderShoppingCart> {
    ArrayList<DtoShoppingCart> dtoShoppingCarts;
    TextView txt_total;
    float total_price;

    public ShoppingCart_Adapter(TextView txt_total, ArrayList<DtoShoppingCart> arrayListDto) {
        this.dtoShoppingCarts = arrayListDto;
        this.txt_total = txt_total;
    }

    @NonNull
    @Override
    public MyHolderShoppingCart onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_products_shoppingcart, parent, false);
        return new ShoppingCart_Adapter.MyHolderShoppingCart(listItem);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyHolderShoppingCart holder, int position) {
        holder.img_prod_shoppingcart.setImageBitmap(dtoShoppingCarts.get(position).getImg_prod_cart());
        holder.txt_name_prod_shoopingcart.setText(dtoShoppingCarts.get(position).getNm_prod());
        holder.txt_full_price_prod_shoppingcart.setText(dtoShoppingCarts.get(position).getFull_price_prod() + "");
        holder.txtQt_prod_shoppingcart_ad.setText(dtoShoppingCarts.get(position).getQt_prod() + "");
        holder.card_adapter_shoppingcart.setElevation(20);
        total_price += dtoShoppingCarts.get(position).getFull_price_prod();
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("pt", "BR"));
        numberFormat.setMaximumFractionDigits(2);
        txt_total.setText("Total R$"+ numberFormat.format(total_price) );
    }

    @Override
    public int getItemCount() {
        return dtoShoppingCarts.size();
    }

    class MyHolderShoppingCart extends RecyclerView.ViewHolder{
        TextView txt_name_prod_shoopingcart, txt_full_price_prod_shoppingcart, txtQt_prod_shoppingcart_ad;
        ImageView img_prod_shoppingcart;
        CardView card_adapter_shoppingcart;

        public MyHolderShoppingCart(@NonNull View itemView) {
            super(itemView);
            txt_name_prod_shoopingcart = itemView.findViewById(R.id.txt_name_prod_shoopingcart);
            txt_full_price_prod_shoppingcart = itemView.findViewById(R.id.txt_full_price_prod_shoppingcart);
            txtQt_prod_shoppingcart_ad = itemView.findViewById(R.id.txtQt_prod_shoppingcart_ad);
            img_prod_shoppingcart = itemView.findViewById(R.id.img_prod_shoppingcart);
            card_adapter_shoppingcart = itemView.findViewById(R.id.card_adapter_shoppingcart);
        }
    }
}
