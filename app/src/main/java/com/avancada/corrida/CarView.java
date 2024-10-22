package com.avancada.corrida;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

public class CarView extends View {
    private Bitmap carBitmap;
    private int positionX;
    private int positionY;
    private float rotationAngle = 0;

    public CarView(Context context, Bitmap carBitmap, int posX, int posY) {
        super(context);
        this.carBitmap = carBitmap;
        this.positionX = posX;
        this.positionY = posY;
    }

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

    public void setRotation(float angle) {
        this.rotationAngle = angle;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Desenha o bitmap na posição especificada
        // Rotaciona o Canvas no centro do carro
        canvas.rotate(rotationAngle, positionX + carBitmap.getWidth() / 2, positionY + carBitmap.getHeight() / 2);

        canvas.drawBitmap(carBitmap, positionX, positionY, null);
    }

    public boolean isInCar(int x, int y) {
        // Verifica se o ponto está dentro dos limites do bitmap do carro
        if (x < positionX || x >= positionX + carBitmap.getWidth() ||
                y < positionY || y >= positionY + carBitmap.getHeight()) {
            return false; // Ponto está fora do carro
        }

        // Coordenadas relativas dentro do bitmap
        int relativeX = x - positionX;
        int relativeY = y - positionY;

        // Verificar a cor do pixel na posição relativa do bitmap do carro
        int pixelColor = carBitmap.getPixel(relativeX, relativeY);

        // Verifica se o pixel não é transparente ou branco
        return pixelColor != 0xFFFFFFFF; // Retorna true se não for branco
    }

    public void rotateAngle(float angle){

    };

}
