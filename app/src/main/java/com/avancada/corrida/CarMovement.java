package com.avancada.corrida;

import android.os.Handler;
import android.util.Log;

import java.util.List;
import java.util.Map;

public class CarMovement {
    private Car car;
    private Track track;
    private List<Car> allCars;
    private Map<Integer, Integer> sensor;
    private int sensorRange;
    private boolean isMoving;
    private int lastDirection = 0;

    public CarMovement(Car car, Track track, List<Car> allCars, Map<Integer, Integer> sensor, int sensorRange) {
        this.car = car;
        this.track = track;
        this.allCars = allCars;
        this.sensor = sensor;
        this.sensorRange = sensorRange;
    }

    public void startMoving() {
        isMoving = true;
        moveCar();
    }

    public void stopMoving() {
        isMoving = false;
    }

    private void moveCar() {

                if (isMoving) {
                    // Atualiza as leituras do sensor
                    updateSensorReadings();
                    Log.e("Sensor", "Leitura do sensor 0: " + sensor.get(0));
                    Log.e("Sensor", "Leitura do sensor 2: " + sensor.get(2));
                    Log.e("Sensor", "Leitura do sensor 4: " + sensor.get(4));
                    Log.e("Sensor", "Leitura do sensor 6: " + sensor.get(6));
                    Log.e("Sensor", "Leitura do sensor 8: " + sensor.get(8));
                    Log.e("Sensor", "Leitura do sensor 10: " + sensor.get(10));
                    Log.e("Sensor", "Leitura do sensor 12: " + sensor.get(12));
                    Log.e("Sensor", "Leitura do sensor 14: " + sensor.get(14));

                    moveInDirection();

                    //Carro está fora da pista
                    if (!track.isInTrack(car.getPositionX(), car.getPositionY())) {
                        car.incrementPenalty();
                        stopMoving();
                    }
                    car.incrementDistance();  // Atualiza a distância percorrida

                }
    }



    // Métodos auxiliares para movimentação, leitura de sensor e colisão
    private void updateSensorReadings() {
        // Lógica da leitura do sensor
        sensor.clear();
        for (int i = 0; i < 16; i++) {
            double angle = i * 22.5;
            int distance = car.getDistanceToObstacle(angle);
            sensor.put(i, distance);
        }
    }

