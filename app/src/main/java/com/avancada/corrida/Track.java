package com.avancada.corrida;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Track {

    private Bitmap trackImage;

    //Construtor da classe Track que carrega a imagem da pista.

    public Track(Context context, int trackImageResource) {
        try {
            // Cria bitmap da imagem da pista sem alterar as dimensões
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;  // Garante que o bitmap não será redimensionado
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;  // Garante 32 bits por pixel (inclui canal alfa)
            trackImage = BitmapFactory.decodeResource(context.getResources(), trackImageResource, options);

            if (trackImage == null) {
                Log.e("Track", "Falha ao carregar a imagem da pista");
            } else {
                Log.d("Track", "Pista carregada com sucesso: " + trackImage.getWidth() + "x" + trackImage.getHeight());
            }
        } catch (Exception e) {
            Log.e("Track", "Erro ao carregar a imagem da pista: " + e.getMessage());
        }
    }

    // Verifica se uma posição (x, y) está dentro dos limites da pista (cor branca).

    public boolean isInTrack(int x, int y) {
        try {
            // Verificar se as coordenadas estão dentro dos limites da imagem
            if (x < 0 || x >= trackImage.getWidth() || y < 0 || y >= trackImage.getHeight()) {
                Log.e("Track", "Coordenadas fora dos limites: (" + x + ", " + y + ")");
                return false; // Fora dos limites da imagem
            }

            // Obter a cor do pixel na posição (x, y)
            int pixelColor = trackImage.getPixel(x, y);

            // Verifica se o pixel é da cor que representa a pista (branco)
            return pixelColor == 0xFFFFFFFF;
        } catch (IllegalArgumentException e) {
            Log.e("Track", "Erro nas coordenadas do pixel: " + e.getMessage());
            return false;
        } catch (Exception e) {
            Log.e("Track", "Erro ao verificar se está na pista: " + e.getMessage());
            return false;
        }
    }

    //Retorna a imagem da pista como um Bitmap.

    public Bitmap getTrackImage() {
        return trackImage;
    }
}
