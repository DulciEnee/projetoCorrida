package com.avancada.corrida;

import android.content.Context;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class RaceManager {
    private FrameLayout mainLayout;       // Layout principal da corrida
    private Track track;                  // Pista da corrida
    private ArrayList<Car> cars;          // Lista de carros participantes
    private Context context;              // Contexto para acesso de recursos
    private static final Semaphore trackSemaphore = new Semaphore(1);  // Semáforo para controle de acesso à faixa exclusiva


    // Construtor RaceManager, inicializa o layout e define os limites da faixa exclusiva.

    public RaceManager(Context context, FrameLayout mainLayout, Track track) {
        this.mainLayout = mainLayout;
        this.track = track;
        this.cars = new ArrayList<>();
        this.context = context;
    }

    //Adiciona carros à pista e define suas posições iniciais.

    public void addCarsToTrack(int carCount) {
        for (int i = 0; i < carCount; i++) {
            String carName = "carro" + (i + 1);

            // Cria o carro e define a posição inicial
            Car car = new Car(mainLayout.getContext(), mainLayout, R.drawable.carro1, 50, 50 * (i + 1), track, carName, cars);
            cars.add(car);  // Adiciona o carro à lista de carros
            car.setPosition(550, 1060 + (i * 50));  // Posição inicial na pista
        }
    }

    // Inicia o movimento de todos os carros na pista.

    public void startRace() {
        for (Car car : cars) {
            car.startMoving();  // Inicia o movimento de cada carro
        }
    }

    // Pausa o movimento de todos os carros na pista.

    public void pauseRace() {
        for (Car car : cars) {
            car.stopMoving();  // Para o movimento de cada carro
        }
    }

    //Finaliza a corrida, determina o vencedor e exibe o resultado.

    public void finishRace() {
        for (Car car : cars) {
            car.stopMoving();  // Garante que todos os carros parem
        }

        // Determinar o vencedor da corrida
        Car winner = null;
        int highestScore = 0;

        for (Car car : cars) {
            int score = car.getDistance() - car.getPenalty();  // Calcula a pontuação do carro
            if (score > highestScore) {
                highestScore = score;
                winner = car;
            }
        }

        // Exibir o vencedor
        if (winner != null) {
            String winnerMessage = "O vencedor é " + winner.getName() + " com pontuação: " + highestScore;
            Toast.makeText(context, winnerMessage, Toast.LENGTH_LONG).show();
            Log.e("RaceManager", winnerMessage);
        } else {
            // Caso não tenha nenhum carro
            Toast.makeText(context, "Nenhum carro participou da corrida", Toast.LENGTH_LONG).show();
            Log.e("RaceManager", "Nenhum carro participou da corrida");
        }
    }

    //Retorna o semáforo de controle de acesso à faixa exclusiva.

    public static Semaphore getTrackSemaphore() {
        return trackSemaphore;
    }
}
