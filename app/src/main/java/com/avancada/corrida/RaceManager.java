package com.avancada.corrida;

import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import java.util.ArrayList;

public class RaceManager {
    private FrameLayout mainLayout;
    private Track track;
    private ArrayList<Car> cars;

    public RaceManager(FrameLayout mainLayout, Track track) {
        this.mainLayout = mainLayout;
        this.track = track;
        this.cars = new ArrayList<>();
    }

    public void addCarsToTrack(int carCount) {
        for (int i = 0; i < carCount; i++) {
            String carName = "carro" + (i + 1);

            // Cria o carro
            Car car = new Car(mainLayout.getContext(), mainLayout, R.drawable.carro1, 50, 50 * (i + 1), track, carName, cars);
            cars.add(car);  // Adiciona o carro à lista de carros

            // Posiciona o carro na pista
            /*if (car.getImageView().getParent() != null) {
                ((ViewGroup) car.getImageView().getParent()).removeView(car.getImageView());
            }
            mainLayout.addView(car.getImageView());*/
            car.setPosition(550, 1060 + (i * 50));
        }
    }

    public void startRace() {
        for (Car car : cars) {
            car.startMoving();  // Inicia o movimento de cada carro
        }
    }

    public void pauseRace() {
        for (Car car : cars) {
            car.stopMoving();  // Para o movimento de cada carro
        }
    }

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
            // Exibir uma mensagem ou fazer log do vencedor
        } else {
            // Caso nenhum carro tenha participado
        }
    }
}
