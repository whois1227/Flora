package dev.jmsaez.florafragments.model.repository;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import dev.jmsaez.florafragments.model.api.FloraClient;
import dev.jmsaez.florafragments.model.entity.CreateResponse;
import dev.jmsaez.florafragments.model.entity.DeleteResponse;
import dev.jmsaez.florafragments.model.entity.Flora;
import dev.jmsaez.florafragments.model.entity.ImageRowResponse;
import dev.jmsaez.florafragments.model.entity.Imagen;
import dev.jmsaez.florafragments.model.entity.RowsResponse;
import dev.jmsaez.florafragments.view.adapter.FloraAdapter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {

    private Context context;
    private FloraClient floraClient;
    private MutableLiveData<ArrayList<Flora>> floraLiveData = new MutableLiveData<>();
    private MutableLiveData<Long> addFloraLiveData = new MutableLiveData<>();
    private MutableLiveData<Long> addImagenLiveData = new MutableLiveData<>();
    private MutableLiveData<Imagen> oneImageLiveData = new MutableLiveData<>();
    private MutableLiveData<ImageRowResponse> imagesLiveData = new MutableLiveData<>();
    private MutableLiveData<DeleteResponse> deleteLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> secondDelete = new MutableLiveData<>();
    private MutableLiveData<RowsResponse> editLiveData = new MutableLiveData<>();

    public Repository(Context context){
        this.context = context;
        this.floraClient = getFloraClient();
    }

    private FloraClient getFloraClient(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://informatica.ieszaidinvergeles.org:10016/AD/felixRDLFapp/public/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return floraClient = retrofit.create(FloraClient.class);
    }

    public void deleteFlora(long id){
        Call<DeleteResponse> delete = floraClient.deleteFlora(id);
        delete.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                //deleteLiveData.setValue(response.body().intValue());
                secondDelete.setValue(response.body().rows);
                Log.v(":::DELETE", response.body()+"");
                Log.v(":::DELETELD", secondDelete.getValue()+"");
            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                Log.v(":::DELETEFail", t.toString());
            }
        });
    }

    public void getFlora(long id){
        floraClient.getFlora(id);
    }

    public void getFlora(){
        Call<ArrayList<Flora>> call = floraClient.getFlora();
        call.enqueue(new Callback<ArrayList<Flora>>() {
            @Override
            public void onResponse(Call<ArrayList<Flora>> call, Response<ArrayList<Flora>> response) {
                floraLiveData.setValue(response.body());

            }
            @Override
            public void onFailure(Call<ArrayList<Flora>> call, Throwable t) {

            }
        });
    }

    public void createFlora(Flora flora){
        Call<CreateResponse> call = floraClient.createFlora(flora);
        call.enqueue(new Callback<CreateResponse>() {
            @Override
            public void onResponse(Call<CreateResponse> call, Response<CreateResponse> response) {
                addFloraLiveData.setValue(response.body().id);
            }

            @Override
            public void onFailure(Call<CreateResponse> call, Throwable t) {

            }
        });

    }

    public void editFlora(long id, Flora flora){
        Call<RowsResponse> edit = floraClient.editFlora(id, flora);
        edit.enqueue(new Callback<RowsResponse>() {
            @Override
            public void onResponse(Call<RowsResponse> call, Response<RowsResponse> response) {
                editLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<RowsResponse> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<ArrayList<Flora>> getFloraLiveData(){
        return floraLiveData;
    }

    public MutableLiveData<Long> getAddFloraLiveData(){
        return  addFloraLiveData;
    }

    public void saveImagen(Intent intent, Imagen imagen) {
        String nombre = "xyzyz.abc";
        copyData(intent, nombre);
        File file = new File(context.getExternalFilesDir(null), nombre);
        Log.v("xyzyx", file.getAbsolutePath());
        subirImagen(file, imagen);
    }

    private void subirImagen(File file, Imagen imagen) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("photo", imagen.nombre+".jpg", requestFile);
        Call<Long> call = floraClient.subirImagen(body, imagen.idflora, imagen.descripcion);
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                addImagenLiveData.setValue(response.body());
                Log.v("xyzyx", "ok");
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.v("xyzyx", "error");
            }
        });
    }

    private boolean copyData(Intent data, String name) {
        Log.v("xyzyx", "copyData");
        boolean result = true;
        Uri uri = data.getData();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = context.getContentResolver().openInputStream(uri);
            out = new FileOutputStream(new File(context.getExternalFilesDir(null), name));
            byte[] buffer = new byte[1024];
            int len;
            int cont = 0;
            while ((len = in.read(buffer)) != -1) {
                cont++;
                Log.v("xyzyx", "copyData" + cont);
                out.write(buffer, 0, len);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            result = false;
            Log.v("xyzyx", e.toString());
        }
        return result;
    }



    public void getImages(long id){
        Call<ImageRowResponse> images = floraClient.getImages(id);
        images.enqueue(new Callback<ImageRowResponse>() {
            @Override
            public void onResponse(Call<ImageRowResponse> call, Response<ImageRowResponse> response) {
                imagesLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ImageRowResponse> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<RowsResponse> getEditLiveData() {
        return editLiveData;
    }
    public MutableLiveData<ImageRowResponse> getImagesLiveData(){
        return this.imagesLiveData;
    }

    public MutableLiveData<DeleteResponse> getDeleteLiveData(){
        return this.deleteLiveData;
    }

    public MutableLiveData<Integer> getSecondDelete() {
        return secondDelete;
    }
}
