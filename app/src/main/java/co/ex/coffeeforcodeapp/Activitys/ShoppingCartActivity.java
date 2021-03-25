package co.ex.coffeeforcodeapp.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import org.jetbrains.annotations.NotNull;

import co.ex.coffeeforcodeapp.Api.ShoppingCart.AsyncShoppingCart;
import co.ex.coffeeforcodeapp.Api.ShoppingCart.DtoShoppingCart;
import co.ex.coffeeforcodeapp.Api.ShoppingCart.ShoppingCartService;
import co.ex.coffeeforcodeapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ShoppingCartActivity extends AppCompatActivity{
    RecyclerView recycler_shoppingcart;
    TextView txt_total;
    LottieAnimationView btngoback_shoppingcart;
    CardView btnBuy_shoppingcart;
    ConstraintLayout base_empty_cart;
    ScrollView swipe_recycler_shoppingcart;
    String baseurl = "https://coffeeforcode.herokuapp.com/";
    CardView infoCartSize_shoppingcart;
    TextView txtCartSize_shoppingcart;
    Handler timer = new Handler();

    //  User information
    String email_user;

    //  Retrofit's
    final Retrofit retrofitShoppingCart = new Retrofit.Builder()
            .baseUrl( baseurl + "shoppingcart/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppingcart);
        recycler_shoppingcart = findViewById(R.id.recycler_shoppingcart);
        txt_total = findViewById(R.id.txt_total);
        infoCartSize_shoppingcart = findViewById(R.id.infoCartSize_shoppingcart);
        txtCartSize_shoppingcart = findViewById(R.id.txtCartSize_shoppingcart);
        btngoback_shoppingcart = findViewById(R.id.btngoback_shoppingcart);
        btnBuy_shoppingcart = findViewById(R.id.btnBuy_shoppingcart);
        swipe_recycler_shoppingcart = findViewById(R.id.swipe_recycler_shoppingcart);
        base_empty_cart = findViewById(R.id.base_empty_cart);
        btnBuy_shoppingcart.setElevation(20);

        // get some information
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        email_user = bundle.getString("email_user");

        loadCart();
        GetCartSize();

        btnBuy_shoppingcart.setOnClickListener(v -> {
            btnBuy_shoppingcart.setElevation(0);
            recycler_shoppingcart.scrollToPosition(recycler_shoppingcart.getAdapter().getItemCount() - 1);
            timer.postDelayed(() -> btnBuy_shoppingcart.setElevation(20),1500);
        });

        btngoback_shoppingcart.setOnClickListener(v -> finish());
    }

    private void loadCart() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(ShoppingCartActivity.this);
        recycler_shoppingcart.setLayoutManager(layoutManager);
        AsyncShoppingCart asyncShoppingCart = new AsyncShoppingCart(recycler_shoppingcart, email_user, txt_total, ShoppingCartActivity.this);
        asyncShoppingCart.execute();
    }

    private void GetCartSize() {
        infoCartSize_shoppingcart.setVisibility(View.GONE);
        ShoppingCartService shoppingCartService = retrofitShoppingCart.create(ShoppingCartService.class);
        Call<DtoShoppingCart> cartCall = shoppingCartService.getCartInfomration(email_user);
        cartCall.enqueue(new Callback<DtoShoppingCart>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<DtoShoppingCart> call, @NotNull Response<DtoShoppingCart> response) {
                if (response.code() == 412){
                    infoCartSize_shoppingcart.setVisibility(View.GONE);
                    txtCartSize_shoppingcart.setText("");
                    recycler_shoppingcart.setVisibility(View.GONE);
                    base_empty_cart.setVisibility(View.VISIBLE);
                }else if(response.code() == 200){
                    assert response.body() != null;
                    txtCartSize_shoppingcart.setText(response.body().getLength() + "");
                    infoCartSize_shoppingcart.setVisibility(View.VISIBLE);
                    recycler_shoppingcart.setVisibility(View.VISIBLE);
                    base_empty_cart.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(@NotNull Call<DtoShoppingCart> call, @NotNull Throwable t) {
                Toast.makeText(ShoppingCartActivity.this, R.string.error_to_find_your_cart, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}