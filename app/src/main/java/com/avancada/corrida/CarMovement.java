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

    public void moveCar() {
        if (isMoving) {
            updateSensorReadings();

            try {
                moveInDirection();
                car.incrementDistance();

                // Verifica se o carro está fora da pista
                if (!track.isInTrack(car.getPositionX(), car.getPositionY())) {
                    car.incrementPenalty();
                    stopMoving();
                }
            } catch (Exception e) {
                Log.e("CarMovement", "Erro ao mover o carro: " + e.getMessage());
            }
        }
    }

    // Atualiza as leituras do sensor
    public void updateSensorReadings() {
        sensor.clear();
        for (int i = 0; i < 16; i++) {
            double angle = i * 22.5;
            int distance = car.getDistanceToObstacle(angle);
            sensor.put(i, distance);
        }
    }

    // Lógica de movimentação baseada nas leituras do sensor
    public void moveInDirection() {
        boolean moved = false;
        Log.e("Sensor", "Leitura do sensor 0: " + sensor.get(0));
        Log.e("Sensor", "Leitura do sensor 2: " + sensor.get(2));
        Log.e("Sensor", "Leitura do sensor 4: " + sensor.get(4));
        Log.e("Sensor", "Leitura do sensor 6: " + sensor.get(6));
        Log.e("Sensor", "Leitura do sensor 8: " + sensor.get(8));
        Log.e("Sensor", "Leitura do sensor 10: " + sensor.get(10));
        Log.e("Sensor", "Leitura do sensor 12: " + sensor.get(12));
        Log.e("Sensor", "Leitura do sensor 14: " + sensor.get(14));

        try {
            if (car.getPositionY() >= 690 && car.getPositionX() >= 540) {
                moved = moveRightUpDown();
            } else if (car.getPositionY() < 690 && car.getPositionX() > 540) {
                moved = moveTopRight();
            } else if (car.getPositionY() <= 690 && car.getPositionX() <= 540) {
                moved = moveLeftDown();
            } else if (car.getPositionY() > 690 && car.getPositionX() < 540) {
                moved = moveLeftBottomRight();
            }

            if (!moved) {
                Log.e("moveCar", "Nenhuma direção disponível, resetando direção.");
                lastDirection = 0;
            }
        } catch (Exception e) {
            Log.e("CarMovement", "Erro ao calcular a direção do movimento: " + e.getMessage());
        }
    }

    private boolean moveRightUpDown() {
        if (sensor.get(0) >= sensorRange && sensor.get(14) >= sensorRange && sensor.get(2) >= sensorRange) {
            car.setPosition(car.getPositionX() + 10, car.getPositionY());
            car.rotateCar(0);
            lastDirection = 0;
            return true;
        } else if (sensor.get(0) >= sensorRange && sensor.get(14) >= sensorRange) {
            car.setPosition(car.getPositionX() + 10, car.getPositionY() - 10);
            car.rotateCar(-45);
            lastDirection = 14;
            return true;
        } else if (sensor.get(0) >= sensorRange && sensor.get(2) >= sensorRange) {
            car.setPosition(car.getPositionX() + 10, car.getPositionY() + 10);
            car.rotateCar(45);
            lastDirection = 2;
            return true;
        } else if (sensor.get(12) >= sensorRange && sensor.get(14) >= sensorRange) {
            car.setPosition(car.getPositionX(), car.getPositionY() - 10);
            car.rotateCar(-90);
            lastDirection = 12;
            return true;
        } else if (sensor.get(2) >= sensorRange && sensor.get(4) >= sensorRange) {
            car.setPosition(car.getPositionX(), car.getPositionY() + 10);
            car.rotateCar(90);
            lastDirection = 4;
            return true;
        }
        return false;
    }

    private boolean moveTopRight() {
        if (sensor.get(10) >= sensorRange && sensor.get(12) >= sensorRange && sensor.get(14) >= sensorRange) {
            car.setPosition(car.getPositionX(), car.getPositionY() - 10);
            car.rotateCar(-90);
            lastDirection = 12;
            return true;
        } else if (sensor.get(0) >= sensorRange && sensor.get(14) >= sensorRange) {
            car.setPosition(car.getPositionX() + 10, car.getPositionY() - 10);
            car.rotateCar(0);
            lastDirection = 0;
            return true;
        } else if (sensor.get(8) >= sensorRange && sensor.get(10) >= sensorRange) {
            car.setPosition(car.getPositionX() - 10, car.getPositionY() - 10);
            car.rotateCar(-45);
            lastDirection = 14;
            return true;
        } else if (sensor.get(8) >= sensorRange) {
            car.setPosition(car.getPositionX() - 10, car.getPositionY());
            car.rotateCar(180);
            lastDirection = 8;
            return true;
        }
        return false;
    }

    private boolean moveLeftDown() {
        if (sensor.get(8) >= sensorRange && sensor.get(10) >= sensorRange && sensor.get(6) >= sensorRange) {
            car.setPosition(car.getPositionX() - 10, car.getPositionY());
            car.rotateCar(180);
            lastDirection = 8;
            return true;
        } else if (sensor.get(10) >= sensorRange && sensor.get(12) >= sensorRange) {
            car.setPosition(car.getPositionX() - 10, car.getPositionY() - 10);
            car.rotateCar(-45);
            lastDirection = 14;
            return true;
        } else if (sensor.get(6) >= sensorRange && sensor.get(4) >= sensorRange) {
            car.setPosition(car.getPositionX() - 10, car.getPositionY() + 10);
            car.rotateCar(-45);
            lastDirection = 14;
            return true;
        } else if (sensor.get(4) >= sensorRange) {
            car.setPosition(car.getPositionX(), car.getPositionY() + 10);
            car.rotateCar(90);
            lastDirection = 4;
            return true;
        }
        return false;
    }

    private boolean moveLeftBottomRight() {
        if (sensor.get(6) >= sensorRange && sensor.get(4) >= sensorRange && sensor.get(2) >= sensorRange) {
            car.setPosition(car.getPositionX(), car.getPositionY() + 10);
            car.rotateCar(90);
            lastDirection = 4;
            return true;
        } else if (sensor.get(8) >= sensorRange && sensor.get(6) >= sensorRange) {
            car.setPosition(car.getPositionX() - 10, car.getPositionY() + 10);
            car.rotateCar(-45);
            lastDirection = 14;
            return true;
        } else if (sensor.get(0) >= sensorRange && sensor.get(2) >= sensorRange) {
            car.setPosition(car.getPositionX() + 10, car.getPositionY() + 10);
            car.rotateCar(45);
            lastDirection = 2;
            return true;
        } else if (sensor.get(0) >= sensorRange) {
            car.setPosition(car.getPositionX() + 10, car.getPositionY());
            car.rotateCar(0);
            lastDirection = 0;
            return true;
        }
        return false;
    }
}
