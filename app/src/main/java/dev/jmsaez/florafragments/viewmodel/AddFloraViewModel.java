package dev.jmsaez.florafragments.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import dev.jmsaez.florafragments.model.entity.Flora;
import dev.jmsaez.florafragments.model.repository.Repository;

public class AddFloraViewModel extends AndroidViewModel {

    private Repository repository;

    public AddFloraViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public void createFlora(Flora flora) {
        repository.createFlora(flora);
    }

    public MutableLiveData<Long> getAddFloraLiveData() {
        return repository.getAddFloraLiveData();
    }
}
