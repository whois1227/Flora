package dev.jmsaez.florafragments.viewmodel;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import dev.jmsaez.florafragments.model.entity.ImageRowResponse;
import dev.jmsaez.florafragments.model.entity.Imagen;
import dev.jmsaez.florafragments.model.repository.Repository;

public class AddImagenViewModel extends AndroidViewModel {

    private Repository repository;

    public AddImagenViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public MutableLiveData<Long> getAddImagenLiveData() {
        return repository.getAddFloraLiveData();
    }

    public void saveImagen(Intent intent, Imagen imagen) {
        repository.saveImagen(intent, imagen);
    }

    public void getImages(long id){
        repository.getImages(id);
    }

    public MutableLiveData<ImageRowResponse> getImagesLiveData(){
        return repository.getImagesLiveData();
    }
}
