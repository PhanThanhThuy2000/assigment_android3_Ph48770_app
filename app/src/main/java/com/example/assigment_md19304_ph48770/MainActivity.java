package com.example.assigment_md19304_ph48770;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
String TAG = "main";
    ListView lvMain;
    List<CarModel> listCarModel;
    CarAdapter carAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lvMain = findViewById(R.id.listviewMain);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);

        Call<List<CarModel>> call = apiService.getCars();
        call.enqueue(new Callback<List<CarModel>>() {
            @Override
            public void onResponse(Call<List<CarModel>> call, Response<List<CarModel>> response) {
                if (response.isSuccessful()) {
                    listCarModel = response.body();

                    carAdapter = new CarAdapter(getApplicationContext(), listCarModel);

                    lvMain.setAdapter(carAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<CarModel>> call, Throwable t) {
                Log.e("Main",t.getMessage());
            }
        });
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarModel xeMoi = new CarModel(null, "xe2", 2023, "Toyota",20000);

                Call<List<CarModel>> callAddXe = apiService.createCars(xeMoi);

                callAddXe.enqueue(new Callback<List<CarModel>>() {
                    @Override
                    public void onResponse(Call<List<CarModel>> call, Response<List<CarModel>> response) {
                        if (response.isSuccessful()) {

                            listCarModel.clear();

                            listCarModel.addAll(response.body());

                            carAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CarModel>> call, Throwable t) {
                        Log.e("Main", t.getMessage());
                    }
                });
            }
        });

        lvMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Lấy đối tượng CarModel tại vị trí cần xóa
                CarModel xeCanXoa = listCarModel.get(position);

                // Gọi API để xóa
                Call<List<CarModel>> callXoaXe = apiService.delete(xeCanXoa.get_id());
                Log.d(TAG, "onCreate: 0");

                callXoaXe.enqueue(new Callback<List<CarModel>>() {
                    @Override
                    public void onResponse(Call<List<CarModel>> call, Response<List<CarModel>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Xóa danh sách cũ và cập nhật danh sách mới
                            listCarModel.clear();
                            Log.d(TAG, "onResponse: 1");
                            listCarModel.addAll(response.body());
                            Log.d(TAG, "onResponse: 2");

                            carAdapter.notifyDataSetChanged();
                            Log.d(TAG, "onResponse: 3");

                        }
                    }

                    @Override
                    public void onFailure(Call<List<CarModel>> call, Throwable t) {
                        // Xử lý khi API call thất bại
                        t.printStackTrace();
                    }
                });

                // Trả về true để xử lý sự kiện long click
                return true;
            }
        });


    }
}




