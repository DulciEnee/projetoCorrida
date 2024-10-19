package com.avancada.corrida;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    //Elementos de interface
    private EditText carCountEditText;
    private Button startButton;
    private Button pauseButton;
    private Button finishButton;
    private Button addButton;
    private FrameLayout mainLayout;

    //Carro e pista
    private ArrayList<Car> cars;
    private int carImage = R.drawable.carro1;  // Substitua com o PNG do carro
    private Track track;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configura os componentes da interface
        carCountEditText = findViewById(R.id.editTextCarCount);
        addButton = findViewById(R.id.buttonAdd);
        startButton = findViewById(R.id.buttonStart);
        pauseButton = findViewById(R.id.buttonPause);
        finishButton = findViewById(R.id.buttonFinish);
        mainLayout = findViewById(R.id.main);

        // Carregar a pista a partir de uma imagem
        track = new Track(this, R.drawable.pista);

        //Inicializa lista de carros
        cars = new ArrayList<>();

        // Adicionar o listener ao mainLayout para garantir que o layout está pronto
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                // Remova o listener após a primeira execução para evitar múltiplas chamadas
                mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // DEPURAÇÃO: Checando tamanho do layout
                int layoutWidth = mainLayout.getWidth();
                int layoutHeight = mainLayout.getHeight();
                Log.e("Namain", "mainLayout width: " + layoutWidth + ", height: " + layoutHeight);
            }
        });

        // Adicionar listeners para os botões
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Namain", "Clicou no botao start");
                String carCountStr = carCountEditText.getText().toString();
                Log.e("Namain", "Tem carro na fila: " + !carCountStr.isEmpty());
                if (!carCountStr.isEmpty()) {
                    int carCount = Integer.parseInt(carCountStr);
                    Log.e("Namain", "N de carros: " + carCount);
                    addCarsToTrack(carCount);
                    Log.e("Namain", "Adicionou carro");
                } else {
                    Toast.makeText(MainActivity.this, "Por favor, insira um número de carros", Toast.LENGTH_SHORT).show();
                    Log.e("Namain", "Número de carros não foi informado");
                }
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Log.e("Namain", "Clicou no botão pause");

            // Inicia a movimentação dos carros
            for (Car car : cars) {
                car.startMoving();
            }
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Namain", "Clicou no botão pause");

                // Inicia a movimentação dos carros
                for (Car car : cars) {
                    car.stopMoving();
                }
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Namain", "Clicou no botão Finish");

                // Parar a movimentação dos carros
                for (Car car : cars) {
                    car.stopMoving();
                }

                // Exibir uma mensagem de fim da corrida
                Toast.makeText(MainActivity.this, "Fim da corrida", Toast.LENGTH_LONG).show();

                // Encontrar o carro com o maior score (distance - penalty)
                Car winner = null;
                int highestScore = 0;

                for (Car car : cars) {
                    int score = car.getDistance() - car.getPenalty(); // Calcular o score
                    if (score > highestScore) {
                        highestScore = score;
                        winner = car; // Atualizar o vencedor
                    }
                }

                // Se houver um vencedor, exibir o nome do carro
                if (winner != null) {
                    String winnerMessage = "O vencedor é " + winner.getName() + " com pontuação: " + highestScore;
                    Toast.makeText(MainActivity.this, winnerMessage, Toast.LENGTH_LONG).show();
                    Log.e("Namain", winnerMessage);
                } else {
                    // Caso não tenha nenhum carro
                    Toast.makeText(MainActivity.this, "Nenhum carro participou da corrida", Toast.LENGTH_LONG).show();
                    Log.e("Namain", "Nenhum carro participou da corrida");
                }
            }
        });
    }

    private void addCarsToTrack(int carCount) {
        Log.e("Namain", "Metodo addCarsToTrack: ");

        for (int i = 0; i < carCount; i++) {
            ImageView existingImageView = findViewById(R.id.imageView);

            // Gera nome dinâmico para o carro
            String carName = "carro" + (i + 1);

            // Cria carros
            Car carro = new Car(this, mainLayout, carImage, 50, 50 * (i + 1), track, carName, cars);
            cars.add(carro);  // Adicionar o carro na lista
            Log.e("Namain", "Carro adicionado: " + carName);

            // Posiciona o carro na pista
            if (carro.getImageView().getParent() != null) {
                ((ViewGroup) carro.getImageView().getParent()).removeView(carro.getImageView());
            }
            mainLayout.addView(carro.getImageView());
            // Chamar setPosition após o ImageView ter sido adicionado ao layout
            carro.setPosition(550 , 1060 + (i * 50));
        }
    }
}


