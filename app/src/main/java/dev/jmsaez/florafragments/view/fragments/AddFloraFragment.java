package dev.jmsaez.florafragments.view.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;

import dev.jmsaez.florafragments.R;
import dev.jmsaez.florafragments.model.entity.Flora;
import dev.jmsaez.florafragments.model.entity.Imagen;
import dev.jmsaez.florafragments.viewmodel.AddFloraViewModel;
import dev.jmsaez.florafragments.viewmodel.AddImagenViewModel;


public class AddFloraFragment extends Fragment {

    private TextInputEditText etName, etFamilia, etIdentificacion, etAltitud, etHabitat, etFitosociologia, etBiotipo,
            etBiologiaReprod, etFloracion, etFructificacion, etExprSex, etPolinizacion, etDispersion, etNumCromo,
            etReprAsex, etDistribucion, etBiologia, etDemografia, etAmenazas, etMedidas;
    private TextInputLayout lyNombreFlora;
    Button btImg;
    private Toolbar toolbar;
    private AddFloraViewModel afvm;
    private ActivityResultLauncher<Intent> launcher;
    private Intent resultadoImagen = null;
    private AddImagenViewModel aivm;
    private ImageView ivAddFlora;
    private MutableLiveData<Uri> image;
    public AddFloraFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_flora, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialize(view);
        navigation(view);
    }

    private void initialize(View view) {

        launcher = getLauncher();
        afvm = new ViewModelProvider(this).get(AddFloraViewModel.class);
        aivm = new ViewModelProvider(this).get(AddImagenViewModel.class);
        toolbar = view.findViewById(R.id.toolbar3);
        toolbar.inflateMenu(R.menu.add_menu);
        toolbar.setOnMenuItemClickListener( item -> {return onOptionsItemSelected(item);});

        image = new MutableLiveData<>();
        ivAddFlora = view.findViewById(R.id.ivAddFlora);
        ivAddFlora.setVisibility(View.GONE);
        image.observe(this, image->{
            ivAddFlora.setVisibility(View.VISIBLE);
            ivAddFlora.setImageURI(image);
        });


        etName = view.findViewById(R.id.etNombreAdd);
        etFamilia = view.findViewById(R.id.etFamiliaAdd);
        etIdentificacion = view.findViewById(R.id.etIdentificacionAdd);
        etAltitud = view.findViewById(R.id.etAltitudAdd);
        etHabitat = view.findViewById(R.id.etHabitatAdd);
        etFitosociologia = view.findViewById(R.id.etFitoAdd);
        etBiotipo = view.findViewById(R.id.etBiotipoAdd);
        etBiologiaReprod = view.findViewById(R.id.etBioReproAdd);
        etFloracion = view.findViewById(R.id.etFloracionAdd);
        etFructificacion = view.findViewById(R.id.etFructuacionAdd);
        etExprSex = view.findViewById(R.id.etExprSexAdd);
        etPolinizacion = view.findViewById(R.id.etPolinizacionAdd);
        etDispersion = view.findViewById(R.id.etDispersionAdd);
        etNumCromo = view.findViewById(R.id.etNumCromoAdd);
        etReprAsex = view.findViewById(R.id.etReprodAsexAdd);
        etDistribucion = view.findViewById(R.id.etDistribucionAdd);
        etBiologia = view.findViewById(R.id.etBiologiaAdd);
        etDemografia = view.findViewById(R.id.etDemografiaAdd);
        etAmenazas = view.findViewById(R.id.etAmenazasAdd);
        etMedidas = view.findViewById(R.id.etMedidasAdd);

        lyNombreFlora = view.findViewById(R.id.lyNameFloraAdd);
        lyNombreFlora.setErrorEnabled(false);
        lyNombreFlora.setHelperText("* Este campo es obligatorio");

        btImg = view.findViewById(R.id.btAddImg);
        btImg.setOnClickListener( l->{
            selectImage();
        });
        dataObserver();
        textListener();
    }

    void navigation(View view){
        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
    }


    void createFlora(){
        Flora flora = new Flora();

        flora.setNombre(etName.getText().toString());
        flora.setFamilia(etFamilia.getText().toString());
        flora.setIdentificacion(etIdentificacion.getText().toString());
        flora.setAltitud(etAltitud.getText().toString());
        flora.setHabitat(etHabitat.getText().toString());
        flora.setFitosociologia(etFitosociologia.getText().toString());
        flora.setBiotipo(etBiotipo.getText().toString());
        flora.setBiologia_reproductiva(etBiologiaReprod.getText().toString());
        flora.setFloracion(etFloracion.getText().toString());
        flora.setFructificacion(etFructificacion.getText().toString());
        flora.setExpresion_sexual(etExprSex.getText().toString());
        flora.setPolinizacion(etPolinizacion.getText().toString());
        flora.setDispersion(etDispersion.getText().toString());
        flora.setNumero_cromosomatico(etNumCromo.getText().toString());
        flora.setReproduccion_asexual(etReprAsex.getText().toString());
        flora.setDistribucion(etDistribucion.getText().toString());
        flora.setBiologia(etBiologia.getText().toString());
        flora.setDemografia(etDemografia.getText().toString());
        flora.setAmenazas(etAmenazas.getText().toString());
        flora.setMedidas_propuestas(etMedidas.getText().toString());

        if(etName.getText().toString().trim().isEmpty()){
            lyNombreFlora.setError("Este campo no puede estar vacío");
            lyNombreFlora.setErrorEnabled(true);
        } else {
            afvm.createFlora(flora);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.saveoption_flora:{
                createFlora();
                return true;
            }
        }
        return false;
    }

    void dataObserver(){
        afvm.getAddFloraLiveData().observe(this, flora ->{
            if(flora > 0){
                if(resultadoImagen != null)
                    uploadDataImage(flora);
                Toast.makeText(getContext(), "Flora creada", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).popBackStack();
            }
        });
    }

    ActivityResultLauncher<Intent> getLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    //respuesta al resultado de haber seleccionado una imagen
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        //copyData(result.getData());
                        resultadoImagen = result.getData();
                        image.setValue(resultadoImagen.getData());
                    }
                }
        );
    }

    Intent getContentIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        return intent;
    }

    void selectImage() {
        Intent intent = getContentIntent();
        launcher.launch(intent);
    }

    private void uploadDataImage(long id) {
            Imagen imagen = new Imagen();
            imagen.nombre = String.valueOf(new Date().getTime());;
            imagen.descripcion = "descripcion";
            imagen.idflora = id;
            aivm.saveImagen(resultadoImagen, imagen);
    }

    void textListener(){
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() >= 0){
                    lyNombreFlora.setErrorEnabled(false);
                }
            }
        });
    }



}
