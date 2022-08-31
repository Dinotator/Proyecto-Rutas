package com.upv.pm_2022.iti_27849_u2_equipo_02;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.maps.android.SphericalUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener {
    private TextView textoRuta;
    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private MarkerOptions mMarkerOptions;
    private LatLng mOrigin, mDestination, mCity;
    private String mostrarSoloParadas, concatenacion, concatenacionTransbordo, mostrarSoloParadasTransbordo;
    private ListView lvlItems;
    private Adaptador adaptador;
    private ArrayList<Entidad> arrayEntidad = new ArrayList<>();
    //Son los PolyLine que estarán dibujando las líneas
    private Polyline mPolylineBus, mPolylineOrigin, mPolylineOriginTransbordo, mPolylineDestination, mPolylineBusTransbordo;

    //Es la API Key con la que se estará trabajando
    private String APIkey = "AIzaSyAuNlrzJBmDH9_Ykw3xnwTnYlmdWMnN4dw", URLBus, URLBusTransbordo, URLOrigin, URLOriginTransbordo, URLDestination, ruta;

    //Spinner que mostrará las rutas
    Spinner spinnerRutas;
    int subirParadaRuta, bajarParadaRuta, tomarRuta, auxRuta,
            tomarRutaTransbordo, subirParadaTransbordo, bajarParadaTransbordo, auxRutaTransbordo;

    //Contiene la LatLng de la parada más cercana al origen
    LatLng LatLngtempOrigin = new LatLng(0, 0);

    //Contiene la LatLng de la parada más cercana al destino
    LatLng LatLngtempDestination = new LatLng(0, 0);

    //Contiene la LatLng de la parada más cercana al origen
    LatLng LatLngtempOriginTransbordo = new LatLng(0, 0);

    //Contiene la LatLng de la parada más cercana al destino
    LatLng LatLngtempDestinationTransbordo = new LatLng(0, 0);


    //Estas variables contendrán la distancia entre cada punto
    double distanciaParadaDestino, distanciaOrigenParada, distanciaParadaAParada,
            distanciaParadaDestinoTransbordo, distanciaOrigenParadaTransbordo, distanciaParadaAParadaTransbordo;

    //Firebase
    DatabaseReference mDatabase;
    private ArrayList<Marker> realTimeMarkers = new ArrayList<>();
    private ArrayList<LatLng> ruta1 = new ArrayList<>();
    private ArrayList<LatLng> ruta2 = new ArrayList<>();
    private ArrayList<LatLng> ruta3 = new ArrayList<>();
    private ArrayList<LatLng> ruta4 = new ArrayList<>();

    private ArrayList<LatLng> ruta6 = new ArrayList<>();
    private ArrayList<LatLng> ruta7 = new ArrayList<>();
    private ArrayList<LatLng> ruta8 = new ArrayList<>();

    private ArrayList<LatLng> ruta10 = new ArrayList<>();
    private ArrayList<LatLng> ruta11 = new ArrayList<>();
    private ArrayList<LatLng> ruta12 = new ArrayList<>();
    private ArrayList<LatLng> ruta13 = new ArrayList<>();
    private ArrayList<LatLng> ruta14 = new ArrayList<>();
    private ArrayList<LatLng> ruta15 = new ArrayList<>();
    private ArrayList<LatLng> ruta16 = new ArrayList<>();
    private ArrayList<LatLng> ruta17 = new ArrayList<>();
    private ArrayList<LatLng> ruta18 = new ArrayList<>();
    private ArrayList<LatLng> ruta19 = new ArrayList<>();
    private ArrayList<LatLng> ruta20 = new ArrayList<>();
    private ArrayList<LatLng> ruta21 = new ArrayList<>();
    private ArrayList<LatLng> ruta22 = new ArrayList<>();
    private ArrayList<LatLng> ruta23 = new ArrayList<>();
    private ArrayList<LatLng> ruta24 = new ArrayList<>();
    private ArrayList<LatLng> ruta25 = new ArrayList<>();
    private ArrayList<LatLng> ruta26 = new ArrayList<>();
    private ArrayList<LatLng> ruta27 = new ArrayList<>();
    private ArrayList<LatLng> ruta28 = new ArrayList<>();
    private ArrayList<LatLng> ruta29 = new ArrayList<>();
    private ArrayList<LatLng> ruta30 = new ArrayList<>();
    private ArrayList<LatLng> ruta31 = new ArrayList<>();
    private ArrayList<LatLng> ruta32 = new ArrayList<>();

    private ArrayList<LatLng> ruta34 = new ArrayList<>();
    private ArrayList<LatLng> ruta35 = new ArrayList<>();

    private ArrayList<LatLng> ruta51 = new ArrayList<>();

    private ArrayList<ArrayList<LatLng>> AllRutas = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        spinnerRutas = findViewById(R.id.rutas);

        //Vector que llenará el spinner
        String[] rutas = {"Selecciona la ruta", "Ruta 1", "Ruta 2", "Ruta 3", "Ruta 4", "Ruta 6", "Ruta7", "Ruta 8", "Ruta 10", "Ruta 11",
                "Ruta 12", "Ruta 13", "Ruta 14", "Ruta 15", "Ruta 16", "Ruta 17", "Ruta 18", "Ruta 19", "Ruta 20", "Ruta 21", "Ruta 22",
                "Ruta 23", "Ruta 24", "Ruta 25", "Ruta 26", "Ruta 27", "Ruta 28", "Ruta 29", "Ruta 30", "Ruta 31", "Ruta 32", "Ruta 34",
                "Ruta 35", "Ruta 51"};


        ArrayAdapter adapterR = new ArrayAdapter(this, android.R.layout.simple_list_item_1, rutas);
        adapterR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        textoRuta = findViewById(R.id.textoRuta);
        spinnerRutas.setAdapter(adapterR); //Se agrega el adapter al elemento spinner
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Almacena la ubicación de Ciudad Victoria
        mCity = new LatLng(23.737203775093132, -99.14192118927346);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Me guía a la ubicación que está en el parámetro, en este caso Cd. Victoria
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mCity));

        //Hace un zoom
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mCity, 14));

        //Extrae la ubicación actual
        getMyLocation();
        mMap.setOnInfoWindowClickListener(this);
        //En este ciclo se extrae los datos del Firebase y los almacena en su arreglo correspondiente
        for (int i = 1; i < 52; i++) {
            int contador = i;
            //Está haciendo un llamado a los hijos de rutas
            mDatabase.child("rutas").child("ruta" + contador).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (Marker marker : realTimeMarkers) {
                        //En caso de hacer algún cambio en el Firebase, se eliminará la vieja información
                        marker.remove();
                        ruta1.clear();
                        ruta2.clear();
                        ruta3.clear();
                        ruta4.clear();

                        ruta6.clear();
                        ruta7.clear();
                        ruta8.clear();

                        ruta10.clear();
                        ruta11.clear();
                        ruta12.clear();
                        ruta13.clear();
                        ruta14.clear();
                        ruta15.clear();
                        ruta16.clear();
                        ruta17.clear();
                        ruta18.clear();
                        ruta19.clear();
                        ruta20.clear();
                        ruta21.clear();
                        ruta22.clear();
                        ruta23.clear();
                        ruta24.clear();
                        ruta25.clear();
                        ruta26.clear();
                        ruta27.clear();
                        ruta28.clear();
                        ruta29.clear();
                        ruta30.clear();
                        ruta31.clear();
                        ruta32.clear();

                        ruta34.clear();
                        ruta35.clear();

                        ruta51.clear();

                    }

                    //Recorre t0do el Firebase a partir de rutas
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        //Lo que está haciendo aquí es almacenar los datos temporalmente en la clase ExtraerRutas
                        //Para luego mediante getters extraer la información y almacenarlo en los arrays correspondientes
                        ExtraerRutas mp = snapshot1.getValue(ExtraerRutas.class);
                        Double latitud = mp.getLatitud();
                        Double longitud = mp.getLongitud();
                        LatLng temporal = new LatLng(latitud, longitud);

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(new LatLng(latitud, longitud));

                        //Dependiendo de que ruta vaya, estará almacenando sus respectivos datos
                        switch (contador) {
                            case 1:
                                ruta1.add(temporal);
                                break;
                            case 2:
                                ruta2.add(temporal);
                                break;
                            case 3:
                                ruta3.add(temporal);
                                break;

                            case 4:
                                ruta4.add(temporal);
                                break;


                            case 6:
                                ruta6.add(temporal);
                                break;
                            case 7:
                                ruta7.add(temporal);
                                break;
                            case 8:
                                ruta8.add(temporal);
                                break;


                            case 10:
                                ruta10.add(temporal);
                                break;
                            case 11:
                                ruta11.add(temporal);
                                break;
                            case 12:
                                ruta12.add(temporal);
                                break;
                            case 13:
                                ruta13.add(temporal);
                                break;
                            case 14:
                                ruta14.add(temporal);
                                break;
                            case 15:
                                ruta15.add(temporal);
                                break;
                            case 16:
                                ruta16.add(temporal);
                                break;
                            case 17:
                                ruta17.add(temporal);
                                break;
                            case 18:
                                ruta18.add(temporal);
                                break;
                            case 19:
                                ruta19.add(temporal);
                                break;
                            case 20:
                                ruta20.add(temporal);
                                break;
                            case 21:
                                ruta21.add(temporal);
                                break;
                            case 22:
                                ruta22.add(temporal);
                                break;
                            case 23:
                                ruta23.add(temporal);
                                break;
                            case 24:
                                ruta24.add(temporal);
                                break;
                            case 25:
                                ruta25.add(temporal);
                                break;
                            case 26:
                                ruta26.add(temporal);
                                break;
                            case 27:
                                ruta27.add(temporal);
                                break;
                            case 28:
                                ruta28.add(temporal);
                                break;
                            case 29:
                                ruta29.add(temporal);
                                break;
                            case 30:
                                ruta30.add(temporal);
                                break;
                            case 31:
                                ruta31.add(temporal);
                                break;
                            case 32:
                                ruta32.add(temporal);
                                break;
                            case 34:
                                ruta34.add(temporal);
                                break;
                            case 35:
                                ruta35.add(temporal);
                                break;
                            case 51:
                                ruta51.add(temporal);
                                break;
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        }
        //Este método es para almacenar en una array de arrays de LatLng
        fillArrayLatLng();

        //Al seleccionar cualquier valor del spinner, mostrará la ruta que haya sido seleccionada
        spinnerRutas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerRutas.getSelectedItemPosition() != 0) {
                    drawRouteBus();
                } else {
                    try {
                        mPolylineBus.remove();
                        mPolylineOrigin.remove();
                    } catch (Exception e) {
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void getMyLocation() {

        // Getting LocationManager object from System Service LOCATION_SERVICE
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            @Override
            //Cada que la ubicación actual cambie, se hará una actualización en tiempo real
            public void onLocationChanged(Location location) {
                mOrigin = new LatLng(location.getLatitude(), location.getLongitude());
                if (mOrigin != null && mDestination != null) {
                    drawRouteOrigin();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED) {
                mMap.setMyLocationEnabled(true);
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, mLocationListener);

                //Al dar un clic largo, se ejecutará lo que viene aquí adentro
                mMap.setOnMapLongClickListener(latLng -> {
                    Calendar calendario = Calendar.getInstance();
                    int hora = calendario.get(Calendar.HOUR_OF_DAY);
                    if (hora > 21 || hora < 5) {
                        Toast.makeText(this, "No hay ningún micro circulando, espere hasta que sea las 5 AM para tomar una ruta", Toast.LENGTH_SHORT).show();
                    } else {
                        //Almacena la LatLng del destino
                        mDestination = latLng;
                        //Limpia el mapa
                        mMap.clear();
                        //Agrega el marcador con la leyenda Destino
                        mMarkerOptions = new MarkerOptions().position(mDestination).title("Destino").snippet("Ver instrucciones");
                        mMap.addMarker(mMarkerOptions);
                        try {
                            //Para saber que ruta tomar, se debe comparar que parada es la más cercana al Destino
                            //Y así, se hará una búsqueda para saber cual es la parada de la ruta más cercana al origen
                            //Esta matríz guardará la distancia de todas las paradas de todas las rutas que hay
                            double matriz[][] = new double[33][25];
                            subirParadaRuta = 0;
                            tomarRuta = 0;
                            auxRuta = 0;
                            bajarParadaRuta = 0;
                            if (mOrigin != null && mDestination != null) {
                                //Ciclo for anidado para ir llenando la matriz
                                for (int i = 0; i < 33; i++) {
                                    for (int j = 0; j < 25; j++) {
                                        matriz[i][j] = SphericalUtil.computeDistanceBetween(mDestination, AllRutas.get(i).get(j));
                                    }
                                }
                                //Para saber cual es la parada más cercana, se almacenará el primer elemento de la matriz en unaa variable double
                                distanciaParadaDestino = matriz[0][0];
                                for (int i = 0; i < 33; i++) {
                                    for (int j = 0; j < 25; j++) {
                                        //En este if compara cual es la distancia más corta de todas las rutas y sus paradas
                                        if (matriz[i][j] <= distanciaParadaDestino) {

                                            //Almacena la distancia más corta
                                            distanciaParadaDestino = matriz[i][j];

                                            //Almacena la parada más cercana al destino
                                            bajarParadaRuta = j;

                                            //Almacena la ruta que se debe tomar
                                            tomarRuta = i;

                                            //Almacena la ubicación de la parada más cercana
                                            LatLngtempDestination = new LatLng(AllRutas.get(i).get(j).latitude, AllRutas.get(i).get(j).longitude);
                                        }
                                    }
                                }

                                //Ahora para saber cual es la parada más cercana al origen en base a la ruta seleccionada
                                //Se hará un proceso similar, el cual ahora se recorrerá todos los datos de la ruta que fue la más corta
                                double array[] = new double[25];

                                //Almacena todas las distancias del origen a todas las paradas
                                for (int i = 0; i < 25; i++) {
                                    array[i] = SphericalUtil.computeDistanceBetween(mOrigin, AllRutas.get(tomarRuta).get(i));
                                }

                                //Se guarda el primer valor del vector
                                distanciaOrigenParada = array[0];

                                for (int i = 0; i < array.length; i++) {
                                    //Al igual que en el anterior if, estará comparando cual es la distancia más corta
                                    if (array[i] <= distanciaOrigenParada) {
                                        //Se guarda la distancia más corta
                                        distanciaOrigenParada = array[i];

                                        //Se guarda la parada más corta
                                        subirParadaRuta = i;

                                        //Guarda la ubicación de dicha parada
                                        LatLngtempOrigin = new LatLng(AllRutas.get(tomarRuta).get(i).latitude, AllRutas.get(tomarRuta).get(i).longitude);
                                    }
                                }

                                //En esta variable guarda la distancia entre las paradas que se recorreran en el camino partiendo del origen al destino
                                distanciaParadaAParada = SphericalUtil.computeDistanceBetween(LatLngtempOrigin, LatLngtempDestination);

                                auxRuta = tomarRuta;
                                auxRuta++;
                                if (auxRuta >= 5 && auxRuta < 8) {
                                    auxRuta++;
                                } else if (auxRuta >= 8 && auxRuta < 31) {
                                    auxRuta = auxRuta + 2;
                                } else if (auxRuta == 31 || auxRuta == 32) {
                                    auxRuta = auxRuta + 3;
                                } else if (auxRuta == 33) {
                                    auxRuta = 51;
                                }

                                if (distanciaOrigenParada > 800) {
                                    double matrizTransbordo[][] = new double[33][25];
                                    subirParadaTransbordo = 0;
                                    bajarParadaTransbordo = 0;
                                    tomarRutaTransbordo = 0;
                                    auxRutaTransbordo = 0;
                                    for (int i = 0; i < 33; i++) {
                                        for (int j = 0; j < 25; j++) {
                                            matrizTransbordo[i][j] = SphericalUtil.computeDistanceBetween(mOrigin, AllRutas.get(i).get(j));
                                        }
                                    }
                                    //Para saber cual es la parada más cercana, se almacenará el primer elemento de la matriz en unaa variable double
                                    distanciaParadaDestinoTransbordo = matrizTransbordo[0][0];
                                    for (int i = 0; i < 33; i++) {
                                        for (int j = 0; j < 25; j++) {
                                            //En este if compara cual es la distancia más corta de todas las rutas y sus paradas
                                            if (matrizTransbordo[i][j] <= distanciaParadaDestinoTransbordo) {

                                                //Almacena la distancia más corta
                                                distanciaParadaDestinoTransbordo = matrizTransbordo[i][j];

                                                //Almacena la parada más cercana al origen
                                                bajarParadaTransbordo = j;

                                                //Almacena la ruta que se debe tomar
                                                tomarRutaTransbordo = i;

                                                //Almacena la ubicación de la parada más cercana
                                                LatLngtempOriginTransbordo = new LatLng(AllRutas.get(i).get(j).latitude, AllRutas.get(i).get(j).longitude);
                                            }
                                        }
                                    }
                                    //Ahora para saber cual es la parada más cercana de la parada de origen y parada destino más cercanos entre sí
                                    //Se hará un proceso similar, el cual ahora se recorrerá todos los datos de la ruta que fue la más corta
                                    double array2[] = new double[25];
                                    //Almacena todas las distancias del origen a todas las paradas
                                    for (int i = 0; i < 25; i++) {
                                        array2[i] = SphericalUtil.computeDistanceBetween(AllRutas.get(tomarRuta).get(subirParadaRuta), AllRutas.get(tomarRutaTransbordo).get(i));
                                    }
                                    //Se guarda el primer valor del vector
                                    distanciaOrigenParadaTransbordo = array2[0];

                                    for (int i = 0; i < array2.length; i++) {
                                        //Al igual que en el anterior if, estará comparando cual es la distancia más corta
                                        if (array2[i] <= distanciaOrigenParadaTransbordo) {
                                            //Se guarda la distancia más corta
                                            distanciaOrigenParadaTransbordo = array2[i];

                                            //Se guarda la parada más corta
                                            subirParadaTransbordo = i;

                                            //Guarda la ubicación de dicha parada
                                            LatLngtempDestinationTransbordo = new LatLng(AllRutas.get(tomarRutaTransbordo).get(i).latitude, AllRutas.get(tomarRutaTransbordo).get(i).longitude);
                                        }
                                    }
                                    auxRutaTransbordo = tomarRutaTransbordo;
                                    auxRutaTransbordo++;
                                    if (auxRutaTransbordo >= 5 && auxRutaTransbordo < 8) {
                                        auxRutaTransbordo++;
                                    } else if (auxRutaTransbordo >= 8 && auxRutaTransbordo < 31) {
                                        auxRutaTransbordo = auxRutaTransbordo + 2;
                                    } else if (auxRutaTransbordo == 31 || auxRutaTransbordo == 32) {
                                        auxRutaTransbordo = auxRutaTransbordo + 3;
                                    } else if (auxRutaTransbordo == 33) {
                                        auxRutaTransbordo = 51;
                                    }
                                    //En esta variable guarda la distancia entre las paradas que se recorreran en el camino partiendo del origen al destino
                                    distanciaParadaAParadaTransbordo = SphericalUtil.computeDistanceBetween(LatLngtempOriginTransbordo, LatLngtempDestinationTransbordo);

                                    drawRouteBusTransbordo();
                                    drawRouteOriginTransbordo();

                                }

                                spinnerRutas.setVisibility(View.GONE);
                                textoRuta.setVisibility(View.GONE);

                                drawRouteOrigin();
                                drawRouteBus();
                                drawRouteDestination();
                            } else {
                                Toast.makeText(MapsActivity.this, "Cargando base de datos...", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(MapsActivity.this, "Hubo un error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.INTERNET,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION

                        }, 100);
                        return;
                    }
                }
            }
        }
    }

    //Estos métodos son para dibujar las rutas
    private void drawRouteOrigin() {
        URLOrigin = getDirectionsUrlOrigin(mOrigin);
        ReadTaskOrigin downloadTask2 = new ReadTaskOrigin();
        downloadTask2.execute(URLOrigin);
    }

    private void drawRouteOriginTransbordo() {
        URLOriginTransbordo = getDirectionsUrlOriginTransbordo();
        ReadTaskOriginTransbordo downloadTask = new ReadTaskOriginTransbordo();
        downloadTask.execute(URLOriginTransbordo);
    }

    private void drawRouteBus() {
        URLBus = getDirectionsUrlBus();
        ReadTaskBus downloadTask = new ReadTaskBus();
        downloadTask.execute(URLBus);
    }

    private void drawRouteBusTransbordo() {
        URLBusTransbordo = getDirectionsUrlBusTransbordo();
        ReadTaskBusTransbordo downloadTask = new ReadTaskBusTransbordo();
        downloadTask.execute(URLBusTransbordo);
    }

    private void drawRouteDestination() {
        URLDestination = getDirectionsUrlDestination(mDestination);
        ReadTaskDestination downloadTask = new ReadTaskDestination();
        downloadTask.execute(URLDestination);
    }


    private String getDirectionsUrlOrigin(LatLng origin) {

        if (distanciaOrigenParada > 800) {
            return "https://maps.googleapis.com/maps/api/directions/json?origin="
                    + origin.latitude + "," + origin.longitude + "&destination=" +
                    LatLngtempOriginTransbordo.latitude + "," + LatLngtempOriginTransbordo.longitude +
                    "&mode=walking&key=" + APIkey;
        } else {
            return "https://maps.googleapis.com/maps/api/directions/json?origin="
                    + origin.latitude + "," + origin.longitude + "&destination=" +
                    LatLngtempOrigin.latitude + "," + LatLngtempOrigin.longitude +
                    "&mode=walking&key=" + APIkey;
        }

    }

    private String getDirectionsUrlOriginTransbordo() {
        return "https://maps.googleapis.com/maps/api/directions/json?origin="
                + AllRutas.get(tomarRutaTransbordo).get(subirParadaTransbordo).latitude + "," + AllRutas.get(tomarRutaTransbordo).get(subirParadaTransbordo).longitude + "&destination=" +
                AllRutas.get(tomarRuta).get(subirParadaRuta).latitude + "," + AllRutas.get(tomarRuta).get(subirParadaRuta).longitude +
                "&mode=walking&key=" + APIkey;
    }

    private String getDirectionsUrlDestination(LatLng destination) {
        return "https://maps.googleapis.com/maps/api/directions/json?origin="
                + LatLngtempDestination.latitude + "," + LatLngtempDestination.longitude + "&destination=" +
                destination.latitude + "," + destination.longitude +
                "&mode=walking&key=" + APIkey;

    }

    private String getDirectionsUrlBus() {

        int aux = auxRuta;
        if (aux == 0) {
            aux = spinnerRutas.getSelectedItemPosition();
            if (aux >= 5 && aux < 8) {
                aux++;
            } else if (aux >= 8 && aux < 31) {
                aux = aux + 2;
            } else if (aux == 31 || aux == 32) {
                aux = aux + 3;
            } else if (aux == 33) {
                aux = 51;
            }
        }
        String parteLink, parteOrigen, parteDestino, key;

        mostrarSoloParadas = "";
        parteLink = "https://maps.googleapis.com/maps/api/directions/json?origin=";
        if (subirParadaRuta < bajarParadaRuta) {
            parteOrigen = AllRutas.get(tomarRuta).get(subirParadaRuta).latitude + "," + AllRutas.get(tomarRuta).get(subirParadaRuta).longitude;
            parteDestino = "&destination=" + AllRutas.get(tomarRuta).get(bajarParadaRuta).latitude + "," + AllRutas.get(tomarRuta).get(bajarParadaRuta).longitude + "&waypoints=optimize:true";
        } else {
            parteOrigen = AllRutas.get(tomarRuta).get(bajarParadaRuta).latitude + "," + AllRutas.get(tomarRuta).get(bajarParadaRuta).longitude;
            parteDestino = "&destination=" + AllRutas.get(tomarRuta).get(subirParadaRuta).latitude + "," + AllRutas.get(tomarRuta).get(subirParadaRuta).longitude + "&waypoints=optimize:true";
        }

        int mayor, menor;
        if (subirParadaRuta < bajarParadaRuta) {
            mayor = bajarParadaRuta;
            menor = subirParadaRuta;
        } else {
            mayor = subirParadaRuta;
            menor = bajarParadaRuta;
        }
        int contador = 0;
        for (int i = menor; i < mayor; i++) {
            if ((AllRutas.get(tomarRuta).get(i).latitude != AllRutas.get(tomarRuta).get(bajarParadaRuta).latitude) && (AllRutas.get(tomarRuta).get(i).latitude != AllRutas.get(tomarRuta).get(subirParadaRuta).latitude)) {
                mostrarSoloParadas = mostrarSoloParadas + "|" + AllRutas.get(tomarRuta).get(i).latitude + "," + AllRutas.get(tomarRuta).get(i).longitude;
                contador++;
            }
        }
        key = "&key=" + APIkey;

        if (contador != 0) {
            concatenacion = parteLink + parteOrigen + parteDestino + mostrarSoloParadas + key;
        } else {
            if (subirParadaRuta < bajarParadaRuta) {
                parteOrigen = AllRutas.get(tomarRuta).get(subirParadaRuta).latitude + "," + AllRutas.get(tomarRuta).get(subirParadaRuta).longitude;
                parteDestino = "&destination=" + AllRutas.get(tomarRuta).get(bajarParadaRuta).latitude + "," + AllRutas.get(tomarRuta).get(bajarParadaRuta).longitude + key;
            } else {
                parteOrigen = AllRutas.get(tomarRuta).get(bajarParadaRuta).latitude + "," + AllRutas.get(tomarRuta).get(bajarParadaRuta).longitude;
                parteDestino = "&destination=" + AllRutas.get(tomarRuta).get(subirParadaRuta).latitude + "," + AllRutas.get(tomarRuta).get(subirParadaRuta).longitude + key;
            }
            concatenacion = parteLink + parteOrigen + parteDestino + key;
        }

        if ((subirParadaRuta == 0) && (bajarParadaRuta == 0)) {
            concatenacion = null;
        }
        switch (aux) {
            case 1:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta1.get(0).latitude + "," + ruta1.get(0).longitude + "&destination=" + ruta1.get(24).latitude + "," + ruta1.get(24).longitude + "&waypoints=optimize:true|" + ruta1.get(1).latitude + "," + ruta1.get(1).longitude + "|" + "" + ruta1.get(2).latitude + "," + ruta1.get(2).longitude + "|" + "" + ruta1.get(3).latitude + "," + ruta1.get(3).longitude + "|" + "" + ruta1.get(4).latitude + "," + ruta1.get(4).longitude + "|" + "" + ruta1.get(5).latitude + "," + ruta1.get(5).longitude + "|" + "" + ruta1.get(6).latitude + "," + ruta1.get(6).longitude + "|" + "" + ruta1.get(7).latitude + "," + ruta1.get(7).longitude + "|" + "" + ruta1.get(8).latitude + "," + ruta1.get(8).longitude + "|" + "" + ruta1.get(9).latitude + "," + ruta1.get(9).longitude + "|" + "" + ruta1.get(10).latitude + "," + ruta1.get(10).longitude + "|" + "" + ruta1.get(11).latitude + "," + ruta1.get(11).longitude + "|" + "" + ruta1.get(12).latitude + "," + ruta1.get(12).longitude + "|" + "" + ruta1.get(13).latitude + "," + ruta1.get(13).longitude + "|" + "" + ruta1.get(14).latitude + "," + ruta1.get(14).longitude + "|" + "" + ruta1.get(15).latitude + "," + ruta1.get(15).longitude + "|" + "" + ruta1.get(16).latitude + "," + ruta1.get(16).longitude + "|" + "" + ruta1.get(17).latitude + "," + ruta1.get(17).longitude + "|" + "" + ruta1.get(18).latitude + "," + ruta1.get(18).longitude + "|" + "" + ruta1.get(19).latitude + "," + ruta1.get(19).longitude + "|" + "" + ruta1.get(20).latitude + "," + ruta1.get(20).longitude + "|" + "" + ruta1.get(21).latitude + "," + ruta1.get(21).longitude + "|" + "" + ruta1.get(22).latitude + "," + ruta1.get(22).longitude + "|" + "" + ruta1.get(23).latitude + "," + ruta1.get(23).longitude + "&key=" + APIkey;
                }
            case 2:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta2.get(0).latitude + "," + ruta2.get(0).longitude + "&destination=" + ruta2.get(24).latitude + "," + ruta2.get(24).longitude + "&waypoints=optimize:true|" + ruta2.get(1).latitude + "," + ruta2.get(1).longitude + "|" + "" + ruta2.get(2).latitude + "," + ruta2.get(2).longitude + "|" + "" + ruta2.get(3).latitude + "," + ruta2.get(3).longitude + "|" + "" + ruta2.get(4).latitude + "," + ruta2.get(4).longitude + "|" + "" + ruta2.get(5).latitude + "," + ruta2.get(5).longitude + "|" + "" + ruta2.get(6).latitude + "," + ruta2.get(6).longitude + "|" + "" + ruta2.get(7).latitude + "," + ruta2.get(7).longitude + "|" + "" + ruta2.get(8).latitude + "," + ruta2.get(8).longitude + "|" + "" + ruta2.get(9).latitude + "," + ruta2.get(9).longitude + "|" + "" + ruta2.get(10).latitude + "," + ruta2.get(10).longitude + "|" + "" + ruta2.get(11).latitude + "," + ruta2.get(11).longitude + "|" + "" + ruta2.get(12).latitude + "," + ruta2.get(12).longitude + "|" + "" + ruta2.get(13).latitude + "," + ruta2.get(13).longitude + "|" + "" + ruta2.get(14).latitude + "," + ruta2.get(14).longitude + "|" + "" + ruta2.get(15).latitude + "," + ruta2.get(15).longitude + "|" + "" + ruta2.get(16).latitude + "," + ruta2.get(16).longitude + "|" + "" + ruta2.get(17).latitude + "," + ruta2.get(17).longitude + "|" + "" + ruta2.get(18).latitude + "," + ruta2.get(18).longitude + "|" + "" + ruta2.get(19).latitude + "," + ruta2.get(19).longitude + "|" + "" + ruta2.get(20).latitude + "," + ruta2.get(20).longitude + "|" + "" + ruta2.get(21).latitude + "," + ruta2.get(21).longitude + "|" + "" + ruta2.get(22).latitude + "," + ruta2.get(22).longitude + "|" + "" + ruta2.get(23).latitude + "," + ruta2.get(23).longitude + "&key=" + APIkey;
                }
            case 3:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta3.get(0).latitude + "," + ruta3.get(0).longitude + "&destination=" + ruta3.get(24).latitude + "," + ruta3.get(24).longitude + "&waypoints=optimize:true|" + ruta3.get(1).latitude + "," + ruta3.get(1).longitude + "|" + "" + ruta3.get(2).latitude + "," + ruta3.get(2).longitude + "|" + "" + ruta3.get(3).latitude + "," + ruta3.get(3).longitude + "|" + "" + ruta3.get(4).latitude + "," + ruta3.get(4).longitude + "|" + "" + ruta3.get(5).latitude + "," + ruta3.get(5).longitude + "|" + "" + ruta3.get(6).latitude + "," + ruta3.get(6).longitude + "|" + "" + ruta3.get(7).latitude + "," + ruta3.get(7).longitude + "|" + "" + ruta3.get(8).latitude + "," + ruta3.get(8).longitude + "|" + "" + ruta3.get(9).latitude + "," + ruta3.get(9).longitude + "|" + "" + ruta3.get(10).latitude + "," + ruta3.get(10).longitude + "|" + "" + ruta3.get(11).latitude + "," + ruta3.get(11).longitude + "|" + "" + ruta3.get(12).latitude + "," + ruta3.get(12).longitude + "|" + "" + ruta3.get(13).latitude + "," + ruta3.get(13).longitude + "|" + "" + ruta3.get(14).latitude + "," + ruta3.get(14).longitude + "|" + "" + ruta3.get(15).latitude + "," + ruta3.get(15).longitude + "|" + "" + ruta3.get(16).latitude + "," + ruta3.get(16).longitude + "|" + "" + ruta3.get(17).latitude + "," + ruta3.get(17).longitude + "|" + "" + ruta3.get(18).latitude + "," + ruta3.get(18).longitude + "|" + "" + ruta3.get(19).latitude + "," + ruta3.get(19).longitude + "|" + "" + ruta3.get(20).latitude + "," + ruta3.get(20).longitude + "|" + "" + ruta3.get(21).latitude + "," + ruta3.get(21).longitude + "|" + "" + ruta3.get(22).latitude + "," + ruta3.get(22).longitude + "|" + "" + ruta3.get(23).latitude + "," + ruta3.get(23).longitude + "&key=" + APIkey;
                }
            case 4:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta4.get(0).latitude + "," + ruta4.get(0).longitude + "&destination=" + ruta4.get(24).latitude + "," + ruta4.get(24).longitude + "&waypoints=optimize:true|" + ruta4.get(1).latitude + "," + ruta4.get(1).longitude + "|" + "" + ruta4.get(2).latitude + "," + ruta4.get(2).longitude + "|" + "" + ruta4.get(3).latitude + "," + ruta4.get(3).longitude + "|" + "" + ruta4.get(4).latitude + "," + ruta4.get(4).longitude + "|" + "" + ruta4.get(5).latitude + "," + ruta4.get(5).longitude + "|" + "" + ruta4.get(6).latitude + "," + ruta4.get(6).longitude + "|" + "" + ruta4.get(7).latitude + "," + ruta4.get(7).longitude + "|" + "" + ruta4.get(8).latitude + "," + ruta4.get(8).longitude + "|" + "" + ruta4.get(9).latitude + "," + ruta4.get(9).longitude + "|" + "" + ruta4.get(10).latitude + "," + ruta4.get(10).longitude + "|" + "" + ruta4.get(11).latitude + "," + ruta4.get(11).longitude + "|" + "" + ruta4.get(12).latitude + "," + ruta4.get(12).longitude + "|" + "" + ruta4.get(13).latitude + "," + ruta4.get(13).longitude + "|" + "" + ruta4.get(14).latitude + "," + ruta4.get(14).longitude + "|" + "" + ruta4.get(15).latitude + "," + ruta4.get(15).longitude + "|" + "" + ruta4.get(16).latitude + "," + ruta4.get(16).longitude + "|" + "" + ruta4.get(17).latitude + "," + ruta4.get(17).longitude + "|" + "" + ruta4.get(18).latitude + "," + ruta4.get(18).longitude + "|" + "" + ruta4.get(19).latitude + "," + ruta4.get(19).longitude + "|" + "" + ruta4.get(20).latitude + "," + ruta4.get(20).longitude + "|" + "" + ruta4.get(21).latitude + "," + ruta4.get(21).longitude + "|" + "" + ruta4.get(22).latitude + "," + ruta4.get(22).longitude + "|" + "" + ruta4.get(23).latitude + "," + ruta4.get(23).longitude + "&key=" + APIkey;
                }

            case 6:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta6.get(0).latitude + "," + ruta6.get(0).longitude + "&destination=" + ruta6.get(24).latitude + "," + ruta6.get(24).longitude + "&waypoints=optimize:true|" + ruta6.get(1).latitude + "," + ruta6.get(1).longitude + "|" + "" + ruta6.get(2).latitude + "," + ruta6.get(2).longitude + "|" + "" + ruta6.get(3).latitude + "," + ruta6.get(3).longitude + "|" + "" + ruta6.get(4).latitude + "," + ruta6.get(4).longitude + "|" + "" + ruta6.get(5).latitude + "," + ruta6.get(5).longitude + "|" + "" + ruta6.get(6).latitude + "," + ruta6.get(6).longitude + "|" + "" + ruta6.get(7).latitude + "," + ruta6.get(7).longitude + "|" + "" + ruta6.get(8).latitude + "," + ruta6.get(8).longitude + "|" + "" + ruta6.get(9).latitude + "," + ruta6.get(9).longitude + "|" + "" + ruta6.get(10).latitude + "," + ruta6.get(10).longitude + "|" + "" + ruta6.get(11).latitude + "," + ruta6.get(11).longitude + "|" + "" + ruta6.get(12).latitude + "," + ruta6.get(12).longitude + "|" + "" + ruta6.get(13).latitude + "," + ruta6.get(13).longitude + "|" + "" + ruta6.get(14).latitude + "," + ruta6.get(14).longitude + "|" + "" + ruta6.get(15).latitude + "," + ruta6.get(15).longitude + "|" + "" + ruta6.get(16).latitude + "," + ruta6.get(16).longitude + "|" + "" + ruta6.get(17).latitude + "," + ruta6.get(17).longitude + "|" + "" + ruta6.get(18).latitude + "," + ruta6.get(18).longitude + "|" + "" + ruta6.get(19).latitude + "," + ruta6.get(19).longitude + "|" + "" + ruta6.get(20).latitude + "," + ruta6.get(20).longitude + "|" + "" + ruta6.get(21).latitude + "," + ruta6.get(21).longitude + "|" + "" + ruta6.get(22).latitude + "," + ruta6.get(22).longitude + "|" + "" + ruta6.get(23).latitude + "," + ruta6.get(23).longitude + "&key=" + APIkey;
                }

            case 7:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta7.get(0).latitude + "," + ruta7.get(0).longitude + "&destination=" + ruta7.get(24).latitude + "," + ruta7.get(24).longitude + "&waypoints=optimize:true|" + ruta7.get(1).latitude + "," + ruta7.get(1).longitude + "|" + "" + ruta7.get(2).latitude + "," + ruta7.get(2).longitude + "|" + "" + ruta7.get(3).latitude + "," + ruta7.get(3).longitude + "|" + "" + ruta7.get(4).latitude + "," + ruta7.get(4).longitude + "|" + "" + ruta7.get(5).latitude + "," + ruta7.get(5).longitude + "|" + "" + ruta7.get(6).latitude + "," + ruta7.get(6).longitude + "|" + "" + ruta7.get(7).latitude + "," + ruta7.get(7).longitude + "|" + "" + ruta7.get(8).latitude + "," + ruta7.get(8).longitude + "|" + "" + ruta7.get(9).latitude + "," + ruta7.get(9).longitude + "|" + "" + ruta7.get(10).latitude + "," + ruta7.get(10).longitude + "|" + "" + ruta7.get(11).latitude + "," + ruta7.get(11).longitude + "|" + "" + ruta7.get(12).latitude + "," + ruta7.get(12).longitude + "|" + "" + ruta7.get(13).latitude + "," + ruta7.get(13).longitude + "|" + "" + ruta7.get(14).latitude + "," + ruta7.get(14).longitude + "|" + "" + ruta7.get(15).latitude + "," + ruta7.get(15).longitude + "|" + "" + ruta7.get(16).latitude + "," + ruta7.get(16).longitude + "|" + "" + ruta7.get(17).latitude + "," + ruta7.get(17).longitude + "|" + "" + ruta7.get(18).latitude + "," + ruta7.get(18).longitude + "|" + "" + ruta7.get(19).latitude + "," + ruta7.get(19).longitude + "|" + "" + ruta7.get(20).latitude + "," + ruta7.get(20).longitude + "|" + "" + ruta7.get(21).latitude + "," + ruta7.get(21).longitude + "|" + "" + ruta7.get(22).latitude + "," + ruta7.get(22).longitude + "|" + "" + ruta7.get(23).latitude + "," + ruta7.get(23).longitude + "&key=" + APIkey;
                }

            case 8:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta8.get(0).latitude + "," + ruta8.get(0).longitude + "&destination=" + ruta8.get(24).latitude + "," + ruta8.get(24).longitude + "&waypoints=optimize:true|" + ruta8.get(1).latitude + "," + ruta8.get(1).longitude + "|" + "" + ruta8.get(2).latitude + "," + ruta8.get(2).longitude + "|" + "" + ruta8.get(3).latitude + "," + ruta8.get(3).longitude + "|" + "" + ruta8.get(4).latitude + "," + ruta8.get(4).longitude + "|" + "" + ruta8.get(5).latitude + "," + ruta8.get(5).longitude + "|" + "" + ruta8.get(6).latitude + "," + ruta8.get(6).longitude + "|" + "" + ruta8.get(7).latitude + "," + ruta8.get(7).longitude + "|" + "" + ruta8.get(8).latitude + "," + ruta8.get(8).longitude + "|" + "" + ruta8.get(9).latitude + "," + ruta8.get(9).longitude + "|" + "" + ruta8.get(10).latitude + "," + ruta8.get(10).longitude + "|" + "" + ruta8.get(11).latitude + "," + ruta8.get(11).longitude + "|" + "" + ruta8.get(12).latitude + "," + ruta8.get(12).longitude + "|" + "" + ruta8.get(13).latitude + "," + ruta8.get(13).longitude + "|" + "" + ruta8.get(14).latitude + "," + ruta8.get(14).longitude + "|" + "" + ruta8.get(15).latitude + "," + ruta8.get(15).longitude + "|" + "" + ruta8.get(16).latitude + "," + ruta8.get(16).longitude + "|" + "" + ruta8.get(17).latitude + "," + ruta8.get(17).longitude + "|" + "" + ruta8.get(18).latitude + "," + ruta8.get(18).longitude + "|" + "" + ruta8.get(19).latitude + "," + ruta8.get(19).longitude + "|" + "" + ruta8.get(20).latitude + "," + ruta8.get(20).longitude + "|" + "" + ruta8.get(21).latitude + "," + ruta8.get(21).longitude + "|" + "" + ruta8.get(22).latitude + "," + ruta8.get(22).longitude + "|" + "" + ruta8.get(23).latitude + "," + ruta8.get(23).longitude + "&key=" + APIkey;
                }
            case 10:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta10.get(0).latitude + "," + ruta10.get(0).longitude + "&destination=" + ruta10.get(24).latitude + "," + ruta10.get(24).longitude + "&waypoints=optimize:true|" + ruta10.get(1).latitude + "," + ruta10.get(1).longitude + "|" + "" + ruta10.get(2).latitude + "," + ruta10.get(2).longitude + "|" + "" + ruta10.get(3).latitude + "," + ruta10.get(3).longitude + "|" + "" + ruta10.get(4).latitude + "," + ruta10.get(4).longitude + "|" + "" + ruta10.get(5).latitude + "," + ruta10.get(5).longitude + "|" + "" + ruta10.get(6).latitude + "," + ruta10.get(6).longitude + "|" + "" + ruta10.get(7).latitude + "," + ruta10.get(7).longitude + "|" + "" + ruta10.get(8).latitude + "," + ruta10.get(8).longitude + "|" + "" + ruta10.get(9).latitude + "," + ruta10.get(9).longitude + "|" + "" + ruta10.get(10).latitude + "," + ruta10.get(10).longitude + "|" + "" + ruta10.get(11).latitude + "," + ruta10.get(11).longitude + "|" + "" + ruta10.get(12).latitude + "," + ruta10.get(12).longitude + "|" + "" + ruta10.get(13).latitude + "," + ruta10.get(13).longitude + "|" + "" + ruta10.get(14).latitude + "," + ruta10.get(14).longitude + "|" + "" + ruta10.get(15).latitude + "," + ruta10.get(15).longitude + "|" + "" + ruta10.get(16).latitude + "," + ruta10.get(16).longitude + "|" + "" + ruta10.get(17).latitude + "," + ruta10.get(17).longitude + "|" + "" + ruta10.get(18).latitude + "," + ruta10.get(18).longitude + "|" + "" + ruta10.get(19).latitude + "," + ruta10.get(19).longitude + "|" + "" + ruta10.get(20).latitude + "," + ruta10.get(20).longitude + "|" + "" + ruta10.get(21).latitude + "," + ruta10.get(21).longitude + "|" + "" + ruta10.get(22).latitude + "," + ruta10.get(22).longitude + "|" + "" + ruta10.get(23).latitude + "," + ruta10.get(23).longitude + "&key=" + APIkey;
                }
            case 11:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta11.get(0).latitude + "," + ruta11.get(0).longitude + "&destination=" + ruta11.get(24).latitude + "," + ruta11.get(24).longitude + "&waypoints=optimize:true|" + ruta11.get(1).latitude + "," + ruta11.get(1).longitude + "|" + "" + ruta11.get(2).latitude + "," + ruta11.get(2).longitude + "|" + "" + ruta11.get(3).latitude + "," + ruta11.get(3).longitude + "|" + "" + ruta11.get(4).latitude + "," + ruta11.get(4).longitude + "|" + "" + ruta11.get(5).latitude + "," + ruta11.get(5).longitude + "|" + "" + ruta11.get(6).latitude + "," + ruta11.get(6).longitude + "|" + "" + ruta11.get(7).latitude + "," + ruta11.get(7).longitude + "|" + "" + ruta11.get(8).latitude + "," + ruta11.get(8).longitude + "|" + "" + ruta11.get(9).latitude + "," + ruta11.get(9).longitude + "|" + "" + ruta11.get(10).latitude + "," + ruta11.get(10).longitude + "|" + "" + ruta11.get(11).latitude + "," + ruta11.get(11).longitude + "|" + "" + ruta11.get(12).latitude + "," + ruta11.get(12).longitude + "|" + "" + ruta11.get(13).latitude + "," + ruta11.get(13).longitude + "|" + "" + ruta11.get(14).latitude + "," + ruta11.get(14).longitude + "|" + "" + ruta11.get(15).latitude + "," + ruta11.get(15).longitude + "|" + "" + ruta11.get(16).latitude + "," + ruta11.get(16).longitude + "|" + "" + ruta11.get(17).latitude + "," + ruta11.get(17).longitude + "|" + "" + ruta11.get(18).latitude + "," + ruta11.get(18).longitude + "|" + "" + ruta11.get(19).latitude + "," + ruta11.get(19).longitude + "|" + "" + ruta11.get(20).latitude + "," + ruta11.get(20).longitude + "|" + "" + ruta11.get(21).latitude + "," + ruta11.get(21).longitude + "|" + "" + ruta11.get(22).latitude + "," + ruta11.get(22).longitude + "|" + "" + ruta11.get(23).latitude + "," + ruta11.get(23).longitude + "&key=" + APIkey;
                }
            case 12:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta12.get(0).latitude + "," + ruta12.get(0).longitude + "&destination=" + ruta12.get(24).latitude + "," + ruta12.get(24).longitude + "&waypoints=optimize:true|" + ruta12.get(1).latitude + "," + ruta12.get(1).longitude + "|" + "" + ruta12.get(2).latitude + "," + ruta12.get(2).longitude + "|" + "" + ruta12.get(3).latitude + "," + ruta12.get(3).longitude + "|" + "" + ruta12.get(4).latitude + "," + ruta12.get(4).longitude + "|" + "" + ruta12.get(5).latitude + "," + ruta12.get(5).longitude + "|" + "" + ruta12.get(6).latitude + "," + ruta12.get(6).longitude + "|" + "" + ruta12.get(7).latitude + "," + ruta12.get(7).longitude + "|" + "" + ruta12.get(8).latitude + "," + ruta12.get(8).longitude + "|" + "" + ruta12.get(9).latitude + "," + ruta12.get(9).longitude + "|" + "" + ruta12.get(10).latitude + "," + ruta12.get(10).longitude + "|" + "" + ruta12.get(11).latitude + "," + ruta12.get(11).longitude + "|" + "" + ruta12.get(12).latitude + "," + ruta12.get(12).longitude + "|" + "" + ruta12.get(13).latitude + "," + ruta12.get(13).longitude + "|" + "" + ruta12.get(14).latitude + "," + ruta12.get(14).longitude + "|" + "" + ruta12.get(15).latitude + "," + ruta12.get(15).longitude + "|" + "" + ruta12.get(16).latitude + "," + ruta12.get(16).longitude + "|" + "" + ruta12.get(17).latitude + "," + ruta12.get(17).longitude + "|" + "" + ruta12.get(18).latitude + "," + ruta12.get(18).longitude + "|" + "" + ruta12.get(19).latitude + "," + ruta12.get(19).longitude + "|" + "" + ruta12.get(20).latitude + "," + ruta12.get(20).longitude + "|" + "" + ruta12.get(21).latitude + "," + ruta12.get(21).longitude + "|" + "" + ruta12.get(22).latitude + "," + ruta12.get(22).longitude + "|" + "" + ruta12.get(23).latitude + "," + ruta12.get(23).longitude + "&key=" + APIkey;
                }
            case 13:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta13.get(0).latitude + "," + ruta13.get(0).longitude + "&destination=" + ruta13.get(24).latitude + "," + ruta13.get(24).longitude + "&waypoints=optimize:true|" + ruta13.get(1).latitude + "," + ruta13.get(1).longitude + "|" + "" + ruta13.get(2).latitude + "," + ruta13.get(2).longitude + "|" + "" + ruta13.get(3).latitude + "," + ruta13.get(3).longitude + "|" + "" + ruta13.get(4).latitude + "," + ruta13.get(4).longitude + "|" + "" + ruta13.get(5).latitude + "," + ruta13.get(5).longitude + "|" + "" + ruta13.get(6).latitude + "," + ruta13.get(6).longitude + "|" + "" + ruta13.get(7).latitude + "," + ruta13.get(7).longitude + "|" + "" + ruta13.get(8).latitude + "," + ruta13.get(8).longitude + "|" + "" + ruta13.get(9).latitude + "," + ruta13.get(9).longitude + "|" + "" + ruta13.get(10).latitude + "," + ruta13.get(10).longitude + "|" + "" + ruta13.get(11).latitude + "," + ruta13.get(11).longitude + "|" + "" + ruta13.get(12).latitude + "," + ruta13.get(12).longitude + "|" + "" + ruta13.get(13).latitude + "," + ruta13.get(13).longitude + "|" + "" + ruta13.get(14).latitude + "," + ruta13.get(14).longitude + "|" + "" + ruta13.get(15).latitude + "," + ruta13.get(15).longitude + "|" + "" + ruta13.get(16).latitude + "," + ruta13.get(16).longitude + "|" + "" + ruta13.get(17).latitude + "," + ruta13.get(17).longitude + "|" + "" + ruta13.get(18).latitude + "," + ruta13.get(18).longitude + "|" + "" + ruta13.get(19).latitude + "," + ruta13.get(19).longitude + "|" + "" + ruta13.get(20).latitude + "," + ruta13.get(20).longitude + "|" + "" + ruta13.get(21).latitude + "," + ruta13.get(21).longitude + "|" + "" + ruta13.get(22).latitude + "," + ruta13.get(22).longitude + "|" + "" + ruta13.get(23).latitude + "," + ruta13.get(23).longitude + "&key=" + APIkey;
                }
            case 14:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta14.get(0).latitude + "," + ruta14.get(0).longitude + "&destination=" + ruta14.get(24).latitude + "," + ruta14.get(24).longitude + "&waypoints=optimize:true|" + ruta14.get(1).latitude + "," + ruta14.get(1).longitude + "|" + "" + ruta14.get(2).latitude + "," + ruta14.get(2).longitude + "|" + "" + ruta14.get(3).latitude + "," + ruta14.get(3).longitude + "|" + "" + ruta14.get(4).latitude + "," + ruta14.get(4).longitude + "|" + "" + ruta14.get(5).latitude + "," + ruta14.get(5).longitude + "|" + "" + ruta14.get(6).latitude + "," + ruta14.get(6).longitude + "|" + "" + ruta14.get(7).latitude + "," + ruta14.get(7).longitude + "|" + "" + ruta14.get(8).latitude + "," + ruta14.get(8).longitude + "|" + "" + ruta14.get(9).latitude + "," + ruta14.get(9).longitude + "|" + "" + ruta14.get(10).latitude + "," + ruta14.get(10).longitude + "|" + "" + ruta14.get(11).latitude + "," + ruta14.get(11).longitude + "|" + "" + ruta14.get(12).latitude + "," + ruta14.get(12).longitude + "|" + "" + ruta14.get(13).latitude + "," + ruta14.get(13).longitude + "|" + "" + ruta14.get(14).latitude + "," + ruta14.get(14).longitude + "|" + "" + ruta14.get(15).latitude + "," + ruta14.get(15).longitude + "|" + "" + ruta14.get(16).latitude + "," + ruta14.get(16).longitude + "|" + "" + ruta14.get(17).latitude + "," + ruta14.get(17).longitude + "|" + "" + ruta14.get(18).latitude + "," + ruta14.get(18).longitude + "|" + "" + ruta14.get(19).latitude + "," + ruta14.get(19).longitude + "|" + "" + ruta14.get(20).latitude + "," + ruta14.get(20).longitude + "|" + "" + ruta14.get(21).latitude + "," + ruta14.get(21).longitude + "|" + "" + ruta14.get(22).latitude + "," + ruta14.get(22).longitude + "|" + "" + ruta14.get(23).latitude + "," + ruta14.get(23).longitude + "&key=" + APIkey;
                }
            case 15:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta15.get(0).latitude + "," + ruta15.get(0).longitude + "&destination=" + ruta15.get(24).latitude + "," + ruta15.get(24).longitude + "&waypoints=optimize:true|" + ruta15.get(1).latitude + "," + ruta15.get(1).longitude + "|" + "" + ruta15.get(2).latitude + "," + ruta15.get(2).longitude + "|" + "" + ruta15.get(3).latitude + "," + ruta15.get(3).longitude + "|" + "" + ruta15.get(4).latitude + "," + ruta15.get(4).longitude + "|" + "" + ruta15.get(5).latitude + "," + ruta15.get(5).longitude + "|" + "" + ruta15.get(6).latitude + "," + ruta15.get(6).longitude + "|" + "" + ruta15.get(7).latitude + "," + ruta15.get(7).longitude + "|" + "" + ruta15.get(8).latitude + "," + ruta15.get(8).longitude + "|" + "" + ruta15.get(9).latitude + "," + ruta15.get(9).longitude + "|" + "" + ruta15.get(10).latitude + "," + ruta15.get(10).longitude + "|" + "" + ruta15.get(11).latitude + "," + ruta15.get(11).longitude + "|" + "" + ruta15.get(12).latitude + "," + ruta15.get(12).longitude + "|" + "" + ruta15.get(13).latitude + "," + ruta15.get(13).longitude + "|" + "" + ruta15.get(14).latitude + "," + ruta15.get(14).longitude + "|" + "" + ruta15.get(15).latitude + "," + ruta15.get(15).longitude + "|" + "" + ruta15.get(16).latitude + "," + ruta15.get(16).longitude + "|" + "" + ruta15.get(17).latitude + "," + ruta15.get(17).longitude + "|" + "" + ruta15.get(18).latitude + "," + ruta15.get(18).longitude + "|" + "" + ruta15.get(19).latitude + "," + ruta15.get(19).longitude + "|" + "" + ruta15.get(20).latitude + "," + ruta15.get(20).longitude + "|" + "" + ruta15.get(21).latitude + "," + ruta15.get(21).longitude + "|" + "" + ruta15.get(22).latitude + "," + ruta15.get(22).longitude + "|" + "" + ruta15.get(23).latitude + "," + ruta15.get(23).longitude + "&key=" + APIkey;
                }
            case 16:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta16.get(0).latitude + "," + ruta16.get(0).longitude + "&destination=" + ruta16.get(24).latitude + "," + ruta16.get(24).longitude + "&waypoints=optimize:true|" + ruta16.get(1).latitude + "," + ruta16.get(1).longitude + "|" + "" + ruta16.get(2).latitude + "," + ruta16.get(2).longitude + "|" + "" + ruta16.get(3).latitude + "," + ruta16.get(3).longitude + "|" + "" + ruta16.get(4).latitude + "," + ruta16.get(4).longitude + "|" + "" + ruta16.get(5).latitude + "," + ruta16.get(5).longitude + "|" + "" + ruta16.get(6).latitude + "," + ruta16.get(6).longitude + "|" + "" + ruta16.get(7).latitude + "," + ruta16.get(7).longitude + "|" + "" + ruta16.get(8).latitude + "," + ruta16.get(8).longitude + "|" + "" + ruta16.get(9).latitude + "," + ruta16.get(9).longitude + "|" + "" + ruta16.get(10).latitude + "," + ruta16.get(10).longitude + "|" + "" + ruta16.get(11).latitude + "," + ruta16.get(11).longitude + "|" + "" + ruta16.get(12).latitude + "," + ruta16.get(12).longitude + "|" + "" + ruta16.get(13).latitude + "," + ruta16.get(13).longitude + "|" + "" + ruta16.get(14).latitude + "," + ruta16.get(14).longitude + "|" + "" + ruta16.get(15).latitude + "," + ruta16.get(15).longitude + "|" + "" + ruta16.get(16).latitude + "," + ruta16.get(16).longitude + "|" + "" + ruta16.get(17).latitude + "," + ruta16.get(17).longitude + "|" + "" + ruta16.get(18).latitude + "," + ruta16.get(18).longitude + "|" + "" + ruta16.get(19).latitude + "," + ruta16.get(19).longitude + "|" + "" + ruta16.get(20).latitude + "," + ruta16.get(20).longitude + "|" + "" + ruta16.get(21).latitude + "," + ruta16.get(21).longitude + "|" + "" + ruta16.get(22).latitude + "," + ruta16.get(22).longitude + "|" + "" + ruta16.get(23).latitude + "," + ruta16.get(23).longitude + "&key=" + APIkey;
                }
            case 17:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta17.get(0).latitude + "," + ruta17.get(0).longitude + "&destination=" + ruta17.get(24).latitude + "," + ruta17.get(24).longitude + "&waypoints=optimize:true|" + ruta17.get(1).latitude + "," + ruta17.get(1).longitude + "|" + "" + ruta17.get(2).latitude + "," + ruta17.get(2).longitude + "|" + "" + ruta17.get(3).latitude + "," + ruta17.get(3).longitude + "|" + "" + ruta17.get(4).latitude + "," + ruta17.get(4).longitude + "|" + "" + ruta17.get(5).latitude + "," + ruta17.get(5).longitude + "|" + "" + ruta17.get(6).latitude + "," + ruta17.get(6).longitude + "|" + "" + ruta17.get(7).latitude + "," + ruta17.get(7).longitude + "|" + "" + ruta17.get(8).latitude + "," + ruta17.get(8).longitude + "|" + "" + ruta17.get(9).latitude + "," + ruta17.get(9).longitude + "|" + "" + ruta17.get(10).latitude + "," + ruta17.get(10).longitude + "|" + "" + ruta17.get(11).latitude + "," + ruta17.get(11).longitude + "|" + "" + ruta17.get(12).latitude + "," + ruta17.get(12).longitude + "|" + "" + ruta17.get(13).latitude + "," + ruta17.get(13).longitude + "|" + "" + ruta17.get(14).latitude + "," + ruta17.get(14).longitude + "|" + "" + ruta17.get(15).latitude + "," + ruta17.get(15).longitude + "|" + "" + ruta17.get(16).latitude + "," + ruta17.get(16).longitude + "|" + "" + ruta17.get(17).latitude + "," + ruta17.get(17).longitude + "|" + "" + ruta17.get(18).latitude + "," + ruta17.get(18).longitude + "|" + "" + ruta17.get(19).latitude + "," + ruta17.get(19).longitude + "|" + "" + ruta17.get(20).latitude + "," + ruta17.get(20).longitude + "|" + "" + ruta17.get(21).latitude + "," + ruta17.get(21).longitude + "|" + "" + ruta17.get(22).latitude + "," + ruta17.get(22).longitude + "|" + "" + ruta17.get(23).latitude + "," + ruta17.get(23).longitude + "&key=" + APIkey;
                }
            case 18:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta18.get(0).latitude + "," + ruta18.get(0).longitude + "&destination=" + ruta18.get(24).latitude + "," + ruta18.get(24).longitude + "&waypoints=optimize:true|" + ruta18.get(1).latitude + "," + ruta18.get(1).longitude + "|" + "" + ruta18.get(2).latitude + "," + ruta18.get(2).longitude + "|" + "" + ruta18.get(3).latitude + "," + ruta18.get(3).longitude + "|" + "" + ruta18.get(4).latitude + "," + ruta18.get(4).longitude + "|" + "" + ruta18.get(5).latitude + "," + ruta18.get(5).longitude + "|" + "" + ruta18.get(6).latitude + "," + ruta18.get(6).longitude + "|" + "" + ruta18.get(7).latitude + "," + ruta18.get(7).longitude + "|" + "" + ruta18.get(8).latitude + "," + ruta18.get(8).longitude + "|" + "" + ruta18.get(9).latitude + "," + ruta18.get(9).longitude + "|" + "" + ruta18.get(10).latitude + "," + ruta18.get(10).longitude + "|" + "" + ruta18.get(11).latitude + "," + ruta18.get(11).longitude + "|" + "" + ruta18.get(12).latitude + "," + ruta18.get(12).longitude + "|" + "" + ruta18.get(13).latitude + "," + ruta18.get(13).longitude + "|" + "" + ruta18.get(14).latitude + "," + ruta18.get(14).longitude + "|" + "" + ruta18.get(15).latitude + "," + ruta18.get(15).longitude + "|" + "" + ruta18.get(16).latitude + "," + ruta18.get(16).longitude + "|" + "" + ruta18.get(17).latitude + "," + ruta18.get(17).longitude + "|" + "" + ruta18.get(18).latitude + "," + ruta18.get(18).longitude + "|" + "" + ruta18.get(19).latitude + "," + ruta18.get(19).longitude + "|" + "" + ruta18.get(20).latitude + "," + ruta18.get(20).longitude + "|" + "" + ruta18.get(21).latitude + "," + ruta18.get(21).longitude + "|" + "" + ruta18.get(22).latitude + "," + ruta18.get(22).longitude + "|" + "" + ruta18.get(23).latitude + "," + ruta18.get(23).longitude + "&key=" + APIkey;
                }
            case 19:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta19.get(0).latitude + "," + ruta19.get(0).longitude + "&destination=" + ruta19.get(24).latitude + "," + ruta19.get(24).longitude + "&waypoints=optimize:true|" + ruta19.get(1).latitude + "," + ruta19.get(1).longitude + "|" + "" + ruta19.get(2).latitude + "," + ruta19.get(2).longitude + "|" + "" + ruta19.get(3).latitude + "," + ruta19.get(3).longitude + "|" + "" + ruta19.get(4).latitude + "," + ruta19.get(4).longitude + "|" + "" + ruta19.get(5).latitude + "," + ruta19.get(5).longitude + "|" + "" + ruta19.get(6).latitude + "," + ruta19.get(6).longitude + "|" + "" + ruta19.get(7).latitude + "," + ruta19.get(7).longitude + "|" + "" + ruta19.get(8).latitude + "," + ruta19.get(8).longitude + "|" + "" + ruta19.get(9).latitude + "," + ruta19.get(9).longitude + "|" + "" + ruta19.get(10).latitude + "," + ruta19.get(10).longitude + "|" + "" + ruta19.get(11).latitude + "," + ruta19.get(11).longitude + "|" + "" + ruta19.get(12).latitude + "," + ruta19.get(12).longitude + "|" + "" + ruta19.get(13).latitude + "," + ruta19.get(13).longitude + "|" + "" + ruta19.get(14).latitude + "," + ruta19.get(14).longitude + "|" + "" + ruta19.get(15).latitude + "," + ruta19.get(15).longitude + "|" + "" + ruta19.get(16).latitude + "," + ruta19.get(16).longitude + "|" + "" + ruta19.get(17).latitude + "," + ruta19.get(17).longitude + "|" + "" + ruta19.get(18).latitude + "," + ruta19.get(18).longitude + "|" + "" + ruta19.get(19).latitude + "," + ruta19.get(19).longitude + "|" + "" + ruta19.get(20).latitude + "," + ruta19.get(20).longitude + "|" + "" + ruta19.get(21).latitude + "," + ruta19.get(21).longitude + "|" + "" + ruta19.get(22).latitude + "," + ruta19.get(22).longitude + "|" + "" + ruta19.get(23).latitude + "," + ruta19.get(23).longitude + "&key=" + APIkey;
                }

            case 20:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta20.get(0).latitude + "," + ruta20.get(0).longitude + "&destination=" + ruta20.get(24).latitude + "," + ruta20.get(24).longitude + "&waypoints=optimize:true|" + ruta20.get(1).latitude + "," + ruta20.get(1).longitude + "|" + "" + ruta20.get(2).latitude + "," + ruta20.get(2).longitude + "|" + "" + ruta20.get(3).latitude + "," + ruta20.get(3).longitude + "|" + "" + ruta20.get(4).latitude + "," + ruta20.get(4).longitude + "|" + "" + ruta20.get(5).latitude + "," + ruta20.get(5).longitude + "|" + "" + ruta20.get(6).latitude + "," + ruta20.get(6).longitude + "|" + "" + ruta20.get(7).latitude + "," + ruta20.get(7).longitude + "|" + "" + ruta20.get(8).latitude + "," + ruta20.get(8).longitude + "|" + "" + ruta20.get(9).latitude + "," + ruta20.get(9).longitude + "|" + "" + ruta20.get(10).latitude + "," + ruta20.get(10).longitude + "|" + "" + ruta20.get(11).latitude + "," + ruta20.get(11).longitude + "|" + "" + ruta20.get(12).latitude + "," + ruta20.get(12).longitude + "|" + "" + ruta20.get(13).latitude + "," + ruta20.get(13).longitude + "|" + "" + ruta20.get(14).latitude + "," + ruta20.get(14).longitude + "|" + "" + ruta20.get(15).latitude + "," + ruta20.get(15).longitude + "|" + "" + ruta20.get(16).latitude + "," + ruta20.get(16).longitude + "|" + "" + ruta20.get(17).latitude + "," + ruta20.get(17).longitude + "|" + "" + ruta20.get(18).latitude + "," + ruta20.get(18).longitude + "|" + "" + ruta20.get(19).latitude + "," + ruta20.get(19).longitude + "|" + "" + ruta20.get(20).latitude + "," + ruta20.get(20).longitude + "|" + "" + ruta20.get(21).latitude + "," + ruta20.get(21).longitude + "|" + "" + ruta20.get(22).latitude + "," + ruta20.get(22).longitude + "|" + "" + ruta20.get(23).latitude + "," + ruta20.get(23).longitude + "&key=" + APIkey;
                }
            case 21:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta21.get(0).latitude + "," + ruta21.get(0).longitude + "&destination=" + ruta21.get(24).latitude + "," + ruta21.get(24).longitude + "&waypoints=optimize:true|" + ruta21.get(1).latitude + "," + ruta21.get(1).longitude + "|" + "" + ruta21.get(2).latitude + "," + ruta21.get(2).longitude + "|" + "" + ruta21.get(3).latitude + "," + ruta21.get(3).longitude + "|" + "" + ruta21.get(4).latitude + "," + ruta21.get(4).longitude + "|" + "" + ruta21.get(5).latitude + "," + ruta21.get(5).longitude + "|" + "" + ruta21.get(6).latitude + "," + ruta21.get(6).longitude + "|" + "" + ruta21.get(7).latitude + "," + ruta21.get(7).longitude + "|" + "" + ruta21.get(8).latitude + "," + ruta21.get(8).longitude + "|" + "" + ruta21.get(9).latitude + "," + ruta21.get(9).longitude + "|" + "" + ruta21.get(10).latitude + "," + ruta21.get(10).longitude + "|" + "" + ruta21.get(11).latitude + "," + ruta21.get(11).longitude + "|" + "" + ruta21.get(12).latitude + "," + ruta21.get(12).longitude + "|" + "" + ruta21.get(13).latitude + "," + ruta21.get(13).longitude + "|" + "" + ruta21.get(14).latitude + "," + ruta21.get(14).longitude + "|" + "" + ruta21.get(15).latitude + "," + ruta21.get(15).longitude + "|" + "" + ruta21.get(16).latitude + "," + ruta21.get(16).longitude + "|" + "" + ruta21.get(17).latitude + "," + ruta21.get(17).longitude + "|" + "" + ruta21.get(18).latitude + "," + ruta21.get(18).longitude + "|" + "" + ruta21.get(19).latitude + "," + ruta21.get(19).longitude + "|" + "" + ruta21.get(20).latitude + "," + ruta21.get(20).longitude + "|" + "" + ruta21.get(21).latitude + "," + ruta21.get(21).longitude + "|" + "" + ruta21.get(22).latitude + "," + ruta21.get(22).longitude + "|" + "" + ruta21.get(23).latitude + "," + ruta21.get(23).longitude + "&key=" + APIkey;
                }
            case 22:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta22.get(0).latitude + "," + ruta22.get(0).longitude + "&destination=" + ruta22.get(24).latitude + "," + ruta22.get(24).longitude + "&waypoints=optimize:true|" + ruta22.get(1).latitude + "," + ruta22.get(1).longitude + "|" + "" + ruta22.get(2).latitude + "," + ruta22.get(2).longitude + "|" + "" + ruta22.get(3).latitude + "," + ruta22.get(3).longitude + "|" + "" + ruta22.get(4).latitude + "," + ruta22.get(4).longitude + "|" + "" + ruta22.get(5).latitude + "," + ruta22.get(5).longitude + "|" + "" + ruta22.get(6).latitude + "," + ruta22.get(6).longitude + "|" + "" + ruta22.get(7).latitude + "," + ruta22.get(7).longitude + "|" + "" + ruta22.get(8).latitude + "," + ruta22.get(8).longitude + "|" + "" + ruta22.get(9).latitude + "," + ruta22.get(9).longitude + "|" + "" + ruta22.get(10).latitude + "," + ruta22.get(10).longitude + "|" + "" + ruta22.get(11).latitude + "," + ruta22.get(11).longitude + "|" + "" + ruta22.get(12).latitude + "," + ruta22.get(12).longitude + "|" + "" + ruta22.get(13).latitude + "," + ruta22.get(13).longitude + "|" + "" + ruta22.get(14).latitude + "," + ruta22.get(14).longitude + "|" + "" + ruta22.get(15).latitude + "," + ruta22.get(15).longitude + "|" + "" + ruta22.get(16).latitude + "," + ruta22.get(16).longitude + "|" + "" + ruta22.get(17).latitude + "," + ruta22.get(17).longitude + "|" + "" + ruta22.get(18).latitude + "," + ruta22.get(18).longitude + "|" + "" + ruta22.get(19).latitude + "," + ruta22.get(19).longitude + "|" + "" + ruta22.get(20).latitude + "," + ruta22.get(20).longitude + "|" + "" + ruta22.get(21).latitude + "," + ruta22.get(21).longitude + "|" + "" + ruta22.get(22).latitude + "," + ruta22.get(22).longitude + "|" + "" + ruta22.get(23).latitude + "," + ruta22.get(23).longitude + "&key=" + APIkey;
                }
            case 23:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta23.get(0).latitude + "," + ruta23.get(0).longitude + "&destination=" + ruta23.get(24).latitude + "," + ruta23.get(24).longitude + "&waypoints=optimize:true|" + ruta23.get(1).latitude + "," + ruta23.get(1).longitude + "|" + "" + ruta23.get(2).latitude + "," + ruta23.get(2).longitude + "|" + "" + ruta23.get(3).latitude + "," + ruta23.get(3).longitude + "|" + "" + ruta23.get(4).latitude + "," + ruta23.get(4).longitude + "|" + "" + ruta23.get(5).latitude + "," + ruta23.get(5).longitude + "|" + "" + ruta23.get(6).latitude + "," + ruta23.get(6).longitude + "|" + "" + ruta23.get(7).latitude + "," + ruta23.get(7).longitude + "|" + "" + ruta23.get(8).latitude + "," + ruta23.get(8).longitude + "|" + "" + ruta23.get(9).latitude + "," + ruta23.get(9).longitude + "|" + "" + ruta23.get(10).latitude + "," + ruta23.get(10).longitude + "|" + "" + ruta23.get(11).latitude + "," + ruta23.get(11).longitude + "|" + "" + ruta23.get(12).latitude + "," + ruta23.get(12).longitude + "|" + "" + ruta23.get(13).latitude + "," + ruta23.get(13).longitude + "|" + "" + ruta23.get(14).latitude + "," + ruta23.get(14).longitude + "|" + "" + ruta23.get(15).latitude + "," + ruta23.get(15).longitude + "|" + "" + ruta23.get(16).latitude + "," + ruta23.get(16).longitude + "|" + "" + ruta23.get(17).latitude + "," + ruta23.get(17).longitude + "|" + "" + ruta23.get(18).latitude + "," + ruta23.get(18).longitude + "|" + "" + ruta23.get(19).latitude + "," + ruta23.get(19).longitude + "|" + "" + ruta23.get(20).latitude + "," + ruta23.get(20).longitude + "|" + "" + ruta23.get(21).latitude + "," + ruta23.get(21).longitude + "|" + "" + ruta23.get(22).latitude + "," + ruta23.get(22).longitude + "|" + "" + ruta23.get(23).latitude + "," + ruta23.get(23).longitude + "&key=" + APIkey;
                }
            case 24:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta24.get(0).latitude + "," + ruta24.get(0).longitude + "&destination=" + ruta24.get(24).latitude + "," + ruta24.get(24).longitude + "&waypoints=optimize:true|" + ruta24.get(1).latitude + "," + ruta24.get(1).longitude + "|" + "" + ruta24.get(2).latitude + "," + ruta24.get(2).longitude + "|" + "" + ruta24.get(3).latitude + "," + ruta24.get(3).longitude + "|" + "" + ruta24.get(4).latitude + "," + ruta24.get(4).longitude + "|" + "" + ruta24.get(5).latitude + "," + ruta24.get(5).longitude + "|" + "" + ruta24.get(6).latitude + "," + ruta24.get(6).longitude + "|" + "" + ruta24.get(7).latitude + "," + ruta24.get(7).longitude + "|" + "" + ruta24.get(8).latitude + "," + ruta24.get(8).longitude + "|" + "" + ruta24.get(9).latitude + "," + ruta24.get(9).longitude + "|" + "" + ruta24.get(10).latitude + "," + ruta24.get(10).longitude + "|" + "" + ruta24.get(11).latitude + "," + ruta24.get(11).longitude + "|" + "" + ruta24.get(12).latitude + "," + ruta24.get(12).longitude + "|" + "" + ruta24.get(13).latitude + "," + ruta24.get(13).longitude + "|" + "" + ruta24.get(14).latitude + "," + ruta24.get(14).longitude + "|" + "" + ruta24.get(15).latitude + "," + ruta24.get(15).longitude + "|" + "" + ruta24.get(16).latitude + "," + ruta24.get(16).longitude + "|" + "" + ruta24.get(17).latitude + "," + ruta24.get(17).longitude + "|" + "" + ruta24.get(18).latitude + "," + ruta24.get(18).longitude + "|" + "" + ruta24.get(19).latitude + "," + ruta24.get(19).longitude + "|" + "" + ruta24.get(20).latitude + "," + ruta24.get(20).longitude + "|" + "" + ruta24.get(21).latitude + "," + ruta24.get(21).longitude + "|" + "" + ruta24.get(22).latitude + "," + ruta24.get(22).longitude + "|" + "" + ruta24.get(23).latitude + "," + ruta24.get(23).longitude + "&key=" + APIkey;
                }

            case 25:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta25.get(0).latitude + "," + ruta25.get(0).longitude + "&destination=" + ruta25.get(24).latitude + "," + ruta25.get(24).longitude + "&waypoints=optimize:true|" + ruta25.get(1).latitude + "," + ruta25.get(1).longitude + "|" + "" + ruta25.get(2).latitude + "," + ruta25.get(2).longitude + "|" + "" + ruta25.get(3).latitude + "," + ruta25.get(3).longitude + "|" + "" + ruta25.get(4).latitude + "," + ruta25.get(4).longitude + "|" + "" + ruta25.get(5).latitude + "," + ruta25.get(5).longitude + "|" + "" + ruta25.get(6).latitude + "," + ruta25.get(6).longitude + "|" + "" + ruta25.get(7).latitude + "," + ruta25.get(7).longitude + "|" + "" + ruta25.get(8).latitude + "," + ruta25.get(8).longitude + "|" + "" + ruta25.get(9).latitude + "," + ruta25.get(9).longitude + "|" + "" + ruta25.get(10).latitude + "," + ruta25.get(10).longitude + "|" + "" + ruta25.get(11).latitude + "," + ruta25.get(11).longitude + "|" + "" + ruta25.get(12).latitude + "," + ruta25.get(12).longitude + "|" + "" + ruta25.get(13).latitude + "," + ruta25.get(13).longitude + "|" + "" + ruta25.get(14).latitude + "," + ruta25.get(14).longitude + "|" + "" + ruta25.get(15).latitude + "," + ruta25.get(15).longitude + "|" + "" + ruta25.get(16).latitude + "," + ruta25.get(16).longitude + "|" + "" + ruta25.get(17).latitude + "," + ruta25.get(17).longitude + "|" + "" + ruta25.get(18).latitude + "," + ruta25.get(18).longitude + "|" + "" + ruta25.get(19).latitude + "," + ruta25.get(19).longitude + "|" + "" + ruta25.get(20).latitude + "," + ruta25.get(20).longitude + "|" + "" + ruta25.get(21).latitude + "," + ruta25.get(21).longitude + "|" + "" + ruta25.get(22).latitude + "," + ruta25.get(22).longitude + "|" + "" + ruta25.get(23).latitude + "," + ruta25.get(23).longitude + "&key=" + APIkey;
                }
            case 26:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta26.get(0).latitude + "," + ruta26.get(0).longitude + "&destination=" + ruta26.get(24).latitude + "," + ruta26.get(24).longitude + "&waypoints=optimize:true|" + ruta26.get(1).latitude + "," + ruta26.get(1).longitude + "|" + "" + ruta26.get(2).latitude + "," + ruta26.get(2).longitude + "|" + "" + ruta26.get(3).latitude + "," + ruta26.get(3).longitude + "|" + "" + ruta26.get(4).latitude + "," + ruta26.get(4).longitude + "|" + "" + ruta26.get(5).latitude + "," + ruta26.get(5).longitude + "|" + "" + ruta26.get(6).latitude + "," + ruta26.get(6).longitude + "|" + "" + ruta26.get(7).latitude + "," + ruta26.get(7).longitude + "|" + "" + ruta26.get(8).latitude + "," + ruta26.get(8).longitude + "|" + "" + ruta26.get(9).latitude + "," + ruta26.get(9).longitude + "|" + "" + ruta26.get(10).latitude + "," + ruta26.get(10).longitude + "|" + "" + ruta26.get(11).latitude + "," + ruta26.get(11).longitude + "|" + "" + ruta26.get(12).latitude + "," + ruta26.get(12).longitude + "|" + "" + ruta26.get(13).latitude + "," + ruta26.get(13).longitude + "|" + "" + ruta26.get(14).latitude + "," + ruta26.get(14).longitude + "|" + "" + ruta26.get(15).latitude + "," + ruta26.get(15).longitude + "|" + "" + ruta26.get(16).latitude + "," + ruta26.get(16).longitude + "|" + "" + ruta26.get(17).latitude + "," + ruta26.get(17).longitude + "|" + "" + ruta26.get(18).latitude + "," + ruta26.get(18).longitude + "|" + "" + ruta26.get(19).latitude + "," + ruta26.get(19).longitude + "|" + "" + ruta26.get(20).latitude + "," + ruta26.get(20).longitude + "|" + "" + ruta26.get(21).latitude + "," + ruta26.get(21).longitude + "|" + "" + ruta26.get(22).latitude + "," + ruta26.get(22).longitude + "|" + "" + ruta26.get(23).latitude + "," + ruta26.get(23).longitude + "&key=" + APIkey;
                }
            case 27:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta27.get(0).latitude + "," + ruta27.get(0).longitude + "&destination=" + ruta27.get(24).latitude + "," + ruta27.get(24).longitude + "&waypoints=optimize:true|" + ruta27.get(1).latitude + "," + ruta27.get(1).longitude + "|" + "" + ruta27.get(2).latitude + "," + ruta27.get(2).longitude + "|" + "" + ruta27.get(3).latitude + "," + ruta27.get(3).longitude + "|" + "" + ruta27.get(4).latitude + "," + ruta27.get(4).longitude + "|" + "" + ruta27.get(5).latitude + "," + ruta27.get(5).longitude + "|" + "" + ruta27.get(6).latitude + "," + ruta27.get(6).longitude + "|" + "" + ruta27.get(7).latitude + "," + ruta27.get(7).longitude + "|" + "" + ruta27.get(8).latitude + "," + ruta27.get(8).longitude + "|" + "" + ruta27.get(9).latitude + "," + ruta27.get(9).longitude + "|" + "" + ruta27.get(10).latitude + "," + ruta27.get(10).longitude + "|" + "" + ruta27.get(11).latitude + "," + ruta27.get(11).longitude + "|" + "" + ruta27.get(12).latitude + "," + ruta27.get(12).longitude + "|" + "" + ruta27.get(13).latitude + "," + ruta27.get(13).longitude + "|" + "" + ruta27.get(14).latitude + "," + ruta27.get(14).longitude + "|" + "" + ruta27.get(15).latitude + "," + ruta27.get(15).longitude + "|" + "" + ruta27.get(16).latitude + "," + ruta27.get(16).longitude + "|" + "" + ruta27.get(17).latitude + "," + ruta27.get(17).longitude + "|" + "" + ruta27.get(18).latitude + "," + ruta27.get(18).longitude + "|" + "" + ruta27.get(19).latitude + "," + ruta27.get(19).longitude + "|" + "" + ruta27.get(20).latitude + "," + ruta27.get(20).longitude + "|" + "" + ruta27.get(21).latitude + "," + ruta27.get(21).longitude + "|" + "" + ruta27.get(22).latitude + "," + ruta27.get(22).longitude + "|" + "" + ruta27.get(23).latitude + "," + ruta27.get(23).longitude + "&key=" + APIkey;
                }
            case 28:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta28.get(0).latitude + "," + ruta28.get(0).longitude + "&destination=" + ruta28.get(24).latitude + "," + ruta28.get(24).longitude + "&waypoints=optimize:true|" + ruta28.get(1).latitude + "," + ruta28.get(1).longitude + "|" + "" + ruta28.get(2).latitude + "," + ruta28.get(2).longitude + "|" + "" + ruta28.get(3).latitude + "," + ruta28.get(3).longitude + "|" + "" + ruta28.get(4).latitude + "," + ruta28.get(4).longitude + "|" + "" + ruta28.get(5).latitude + "," + ruta28.get(5).longitude + "|" + "" + ruta28.get(6).latitude + "," + ruta28.get(6).longitude + "|" + "" + ruta28.get(7).latitude + "," + ruta28.get(7).longitude + "|" + "" + ruta28.get(8).latitude + "," + ruta28.get(8).longitude + "|" + "" + ruta28.get(9).latitude + "," + ruta28.get(9).longitude + "|" + "" + ruta28.get(10).latitude + "," + ruta28.get(10).longitude + "|" + "" + ruta28.get(11).latitude + "," + ruta28.get(11).longitude + "|" + "" + ruta28.get(12).latitude + "," + ruta28.get(12).longitude + "|" + "" + ruta28.get(13).latitude + "," + ruta28.get(13).longitude + "|" + "" + ruta28.get(14).latitude + "," + ruta28.get(14).longitude + "|" + "" + ruta28.get(15).latitude + "," + ruta28.get(15).longitude + "|" + "" + ruta28.get(16).latitude + "," + ruta28.get(16).longitude + "|" + "" + ruta28.get(17).latitude + "," + ruta28.get(17).longitude + "|" + "" + ruta28.get(18).latitude + "," + ruta28.get(18).longitude + "|" + "" + ruta28.get(19).latitude + "," + ruta28.get(19).longitude + "|" + "" + ruta28.get(20).latitude + "," + ruta28.get(20).longitude + "|" + "" + ruta28.get(21).latitude + "," + ruta28.get(21).longitude + "|" + "" + ruta28.get(22).latitude + "," + ruta28.get(22).longitude + "|" + "" + ruta28.get(23).latitude + "," + ruta28.get(23).longitude + "&key=" + APIkey;
                }
            case 29:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta29.get(0).latitude + "," + ruta29.get(0).longitude + "&destination=" + ruta29.get(24).latitude + "," + ruta29.get(24).longitude + "&waypoints=optimize:true|" + ruta29.get(1).latitude + "," + ruta29.get(1).longitude + "|" + "" + ruta29.get(2).latitude + "," + ruta29.get(2).longitude + "|" + "" + ruta29.get(3).latitude + "," + ruta29.get(3).longitude + "|" + "" + ruta29.get(4).latitude + "," + ruta29.get(4).longitude + "|" + "" + ruta29.get(5).latitude + "," + ruta29.get(5).longitude + "|" + "" + ruta29.get(6).latitude + "," + ruta29.get(6).longitude + "|" + "" + ruta29.get(7).latitude + "," + ruta29.get(7).longitude + "|" + "" + ruta29.get(8).latitude + "," + ruta29.get(8).longitude + "|" + "" + ruta29.get(9).latitude + "," + ruta29.get(9).longitude + "|" + "" + ruta29.get(10).latitude + "," + ruta29.get(10).longitude + "|" + "" + ruta29.get(11).latitude + "," + ruta29.get(11).longitude + "|" + "" + ruta29.get(12).latitude + "," + ruta29.get(12).longitude + "|" + "" + ruta29.get(13).latitude + "," + ruta29.get(13).longitude + "|" + "" + ruta29.get(14).latitude + "," + ruta29.get(14).longitude + "|" + "" + ruta29.get(15).latitude + "," + ruta29.get(15).longitude + "|" + "" + ruta29.get(16).latitude + "," + ruta29.get(16).longitude + "|" + "" + ruta29.get(17).latitude + "," + ruta29.get(17).longitude + "|" + "" + ruta29.get(18).latitude + "," + ruta29.get(18).longitude + "|" + "" + ruta29.get(19).latitude + "," + ruta29.get(19).longitude + "|" + "" + ruta29.get(20).latitude + "," + ruta29.get(20).longitude + "|" + "" + ruta29.get(21).latitude + "," + ruta29.get(21).longitude + "|" + "" + ruta29.get(22).latitude + "," + ruta29.get(22).longitude + "|" + "" + ruta29.get(23).latitude + "," + ruta29.get(23).longitude + "&key=" + APIkey;
                }
            case 30:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta30.get(0).latitude + "," + ruta30.get(0).longitude + "&destination=" + ruta30.get(24).latitude + "," + ruta30.get(24).longitude + "&waypoints=optimize:true|" + ruta30.get(1).latitude + "," + ruta30.get(1).longitude + "|" + "" + ruta30.get(2).latitude + "," + ruta30.get(2).longitude + "|" + "" + ruta30.get(3).latitude + "," + ruta30.get(3).longitude + "|" + "" + ruta30.get(4).latitude + "," + ruta30.get(4).longitude + "|" + "" + ruta30.get(5).latitude + "," + ruta30.get(5).longitude + "|" + "" + ruta30.get(6).latitude + "," + ruta30.get(6).longitude + "|" + "" + ruta30.get(7).latitude + "," + ruta30.get(7).longitude + "|" + "" + ruta30.get(8).latitude + "," + ruta30.get(8).longitude + "|" + "" + ruta30.get(9).latitude + "," + ruta30.get(9).longitude + "|" + "" + ruta30.get(10).latitude + "," + ruta30.get(10).longitude + "|" + "" + ruta30.get(11).latitude + "," + ruta30.get(11).longitude + "|" + "" + ruta30.get(12).latitude + "," + ruta30.get(12).longitude + "|" + "" + ruta30.get(13).latitude + "," + ruta30.get(13).longitude + "|" + "" + ruta30.get(14).latitude + "," + ruta30.get(14).longitude + "|" + "" + ruta30.get(15).latitude + "," + ruta30.get(15).longitude + "|" + "" + ruta30.get(16).latitude + "," + ruta30.get(16).longitude + "|" + "" + ruta30.get(17).latitude + "," + ruta30.get(17).longitude + "|" + "" + ruta30.get(18).latitude + "," + ruta30.get(18).longitude + "|" + "" + ruta30.get(19).latitude + "," + ruta30.get(19).longitude + "|" + "" + ruta30.get(20).latitude + "," + ruta30.get(20).longitude + "|" + "" + ruta30.get(21).latitude + "," + ruta30.get(21).longitude + "|" + "" + ruta30.get(22).latitude + "," + ruta30.get(22).longitude + "|" + "" + ruta30.get(23).latitude + "," + ruta30.get(23).longitude + "&key=" + APIkey;
                }
            case 31:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta31.get(0).latitude + "," + ruta31.get(0).longitude + "&destination=" + ruta31.get(24).latitude + "," + ruta31.get(24).longitude + "&waypoints=optimize:true|" + ruta31.get(1).latitude + "," + ruta31.get(1).longitude + "|" + "" + ruta31.get(2).latitude + "," + ruta31.get(2).longitude + "|" + "" + ruta31.get(3).latitude + "," + ruta31.get(3).longitude + "|" + "" + ruta31.get(4).latitude + "," + ruta31.get(4).longitude + "|" + "" + ruta31.get(5).latitude + "," + ruta31.get(5).longitude + "|" + "" + ruta31.get(6).latitude + "," + ruta31.get(6).longitude + "|" + "" + ruta31.get(7).latitude + "," + ruta31.get(7).longitude + "|" + "" + ruta31.get(8).latitude + "," + ruta31.get(8).longitude + "|" + "" + ruta31.get(9).latitude + "," + ruta31.get(9).longitude + "|" + "" + ruta31.get(10).latitude + "," + ruta31.get(10).longitude + "|" + "" + ruta31.get(11).latitude + "," + ruta31.get(11).longitude + "|" + "" + ruta31.get(12).latitude + "," + ruta31.get(12).longitude + "|" + "" + ruta31.get(13).latitude + "," + ruta31.get(13).longitude + "|" + "" + ruta31.get(14).latitude + "," + ruta31.get(14).longitude + "|" + "" + ruta31.get(15).latitude + "," + ruta31.get(15).longitude + "|" + "" + ruta31.get(16).latitude + "," + ruta31.get(16).longitude + "|" + "" + ruta31.get(17).latitude + "," + ruta31.get(17).longitude + "|" + "" + ruta31.get(18).latitude + "," + ruta31.get(18).longitude + "|" + "" + ruta31.get(19).latitude + "," + ruta31.get(19).longitude + "|" + "" + ruta31.get(20).latitude + "," + ruta31.get(20).longitude + "|" + "" + ruta31.get(21).latitude + "," + ruta31.get(21).longitude + "|" + "" + ruta31.get(22).latitude + "," + ruta31.get(22).longitude + "|" + "" + ruta31.get(23).latitude + "," + ruta31.get(23).longitude + "&key=" + APIkey;
                }
            case 32:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta32.get(0).latitude + "," + ruta32.get(0).longitude + "&destination=" + ruta32.get(24).latitude + "," + ruta32.get(24).longitude + "&waypoints=optimize:true|" + ruta32.get(1).latitude + "," + ruta32.get(1).longitude + "|" + "" + ruta32.get(2).latitude + "," + ruta32.get(2).longitude + "|" + "" + ruta32.get(3).latitude + "," + ruta32.get(3).longitude + "|" + "" + ruta32.get(4).latitude + "," + ruta32.get(4).longitude + "|" + "" + ruta32.get(5).latitude + "," + ruta32.get(5).longitude + "|" + "" + ruta32.get(6).latitude + "," + ruta32.get(6).longitude + "|" + "" + ruta32.get(7).latitude + "," + ruta32.get(7).longitude + "|" + "" + ruta32.get(8).latitude + "," + ruta32.get(8).longitude + "|" + "" + ruta32.get(9).latitude + "," + ruta32.get(9).longitude + "|" + "" + ruta32.get(10).latitude + "," + ruta32.get(10).longitude + "|" + "" + ruta32.get(11).latitude + "," + ruta32.get(11).longitude + "|" + "" + ruta32.get(12).latitude + "," + ruta32.get(12).longitude + "|" + "" + ruta32.get(13).latitude + "," + ruta32.get(13).longitude + "|" + "" + ruta32.get(14).latitude + "," + ruta32.get(14).longitude + "|" + "" + ruta32.get(15).latitude + "," + ruta32.get(15).longitude + "|" + "" + ruta32.get(16).latitude + "," + ruta32.get(16).longitude + "|" + "" + ruta32.get(17).latitude + "," + ruta32.get(17).longitude + "|" + "" + ruta32.get(18).latitude + "," + ruta32.get(18).longitude + "|" + "" + ruta32.get(19).latitude + "," + ruta32.get(19).longitude + "|" + "" + ruta32.get(20).latitude + "," + ruta32.get(20).longitude + "|" + "" + ruta32.get(21).latitude + "," + ruta32.get(21).longitude + "|" + "" + ruta32.get(22).latitude + "," + ruta32.get(22).longitude + "|" + "" + ruta32.get(23).latitude + "," + ruta32.get(23).longitude + "&key=" + APIkey;
                }

            case 34:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta34.get(0).latitude + "," + ruta34.get(0).longitude + "&destination=" + ruta34.get(24).latitude + "," + ruta34.get(24).longitude + "&waypoints=optimize:true|" + ruta34.get(1).latitude + "," + ruta34.get(1).longitude + "|" + "" + ruta34.get(2).latitude + "," + ruta34.get(2).longitude + "|" + "" + ruta34.get(3).latitude + "," + ruta34.get(3).longitude + "|" + "" + ruta34.get(4).latitude + "," + ruta34.get(4).longitude + "|" + "" + ruta34.get(5).latitude + "," + ruta34.get(5).longitude + "|" + "" + ruta34.get(6).latitude + "," + ruta34.get(6).longitude + "|" + "" + ruta34.get(7).latitude + "," + ruta34.get(7).longitude + "|" + "" + ruta34.get(8).latitude + "," + ruta34.get(8).longitude + "|" + "" + ruta34.get(9).latitude + "," + ruta34.get(9).longitude + "|" + "" + ruta34.get(10).latitude + "," + ruta34.get(10).longitude + "|" + "" + ruta34.get(11).latitude + "," + ruta34.get(11).longitude + "|" + "" + ruta34.get(12).latitude + "," + ruta34.get(12).longitude + "|" + "" + ruta34.get(13).latitude + "," + ruta34.get(13).longitude + "|" + "" + ruta34.get(14).latitude + "," + ruta34.get(14).longitude + "|" + "" + ruta34.get(15).latitude + "," + ruta34.get(15).longitude + "|" + "" + ruta34.get(16).latitude + "," + ruta34.get(16).longitude + "|" + "" + ruta34.get(17).latitude + "," + ruta34.get(17).longitude + "|" + "" + ruta34.get(18).latitude + "," + ruta34.get(18).longitude + "|" + "" + ruta34.get(19).latitude + "," + ruta34.get(19).longitude + "|" + "" + ruta34.get(20).latitude + "," + ruta34.get(20).longitude + "|" + "" + ruta34.get(21).latitude + "," + ruta34.get(21).longitude + "|" + "" + ruta34.get(22).latitude + "," + ruta34.get(22).longitude + "|" + "" + ruta34.get(23).latitude + "," + ruta34.get(23).longitude + "&key=" + APIkey;
                }
            case 35:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta35.get(0).latitude + "," + ruta35.get(0).longitude + "&destination=" + ruta35.get(24).latitude + "," + ruta35.get(24).longitude + "&waypoints=optimize:true|" + ruta35.get(1).latitude + "," + ruta35.get(1).longitude + "|" + "" + ruta35.get(2).latitude + "," + ruta35.get(2).longitude + "|" + "" + ruta35.get(3).latitude + "," + ruta35.get(3).longitude + "|" + "" + ruta35.get(4).latitude + "," + ruta35.get(4).longitude + "|" + "" + ruta35.get(5).latitude + "," + ruta35.get(5).longitude + "|" + "" + ruta35.get(6).latitude + "," + ruta35.get(6).longitude + "|" + "" + ruta35.get(7).latitude + "," + ruta35.get(7).longitude + "|" + "" + ruta35.get(8).latitude + "," + ruta35.get(8).longitude + "|" + "" + ruta35.get(9).latitude + "," + ruta35.get(9).longitude + "|" + "" + ruta35.get(10).latitude + "," + ruta35.get(10).longitude + "|" + "" + ruta35.get(11).latitude + "," + ruta35.get(11).longitude + "|" + "" + ruta35.get(12).latitude + "," + ruta35.get(12).longitude + "|" + "" + ruta35.get(13).latitude + "," + ruta35.get(13).longitude + "|" + "" + ruta35.get(14).latitude + "," + ruta35.get(14).longitude + "|" + "" + ruta35.get(15).latitude + "," + ruta35.get(15).longitude + "|" + "" + ruta35.get(16).latitude + "," + ruta35.get(16).longitude + "|" + "" + ruta35.get(17).latitude + "," + ruta35.get(17).longitude + "|" + "" + ruta35.get(18).latitude + "," + ruta35.get(18).longitude + "|" + "" + ruta35.get(19).latitude + "," + ruta35.get(19).longitude + "|" + "" + ruta35.get(20).latitude + "," + ruta35.get(20).longitude + "|" + "" + ruta35.get(21).latitude + "," + ruta35.get(21).longitude + "|" + "" + ruta35.get(22).latitude + "," + ruta35.get(22).longitude + "|" + "" + ruta35.get(23).latitude + "," + ruta35.get(23).longitude + "&key=" + APIkey;
                }

            case 51:
                if (concatenacion != null) {
                    return concatenacion;
                } else {
                    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + ruta51.get(0).latitude + "," + ruta51.get(0).longitude + "&destination=" + ruta51.get(24).latitude + "," + ruta51.get(24).longitude + "&waypoints=optimize:true|" + ruta51.get(1).latitude + "," + ruta51.get(1).longitude + "|" + "" + ruta51.get(2).latitude + "," + ruta51.get(2).longitude + "|" + "" + ruta51.get(3).latitude + "," + ruta51.get(3).longitude + "|" + "" + ruta51.get(4).latitude + "," + ruta51.get(4).longitude + "|" + "" + ruta51.get(5).latitude + "," + ruta51.get(5).longitude + "|" + "" + ruta51.get(6).latitude + "," + ruta51.get(6).longitude + "|" + "" + ruta51.get(7).latitude + "," + ruta51.get(7).longitude + "|" + "" + ruta51.get(8).latitude + "," + ruta51.get(8).longitude + "|" + "" + ruta51.get(9).latitude + "," + ruta51.get(9).longitude + "|" + "" + ruta51.get(10).latitude + "," + ruta51.get(10).longitude + "|" + "" + ruta51.get(11).latitude + "," + ruta51.get(11).longitude + "|" + "" + ruta51.get(12).latitude + "," + ruta51.get(12).longitude + "|" + "" + ruta51.get(13).latitude + "," + ruta51.get(13).longitude + "|" + "" + ruta51.get(14).latitude + "," + ruta51.get(14).longitude + "|" + "" + ruta51.get(15).latitude + "," + ruta51.get(15).longitude + "|" + "" + ruta51.get(16).latitude + "," + ruta51.get(16).longitude + "|" + "" + ruta51.get(17).latitude + "," + ruta51.get(17).longitude + "|" + "" + ruta51.get(18).latitude + "," + ruta51.get(18).longitude + "|" + "" + ruta51.get(19).latitude + "," + ruta51.get(19).longitude + "|" + "" + ruta51.get(20).latitude + "," + ruta51.get(20).longitude + "|" + "" + ruta51.get(21).latitude + "," + ruta51.get(21).longitude + "|" + "" + ruta51.get(22).latitude + "," + ruta51.get(22).longitude + "|" + "" + ruta51.get(23).latitude + "," + ruta51.get(23).longitude + "&key=" + APIkey;
                }
        }

        return "";

    }

    private String getDirectionsUrlBusTransbordo() {

        String parteLink, parteOrigen, parteDestino, key;

        mostrarSoloParadasTransbordo = "";
        parteLink = "https://maps.googleapis.com/maps/api/directions/json?origin=";
        if (subirParadaTransbordo < bajarParadaTransbordo) {
            parteOrigen = AllRutas.get(tomarRutaTransbordo).get(subirParadaTransbordo).latitude + "," + AllRutas.get(tomarRutaTransbordo).get(subirParadaTransbordo).longitude;
            parteDestino = "&destination=" + AllRutas.get(tomarRutaTransbordo).get(bajarParadaTransbordo).latitude + "," + AllRutas.get(tomarRutaTransbordo).get(bajarParadaTransbordo).longitude + "&waypoints=optimize:true";
        } else {
            parteOrigen = AllRutas.get(tomarRutaTransbordo).get(bajarParadaTransbordo).latitude + "," + AllRutas.get(tomarRutaTransbordo).get(bajarParadaTransbordo).longitude;
            parteDestino = "&destination=" + AllRutas.get(tomarRutaTransbordo).get(subirParadaTransbordo).latitude + "," + AllRutas.get(tomarRutaTransbordo).get(subirParadaTransbordo).longitude + "&waypoints=optimize:true";
        }

        int mayor, menor;
        if (subirParadaTransbordo < bajarParadaTransbordo) {
            mayor = bajarParadaTransbordo;
            menor = subirParadaTransbordo;
        } else {
            mayor = subirParadaTransbordo;
            menor = bajarParadaTransbordo;
        }
        int contador = 0;
        for (int i = menor; i < mayor; i++) {
            if ((AllRutas.get(tomarRutaTransbordo).get(i).latitude != AllRutas.get(tomarRutaTransbordo).get(bajarParadaTransbordo).latitude) && (AllRutas.get(tomarRutaTransbordo).get(i).latitude != AllRutas.get(tomarRutaTransbordo).get(subirParadaTransbordo).latitude)) {
                mostrarSoloParadasTransbordo = mostrarSoloParadasTransbordo + "|" + AllRutas.get(tomarRutaTransbordo).get(i).latitude + "," + AllRutas.get(tomarRutaTransbordo).get(i).longitude;
                contador++;
            }
        }
        key = "&key=" + APIkey;

        if (contador != 0) {
            concatenacionTransbordo = parteLink + parteOrigen + parteDestino + mostrarSoloParadasTransbordo + key;
        } else {
            if (subirParadaTransbordo < bajarParadaTransbordo) {
                parteOrigen = AllRutas.get(tomarRutaTransbordo).get(subirParadaTransbordo).latitude + "," + AllRutas.get(tomarRutaTransbordo).get(subirParadaTransbordo).longitude;
                parteDestino = "&destination=" + AllRutas.get(tomarRutaTransbordo).get(bajarParadaTransbordo).latitude + "," + AllRutas.get(tomarRutaTransbordo).get(bajarParadaTransbordo).longitude + key;
            } else {
                parteOrigen = AllRutas.get(tomarRutaTransbordo).get(bajarParadaTransbordo).latitude + "," + AllRutas.get(tomarRutaTransbordo).get(bajarParadaTransbordo).longitude;
                parteDestino = "&destination=" + AllRutas.get(tomarRutaTransbordo).get(subirParadaTransbordo).latitude + "," + AllRutas.get(tomarRutaTransbordo).get(subirParadaTransbordo).longitude + key;
            }
            concatenacionTransbordo = parteLink + parteOrigen + parteDestino + key;
        }
        return concatenacionTransbordo;
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.info, null, false);
        lvlItems = (ListView) v.findViewById(R.id.lvItems);
        llenarItems();
        builder.setView(v);

        builder.setNegativeButton("Volver", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                arrayEntidad.clear();
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void llenarItems() {

        if (distanciaOrigenParada > 800) {
            arrayEntidad.add(new Entidad(R.drawable.caminar, "Tome la ruta " + auxRutaTransbordo, "Siga la primera línea negra y camine a la parada " + (bajarParadaTransbordo + 1), "Tiempo estimado: " + String.format("%.2f",(distanciaParadaDestinoTransbordo/1000)/0.033) + " minutos")) ;
            arrayEntidad.add(new Entidad(R.drawable.autobus, "Espere al camión y suba", "Se bajará en la parada " + (subirParadaTransbordo + 1), "Tiempo estimado: " + String.format("%.2f",(distanciaOrigenParadaTransbordo/1000)/0.053) + " minutos")) ;

            arrayEntidad.add(new Entidad(R.drawable.caminar, "Tome la ruta " + auxRuta, "Siga la segunda línea negra y camine a la parada " + (subirParadaRuta + 1), "Tiempo estimado: " + String.format("%.2f",(distanciaOrigenParada/1000)/0.063) + " minutos")) ;
            arrayEntidad.add(new Entidad(R.drawable.autobus, "Espere al camión y suba", "Se bajará en la parada " + (bajarParadaRuta + 1), "Tiempo estimado: " + String.format("%.2f",(distanciaParadaAParada/1000)/0.053) + " minutos")) ;

            arrayEntidad.add(new Entidad(R.drawable.caminar, "Siga la línea negra", "Al seguirla, llegará a su destino", "Tiempo estimado: " + String.format("%.2f",(distanciaParadaDestinoTransbordo/1000)/0.033) + " minutos")) ;

        } else {
            arrayEntidad.add(new Entidad(R.drawable.caminar, "Tome la ruta " + auxRuta, "Siga la primera línea negra y camine a la parada " + (subirParadaRuta + 1), "Tiempo estimado: " + String.format("%.2f",(distanciaOrigenParada/1000)/0.033) + " minutos")) ;
            arrayEntidad.add(new Entidad(R.drawable.autobus, "Espere al camión y suba", "Se bajará en la parada " + (bajarParadaRuta + 1), "Tiempo estimado: " + String.format("%.2f",(distanciaParadaAParada/1000)/0.053) + " minutos")) ;

            arrayEntidad.add(new Entidad(R.drawable.caminar, "Siga la línea negra", "Al seguirla, llegará a su destino", "Tiempo estimado: " + String.format("%.2f",(distanciaParadaDestino/1000)/0.033) + " minutos")) ;

        }
        adaptador = new Adaptador(this, arrayEntidad);
        lvlItems.setAdapter(adaptador);
    }

    /**
     * A method to download json data from url
     */
    private class ReadTaskBus extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            new ParserTaskBus().execute(result);


        }
    }

    private class ParserTaskBus extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            if (routes.size() > 0) {

                // traversing through routes
                for (int i = 0; i < routes.size(); i++) {
                    points = new ArrayList<LatLng>();
                    polyLineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = routes.get(i);

                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    polyLineOptions.addAll(points);
                    polyLineOptions.width(10);
                    int aux = spinnerRutas.getSelectedItemPosition();

                    if (aux >= 5 && aux < 8) {
                        aux++;
                    } else if (aux >= 8 && aux < 31) {
                        aux = aux + 2;
                    } else if (aux == 31 || aux == 32) {
                        aux = aux + 3;
                    } else if (aux == 33) {
                        aux = 51;
                    }
                    if (auxRuta != 0) {
                        aux = 0;
                    }


                    if (auxRuta == 1 || aux == 1) {
                        polyLineOptions.color(Color.parseColor("#D2D1D1"));

                    } else if (auxRuta == 2 || aux == 2) {
                        polyLineOptions.color(Color.parseColor("#3229AE"));

                    } else if (auxRuta == 3 || aux == 3) {
                        polyLineOptions.color(Color.parseColor("#72EAF9"));

                    } else if (auxRuta == 4 || aux == 4) {
                        polyLineOptions.color(Color.parseColor("#0027C8"));

                    } else if (auxRuta == 6 || aux == 6) {
                        polyLineOptions.color(Color.parseColor("#E5F860"));

                    } else if (auxRuta == 7 || aux == 7) {
                        polyLineOptions.color(Color.parseColor("#D3EB29"));

                    } else if (auxRuta == 8 || aux == 8) {
                        polyLineOptions.color(Color.parseColor("#DFFF00"));

                    } else if (auxRuta == 10 || aux == 10) {
                        polyLineOptions.color(Color.parseColor("#C3DA25"));

                    } else if (auxRuta == 11 || aux == 11) {
                        polyLineOptions.color(Color.parseColor("#FFBA25"));

                    } else if (auxRuta == 12 || aux == 12) {
                        polyLineOptions.color(Color.parseColor("#FFA200"));

                    } else if (auxRuta == 13 || aux == 13) {
                        polyLineOptions.color(Color.parseColor("#FF2D2D"));

                    } else if (auxRuta == 14 || aux == 14) {
                        polyLineOptions.color(Color.parseColor("#00E118"));

                    } else if (auxRuta == 15 || aux == 15) {
                        polyLineOptions.color(Color.parseColor("#D200C2"));

                    } else if (auxRuta == 16 || aux == 16) {
                        polyLineOptions.color(Color.parseColor("#1EA600"));

                    } else if (auxRuta == 17 || aux == 17) {
                        polyLineOptions.color(Color.parseColor("#64D7FF"));

                    } else if (auxRuta == 18 || aux == 18) {
                        polyLineOptions.color(Color.parseColor("#00EC07"));

                    } else if (auxRuta == 19 || aux == 19) {
                        polyLineOptions.color(Color.parseColor("#31CD36"));

                    } else if (auxRuta == 20 || aux == 20) {
                        polyLineOptions.color(Color.parseColor("#49FF33"));

                    } else if (auxRuta == 21 || aux == 21) {
                        polyLineOptions.color(Color.parseColor("#EAF723"));

                    } else if (auxRuta == 22 || aux == 22) {
                        polyLineOptions.color(Color.parseColor("#57E23B"));

                    } else if (auxRuta == 23 || aux == 23) {
                        polyLineOptions.color(Color.parseColor("#D80000"));

                    } else if (auxRuta == 24 || aux == 24) {
                        polyLineOptions.color(Color.parseColor("#53E147"));

                    } else if (auxRuta == 25 || aux == 25) {
                        polyLineOptions.color(Color.parseColor("#66F100"));

                    } else if (auxRuta == 26 || aux == 26) {
                        polyLineOptions.color(Color.parseColor("#8E00F1"));

                    } else if (auxRuta == 27 || aux == 27) {
                        polyLineOptions.color(Color.parseColor("#369BFF"));

                    } else if (auxRuta == 28 || aux == 28) {
                        polyLineOptions.color(Color.parseColor("#F6FB44"));

                    } else if (auxRuta == 29 || aux == 29) {
                        polyLineOptions.color(Color.parseColor("#F33434"));

                    } else if (auxRuta == 30 || aux == 30) {
                        polyLineOptions.color(Color.parseColor("#0075DC"));

                    } else if (auxRuta == 31 || aux == 31) {
                        polyLineOptions.color(Color.parseColor("#E21F00"));

                    } else if (auxRuta == 32 || aux == 32) {
                        polyLineOptions.color(Color.parseColor("#F0F014"));

                    } else if (auxRuta == 34 || aux == 34) {
                        polyLineOptions.color(Color.parseColor("#DA2121"));

                    } else if (auxRuta == 35 || aux == 35) {
                        polyLineOptions.color(Color.parseColor("#F3F938"));

                    } else if (auxRuta == 51 || aux == 51) {
                        polyLineOptions.color(Color.parseColor("#27EA38"));
                    }
                }
                // Drawing polyline in the Google Map for the i-th route
                if (polyLineOptions != null) {
                    if (mPolylineBus != null) {
                        mPolylineBus.remove();
                    }
                    mPolylineBus = mMap.addPolyline(polyLineOptions);
                } else
                    Toast.makeText(getApplicationContext(), "NO HAY DATOS DE LA RUTA", Toast.LENGTH_SHORT).show();

            }

        }

    }

    /**
     * A method to download json data from url
     */
    private class ReadTaskBusTransbordo extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            new ParserTaskBusTransbordo().execute(result);


        }
    }

    private class ParserTaskBusTransbordo extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            if (routes.size() > 0) {

                // traversing through routes
                for (int i = 0; i < routes.size(); i++) {
                    points = new ArrayList<LatLng>();
                    polyLineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = routes.get(i);

                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    polyLineOptions.addAll(points);
                    polyLineOptions.width(10);


                    if (auxRutaTransbordo == 1) {
                        polyLineOptions.color(Color.parseColor("#D2D1D1"));

                    } else if (auxRutaTransbordo == 2) {
                        polyLineOptions.color(Color.parseColor("#3229AE"));

                    } else if (auxRutaTransbordo == 3) {
                        polyLineOptions.color(Color.parseColor("#72EAF9"));

                    } else if (auxRutaTransbordo == 4) {
                        polyLineOptions.color(Color.parseColor("#0027C8"));

                    } else if (auxRutaTransbordo == 6) {
                        polyLineOptions.color(Color.parseColor("#E5F860"));

                    } else if (auxRutaTransbordo == 7) {
                        polyLineOptions.color(Color.parseColor("#D3EB29"));

                    } else if (auxRutaTransbordo == 8) {
                        polyLineOptions.color(Color.parseColor("#DFFF00"));

                    } else if (auxRutaTransbordo == 10) {
                        polyLineOptions.color(Color.parseColor("#C3DA25"));

                    } else if (auxRutaTransbordo == 11) {
                        polyLineOptions.color(Color.parseColor("#FFBA25"));

                    } else if (auxRutaTransbordo == 12) {
                        polyLineOptions.color(Color.parseColor("#FFA200"));

                    } else if (auxRutaTransbordo == 13) {
                        polyLineOptions.color(Color.parseColor("#FF2D2D"));

                    } else if (auxRutaTransbordo == 14) {
                        polyLineOptions.color(Color.parseColor("#00E118"));

                    } else if (auxRutaTransbordo == 15) {
                        polyLineOptions.color(Color.parseColor("#D200C2"));

                    } else if (auxRutaTransbordo == 16) {
                        polyLineOptions.color(Color.parseColor("#1EA600"));

                    } else if (auxRutaTransbordo == 17) {
                        polyLineOptions.color(Color.parseColor("#64D7FF"));

                    } else if (auxRutaTransbordo == 18) {
                        polyLineOptions.color(Color.parseColor("#00EC07"));

                    } else if (auxRutaTransbordo == 19) {
                        polyLineOptions.color(Color.parseColor("#31CD36"));

                    } else if (auxRutaTransbordo == 20) {
                        polyLineOptions.color(Color.parseColor("#49FF33"));

                    } else if (auxRutaTransbordo == 21) {
                        polyLineOptions.color(Color.parseColor("#EAF723"));

                    } else if (auxRutaTransbordo == 22) {
                        polyLineOptions.color(Color.parseColor("#57E23B"));

                    } else if (auxRutaTransbordo == 23) {
                        polyLineOptions.color(Color.parseColor("#D80000"));

                    } else if (auxRutaTransbordo == 24) {
                        polyLineOptions.color(Color.parseColor("#53E147"));

                    } else if (auxRutaTransbordo == 25) {
                        polyLineOptions.color(Color.parseColor("#66F100"));

                    } else if (auxRutaTransbordo == 26) {
                        polyLineOptions.color(Color.parseColor("#8E00F1"));

                    } else if (auxRutaTransbordo == 27) {
                        polyLineOptions.color(Color.parseColor("#369BFF"));

                    } else if (auxRutaTransbordo == 28) {
                        polyLineOptions.color(Color.parseColor("#F6FB44"));

                    } else if (auxRutaTransbordo == 29) {
                        polyLineOptions.color(Color.parseColor("#F33434"));

                    } else if (auxRutaTransbordo == 30) {
                        polyLineOptions.color(Color.parseColor("#0075DC"));

                    } else if (auxRutaTransbordo == 31) {
                        polyLineOptions.color(Color.parseColor("#E21F00"));

                    } else if (auxRutaTransbordo == 32) {
                        polyLineOptions.color(Color.parseColor("#F0F014"));

                    } else if (auxRutaTransbordo == 34) {
                        polyLineOptions.color(Color.parseColor("#DA2121"));

                    } else if (auxRutaTransbordo == 35) {
                        polyLineOptions.color(Color.parseColor("#F3F938"));

                    } else if (auxRutaTransbordo == 51) {
                        polyLineOptions.color(Color.parseColor("#27EA38"));
                    }
                }
                // Drawing polyline in the Google Map for the i-th route
                if (polyLineOptions != null) {
                    if (mPolylineBusTransbordo != null) {
                        mPolylineBusTransbordo.remove();
                    }
                    mPolylineBusTransbordo = mMap.addPolyline(polyLineOptions);
                } else
                    Toast.makeText(getApplicationContext(), "NO HAY DATOS DE LA RUTA", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * A method to download json data from url
     */
    private class ReadTaskOrigin extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTaskOrigin().execute(result);
        }
    }

    private class ParserTaskOrigin extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            if (routes.size() > 0) {

                // traversing through routes
                for (int i = 0; i < routes.size(); i++) {
                    points = new ArrayList<LatLng>();
                    polyLineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = routes.get(i);

                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    polyLineOptions.addAll(points);
                    polyLineOptions.width(10);
                    polyLineOptions.color(Color.parseColor("#000000"));

                }

                // Drawing polyline in the Google Map for the i-th route
                if (polyLineOptions != null) {
                    if (mPolylineOrigin != null) {
                        mPolylineOrigin.remove();
                    }
                    mPolylineOrigin = mMap.addPolyline(polyLineOptions);
                } else
                    Toast.makeText(getApplicationContext(), "NO HAY DATOS DE LA RUTA", Toast.LENGTH_SHORT).show();

            }

        }

    }

    /**
     * A method to download json data from url
     */
    private class ReadTaskOriginTransbordo extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            new ParserTaskOriginTransbordo().execute(result);

        }
    }

    private class ParserTaskOriginTransbordo extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            if (routes.size() > 0) {

                // traversing through routes
                for (int i = 0; i < routes.size(); i++) {
                    points = new ArrayList<LatLng>();
                    polyLineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = routes.get(i);

                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    polyLineOptions.addAll(points);
                    polyLineOptions.width(10);
                    polyLineOptions.color(Color.parseColor("#000000"));

                }

                // Drawing polyline in the Google Map for the i-th route
                if (polyLineOptions != null) {
                    if (mPolylineOriginTransbordo != null) {
                        mPolylineOriginTransbordo.remove();
                    }
                    mPolylineOriginTransbordo = mMap.addPolyline(polyLineOptions);
                } else
                    Toast.makeText(getApplicationContext(), "NO HAY DATOS DE LA RUTA", Toast.LENGTH_SHORT).show();

            }

        }

    }

    private class ReadTaskDestination extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            new ParserTaskDestination().execute(result);

        }
    }

    private class ParserTaskDestination extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            if (routes.size() > 0) {

                // traversing through routes
                for (int i = 0; i < routes.size(); i++) {
                    points = new ArrayList<LatLng>();
                    polyLineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = routes.get(i);

                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    polyLineOptions.addAll(points);
                    polyLineOptions.width(10);
                    polyLineOptions.color(Color.parseColor("#000000"));
                }

                // Drawing polyline in the Google Map for the i-th route
                if (polyLineOptions != null) {
                    if (mPolylineDestination != null) {
                        mPolylineDestination.remove();
                    }
                    mPolylineDestination = mMap.addPolyline(polyLineOptions);
                } else
                    Toast.makeText(getApplicationContext(), "NO HAY DATOS DE LA RUTA", Toast.LENGTH_SHORT).show();

            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        if (requestCode == 100) {
            if (!verifyAllPermissions(grantResults)) {
                Toast.makeText(getApplicationContext(), "Active el permiso *Permitir todo el tiempo* y reinicie la aplicación.", Toast.LENGTH_LONG).show();
            } else {
                getMyLocation();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean verifyAllPermissions(int[] grantResults) {

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settingsMap:
                return true;
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;


            case R.id.delete:
                showDialogDelete();
                return true;

            case R.id.help:
                showDialogHelp();
                return true;

            case R.id.about:
                showDialogAbout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void showDialogAbout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.about, null);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDialogHelp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.help, null);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDialogDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmación")
                .setMessage("¿Está seguro de limpiar el mapa?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    try {
                        concatenacion = "";
                        bajarParadaRuta = 0;
                        subirParadaRuta = 0;

                        mPolylineBus.remove();
                        mPolylineBusTransbordo.remove();
                        mPolylineOrigin.remove();
                        mPolylineDestination.remove();
                        tomarRuta = 0;
                        tomarRutaTransbordo = 0;
                        mostrarSoloParadas = "";
                        auxRuta = 0;

                        spinnerRutas.setSelection(0);
                        mMap.clear();
                        mDestination = null;
                        distanciaParadaDestino = 0;
                        distanciaOrigenParada = 0;
                        spinnerRutas.setVisibility(View.VISIBLE);
                        textoRuta.setVisibility(View.VISIBLE);

                        URLBus = "";
                        URLDestination = "";
                        URLOrigin = "";
                        URLBusTransbordo = "";


                    } catch (Exception e) {
                    }

                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();

    }

    private void fillArrayLatLng() {
        AllRutas.add(ruta1);
        AllRutas.add(ruta2);
        AllRutas.add(ruta3);
        AllRutas.add(ruta4);

        AllRutas.add(ruta6);
        AllRutas.add(ruta7);
        AllRutas.add(ruta8);

        AllRutas.add(ruta10);
        AllRutas.add(ruta11);
        AllRutas.add(ruta12);
        AllRutas.add(ruta13);
        AllRutas.add(ruta14);
        AllRutas.add(ruta15);
        AllRutas.add(ruta16);
        AllRutas.add(ruta17);
        AllRutas.add(ruta18);
        AllRutas.add(ruta19);
        AllRutas.add(ruta20);
        AllRutas.add(ruta21);
        AllRutas.add(ruta22);
        AllRutas.add(ruta23);
        AllRutas.add(ruta24);
        AllRutas.add(ruta25);
        AllRutas.add(ruta26);
        AllRutas.add(ruta27);
        AllRutas.add(ruta28);
        AllRutas.add(ruta29);
        AllRutas.add(ruta30);
        AllRutas.add(ruta31);
        AllRutas.add(ruta32);

        AllRutas.add(ruta34);
        AllRutas.add(ruta35);

        AllRutas.add(ruta51);
    }

}