    private void moveInDirection() {

        // Lógica de movimento baseado nas leituras do sensor
        boolean moved = false;

        if( car.getPositionY() >= 690 && car.getPositionX() >= 540){
            if (sensor.get(0) >= sensorRange && sensor.get(14) >= sensorRange && sensor.get(2) >= sensorRange) {
                Log.e("moveCar", "Indo pra direita.");
                car.setPosition(car.getPositionX() + 10, car.getPositionY());
                car.rotateCar(0);
                lastDirection = 0;
                moved = true;
            }else if (sensor.get(0) >= sensorRange && sensor.get(14) >= sensorRange){
                Log.e("moveCar", "Carro movendo pra cima e pra direita.");
                car.setPosition(car.getPositionX() + 10, car.getPositionY() - 10);
                car.rotateCar(-45); // Rotaciona para -45º
                lastDirection = 14;
                moved = true;
            }else if(sensor.get(0) >= sensorRange && sensor.get(2) >= sensorRange){
                Log.e("moveCar", "Carro movendo pra direita e pra baixo.");
                car.setPosition(car.getPositionX() + 10, car.getPositionY() + 10); // Move para a direita e para baixo
                car.rotateCar(45); // Rotaciona para 45º
                lastDirection = 2;
                moved = true;
            }else if(sensor.get(12) >= sensorRange && sensor.get(14) >= sensorRange){
                Log.e("moveCar", "Indo pra cima");
                car.setPosition(car.getPositionX(), car.getPositionY() - 10);
                car.rotateCar(-90); // Rotaciona a imagem para esquerda
                lastDirection = 12;
                moved = true;
            }else if(sensor.get(2) >= sensorRange && sensor.get(4) >= sensorRange){
                Log.e("moveCar", "Carro movendo para baixo.");
                car.setPosition(car.getPositionX(), car.getPositionY() + 10);
                car.rotateCar(90); // Rotaciona a imagem para direita
                lastDirection = 4;
                moved = true;
            }
        }else if (car.getPositionY() < 690 && car.getPositionX() > 540) {
            if(sensor.get(10) >= sensorRange && sensor.get(12) >= sensorRange && sensor.get(14) >= sensorRange){
                Log.e("moveCar", "Indo pra cima");
                car.setPosition(car.getPositionX(), car.getPositionY() - 10);
                car.rotateCar(-90); // Rotaciona a imagem para esquerda
                lastDirection = 12;
                moved = true;
            }else if (sensor.get(0) >= sensorRange && sensor.get(14) >= sensorRange) {
                Log.e("moveCar", "Indo pra cima e pra direita.");
                car.setPosition(car.getPositionX() + 10, car.getPositionY() - 10);
                car.rotateCar(0);
                lastDirection = 0;
                moved = true;
            }else if (sensor.get(8) >= sensorRange && sensor.get(10) >= sensorRange){
                Log.e("moveCar", "Carro movendo pra cima e pra esquerda.");
                car.setPosition(car.getPositionX() - 10, car.getPositionY() - 10);
                car.rotateCar(-45); // Rotaciona para -45º
                lastDirection = 14;
                moved = true;
            }else if(sensor.get(8) >= sensorRange ){
                Log.e("moveCar", "Indo pra esquerda.");
                car.setPosition(car.getPositionX() - 10, car.getPositionY());
                car.rotateCar(180); // Rotaciona a imagem para trás
                lastDirection = 8;
                moved = true;
            }
        }else if(car.getPositionY() <= 690 && car.getPositionX() <= 540) {
            if(sensor.get(8) >= sensorRange && sensor.get(10) >= sensorRange && sensor.get(6) >= sensorRange){
                Log.e("moveCar", "Indo pra esquerda.");
                car.setPosition(car.getPositionX() - 10, car.getPositionY());
                car.rotateCar(180); // Rotaciona a imagem para trás
                lastDirection = 8;
                moved = true;
            }else if(sensor.get(10) >= sensorRange && sensor.get(12) >= sensorRange){
                Log.e("moveCar", "Carro movendo pra cima e pra esquerda.");
                car.setPosition(car.getPositionX() - 10, car.getPositionY() - 10);
                car.rotateCar(-45); // Rotaciona para -45º
                lastDirection = 14;
                moved = true;
            }else if (sensor.get(6) >= sensorRange && sensor.get(4) >= sensorRange) {
                Log.e("moveCar", "Carro movendo pra baixo e pra esquerda.");
                car.setPosition(car.getPositionX() - 10, car.getPositionY() + 10);
                car.rotateCar(-45); // Rotaciona para -45º
                lastDirection = 14;
                moved = true;
            }else if (sensor.get(4) >= sensorRange){
                Log.e("moveCar", "Carro movendo para baixo.");
                car.setPosition(car.getPositionX(), car.getPositionY() + 10);
                car.rotateCar(90); // Rotaciona a imagem para direita
                lastDirection = 4;
                moved = true;
            }
        }else if(car.getPositionY() > 690 && car.getPositionX() < 540){
            if (sensor.get(6) >= sensorRange && sensor.get(4) >= sensorRange && sensor.get(2) >= sensorRange){
                Log.e("moveCar", "Carro movendo para baixo.");
                car.setPosition(car.getPositionX(), car.getPositionY() + 10);
                car.rotateCar(90); // Rotaciona a imagem para direita
                lastDirection = 4;
                moved = true;
            }else if(sensor.get(8) >= sensorRange && sensor.get(6) >= sensorRange){
                Log.e("moveCar", "Carro movendo pra baixo e pra esquerda.");
                car.setPosition(car.getPositionX() - 10, car.getPositionY() + 10);
                car.rotateCar(-45); // Rotaciona para -45º
                lastDirection = 14;
                moved = true;
            }else if(sensor.get(0) >= sensorRange && sensor.get(2) >= sensorRange){
                Log.e("moveCar", "Carro movendo pra direita e pra baixo.");
                car.setPosition(car.getPositionX() + 10, car.getPositionY() + 10);
                car.rotateCar(45); // Rotaciona para 45º
                lastDirection = 2;
                moved = true;
            }else if (sensor.get(0) >= sensorRange) {
                Log.e("moveCar", "Indo pra direita.");
                car.setPosition(car.getPositionX() + 10, car.getPositionY());
                car.rotateCar(0);
                lastDirection = 0;
                moved = true;
            }
        }

        // Se não conseguiu se mover, reseta a última direção e tenta novamente
        if (!moved) {
            Log.e("moveCar", "Nenhuma direção disponível, resetando direção.");
            lastDirection = 0; // Reseta a direção para tentar novamente
        }
    }
}
