package com.avancada.corrida;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Track {
    private Bitmap trackImage;

    public Track(Context context, int trackImageResource) {

        //Cria bitmap da imagem pista sem alterar as dimensões
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;  // Garante que o bitmap não será redimensionado
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;  // Garante 32 bits por pixel (inclui canal alfa)
        trackImage = BitmapFactory.decodeResource(context.getResources(), trackImageResource, options);

        // DEPURAÇÃO
        if (trackImage == null) {
            Log.e("Track", "Falha ao carregar a imagem da pista");
        } else {
            Log.d("Track", "Pista carregada com sucesso: " + trackImage.getWidth() + "x" + trackImage.getHeight());
        }

    }

    public boolean isInTrack(int x, int y) {
        //DEPURAÇÃO: Verifca cor da pista
        int pixelColor1 = trackImage.getPixel(x, y);
        Log.d("Track", "Pixel Color: " + Integer.toHexString(pixelColor1)); // Log do valor da cor do pixel

        // DEPURAÇÃO: Verificar se as coordenadas estão dentro da imagem
        if (x < 0 || x >= trackImage.getWidth() || y < 0 || y >= trackImage.getHeight()) {
            Log.e("Track", "Coordenadas fora dos limites: (" + x + ", " + y + ")");
            return false; // Fora dos limites da imagem
        }

        // Obter a cor do pixel na posição (x, y)
        int pixelColor = trackImage.getPixel(x, y);

        // Verifica se o pixel é da cor que representa a pista
        return pixelColor == 0xFFFFFFFF;  // Branco
    }
    // Método para retornar a imagem da pista como um Bitmap
    public Bitmap getTrackImage() {
        return trackImage;
    }
}