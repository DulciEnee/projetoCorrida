package com.avancada.corrida;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Car implements Runnable {
    private String name;
    private int distance;
    private int penalty;
    private int positionX;
    private int positionY;
    private ImageView imageView;
    private CarView carView;
    private Map<Integer, Integer> sensor;
    private int sensorRange = 45; // Tamanho do sensor em pixels
    private Track track;  // Referência à pista
    private Bitmap carBitmap;
    private ArrayList<Car> cars;
    private boolean isRunning = false;
    private long moveInterval = 50; // Intervalo entre movimentos em milissegundos
    private CarMovement carMovement;

    // Construtor da classe Car. Inicializa os atributos e adiciona a imagem do carro ao layout.

    public Car(Context context, FrameLayout layout, int carImage, int posX, int posY, Track track, String name, ArrayList<Car> cars) {
        this.name = name;
        this.positionX = posX;
        this.positionY = posY;
        this.track = track;
        this.cars = cars;
        this.sensor = new HashMap<>();

        try {
            // Carregar o Bitmap do carro a partir do recurso
            Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), carImage);
            carBitmap = Bitmap.createScaledBitmap(originalBitmap, 40, 40, true);
            carView = new CarView(context, carBitmap, posX, posY);
            layout.addView(carView);

            carMovement = new CarMovement(this, track, cars, sensor, sensorRange);
        } catch (Exception e) {
            Log.e("Car", "Erro ao inicializar o carro: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {
            if (isRunning && isInExclusiveTrack(positionX, positionY)) {
                try {
                    RaceManager.getTrackSemaphore().acquire();
                    Log.d("Semaphore", "Semáforo adquirido por " + name);

                    while (isRunning && isInExclusiveTrack(positionX, positionY)) {
                        moveCar();
                        Thread.sleep(moveInterval);
                    }

                } catch (InterruptedException e) {
                    Log.e("Car", "Movimento interrompido: " + e.getMessage());
                } finally {
                    RaceManager.getTrackSemaphore().release();
                    Log.d("Semaphore", "Semáforo liberado por " + name);
                }
            } else {
                moveCar();
                try {
                    Thread.sleep(moveInterval);
                } catch (InterruptedException e) {
                    Log.e("Car", "Movimento interrompido fora da faixa exclusiva: " + e.getMessage());
                }
            }
        }
    }

    // Verifica se o carro está na faixa exclusiva da pista.

    private boolean isInExclusiveTrack(int x, int y) {
        return (x > 437 && x < 634) && (y > 188 && y < 377);
    }

    //Move o carro chamando a lógica de movimento de CarMovement.

    public void moveCar() {
        carMovement.startMoving();
    }

    //Verifica se o carro pode se mover para a posição atual na pista.

    public boolean canMove() {
        return track.isInTrack(positionX, positionY);
    }

    //Inicia o movimento do carro em uma nova thread.

    public void startMoving() {
        isRunning = true;
        new Thread(this).start();
    }

    //Para o movimento do carro e chama a lógica de parada de CarMovement.

    public void stopMoving() {
        isRunning = false;
        carMovement.stopMoving();
    }

    //Rotaciona a imagem do carro para um determinado ângulo.

    public void rotateCar(float angle) {
        carView.setRotation(angle);
    }

    //
    public int getDistanceToObstacle(double angle) {
        int deltaX = (int) (sensorRange * Math.cos(Math.toRadians(angle)));
        int deltaY = (int) (sensorRange * Math.sin(Math.toRadians(angle)));

        for (int d = 1; d <= sensorRange; d++) {
            int checkX = positionX + deltaX * d / sensorRange;
            int checkY = positionY + deltaY * d / sensorRange;

            if (!track.isInTrack(checkX, checkY)) {
                return d;
            }

            for (Car otherCar : cars) {
                if (!this.equals(otherCar) && otherCar.carView.isInCar(checkX, checkY)) {
                    Log.d("Sensor", "Colisão com outro carro detectada na posição: (" + checkX + ", " + checkY + ")");
                    return d;
                }
            }
        }
        return sensorRange;
    }

    //Verifica se houve colisão com outro carro.

    public boolean checkCollision(Car otherCar) {
        return this.positionX == otherCar.positionX && this.positionY == otherCar.positionY;
    }

    // Métodos auxiliares, getters e setters

    public void setPosition(int x, int y) {
        this.positionX = x;
        this.positionY = y;
        carView.setPosition(x, y);
    }
    public void setMoveInterval(long interval) {
        this.moveInterval = interval;
    }

    public void incrementDistance() {
        distance++;
    }

    public void incrementPenalty() {
        penalty++;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public String getName() {
        return name;
    }

    public Bitmap getCarBitmap() {
        return carBitmap;
    }

    public int getPenalty() {
        return penalty;
    }

    public int getDistance() {
        return distance;
    }

    public void setPenalty(int p) {
        this.penalty = p;
    }

    public void setDistance(int d) {
        this.distance = d;
    }
}