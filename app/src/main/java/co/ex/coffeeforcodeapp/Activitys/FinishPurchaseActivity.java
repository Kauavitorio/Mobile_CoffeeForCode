package co.ex.coffeeforcodeapp.Activitys;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import co.ex.coffeeforcodeapp.Adapters.LoadingDialog;
import co.ex.coffeeforcodeapp.Api.ShoppingCart.DtoShoppingCart;
import co.ex.coffeeforcodeapp.Api.ShoppingCart.ShoppingCartService;
import co.ex.coffeeforcodeapp.Api.User.DtoUsers;
import co.ex.coffeeforcodeapp.Api.User.UsersService;
import co.ex.coffeeforcodeapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FinishPurchaseActivity extends AppCompatActivity {
    TextView txtAddress_purchase;
    TextView txtCartSize_purchase;
    CardView infoCartSize_purchase, btnGoBackPurchase;

    //  User information
    int id_user, partner;
    String nm_user, email_user, phone_user, zipcode, address_user, complement, img_user, cpf_user, partner_Startdate;
    LoadingDialog loadingDialog = new LoadingDialog(FinishPurchaseActivity.this);

    //  Retrofit's
    String baseurl = "https://coffeeforcode.herokuapp.com/";
    final Retrofit retrofitShoppingCart = new Retrofit.Builder()
            .baseUrl( baseurl + "shoppingcart/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    final Retrofit retrofitUser = new Retrofit.Builder()
            .baseUrl("https://coffeeforcode.herokuapp.com/user/info/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_finish_purchase);
        txtAddress_purchase = findViewById(R.id.txtAddress_purchase);
        txtCartSize_purchase = findViewById(R.id.txtCartSize_purchase);
        infoCartSize_purchase = findViewById(R.id.infoCartSize_purchase);
        btnGoBackPurchase = findViewById(R.id.btnGoBackPurchase);

        // get some information
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        email_user = bundle.getString("email_user");

        loadUserInformation();
        GetCartSize();
        Toast.makeText(this, R.string.under_development, Toast.LENGTH_LONG).show();

        btnGoBackPurchase.setOnClickListener(v -> goTo_main());
    }

    public void loadUserInformation(){
        loadingDialog.startLoading();
        UsersService usersService = retrofitUser.create(UsersService.class);
        Call<DtoUsers> call = usersService.infoUser(email_user);
        call.enqueue(new Callback<DtoUsers>() {
            @Override
            public void onResponse(@NotNull Call<DtoUsers> call, @NotNull Response<DtoUsers> response) {
                if (response.code() == 200){
                    loadingDialog.dimissDialog();
                    id_user = response.body().getId_user();
                    nm_user = response.body().getNm_user();
                    phone_user = response.body().getPhone_user();
                    cpf_user = response.body().getCpf_user();
                    partner = response.body().getPartner();
                    partner_Startdate = response.body().getPartner_Startdate();
                    address_user = response.body().getAddress_user();
                    complement = response.body().getComplement();
                    zipcode = response.body().getZipcode();
                    img_user = response.body().getImg_user();
                    if (address_user == null || address_user.equals(" ") || address_user.length() < 3){
                        AlertDialog.Builder alertAddress = new AlertDialog.Builder(FinishPurchaseActivity.this)
                                .setTitle(R.string.addres_no_registred)
                                .setCancelable(false)
                                .setMessage(R.string.desc_purchase_no_address)
                                .setPositiveButton(R.string.singup, (dialog, which) -> {
                                    Intent irpara_perfil = new Intent(FinishPurchaseActivity.this, RegisterAddressActivity.class);
                                    irpara_perfil.putExtra("id_user", id_user);
                                    irpara_perfil.putExtra("email_user", email_user);
                                    irpara_perfil.putExtra("nm_user", nm_user);
                                    irpara_perfil.putExtra("cpf_user", cpf_user);
                                    irpara_perfil.putExtra("phone_user", phone_user);
                                    irpara_perfil.putExtra("address_user", address_user);
                                    irpara_perfil.putExtra("img_user", img_user);
                                    irpara_perfil.putExtra("partner", partner);
                                    irpara_perfil.putExtra("partner_Startdate", partner_Startdate);
                                    startActivity(irpara_perfil);
                                    finish();
                                })
                                .setNeutralButton(R.string.register_later, (dialog, which) -> goTo_main());
                        alertAddress.show();

                    }else{
                        String fullAddress = address_user + ", " + complement + ", " + zipcode;
                        txtAddress_purchase.setText(fullAddress);
                    }
                }else{
                    loadingDialog.dimissDialog();
                    Toast.makeText(FinishPurchaseActivity.this, "Error" + response.body() + "\n" + response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NotNull Call<DtoUsers> call, @NotNull Throwable t) {
                loadingDialog.dimissDialog();
                Toast.makeText(FinishPurchaseActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void goTo_main() {
        Intent GoBack_ToMain = new Intent(FinishPurchaseActivity.this, MainActivity.class);
        GoBack_ToMain.putExtra("id_user", id_user);
        GoBack_ToMain.putExtra("nm_user", nm_user);
        GoBack_ToMain.putExtra("email_user", email_user);
        GoBack_ToMain.putExtra("phone_user", phone_user);
        GoBack_ToMain.putExtra("zipcode", zipcode);
        GoBack_ToMain.putExtra("address_user", address_user);
        GoBack_ToMain.putExtra("complement", complement);
        GoBack_ToMain.putExtra("img_user", img_user);
        GoBack_ToMain.putExtra("address_user", address_user);
        GoBack_ToMain.putExtra("cpf_user", cpf_user);
        GoBack_ToMain.putExtra("partner", partner);
        GoBack_ToMain.putExtra("partner_Startdate", partner_Startdate);
        GoBack_ToMain.putExtra("statusavisoend","desativado");
        startActivity(GoBack_ToMain);
        finish();
    }

    private void GetCartSize() {
        infoCartSize_purchase.setVisibility(View.GONE);
        ShoppingCartService shoppingCartService = retrofitShoppingCart.create(ShoppingCartService.class);
        Call<DtoShoppingCart> cartCall = shoppingCartService.getCartInfomration(email_user);
        cartCall.enqueue(new Callback<DtoShoppingCart>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<DtoShoppingCart> call, @NotNull Response<DtoShoppingCart> response) {
                if (response.code() == 412){
                    infoCartSize_purchase.setVisibility(View.GONE);
                    txtCartSize_purchase.setText("");
                }else if(response.code() == 200){
                    assert response.body() != null;
                    txtCartSize_purchase.setText(response.body().getLength() + "");
                    infoCartSize_purchase.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(@NotNull Call<DtoShoppingCart> call, @NotNull Throwable t) {
                Toast.makeText(FinishPurchaseActivity.this, R.string.error_to_find_your_cart, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        goTo_main();
    }
}