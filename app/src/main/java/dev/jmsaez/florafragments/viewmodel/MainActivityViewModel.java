package dev.jmsaez.florafragments.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import dev.jmsaez.florafragments.model.entity.DeleteResponse;
import dev.jmsaez.florafragments.model.entity.Flora;
import dev.jmsaez.florafragments.model.entity.Flora;
import dev.jmsaez.florafragments.model.entity.RowsResponse;
import dev.jmsaez.florafragments.model.repository.Repository;

public class MainActivityViewModel extends AndroidViewModel {

    Repository repository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.repository = new Repository(application);
    }

    public void deleteFlora(long id) {
        repository.deleteFlora(id);
    }

    public void getFlora(long id) {
        repository.getFlora(id);
    }

    public void getFlora() {
        repository.getFlora();
    }

    public void createFlora(Flora flora) {
        repository.createFlora(flora);
    }

    public void editFlora(long id, Flora flora) {
        repository.editFlora(id, flora);
    }

    public MutableLiveData<ArrayList<Flora>> getFloraLiveData() {
        return repository.getFloraLiveData();
    }


    public MutableLiveData<DeleteResponse> getDeleteLiveData(){
        return repository.getDeleteLiveData();
    }

    public MutableLiveData<Integer> getSecondDelete() {
        return repository.getSecondDelete();
    }

    public MutableLiveData<RowsResponse> getEditLiveData() {
        return repository.getEditLiveData();
    }
}
