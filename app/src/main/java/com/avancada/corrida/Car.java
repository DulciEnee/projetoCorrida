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


public class Car {
    private String name;
    private int distance;
    private int penalty;
    private int positionX;
    private int positionY;
    private ImageView imageView;
    private CarView carView;
    private CarMovement carMovement;
    private Map<Integer, Integer> sensor;
    private int sensorRange = 45; // Tamanho do sensor (d) em pixels
    private Track track;  // A pista na qual o carro está
    private Bitmap carBitmap;
    private ArrayList<Car> cars;



    public Car(Context context, FrameLayout layout, int carImage, int posX, int posY, Track track, String name, ArrayList<Car> cars) {
        imageView = new ImageView(context);
        imageView.setImageResource(carImage);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(40, 40)); // Definir o tamanho do carro
        imageView.setVisibility(View.VISIBLE);
        // Gerar um ID único para o ImageView
        imageView.setId(View.generateViewId());
        // Adicionar a imagem do carro no layout
        //layout.addView(imageView);


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
        this.carMovement = new CarMovement(this, track, cars, sensor, 45);
        this.track = track;
        this.cars = cars;
    }

    // Métodos que alteram o movimento do carro
    public void startMoving() {
        carMovement.startMoving();
    }

    public void stopMoving() {
        carMovement.stopMoving();
    }

    public void rotateCar(float angle) {
        imageView.setRotation(angle);
    }
    public int getDistanceToObstacle(double angle) {
        //sensorRange é multiplicado pelos resultados de seno e cosseno para obter o deslocamento horizontal e vertical em função desse alcance.
        //A função Math.cos() calcula o "delta" no eixo X, ou seja, o quanto o sensor vai se deslocar horizontalmente na direção do ângulo fornecido.
        int deltaX = (int) (sensorRange * Math.cos(Math.toRadians(angle)));
        //A função Math.sin() calcula o "delta" no eixo Y, representando o deslocamento vertical na direção do ângulo.
        int deltaY = (int) (sensorRange * Math.sin(Math.toRadians(angle)));

        //Loop que simula a verificação do sensor, indo de 1 até o valor máximo de sensorRange.
        //A cada iteração, o código calcula uma nova posição checkX e checkY ao longo da direção do ângulo. Essa nova posição é obtida ao somar o deslocamento em X e Y multiplicado por d, que representa a distância percorrida até o momento em relação à distância total do sensor.
        //As coordenadas checkX e checkY representam o ponto atual sendo verificado na linha do sensor.
        for (int d = 1; d <= sensorRange; d++) {
            int checkX = positionX + deltaX * d / sensorRange;
            int checkY = positionY + deltaY * d / sensorRange;

            // Verificar se a posição está fora dos limites da pista
            if (!track.isInTrack(checkX, checkY)) {
                return d; // Retorna a distância até o obstáculo
            }
            // Verificar se há outro carro na posição escaneada
            for (Car otherCar : cars) {
                if (!this.equals(otherCar)) { // Não verificar o próprio carro
                    // Verificar se as coordenadas estão sobre outro carro
                    if (otherCar.carView.isInCar(checkX, checkY)) {
                        Log.d("Sensor", "Colisão com outro carro detectada na posição: (" + checkX + ", " + checkY + ")");
                        return d; // Retorna a distância até o obstáculo (outro carro)
                    }
                }
            }

        }
        return sensorRange; // Nenhum obstáculo detectado dentro do alcance
    }
    public boolean checkCollision(Car otherCar) {
        // Verificar se as posições de this e otherCar se sobrepõem
        return this.positionX == otherCar.positionX && this.positionY == otherCar.positionY;
    }

    // Métodos set
    public void setPosition(int x, int y) {
        this.positionX = x;
        this.positionY = y;
        imageView.setX(x);
        imageView.setY(y);
        carView.setPosition(x, y); // Atualiza também a posição no CarView (Bitmap)
    }
    public void incrementDistance() {
        distance++;
    }

    public void incrementPenalty() {
        penalty++;
    }

    // Métodos get
    public int getPositionX() {
        return positionX;
    }
    public int getPositionY() {
        return positionY;
    }
    public String getName() {
        return name;
    }
    public ImageView getImageView() {
        return imageView;
    }
    // Método para transformar o ImageView em um Bitmap
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
