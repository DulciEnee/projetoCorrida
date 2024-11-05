package com.avancada.corrida;

import android.content.Context;
import android.widget.FrameLayout;
import java.util.ArrayList;

public class SpecialCar extends Car {
    private static final long SPECIAL_MOVE_INTERVAL = 30; // Intervalo menor para aumentar a velocidade

    public SpecialCar(Context context, FrameLayout layout, int carImage, int posX, int posY, Track track, String name, ArrayList<Car> cars) {
        super(context, layout, carImage, posX, posY, track, name, cars);
        this.setMoveInterval(SPECIAL_MOVE_INTERVAL); // Defina o intervalo mais rápido
    }

    @Override
    public void run() {
        // Aumenta a prioridade da thread para maximizar a frequência de atualização
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        super.run();
    }
}
