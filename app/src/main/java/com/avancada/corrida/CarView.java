package com.avancada.corrida;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

// Classe responsável pela visualização do carro na tela,
// representada como um Bitmap rotacionável e movível.
public class CarView extends View {
    private Bitmap carBitmap;
    private int positionX;
    private int positionY;
    private float rotationAngle = 0;

    // Construtor para inicializar a visão do carro com o Bitmap e posição inicial.

    public CarView(Context context, Bitmap carBitmap, int posX, int posY) {
        super(context);
        this.carBitmap = carBitmap;
        this.positionX = posX;
        this.positionY = posY;
    }

    // Define a nova posição do carro na tela.
    public void setPosition(int x, int y) {
        this.positionX = x;
        this.positionY = y;
        invalidate(); // Atualiza a View
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    //Define o ângulo de rotação do carro.
    public void setRotation(float angle) {
        this.rotationAngle = angle;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Rotaciona o Canvas no centro do carro e desenha o Bitmap na posição especificada.
        try {
            canvas.rotate(rotationAngle, positionX + carBitmap.getWidth() / 2f, positionY + carBitmap.getHeight() / 2f);
            canvas.drawBitmap(carBitmap, positionX, positionY, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Verifica se um ponto específico está dentro dos limites do carro,

    public boolean isInCar(int x, int y) {
        // Verifica se o ponto está fora dos limites do bitmap do carro.
        if (x < positionX || x >= positionX + carBitmap.getWidth() ||
                y < positionY || y >= positionY + carBitmap.getHeight()) {
            return false;
        }

        // Calcula as coordenadas relativas dentro do bitmap.
        int relativeX = x - positionX;
        int relativeY = y - positionY;

        try {
            // Obtém a cor do pixel na posição relativa dentro do bitmap.
            int pixelColor = carBitmap.getPixel(relativeX, relativeY);
            return pixelColor != 0xFFFFFFFF; // Retorna true se o pixel não for branco.
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //Rotaciona o carro por um ângulo específico.

    public void rotateAngle(float angle) {
        this.rotationAngle += angle;
        invalidate();
    }
}
