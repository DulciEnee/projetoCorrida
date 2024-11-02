package com.avancada.corrida;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Elementos da interface
    private EditText carCountEditText;
    private Button startButton;
    private Button pauseButton;
    private Button finishButton;
    private Button addButton;
    private FrameLayout mainLayout;

    // Carros e pista
    private ArrayList<Car> cars;
    private int carImage = R.drawable.carro1; // Imagem do carro
    private Track track;
    private RaceManager raceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configuração dos componentes da interface
        carCountEditText = findViewById(R.id.editTextCarCount);
        addButton = findViewById(R.id.buttonAdd);
        startButton = findViewById(R.id.buttonStart);
        pauseButton = findViewById(R.id.buttonPause);
        finishButton = findViewById(R.id.buttonFinish);
        mainLayout = findViewById(R.id.main);

        // Inicializa a pista a partir de uma imagem
        track = new Track(this, R.drawable.pista);

        // Inicializa a lista de carros e o RaceManager
        cars = new ArrayList<>();
        raceManager = new RaceManager(this, mainLayout, track);

        // Listener para capturar o tamanho do layout após o carregamento
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int layoutWidth = mainLayout.getWidth();
                int layoutHeight = mainLayout.getHeight();
                Log.e("MainActivity", "Layout width: " + layoutWidth + ", height: " + layoutHeight);
            }
        });

        // Configuração dos botões com listeners para ações
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCarsButtonAction();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                raceManager.startRace();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                raceManager.pauseRace();
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                raceManager.finishRace();
            }
        });
    }

    //Adiciona carros à pista conforme a quantidade especificada no campo de entrada.

    private void addCarsButtonAction() {
        try {
            String carCountStr = carCountEditText.getText().toString();
            if (!carCountStr.isEmpty()) {
                int carCount = Integer.parseInt(carCountStr);
                raceManager.addCarsToTrack(carCount);
                Log.e("MainActivity", "Adicionou " + carCount + " carros na pista.");
            } else {
                Toast.makeText(this, "Por favor, insira um número de carros", Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "Número de carros não foi informado");
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Número inválido de carros", Toast.LENGTH_SHORT).show();
            Log.e("MainActivity", "Erro ao parsear o número de carros: " + e.getMessage());
        } catch (Exception e) {
            Log.e("MainActivity", "Erro ao adicionar carros: " + e.getMessage());
        }
    }
}
