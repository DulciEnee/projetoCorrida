package com.avancada.corrida;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private int sensorRange = 45; // Tamanho do sensor (d) em pixels
    private Track track;  // A pista na qual o carro está
    private Bitmap carBitmap;
    private ArrayList<Car> cars;
    private boolean isRunning = false;
    private long moveInterval = 100; // Intervalo de tempo entre movimentos em milissegundos

    public Car(Context context, FrameLayout layout, int carImage, int posX, int posY, Track track, String name, ArrayList<Car> cars) {
        imageView = new ImageView(context);
        // Carregar o Bitmap do carro a partir do recurso
        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), carImage);
        // Redimensionar o Bitmap para o mesmo tamanho que o ImageView
        carBitmap = Bitmap.createScaledBitmap(originalBitmap, 40, 40, true);

        // Criar o CarView e adicionar ao layout
        carView = new CarView(context, carBitmap, posX, posY);
        layout.addView(carView);

        this.positionX = posX;
        this.positionY = posY;
        this.name = name;
        sensor = new HashMap<>();
        this.track = track;
        this.cars = cars;
    }

    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {
            moveCar(); // Lógica de movimentação do carro
            try {
                Thread.sleep(moveInterval); // Espera entre movimentos
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void moveCar() {
        if (canMove()) {
            // Adicione sua lógica de movimentação aqui
            positionX += 5; // Exemplo de movimentação simples
            carView.setPosition(positionX, positionY);
            incrementDistance();
        }
    }

    public boolean canMove() {
        // Adicione a lógica de verificação se o carro pode se mover
        return track.isInTrack(positionX, positionY);
    }

    public void startMoving() {
        isRunning = true;
        new Thread(this).start(); // Inicia a execução do carro em uma nova thread
    }

    public void stopMoving() {
        isRunning = false;
    }

    // Métodos auxiliares, getters e setters continuam os mesmos...

    public void rotateCar(float angle) {
        carView.setRotation(angle);
    }

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
                if (!this.equals(otherCar)) {
                    if (otherCar.carView.isInCar(checkX, checkY)) {
                        Log.d("Sensor", "Colisão com outro carro detectada na posição: (" + checkX + ", " + checkY + ")");
                        return d;
                    }
                }
            }
        }
        return sensorRange;
    }

    public boolean checkCollision(Car otherCar) {
        return this.positionX == otherCar.positionX && this.positionY == otherCar.positionY;
    }

    public void setPosition(int x, int y) {
        this.positionX = x;
        this.positionY = y;
        carView.setPosition(x, y);
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
}
