package com.avancada.corrida;

import android.content.Context;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class RaceManager {
    private FrameLayout mainLayout;       // Layout principal da corrida
    private Track track;                  // Pista da corrida
    private ArrayList<Car> cars;          // Lista de carros participantes
    private Context context;              // Contexto para acesso de recursos
    private static final Semaphore trackSemaphore = new Semaphore(1);  // Semáforo para controle de acesso à faixa exclusiva
    private FirebaseFirestore db;

    // Construtor RaceManager, inicializa o layout e define os limites da faixa exclusiva.
    public RaceManager(Context context, FrameLayout mainLayout, Track track) {
        this.mainLayout = mainLayout;
        this.track = track;
        this.cars = new ArrayList<>();
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
    }

    // Adiciona carros à pista ou carrega do banco de dados se existirem
    public void addCarsToTrack(int carCount) {
        db.collection("cars").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                // Carrega os carros do Firestore se existirem
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String name = document.getString("name");
                    int posX = document.getLong("positionX").intValue();
                    int posY = document.getLong("positionY").intValue();
                    int distance = document.getLong("distance").intValue();
                    int penalty = document.getLong("penalty").intValue();

                    Car car = new Car(context, mainLayout, R.drawable.carro1, posX, posY, track, name, cars);
                    car.setPosition(posX, posY);
                    car.setDistance(distance);
                    car.setPenalty(penalty);
                    cars.add(car);  // Adiciona o carro carregado à lista
                }
                Log.d("RaceManager", "Carros carregados do Firestore com sucesso.");
            } else {
                // Caso não existam carros no Firestore, cria carros novos
                for (int i = 0; i < carCount; i++) {
                    String carName = "carro" + (i + 1);

                    Car car = new Car(mainLayout.getContext(), mainLayout, R.drawable.carro1, 50, 50, track, carName, cars);
                    cars.add(car);  // Adiciona o carro à lista de carros
                    car.setPosition(550, 1060 + (i * 50));  // Posição inicial na pista
                }
                Log.d("RaceManager", "Nenhum carro encontrado no Firestore. Carros novos foram criados.");
            }
        }).addOnFailureListener(e -> Log.e("RaceManager", "Erro ao verificar o Firestore", e));
    }

    public void addEspecialCarsToTrack() {
        String carName = "SpecialCar";

        SpecialCar specialCar = new SpecialCar(mainLayout.getContext(), mainLayout, R.drawable.carro2, 50, 50, track, carName, cars);
        cars.add(specialCar);  // Adiciona o carro especial à lista de carros
        specialCar.setPosition(550, 1100);  // Posição inicial na pista específica para carros especiais
        specialCar.startMoving(); // Inicia o movimento do carro especial
    }

    public void startRace() {
        for (Car car : cars) {
            car.startMoving();  // Inicia o movimento de cada carro
        }
    }

    public void pauseRace() {
        saveCarStates();
        for (Car car : cars) {
            car.stopMoving();  // Para o movimento de cada carro
        }
    }

    public void finishRace() {
        for (Car car : cars) {
            car.stopMoving();  // Garante que todos os carros parem
        }

        Car winner = null;
        int highestScore = 0;

        for (Car car : cars) {
            int score = car.getDistance() - car.getPenalty();
            if (score > highestScore) {
                highestScore = score;
                winner = car;
            }
        }

        if (winner != null) {
            String winnerMessage = "O vencedor é " + winner.getName() + " com pontuação: " + highestScore;
            Toast.makeText(context, winnerMessage, Toast.LENGTH_LONG).show();
            Log.e("RaceManager", winnerMessage);
        } else {
            Toast.makeText(context, "Nenhum carro participou da corrida", Toast.LENGTH_LONG).show();
            Log.e("RaceManager", "Nenhum carro participou da corrida");
        }
        saveCarStates();
    }

    public void saveCarStates() {
        for (Car car : cars) {
            Map<String, Object> carData = new HashMap<>();
            carData.put("name", car.getName());
            carData.put("positionX", car.getPositionX());
            carData.put("positionY", car.getPositionY());
            carData.put("distance", car.getDistance());
            carData.put("penalty", car.getPenalty());

            db.collection("cars").add(carData)
                    .addOnSuccessListener(aVoid -> Log.d("RaceManager", "Estado do carro " + car.getName() + " salvo com sucesso"))
                    .addOnFailureListener(e -> Log.e("RaceManager", "Erro ao salvar estado do carro " + car.getName(), e));
        }
    }

    private Car findCarByName(String name) {
        for (Car car : cars) {
            if (car.getName().equals(name)) {
                return car;
            }
        }
        return null;
    }

    public static Semaphore getTrackSemaphore() {
        return trackSemaphore;
    }
}
